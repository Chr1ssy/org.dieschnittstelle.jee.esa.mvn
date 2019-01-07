package org.dieschnittstelle.jee.esa.jrs;

import org.dieschnittstelle.jee.esa.entities.erp.ws.AbstractProduct;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/*
 * UE JRS2: 
 * deklarieren Sie hier Methoden fuer:
 * - die Erstellung eines Produkts
 * - das Auslesen aller Produkte
 * - das Auslesen eines Produkts
 * - die Aktualisierung eines Produkts
 * - das Loeschen eines Produkts
 * und machen Sie diese Methoden mittels JAX-RS Annotationen als WebService verfuegbar
 */

/*
 * UE JRS3: aendern Sie Argument- und Rueckgabetypen der Methoden von IndividualisedProductItem auf AbstractProduct
 */
@Path("/products")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IProductCRUDService {
	@POST
	AbstractProduct createProduct(AbstractProduct prod);
	@GET
	 List<AbstractProduct> readAllProducts();
	@PUT
	@Path("/{AbstractProductId}")
	AbstractProduct updateProduct(@PathParam("AbstractProductId")long id,AbstractProduct update);
	@DELETE
	@Path("/{AbstractProductId}")
	boolean deleteProduct(@PathParam("AbstractProductId")long id);
	@GET
	@Path("/{AbstractProductId}")
	AbstractProduct readProduct(@PathParam("AbstractProductId")long id);
			
}
