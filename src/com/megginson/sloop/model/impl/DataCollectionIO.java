package com.megginson.sloop.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.ui.DataCollectionLoader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Static methods for creating data collections.
 * 
 * Typical usage is as follows:
 * 
 * <pre>
 * try {
 * 	DataCollection collection = DataCollectionIO.readCSV(myUrl);
 * } catch (RedirectException e1) {
 * 	// redirect to e.getRedirectURL();
 * } catch (IOException e2) {
 * 	// handle a general loading exception
 * }
 * </pre>
 * 
 * In Android, this loading has to happen asynchronously to avoid freezing the
 * UI: see {@link DataCollectionLoader#loadInBackground()}, which uses this
 * class to do the actual loading on a background thread..
 * 
 * @author David Megginson
 */
public class DataCollectionIO {

	/**
	 * Load a data collection from a URL.
	 * 
	 * @param url
	 *            the URL from which to load the data collection.
	 * @return the data collection loaded.
	 * @throws RedirectException
	 *             if Sloop can't handle the content type, and needs to redirect
	 *             to a general browser.
	 * @throws IOException
	 *             if a general loading error occurs.
	 */
	public static DataCollection readCSV(String url) throws IOException {
		DataCollection dataCollection = null;
		Reader input = new InputStreamReader(openURL(url));

		try {
			dataCollection = readCSV(input);
		} finally {
			input.close();
		}

		// get the fragment
		String fragment = new URL(url).getRef();
		if (fragment != null && fragment.length() > 0) {
			processFragment(dataCollection, fragment);
		}

		return dataCollection;
	}

	/**
	 * Load a data collection from a Reader.
	 * 
	 * @param input
	 *            the Reader containing the data collection.
	 * @return the data collection.
	 * @throws IOException
	 *             if there is an error reading the collection.
	 */
	private static DataCollection readCSV(Reader input) throws IOException {
		CSVReader csvReader = new CSVReader(input);
		String[] entries;

		String[] headers = csvReader.readNext();
		DataCollectionImpl dataCollectionImpl = new DataCollectionImpl(headers);

		while ((entries = csvReader.readNext()) != null) {
			dataCollectionImpl.addRecord(entries);
		}

		csvReader.close();

		return dataCollectionImpl;
	}

	/**
	 * Attempt to open a URL for reading a data collection.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 *             if there is an error opening the URL.
	 * @throws RedirectException
	 *             if Sloop can't handle the content type, and wants a browser
	 *             redirect.
	 */
	private static InputStream openURL(String url) throws IOException {
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
			// Ugly, but that's subclasses for ya
			throw new DataCollectionIO().new RedirectException(url);
		}
	}

	/**
	 * Process the fragment identifier and set up default filters for the data
	 * collection.
	 * 
	 * @param dataCollection
	 *            the data collection to filter.
	 * @param fragment
	 *            the URL fragment identifier.
	 */
	@SuppressWarnings("deprecation")
	private static void processFragment(DataCollection dataCollection,
			String fragment) {
		String filterStrings[] = fragment.split("&");
		for (String filterString : filterStrings) {
			if (filterString.contains("=")) {
				// FIXME error if too many parts
				String parts[] = fragment.split("=");
				String name = URLDecoder.decode(parts[0]);
				String value = URLDecoder.decode(parts[1]);
				dataCollection.putColumnFilter(name, new EqualsStringFilter(
						value));
			} else {
				String pattern = URLDecoder.decode(filterString);
				dataCollection.setTextFilter(new ContainsStringFilter(pattern));
			}
			dataCollection.setFilteringEnabled(true);
		}
	}

	/**
	 * Exception for a non-CSV URL that should cause a browser redirect.
	 * 
	 * The method that catches this exception should redirect to the URL at
	 * {@link #getRedirectUrl()}.
	 * 
	 * @author David Megginson
	 */
	public class RedirectException extends IOException {

		private static final long serialVersionUID = 1L;

		private String mRedirectUrl;

		/**
		 * Create a new URL redirect.
		 * 
		 * @param redirectUrl
		 *            the redirect URL (not a message).
		 */
		public RedirectException(String redirectUrl) {
			super("Redirect to " + redirectUrl);
			mRedirectUrl = redirectUrl;
		}

		/**
		 * Get the redirect (non-CSV) URL.
		 * 
		 * @return the redirect URL from the exception.
		 */
		public String getRedirectUrl() {
			return mRedirectUrl;
		}
	}

}
