package org.dieschnittstelle.jee.esa.basics;


import org.dieschnittstelle.jee.esa.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.jee.esa.basics.annotations.DisplayAs;
import org.dieschnittstelle.jee.esa.basics.annotations.StockItemProxyImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.dieschnittstelle.jee.esa.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {

			showAttributes(((StockItemProxyImpl)consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	/*
	 * UE BAS2 
	 */

	private static void showAttributes(Object consumable) {
		String out = consumable.getClass().getSimpleName();
		for (Field field : consumable.getClass().getDeclaredFields()) {
            String fieldName =  field.getName();
			if(field.isAnnotationPresent(DisplayAs.class)){
			    out += " " +field.getAnnotation(DisplayAs.class).value()  +":";
			}else{
			out += " " +fieldName+":";
			}
			String getterName = "get"+  field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
			for (Method getter :consumable.getClass().getDeclaredMethods()){
				if(getterName.equals(getter.getName())){
					try {
						out+= " " +getter.invoke(consumable).toString();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}

			}

		}
		System.out.println(out);
	}

}
