package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping;

import org.apache.logging.log4j.Logger;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.*;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;
import org.dieschnittstelle.jee.esa.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.Campaign;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.ProductBundle;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.util.List;

@Stateful
@Remote(ShoppingSessionFacadeRemote.class)
public class ShoppingSessionFacadeStateful implements ShoppingSessionFacadeRemote {

    protected Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShoppingSessionFacadeStateful.class);

    /*
     * the three beans that are used
     */

    @EJB
    private ShoppingCartLocal shoppingCart;
    @EJB
    private CustomerTrackingLocal customerTracking;
    @EJB
    private CampaignTrackingLocal campaignTracking;

    @EJB
    private ProductCRUDLocal productCRUD;
    @EJB
    private StockSystemLocal stockSystem;

    private Customer customer;
    private AbstractTouchpoint touchpoint;
    private boolean endSession = false;

    @Override
    public void setTouchpoint(AbstractTouchpoint touchpoint) {
        this.touchpoint = touchpoint;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void addProduct(AbstractProduct product, int units) {
        this.shoppingCart.addItem(new ShoppingCartItem(product.getId(), units, product instanceof Campaign));
    }

    @Override
    public void purchase()  throws ShoppingException {
        logger.info("purchase()");

        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException(
                    "cannot commit shopping session! Either customer or touchpoint has not been set: " + this.customer
                            + "/" + this.touchpoint);
        }

        // verify the campaigns
        verifyCampaigns();

        // remove the products from stock
        checkAndRemoveProductsFromStock();

        // then we add a new customer transaction for the current purchase
        List<ShoppingCartItem> products = this.shoppingCart.getItems();
        CustomerTransaction transaction = new CustomerTransaction(this.customer, this.touchpoint, products);
        transaction.setCompleted(true);
        customerTracking.createTransaction(transaction);
        this.endSession = true; // Set end of session
        logger.info("purchase(): done.\n");
    }

    /*
     * verify whether campaigns are still valid
     */
    public void verifyCampaigns() throws ShoppingException {
        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException("cannot verify campaigns! No touchpoint has been set!");
        }

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {
            if (item.isCampaign()) {
                int availableCampaigns = this.campaignTracking.existsValidCampaignExecutionAtTouchpoint(
                        item.getErpProductId(), this.touchpoint);
                logger.info("got available campaigns for product " + item.getErpProductId() + ": "
                        + availableCampaigns);
                // we check whether we have sufficient campaign items available
                if (availableCampaigns < item.getUnits()) {
                    throw new ShoppingException("verifyCampaigns() failed for productBundle " + item
                            + " at touchpoint " + this.touchpoint + "! Need " + item.getUnits()
                            + " instances of campaign, but only got: " + availableCampaigns);
                }
            }
        }
    }

    /*
     * to be implemented as server-side method for PAT2
     */
    private void checkAndRemoveProductsFromStock() {
        logger.info("checkAndRemoveProductsFromStock");

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {

            AbstractProduct abstractProduct = this.productCRUD.readProduct(item.getErpProductId());

            if (item.isCampaign()) {
                this.campaignTracking.purchaseCampaignAtTouchpoint(item.getErpProductId(), this.touchpoint,
                        item.getUnits());
                Campaign campaign = (Campaign) abstractProduct;

                for (ProductBundle productBundle : campaign.getBundles()) {
                    int size = item.getUnits() * productBundle.getUnits();

                    if (this.stockSystem.getUnitsOnStock(productBundle.getProduct(), this.touchpoint.getErpPointOfSaleId()) >= size) {
                        this.stockSystem.removeFromStock(productBundle.getProduct(), this.touchpoint.getErpPointOfSaleId(), size);
                    }
                }
            } else {
                IndividualisedProductItem individualisedProductItem = (IndividualisedProductItem) abstractProduct;

                if (this.stockSystem.getUnitsOnStock(individualisedProductItem, this.touchpoint.getErpPointOfSaleId()) >= item.getUnits()) {
                    this.stockSystem.removeFromStock(individualisedProductItem, this.touchpoint.getErpPointOfSaleId(), item.getUnits());
                }
            }
        }
    }

    @PreDestroy
    private void preDestroy() {
        if (!this.endSession) {
            CustomerTransaction customerTransaction = new CustomerTransaction(this.customer, this.touchpoint, this.shoppingCart.getItems());
            this.customerTracking.createTransaction(customerTransaction);
        }
    }
}
