package com.megginson.sloop.ui;

import com.megginson.sloop.model.DataCollection;

/**
 * The result of loading a {@link DataCollection}
 * 
 * The {@link DataCollectionLoader} class uses this class to represent its
 * result
 * 
 * Because the loader task runs on a background thread, we can't simply throw an
 * exception from it on error; instead, we need to wrap our result (a
 * {@link DataCollection}) in a class like this, together with information about
 * error status, then check it in the handler method in the main thread when the
 * load is complete.
 * 
 * @author David Megginson
 * @see DataCollectionLoader#loadInBackground()
 * @see MainActivity#onLoadFinished(android.content.Loader,
 *      DataCollectionResult)
 */
class DataCollectionResult {

	private DataCollection mDataCollection;

	private String mErrorMessage = null;

	/**
	 * Create the result of a successful load.
	 * 
	 * @param dataCollection
	 *            The data collection to return.
	 */
	DataCollectionResult(DataCollection dataCollection) {
		mDataCollection = dataCollection;
		mErrorMessage = null;
	}

	/**
	 * Create the result of an unsuccessful load.
	 * 
	 * @param errorMessage
	 *            The error message.
	 */
	DataCollectionResult(String errorMessage) {
		mErrorMessage = errorMessage;
		mDataCollection = null;
	}

	/**
	 * Test whether the load was successful.
	 * 
	 * @return true if there is a {@link DataCollection} available; false if
	 *         there is an error message.
	 */
	boolean hasError() {
		return mErrorMessage != null;
	}

	/**
	 * Get the data collection from a successful load.
	 * 
	 * @return A data collection if the load was successful; null otherwise.
	 * @see #hasError()
	 */
	DataCollection getDataCollection() {
		return mDataCollection;
	}

	/**
	 * Get the error message from an unsuccessful load.
	 * 
	 * @return An error message if the load was unsuccessful; null otherwise.
	 * @see #hasError()
	 */
	String getErrorMessage() {
		return mErrorMessage;
	}

}
