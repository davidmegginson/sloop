package com.megginson.sloop.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.megginson.sloop.io.DataCollectionIO;
import com.megginson.sloop.model.DataCollection;

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
			DataCollection dataCollection = null;
			try {
				InputStream input = null;
				// FIXME poor excuse for caching
				if (!mForceLoad && mUrl.equals(sLastUrl)) {
					return new DataCollectionResult(sLastDataCollection);
				}
				input = openURL(mUrl);
				if (input == null) {
					return new DataCollectionResult(mUrl);
				} else {
					try {
						dataCollection = DataCollectionIO
								.readCSV(new InputStreamReader(input, Charset
										.forName("utf8")));
					} finally {
						input.close();
					}
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

	/**
	 * Attempt to open a URL for reading a data collection.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private InputStream openURL(String url) throws IOException {
		HttpClient client = new DefaultHttpClient();

		// Check the content type
		HttpHead headRequest = new HttpHead(url);
		HttpResponse headResponse = client.execute(headRequest);
		Header contentType = headResponse.getFirstHeader("Content-Type");

		// Load only if it's text/csv
		if (contentType != null
				&& contentType.getValue().startsWith("text/csv")) {
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			return entity.getContent();
		} else {
			// return null to try the browser instead
			return null;
		}
	}

}
