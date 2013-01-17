package com.megginson.sloop.app;

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
	private String url = null;

	private InputStream input = null;

	/**
	 * Last data collection loaded.
	 */
	private DataCollection dataCollection = null;

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
		return url;
	}

	/**
	 * Set the URL for the next load.
	 * 
	 * @param url
	 *            the URL for the next load, as a string.
	 */
	public void setURL(String url) {
		if (url != this.url) {
			dataCollection = null;
			this.url = url;
		}
	}

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	@Override
	public DataCollection loadInBackground() {
		if (this.dataCollection == null) {
			DataCollection dataCollection = null;
			try {
				if (url != null) {
					input = new URL(url).openStream();
				}
				try {
					dataCollection = DataCollectionIO
							.readCSV(new InputStreamReader(input, Charset
									.forName("utf8")));
				} finally {
					if (url != null) {
						input.close();
					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			this.dataCollection = dataCollection;
		}
		return this.dataCollection;
	}

}
