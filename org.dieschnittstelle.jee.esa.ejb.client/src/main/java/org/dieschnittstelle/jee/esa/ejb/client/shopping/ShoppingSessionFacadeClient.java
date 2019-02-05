package org.dieschnittstelle.jee.esa.ejb.client.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.EJBProxyFactory;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping.ShoppingSessionFacadeRemote;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

public class ShoppingSessionFacadeClient implements ShoppingBusinessDelegate {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShoppingSessionFacadeClient.class);

	/*
	 * Use a ejbProxy for the ShoppingSessionFacadeRemote interface
	 */
	private ShoppingSessionFacadeRemote ejbProxy;

	public ShoppingSessionFacadeClient() {
		/* Instantiate the ejbProxy using the EJBProxyFactory (see the other client classes) */
		ejbProxy = EJBProxyFactory.getInstance().getProxy(ShoppingSessionFacadeRemote.class, "ejb:org.dieschnittstelle.jee.esa.ejb/org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm/ShoppingSessionFacadeStateful!org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping.ShoppingSessionFacadeRemote?stateful");
	}

	/* Implement the following methods using the ejbProxy */

	@Override
	public void setTouchpoint(AbstractTouchpoint touchpoint) {
		ejbProxy.setTouchpoint(touchpoint);
	}

	@Override
	public void setCustomer(Customer customer) {
		ejbProxy.setCustomer(customer);
	}

	@Override
	public void addProduct(AbstractProduct product, int units) {
		ejbProxy.addProduct(product, units);
	}

	@Override
	public void purchase() throws ShoppingException {
		ejbProxy.purchase();
	}

}
