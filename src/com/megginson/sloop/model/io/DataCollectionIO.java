package com.megginson.sloop.model.io;

import java.io.IOException;
import java.io.Reader;

import com.megginson.sloop.model.DataCollection;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Static methods for creating data collections.
 * 
 * @author David Megginson
 */
public class DataCollectionIO {
	
	public static DataCollection readCSV (Reader input) throws IOException {
		CSVReader csvReader = new CSVReader(input);
		String [] entries;
		
		String [] headers = csvReader.readNext();
		DataCollection dataCollection = new DataCollection(headers);
		
		while ((entries = csvReader.readNext()) != null) {
			dataCollection.addRecord(entries);
		}
		
		csvReader.close();
		
		return dataCollection;
	}

}
