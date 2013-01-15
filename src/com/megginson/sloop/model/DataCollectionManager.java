package com.megginson.sloop.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Manage data collections for the application.
 * 
 * TODO expire older files in the cache.
 * 
 * @author David Megginson
 */
public class DataCollectionManager {

	private HashMap<String, DataCollection> dataCollectionMap = new HashMap<String, DataCollection>();

	public DataCollectionManager() {
		super();
	}

	/**
	 * Fetch a data collection, loading only if it doesn't exist.
	 * 
	 * @param url
	 *            The URL of the CSV file.
	 * @return A parsed data collection.
	 * @throws IOException
	 *             if there is an error loading the collection.
	 */
	public DataCollection getCollection(String url) throws IOException {
		DataCollection dataCollection = dataCollectionMap.get(url);
		if (dataCollection == null) {
			dataCollection = reloadCollection(url);
		}
		return dataCollection;
	}

	/**
	 * Fetch a data collection from a specific input stream, loading only if it
	 * doesn't exist.
	 * 
	 * @param url
	 *            The URL of the CSV file (won't be accessed).
	 * @param input
	 *            The input stream containing the data.
	 * @return A parsed data collection.
	 * @throws IOException
	 *             If there is an error loading the collection.
	 */
	public DataCollection getCollection(String url, InputStream input)
			throws IOException {
		DataCollection dataCollection = dataCollectionMap.get(url);
		if (dataCollection == null) {
			dataCollection = reloadCollection(url, input);
		}
		return dataCollection;
	}

	/**
	 * Fetch a data collection, always reloading from source.
	 * 
	 * @param url
	 *            The URL of the CSV file.
	 * @return A parsed data collection.
	 * @throws IOException
	 *             if there is an error loading the collection.
	 */
	public DataCollection reloadCollection(String url) throws IOException {
		InputStream input = new URL(url).openStream();
		try {
			DataCollection dataCollection = reloadCollection(url, input);
			return dataCollection;
		} finally {
			input.close();
		}
	}

	/**
	 * Fetch a data collection from a specific input stream, always reloading
	 * from source.
	 * 
	 * @param url
	 *            The URL of the CSV file.
	 * @param input
	 *            The input stream containing the data.
	 * @return A parsed data collection.
	 * @throws IOException
	 *             if there is an error loading the collection.
	 */
	public DataCollection reloadCollection(String url, InputStream input)
			throws IOException {
		DataCollection dataCollection = DataCollectionIO
				.readCSV(new InputStreamReader(input, Charset.forName("utf8")));
		dataCollectionMap.put(url, dataCollection);
		return dataCollection;
	}

}
