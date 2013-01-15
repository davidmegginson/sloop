package com.megginson.sloop.app;

import java.io.IOException;
import java.io.InputStream;

import com.megginson.sloop.model.DataCollection;
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

	private DataCollection currentDataCollection = null;

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

	/**
	 * Get the currently-active data collection.
	 * 
	 * @return The currently-active data collection, or null if none has been
	 *         loaded yet.
	 * @see #getDataCollection(String)
	 * @see #getDataCollection(String, InputStream)
	 */
	public DataCollection getCurrentDataCollection() {
		return currentDataCollection;
	}

	/**
	 * Get a data collection and set it as the current one for the app.
	 * 
	 * Invokes {@link DataCollectionManager#getCollection(String)} to load the
	 * collection, then sets it as the current collection returned by
	 * {@link #getCurrentDataCollection()}.
	 * 
	 * @param url
	 *            The URL of the data collection.
	 * @return The newly-loaded data collection.
	 * @throws IOException
	 *             If there is an error loading the collection.
	 */
	public DataCollection getDataCollection(String url) throws IOException {
		DataCollection dataCollection = getDataCollectionManager()
				.getCollection(url);
		currentDataCollection = dataCollection;
		return dataCollection;
	}

	/**
	 * Get a data collection and set it as the current one for the app.
	 * 
	 * This method essentially lets the caller fake loading a file from a URL.
	 * 
	 * Invokes {@link DataCollectionManager#getCollection(String)} to load the
	 * collection, then sets it as the current collection returned by
	 * {@link #getCurrentDataCollection()}.
	 * 
	 * @param url
	 *            The URL of the data collection (not used for loading).
	 * @param input
	 *            The input stream containing the actual data collection.
	 * @return The newly-loaded data collection.
	 * @throws IOException
	 *             If there is an error loading the collection.
	 */
	public DataCollection getDataCollection(String url, InputStream input)
			throws IOException {
		DataCollection dataCollection = getDataCollectionManager()
				.getCollection(url, input);
		currentDataCollection = dataCollection;
		return dataCollection;
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
