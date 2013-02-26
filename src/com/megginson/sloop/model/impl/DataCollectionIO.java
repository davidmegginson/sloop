package com.megginson.sloop.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Static methods for creating data collections.
 * 
 * @author David Megginson
 */
public class DataCollectionIO {

	public static DataCollectionImpl readCSV(String url) throws IOException {
		Reader input = new InputStreamReader(openURL(url));
		try {
			return readCSV(input);
		} finally {
			input.close();
		}
	}

	public static DataCollectionImpl readCSV(Reader input) throws IOException {
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
	 * Exception for a non-CSV URL that should cause a browser redirect.
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
