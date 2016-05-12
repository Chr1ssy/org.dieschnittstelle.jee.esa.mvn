package org.dieschnittstelle.jee.esa.entities.erp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;


public class Campaign extends AbstractProduct implements Serializable {

	protected static Logger logger = Logger.getLogger(Campaign.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4407600000386810001L;

	private List<ProductBundle> bundles;

	public Campaign() {
		logger.info("<constructor>"); this.bundles = new ArrayList<ProductBundle>();
	}

	public Campaign(String name) {
		super(name);
		this.bundles = new ArrayList<ProductBundle>();
	}

	public Collection<ProductBundle> getBundles() {
		return this.bundles;
	}

	public void setBundles(List<ProductBundle> bundles) {
		this.bundles = bundles;
	}

	public void addBundle(ProductBundle bundle) {
		this.bundles.add(bundle);
	}

	public String toString() {
		return "{Campaign " + this.getId() + ", " + this.getName() + ", "
				+ this.bundles + "}";
	}

//	public boolean equals(Object other) {
//		return EqualsBuilder.reflectionEquals(this, other,
//				new String[] { "bundles" })
//				&& LangUtils
//						.setequals(this.bundles, ((Campaign) other).bundles);
//	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this,
				new String[] { "bundles" });
	}

}
