package org.dieschnittstelle.jee.esa.ejb.client.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.erp.ws.AbstractProduct;

public class ShoppingSessionFacadeClient implements ShoppingBusinessDelegate {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShoppingSessionFacadeClient.class);

	/*
	 * TODO use a proxy for the ShoppingSessionFacadeRemote interface
	 */

	public ShoppingSessionFacadeClient() {
		/* TODO: instantiate the proxy using the EJBProxyFactory (see the other client classes) */
	}

	/* TODO: implement the following methods using the proxy */

	@Override
	public void setTouchpoint(AbstractTouchpoint touchpoint) {
	
	}

	@Override
	public void setCustomer(Customer customer) {
	
	}

	@Override
	public void addProduct(AbstractProduct product, int units) {
	
	}

	@Override
	public void purchase() throws ShoppingException {
	
	}

}
