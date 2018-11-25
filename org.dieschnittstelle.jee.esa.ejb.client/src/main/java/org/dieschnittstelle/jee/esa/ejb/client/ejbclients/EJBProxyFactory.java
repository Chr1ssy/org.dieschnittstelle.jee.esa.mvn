package org.dieschnittstelle.jee.esa.ejb.client.ejbclients;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.client.Constants;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.erp.ws.AbstractProduct;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/*
 * Created by master on 17.02.17.
 *
 * creates ejb proxies given a uri and a remote interface
 * alternatively creates a rest service proxy for the interface
 *
 * does some checks on the uris and interfaces
 * realised as singleton
 */
public class EJBProxyFactory {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(EJBProxyFactory.class);

    // some custom runtime exception
    private static class EJBProxyException extends RuntimeException {

        public EJBProxyException(String msg) {
            super(msg);
        }

        public EJBProxyException(String msg, Throwable cause) {
            super(msg, cause);
        }

    }

    // the instance
    private static EJBProxyFactory instance;

    // public method for initialising the factory
    public static void initialise(String webAPIBaseUrl,boolean useWebAPI) {
        logger.info("initialise(): webAPIBaseUrl: " + webAPIBaseUrl);
        logger.info("initialise(): useWebAPIAsDefault: " + useWebAPI);
        if (instance != null) {
            logger.warn("initialise() was called on EJBProxyFactory, but there already exists an instance. Will not overwrite it.");
            return;
        }
        instance = new EJBProxyFactory(webAPIBaseUrl,useWebAPI);
    }

    // alternative initialisation which will result in properties (base url and web api default usage) being read from a configuration file
    public static void initialise() {
        if (instance != null) {
            logger.warn("initialise() was called on EJBProxyFactory, but there already exists an instance. Will not overwrite it.");
            return;
        }

        try {
            Properties props = new Properties();
            props.load(EJBProxyFactory.class.getClassLoader().getResourceAsStream("esa-ejb-client.properties"));
            logger.info("initialise(): loaded properties: " + props);

            initialise(props.getProperty("esa.ejb.client.webAPIBaseUrl", Constants.WEB_API_BASE_URL),Boolean.valueOf(props.getProperty("esa.ejb.client.useWebAPIAsDefault","false")));
        }
        catch (Exception e) {
            throw new EJBProxyException("initialise(): got exception trying to read properties from file: " + e,e);
        }

    }


    // this gives us the instance
    public static EJBProxyFactory getInstance() {
        if (instance == null) {
            throw new EJBProxyException("getInstance() was invoked, but no instance has been created yet. Need to call initialise() before");
        }
        return instance;
    }

    // when instantiating the factory, we specify whether ejb proxies or rest service proxies shall be created
    private boolean useWebAPIAsDefault;

    // this is the jndi context to be used
    private Context jndiContext;

    // this is the client-side representation of the web api, which gives access to the different services offered via this api
    private ResteasyWebTarget webAPI;

    private EJBProxyFactory(String webAPIBaseUrl,boolean useWebAPIAsDefault) {
        this.useWebAPIAsDefault = useWebAPIAsDefault;

        // we check whether polymorphism is handled for products and touchpoints
        if (useWebAPIAsDefault && (!AbstractTouchpoint.class.isAnnotationPresent(JsonTypeInfo.class) || !AbstractProduct.class.isAnnotationPresent(JsonTypeInfo.class))) {
            throw new EJBProxyException("access to web api cannot be supported as polymorphism is not handled completely. Check annotations on AbstractTouchpoint and AbstractProduct! Remember to also restart the server-side application once changes have been made.");
        }
        else {
            logger.info("consistency check of datamodel classes succeeded. Both ejb and web api access should work.");
        }

        if (useWebAPIAsDefault) {
            System.out.println("\n%%%%%%%%%%%% EJBProxyFactory: EJBs will be accessed via REST API %%%%%%%%%%%\n\n");
        }
        else {
            System.out.println("\n%%%%%%%%%%%% EJBProxyFactory: EJBs will be accessed via EJB proxies %%%%%%%%%%%\n\n");
        }

        try {
            // we both instantiate the jndiContext and the webAPI
            this.jndiContext = new InitialContext();

            // this is the webAPI instantiation - here, we hard-code the baseUrl for the webAPI, could be passed as an argument, though
            ResteasyClient client = new ResteasyClientBuilder().build();
            this.webAPI = client.target(webAPIBaseUrl);
        } catch (Exception e) {
            throw new EJBProxyException("got exception trying to instantiate proxy factory: " + e, e);
        }

    }

    // use the default setting for whether ejb or rest service proxies shall be created
    public <T> T getProxy(Class<T> ejbInterface, String ejbUri) {
        return getProxy(ejbInterface, ejbUri, this.useWebAPIAsDefault);
    }

    // allow to specify what kind of proxy shall be created
    public <T> T getProxy(Class<T> ejbInterface, String ejbUri, boolean useWebAPI) {
        T proxy;

        try {
            if (useWebAPI) {
                proxy = this.webAPI.proxy(ejbInterface);
            } else {
                if (!ejbUri.startsWith("ejb:")) {
                    throw new EJBProxyException("malformed ejbUri: " + ejbUri + ". It needs to start with: \"ejb:\"");
                }
                proxy = (T) this.jndiContext.lookup(ejbUri);
            }
        } catch (Exception e) {
            throw new EJBProxyException("got exception trying to create a " + (useWebAPI ? " web service " : " EJB ") + " proxy for interface " + ejbInterface + ": " + e, e);
        }

        logger.info("getProxy(): returning proxy for " + ejbInterface + ": " + proxy);

        return proxy;
    }

    // we make transparent whether we use the webAPI as default or not
    public boolean usesWebAPIAsDefault() {
        return this.useWebAPIAsDefault;
    }

}
