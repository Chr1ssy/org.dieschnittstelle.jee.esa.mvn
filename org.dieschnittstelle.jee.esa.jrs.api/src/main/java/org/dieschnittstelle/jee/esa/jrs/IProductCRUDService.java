package org.dieschnittstelle.jee.esa.jrs;

import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

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
	 IndividualisedProductItem createProduct(IndividualisedProductItem prod);
	@GET
	 List<IndividualisedProductItem> readAllProducts();
	@PUT
	@Path("/{IndividualisedProductItemId}")
	IndividualisedProductItem updateProduct(@PathParam("IndividualisedProductItemId")long id,IndividualisedProductItem update);
	@DELETE
	@Path("/{IndividualisedProductItemId}")
	boolean deleteProduct(@PathParam("IndividualisedProductItemId")long id);
	@GET
	@Path("/{IndividualisedProductItemId}")
	IndividualisedProductItem readProduct(@PathParam("IndividualisedProductItemId")long id);
			
}
