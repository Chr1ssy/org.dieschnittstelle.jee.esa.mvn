package org.dieschnittstelle.jee.esa.ejb.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

public class StockSystemClient implements StockSystemRemote {

	private StockSystemRemote ejbProxy;
	
	public StockSystemClient() throws Exception {
		// TODO: obtain a proxy specifying the ejb interface and uri. Let all subsequent methods use the proxy.
		//url aus log einfügen nach Stocksystemremote suchen
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(StockSystemRemote.class,"ejb:org.dieschnittstelle.jee.esa.ejb/org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp/StockSystemSingelton!org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote");
	}
	
	
	@Override
	public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
		this.ejbProxy.addToStock(product, pointOfSaleId, units);
	}

	@Override
	public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId,
			int units) {
		this.ejbProxy.removeFromStock(product, pointOfSaleId, units);
	}

	@Override
	public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
		return this.ejbProxy.getProductsOnStock(pointOfSaleId);

	}

	@Override
	public List<IndividualisedProductItem> getAllProductsOnStock() {
		return this.ejbProxy.getAllProductsOnStock();

	}

	@Override
	public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
		return this.ejbProxy.getUnitsOnStock(product, pointOfSaleId);
	}

	@Override
	public int getTotalUnitsOnStock(IndividualisedProductItem product) {
		return this.ejbProxy.getTotalUnitsOnStock(product);
	}

	@Override
	public List<Long> getPointsOfSale(IndividualisedProductItem product) {
		return this.ejbProxy.getPointsOfSale(product);

	}


}
