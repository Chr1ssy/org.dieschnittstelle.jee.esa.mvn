package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Stateless
//@Remote(ProductCRUDRemote.class)
public class ProductCRUDStateless implements ProductCRUDRemote {

    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {

        em.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        //error on interpretation of querry
        Query query = em.createQuery("select  p from AbstractProduct as p");
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        update=em.merge(update);

        return update;
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return em.find(AbstractProduct.class,productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        em.remove(em.find(AbstractProduct.class,productID));
        return true;
    }
}
