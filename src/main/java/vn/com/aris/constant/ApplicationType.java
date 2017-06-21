package vn.com.aris.constant;


/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public enum ApplicationType {
	ANDROID(1), IOS(2);
	private int value;

	private ApplicationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
