package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;

@Local
@Path("/tracking")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CustomerTrackingLocal {

    @POST
    public void createTransaction(CustomerTransaction transaction);

    @GET
    public List<CustomerTransaction> readAllTransactions();

}
