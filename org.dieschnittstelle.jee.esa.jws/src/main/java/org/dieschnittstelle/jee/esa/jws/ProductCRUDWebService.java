package org.dieschnittstelle.jee.esa.jws;

import org.dieschnittstelle.jee.esa.entities.GenericCRUDExecutor;
import org.dieschnittstelle.jee.esa.entities.erp.ws.AbstractProduct;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import java.util.List;

public class ProductCRUDWebService implements IProductCRUDService {

	private GenericCRUDExecutor<AbstractProduct> productCRUD;

	public ProductCRUDWebService(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		// read out the dataAccessor
		this.productCRUD = (GenericCRUDExecutor<AbstractProduct>)servletContext.getAttribute("productCRUD");
	}
	

	@Override
	public List<AbstractProduct> readAllProducts() {
		return (List)this.productCRUD.readAllObjects();
	}

	@Override
	public AbstractProduct createProduct(AbstractProduct product) {
		return this.productCRUD.createObject(product);
	}

	@Override
	public boolean deleteProduct(long id) {
		return this.productCRUD.deleteObject(id);
	}


	@Override
	public AbstractProduct readProduct(long id) {
		AbstractProduct p = this.productCRUD.readObject(id);
		if (p != null) {
			return p;
		}
		else {
			throw new NotFoundException("the product with id " + id + " does not exist!");
		}
	}

	@Override
	public AbstractProduct updateProduct(AbstractProduct update) {
		return this.productCRUD.updateObject(update);
	}

}
