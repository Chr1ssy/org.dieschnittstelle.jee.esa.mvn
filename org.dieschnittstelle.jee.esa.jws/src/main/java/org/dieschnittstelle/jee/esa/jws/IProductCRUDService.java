package org.dieschnittstelle.jee.esa.jws;

import org.dieschnittstelle.jee.esa.entities.erp.ws.AbstractProduct;

import javax.ws.rs.*;
import java.util.List;

@Path("/products")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IProductCRUDService {
	
	@GET
	public List<?> readAllProducts();
	
	@POST
	public AbstractProduct createProduct(AbstractProduct product);
	
	@DELETE
	@Path("/{productId}")
	public boolean deleteProduct(@PathParam("productId") long id);
	
	@GET
	@Path("/{productId}")
	public AbstractProduct readProduct(@PathParam("productId") long id);

	@PUT
	AbstractProduct updateProduct(AbstractProduct update);
	
}
