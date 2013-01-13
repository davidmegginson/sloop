package com.megginson.sloop.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.res.AssetManager;

/**
 * Create a dummy data collection for testing.
 * 
 * TODO delete this class later.
 * 
 * @author David Megginson
 */
public class DummyDataCollectionFactory {
	
	public static DataCollection readDataCollection(AssetManager assetManager, String assetName) throws IOException {
		InputStream input = assetManager.open(assetName);
		DataCollection dataCollection = DataCollectionFactory.readCSV(new InputStreamReader(input, Charset.forName("utf8")));
		input.close();
		return dataCollection;
	}

	/**
	 * Create a dummy data collection.
	 * 
	 * @return A dummy data collection with 100 rows and 20 columns.
	 */
	public static DataCollection createDataCollection() {
		DataCollection dataCollection = new DataCollection();
		for (int row = 1; row <= 100; row++) {
			dataCollection.add(createDataRecord(row));
		}
		return dataCollection;
	}

	private static DataRecord createDataRecord(int row) {
		DataRecord dataRecord = new DataRecord();
		for (int column = 1; column <= 20; column++) {
			dataRecord.add(createDataEntry(row, column));
		}
		return dataRecord;
	}

	private static DataEntry createDataEntry(int row, int column) {
		return new DataEntry(
				"Label-" + row + '-' + column,
				"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
	}

}
