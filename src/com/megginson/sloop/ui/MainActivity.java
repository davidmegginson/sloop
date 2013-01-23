package com.megginson.sloop.ui;

import java.io.IOException;
import java.io.InputStream;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.megginson.sloop.R;
import com.megginson.sloop.app.DataCollectionLoader;
import com.megginson.sloop.app.SloopApp;
import com.megginson.sloop.model.DataCollection;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<DataCollection> {

	public static String SAMPLE_FILE = "pwgsc_pre-qualified_supplier_data.csv";

	/**
	 * The {@link PagerAdapter} for the current data collection.
	 */
	DataCollectionPagerAdapter mDataCollectionPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the data collection.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create a DataCollection and wrap it in a pager adapter.
		DataCollection dataCollection = null;
		try {
			InputStream input = getAssets().open(
					"pwgsc_pre-qualified_supplier_data.csv");
			try {
				dataCollection = SloopApp.app.getDataCollection(SAMPLE_FILE, input);
			} finally {
				input.close();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
		mDataCollectionPagerAdapter = new DataCollectionPagerAdapter(
				getSupportFragmentManager(), dataCollection);

		// Set up the ViewPager with the data collection adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDataCollectionPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public Loader<DataCollection> onCreateLoader(int id, Bundle args) {
		// only one loader for now, so ignore id
		// XXX do we have to do anything with args?
		DataCollectionLoader loader = new DataCollectionLoader(getApplicationContext());
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<DataCollection> loader, DataCollection dataCollection) {
		// TODO update the viewpager with the new data
	}

	@Override
	public void onLoaderReset(Loader<DataCollection> dataCollection) {
		// TODO clear old data from the viewpager
	}

}
