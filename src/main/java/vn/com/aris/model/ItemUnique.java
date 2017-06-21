package vn.com.aris.model;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ItemUnique {
	private String itemCode;
	private String currencyOfProceeds;
	private String parentIdentifier;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getCurrencyOfProceeds() {
		return currencyOfProceeds;
	}
	public void setCurrencyOfProceeds(String currencyOfProceeds) {
		this.currencyOfProceeds = currencyOfProceeds;
	}
	public String getParentIdentifier() {
		return parentIdentifier;
	}
	public void setParentIdentifier(String parentIdentifier) {
		this.parentIdentifier = parentIdentifier;
	}
	public ItemUnique(String itemCode, String currencyOfProceeds,
			String parentIdentifier) {
		this.itemCode = itemCode;
		this.currencyOfProceeds = currencyOfProceeds;
		this.parentIdentifier = parentIdentifier;
	}
	
	public int hashCode() {
		int hashcode = 0;
		hashcode = itemCode.hashCode();
		hashcode += currencyOfProceeds.hashCode();
		hashcode += parentIdentifier.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ItemUnique) {
			ItemUnique pp = (ItemUnique) obj;
			return (pp.itemCode.equals(this.itemCode) && pp.currencyOfProceeds.equals(this.currencyOfProceeds)&&pp.parentIdentifier.equals(this.parentIdentifier));
		} else {
			return false;
		}
	}
	
}
