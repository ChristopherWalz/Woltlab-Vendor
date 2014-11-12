package de.cwalz.android.woltlabVendor;

public interface ICallback {
	public void onSuccess(float newBalance);

	public void onFailure(String error);
}