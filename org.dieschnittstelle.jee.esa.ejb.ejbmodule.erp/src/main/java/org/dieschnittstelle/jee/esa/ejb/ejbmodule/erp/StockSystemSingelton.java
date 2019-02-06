package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.StockItemCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Remote(StockSystemRemote.class)
@WebService(targetNamespace = "http://dieschnittstelle.org/jee/esa/jws", endpointInterface = "org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote", serviceName = "StockSystemWebService")
public class StockSystemSingelton implements StockSystemRemote, StockSystemLocal {

    @EJB
    private PointOfSaleCRUDLocal posCRUD;
    @EJB
    private StockItemCRUDLocal siCRUD;
    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem si = siCRUD.readStockItem(product, pos);
        if (si == null){
            si = new StockItem(product,pos,units);
            siCRUD.createStockItem(si);
        }else{
            si.setUnits(si.getUnits() + units);
            siCRUD.updateStockItem(si);

        }

    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);

        StockItem si = siCRUD.readStockItem(product, pos);
        si.setUnits(si.getUnits() - units);
        siCRUD.updateStockItem(si);

    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        List<StockItem> si = siCRUD.readStockItemsForPointOfSale(pos);
        List<IndividualisedProductItem> productItemList = new ArrayList<IndividualisedProductItem>();
        if (si != null) {
            for (StockItem stockitem : si) {
                productItemList.add(stockitem.getProduct());
            }
        }
        return productItemList;

    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        List<PointOfSale> poslist = posCRUD.readAllPointsOfSale();
        List<IndividualisedProductItem> productlist = new ArrayList<>();
        List<IndividualisedProductItem> productlistNoDuplicates = new ArrayList<>();
        if (poslist != null) {
            for (PointOfSale pos : poslist) {
                productlist = this.getProductsOnStock(pos.getId());
                if (productlist != null) {
                    for (IndividualisedProductItem item : productlist) {
                        productlistNoDuplicates.add(item);
                    }
                }
            }
        }
        List <IndividualisedProductItem> all = (List) Arrays.stream(productlistNoDuplicates.toArray()).distinct().collect(Collectors.toList());
        return all;

    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);

        StockItem si = siCRUD.readStockItem(product, pos);

        if (si == null) {
            return 0;
        } else {
            return si.getUnits();
        }


    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        List<StockItem> si = siCRUD.readStockItemsForProduct(product);
        int totalUnits = 0;
        if (si != null) {
            for (StockItem unit : si) {
                totalUnits += unit.getUnits();
            }
        }
        return totalUnits;

    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        List<StockItem> si = siCRUD.readStockItemsForProduct(product);
        List<Long> pointsOfSale = new ArrayList<Long>();
        for (StockItem stockItem : si) {
            pointsOfSale.add( stockItem.getPos().getId());
        }
        return pointsOfSale;


    }

    @Override
    public List<StockItem> getCompleteStock() {
        return null;
    }
}
