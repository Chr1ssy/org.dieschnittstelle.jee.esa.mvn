package org.dieschnittstelle.jee.esa.jrs;

import java.util.List;

import org.dieschnittstelle.jee.esa.entities.GenericCRUDExecutor;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/*
UE JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	private GenericCRUDExecutor<AbstractProduct> productCRUD;

	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request){
		this.productCRUD =(GenericCRUDExecutor<AbstractProduct>) servletContext.getAttribute("productCRUD");
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		// TODO Auto-generated method stub
		return (IndividualisedProductItem) this.productCRUD.createObject(prod);
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		// TODO Auto-generated method stub
		return (List)this.productCRUD.readAllObjects();
	}

	@Override
	public IndividualisedProductItem updateProduct(long id, IndividualisedProductItem update) {
		IndividualisedProductItem old = (IndividualisedProductItem) this.productCRUD.readObject(id);
		old.setName(update.getName());
		old.setExpirationAfterStocked(update.getExpirationAfterStocked());
		old.setProductType(update.getProductType());
		old.setPrice(update.getPrice());
		old.onPostUpdate();
		return (IndividualisedProductItem) this.productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
		// TODO Auto-generated method stub
		return this.productCRUD.deleteObject(id);
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {
		// TODO Auto-generated method stub
		return (IndividualisedProductItem) this.productCRUD.readObject(id);
	}
	
}
