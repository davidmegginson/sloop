package com.megginson.sloop.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Utility methods for activities.
 * 
 * @author David Megginson
 */
class ActivitiesUtil {

	/**
	 * URL for general help.
	 */
	protected final static String HELP_URL = "http://sloopdata.org";

	/**
	 * Show general help in the browser.
	 * 
	 * @param fromActivity
	 *            the source activity.
	 */
	protected static void doHelp(Activity fromActivity) {
		doLaunchBrowser(fromActivity, HELP_URL);
	}

	/**
	 * End this activity and launch a non-CSV URL.
	 * 
	 * This method invokes {@link #finish()}.
	 * 
	 * @param fromActivity
	 *            the source activity.
	 * @param url
	 *            The URL to launch in the browser (etc.).
	 */
	protected static void doLaunchBrowser(Activity fromActivity, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		fromActivity.startActivity(intent);
	}
	
	/**
	 * Hide the soft keyboard.
	 */
	protected static void doHideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}



}
