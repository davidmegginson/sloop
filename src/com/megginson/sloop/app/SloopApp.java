package com.megginson.sloop.app;

import android.app.Application;

/**
 * Controller layer for the Sloop application.
 * 
 * This class defines the top-level app object for the Sloop app. It provides
 * access to various components of the app, and defines functionality.
 * 
 * @author David Megginson
 */
public class SloopApp extends Application {

	public SloopApp() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// save as a singleton
		app = this;
	}

	//
	// Static singleton access
	//

	public static SloopApp app;

}
