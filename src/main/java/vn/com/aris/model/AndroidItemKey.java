package vn.com.aris.model;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class AndroidItemKey {
	private String productId;
	private String skuId;
	private String currencyOfSale;
	private String orderChargedDate;
	public int hashCode() {
		int hashcode = 0;
		hashcode = productId.hashCode();
		hashcode += currencyOfSale.hashCode();
		hashcode += orderChargedDate.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof AndroidItemKey) {
			AndroidItemKey pp = (AndroidItemKey) obj;
			return (pp.productId.equals(this.productId)
					&& pp.currencyOfSale.equals(this.currencyOfSale) && pp.orderChargedDate
						.equals(this.orderChargedDate)&&pp.skuId.equals(this.skuId));
		} else {
			return false;
		}
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getCurrencyOfSale() {
		return currencyOfSale;
	}

	public void setCurrencyOfSale(String currencyOfSale) {
		this.currencyOfSale = currencyOfSale;
	}

	public String getOrderChargedDate() {
		return orderChargedDate;
	}

	public void setOrderChargedDate(String orderChargedDate) {
		this.orderChargedDate = orderChargedDate;
	}

	public AndroidItemKey(String productId, String skuId,
			String currencyOfSale, String orderChargedDate) {
		this.productId = productId;
		this.skuId = skuId;
		this.currencyOfSale = currencyOfSale;
		this.orderChargedDate = orderChargedDate;
	}
	
}
