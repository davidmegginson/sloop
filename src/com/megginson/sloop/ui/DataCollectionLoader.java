package com.megginson.sloop.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.io.DataCollectionIO;

/**
 * Load a {@link DataCollection} in a separate thread.
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
			DataCollection dataCollection = null;
			try {
				InputStream input = null;
				if (mResourceName != null) {
					input = getContext().getAssets().open(mResourceName);
				} else {
					// FIXME poor excuse for caching
					if (mUrl.equals(sLastUrl)) {
						return new DataCollectionResult(sLastDataCollection);
					}
					input = openURL(mUrl);
				}
				try {
					if (input != null) {
						dataCollection = DataCollectionIO
								.readCSV(new InputStreamReader(input, Charset
										.forName("utf8")));
					}
				} finally {
					input.close();
				}
			} catch (Throwable t) {
				return new DataCollectionResult(t);
			}
			mDataCollection = dataCollection;

			// FIXME poor excuse for caching
			sLastDataCollection = mDataCollection;
			sLastUrl = mUrl;
		}
		return new DataCollectionResult(mDataCollection);
	}

	private InputStream openURL(String url) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return entity.getContent();
		} else {
			return null;
		}
	}

}
