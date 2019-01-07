package org.dieschnittstelle.jee.esa.entities.erp.ws;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductType {

	BREAD, ROLL, PASTRY;
	
	@JsonCreator
	public static ProductType deserialise(String pt) {	
		return ProductType.valueOf(ProductType.class,pt);
	}
}
