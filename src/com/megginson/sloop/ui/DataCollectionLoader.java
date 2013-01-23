package com.megginson.sloop.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataCollectionIO;

/**
 * Load a {@link DataCollection} in a separate thread.
 * 
 * @author David Megginson
 */
public class DataCollectionLoader extends AsyncTaskLoader<DataCollection> {

	/**
	 * URL of the data collection to be loaded.
	 */
	private String mUrl = null;
	
	private String mResourceName = null;

	/**
	 * Last data collection loaded.
	 */
	private DataCollection mDataCollection = null;

	public DataCollectionLoader(Context context) {
		super(context);
	}

	/**
	 * Get the URL for the next load.
	 * 
	 * @return the URL for the next load, as a string, or null if none has been
	 *         set.
	 */
	public String getURL() {
		return mUrl;
	}

	/**
	 * Set the URL for the next load.
	 * 
	 * @param url
	 *            the URL for the next load, as a string.
	 */
	public void setURL(String url) {
		if (url != mUrl) {
			mDataCollection = null;
			mUrl = url;
		}
	}
	
	public String getResourceName() {
		return mResourceName;
	}
	
	public void setResourceName(String resourceName) {
		if (resourceName != mResourceName) {
			mDataCollection = null;
			mResourceName = resourceName;
		}
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		if (mDataCollection != null) {
			deliverResult(mDataCollection);
		}
		if (takeContentChanged() || mDataCollection == null) {
			forceLoad();
		}
		super.onStartLoading();
	}

	@Override
	public DataCollection loadInBackground() {
		if (mDataCollection == null) {
			DataCollection dataCollection = null;
			try {
				InputStream input = null;
				if (mResourceName != null) {
					input = getContext().getAssets().open(mResourceName);
				} else {
					input = new URL(mUrl).openStream();
				}
				try {
					dataCollection = DataCollectionIO
							.readCSV(new InputStreamReader(input, Charset
									.forName("utf8")));
				} finally {
						input.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			mDataCollection = dataCollection;
		}
		return mDataCollection;
	}

}
