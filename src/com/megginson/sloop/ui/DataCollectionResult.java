package com.megginson.sloop.ui;

import com.megginson.sloop.activities.MainActivity;
import com.megginson.sloop.model.DataCollection;

/**
 * The result of loading a {@link DataCollectionImpl}
 * 
 * The {@link DataCollectionLoader} class uses this class to represent its
 * result
 * 
 * Because the loader task runs on a background thread, we can't simply throw an
 * exception from it on error; instead, we need to wrap our result (a
 * {@link DataCollectionImpl}) in a class like this, together with information about
 * error status, then check it in the handler method in the main thread when the
 * load is complete.
 * 
 * @author David Megginson
 * @see DataCollectionLoader#loadInBackground()
 * @see MainActivity#onLoadFinished(android.content.Loader,
 *      DataCollectionResult)
 */
public class DataCollectionResult {

	private DataCollection mDataCollection;
	
	private String mRedirectUrl;
	
	private Throwable mThrowable;
	
	/**
	 * Create the result of a successful load.
	 * 
	 * @param dataCollection
	 *            The data collection to return.
	 */
	public DataCollectionResult(DataCollection dataCollection) {
		mDataCollection = dataCollection;
		mRedirectUrl = null;
		mThrowable = null;
	}
	
	/**
	 * Result for a load that needs to redirect to a non-CSV URL.
	 * 
	 * @param redirectUrl the URL to send to the browser.
	 */
	public DataCollectionResult(String redirectUrl) {
		mDataCollection = null;
		mRedirectUrl = redirectUrl;
		mThrowable = null;
	}

	/**
	 * Create the result of an unsuccessful load.
	 * 
	 * @param errorMessage
	 *            The error message.
	 */
	public DataCollectionResult(Throwable throwable) {
		mDataCollection = null;
		mRedirectUrl = null;
		mThrowable = throwable;
	}

	/**
	 * Test whether the load was successful.
	 * 
	 * @return true if there is a {@link DataCollectionImpl} available; false if
	 *         there is an error message.
	 */
	public boolean hasError() {
		return mThrowable != null;
	}

	/**
	 * Get the data collection from a successful load.
	 * 
	 * @return A data collection if the load was successful; null otherwise.
	 * @see #hasError()
	 */
	public DataCollection getDataCollection() {
		return mDataCollection;
	}
	
	public String getRedirectUrl() {
		return mRedirectUrl;
	}

	/**
	 * Get the error message from an unsuccessful load.
	 * 
	 * @return An error message if the load was unsuccessful; null otherwise.
	 * @see #hasError()
	 */
	public Throwable getThrowable() {
		return mThrowable;
	}

}
