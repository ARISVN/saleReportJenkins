package vn.com.aris.model;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class AndroidAppKey {
	private String productId;
	private String currencyOfSale;
	private String orderChargedDate;

	public AndroidAppKey(String productId, String currencyOfSale,
			String orderChargedDate) {
		this.productId = productId;
		this.currencyOfSale = currencyOfSale;
		this.orderChargedDate = orderChargedDate;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public int hashCode() {
		int hashcode = 0;
		hashcode = productId.hashCode();
		hashcode += currencyOfSale.hashCode();
		hashcode += orderChargedDate.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof AndroidAppKey) {
			AndroidAppKey pp = (AndroidAppKey) obj;
			return (pp.productId.equals(this.productId)
					&& pp.currencyOfSale.equals(this.currencyOfSale) && pp.orderChargedDate
						.equals(this.orderChargedDate));
		} else {
			return false;
		}
	}
}
