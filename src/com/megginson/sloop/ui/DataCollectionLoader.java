package com.megginson.sloop.ui;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.impl.DataCollectionIO;
import com.megginson.sloop.model.impl.DataCollectionIO.RedirectException;

/**
 * Load a {@link DataCollectionImpl} in a separate thread.
 * 
 * @author David Megginson
 */
public class DataCollectionLoader extends AsyncTaskLoader<DataCollectionResult> {

	// Poor-man's cache

	private static String sLastUrl = null;

	private static DataCollection sLastDataCollection = null;

	/**
	 * URL of the data collection to be loaded.
	 */
	private String mUrl = null;
	
	/**
	 * Should we force load, even if we already have the data?
	 */
	private boolean mForceLoad = false;

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
	
	public boolean isForceLoad() {
		return mForceLoad;
	}
	
	public void setForceLoad(boolean forceLoad) {
		mForceLoad = forceLoad;
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		if (mDataCollection != null) {
			deliverResult(new DataCollectionResult(mDataCollection));
		}
		if (takeContentChanged() || mDataCollection == null) {
			forceLoad();
		}
		super.onStartLoading();
	}

	@Override
	public DataCollectionResult loadInBackground() {
		if (mDataCollection == null) {
			try {
				// FIXME poor excuse for caching
				if (!mForceLoad && mUrl.equals(sLastUrl)) {
					mDataCollection = sLastDataCollection;
				} else {
					mDataCollection = DataCollectionIO.readCSV(mUrl);
				}
			} catch (RedirectException e) {
				return new DataCollectionResult(e.getRedirectUrl());
			} catch (Throwable t) {
				return new DataCollectionResult(t);
			}

			// FIXME poor excuse for caching
			sLastDataCollection = mDataCollection;
			sLastUrl = mUrl;
		}
		return new DataCollectionResult(mDataCollection);
	}

}
