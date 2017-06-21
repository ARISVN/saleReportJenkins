package vn.com.aris.model;

import java.util.HashMap;
import java.util.Map;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class RowUnique {
	private String sku;
	private String currencyOfProceeds;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCurrencyOfProceeds() {
		return currencyOfProceeds;
	}

	public void setCurrencyOfProceeds(String currencyOfProceeds) {
		this.currencyOfProceeds = currencyOfProceeds;
	}

	public RowUnique(String sku, String currencyOfProceeds) {
		this.sku = sku;
		this.currencyOfProceeds = currencyOfProceeds;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = sku.hashCode();
		hashcode += currencyOfProceeds.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof RowUnique) {
			RowUnique pp = (RowUnique) obj;
			return (pp.sku.equals(this.sku) && pp.currencyOfProceeds.equals(this.currencyOfProceeds));
		} else {
			return false;
		}
	}
	public String toString()
	{
		return sku+"/"+currencyOfProceeds;
	}
	
}
