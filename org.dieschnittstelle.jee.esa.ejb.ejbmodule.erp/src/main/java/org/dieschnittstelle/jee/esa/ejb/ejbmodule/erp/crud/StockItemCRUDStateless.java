package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.ProductAtPosPK;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Stateless
@Local(StockItemCRUDLocal.class)
public class StockItemCRUDStateless implements StockItemCRUDLocal {
    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;

    @Override
    public StockItem createStockItem(StockItem item) {
//        if(!em.contains(item.getProduct())){
//            item.setProduct(em.merge(item.getProduct()));
//        }
//        em.persist(item);

        return em.merge(item);
    }

    @Override
    public StockItem readStockItem(IndividualisedProductItem prod, PointOfSale pos) {
        return em.find(StockItem.class, new ProductAtPosPK(prod,pos));
    }

    @Override
    public StockItem updateStockItem(StockItem item) {

        return em.merge(item);
    }

    @Override
    public List<StockItem> readAllStockItems() {
        return em.createQuery("SELECT s FROM StockItem AS s").getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForProduct(IndividualisedProductItem prod) {
        return em.createQuery("SELECT s FROM StockItem AS s WHERE s.product = " + prod.getId()).getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForPointOfSale(PointOfSale pos) {
        return em.createQuery("SELECT s FROM StockItem AS s WHERE s.pos = " + pos.getId()).getResultList();
    }
}
