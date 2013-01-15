package com.megginson.sloop.app;

import com.megginson.sloop.model.DataCollectionManager;

/**
 * Controller layer for the Sloop application.
 * 
 * This class defines the top-level app object for the Sloop app. It provides
 * access to various components of the app, and defines functionality.
 * 
 * @author David Megginson
 */
public class SloopApp {

	private DataCollectionManager dataCollectionManager = new DataCollectionManager();

	/**
	 * Private constructor to force a singleton.
	 */
	private SloopApp() {
		super();
	}

	/**
	 * Get the manager for loading data collections.
	 * 
	 * This is the main access point to the data model. See the
	 * {@link DataCollectionManager} class for details.
	 * 
	 * @return The applications data collection manager.
	 */
	public DataCollectionManager getDataCollectionManager() {
		return dataCollectionManager;
	}
	
	//
	// Static singleton access
	//

	private static SloopApp app = null;

	/**
	 * Return a singleton instance of the Sloop app.
	 * 
	 * @return The Sloop app (created if needed).
	 */
	public static SloopApp getInstance() {
		if (app == null) {
			app = new SloopApp();
		}
		return app;
	}

}
