package com.megginson.sloop.model;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Static methods for creating data collections.
 * 
 * @author David Megginson
 */
public class DataCollectionIO {
	
	public static DataCollection readCSV (Reader input) throws IOException {
		DataCollection dataCollection = new DataCollection();
		
		CSVReader csvReader = new CSVReader(input);
		String [] entries;
		
		String [] headers = csvReader.readNext();
		while ((entries = csvReader.readNext()) != null) {
			DataRecord dataRecord = new DataRecord();
			for (int i =0; i < headers.length && i < entries.length; i++) {
				dataRecord.addEntry(headers[i], entries[i]);
			}
			dataCollection.add(dataRecord);
		}
		
		csvReader.close();
		
		return dataCollection;
	}

}
