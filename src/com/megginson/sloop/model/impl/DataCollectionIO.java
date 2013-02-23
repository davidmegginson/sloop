package com.megginson.sloop.model.impl;

import java.io.IOException;
import java.io.Reader;


import au.com.bytecode.opencsv.CSVReader;

/**
 * Static methods for creating data collections.
 * 
 * @author David Megginson
 */
public class DataCollectionIO {
	
	public static DataCollectionImpl readCSV (Reader input) throws IOException {
		CSVReader csvReader = new CSVReader(input);
		String [] entries;
		
		String [] headers = csvReader.readNext();
		DataCollectionImpl dataCollectionImpl = new DataCollectionImpl(headers);
		
		while ((entries = csvReader.readNext()) != null) {
			dataCollectionImpl.addRecord(entries);
		}
		
		csvReader.close();
		
		return dataCollectionImpl;
	}

}
