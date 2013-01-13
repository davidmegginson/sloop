package com.megginson.sloop.app;

import java.io.IOException;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DummyDataCollectionFactory;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	DataCollectionPagerAdapter mDataRecordPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create a DataCollection and wrap it in a pager adapter.
		// DataCollection dataCollection = DummyDataCollectionFactory.createDataCollection();
		DataCollection dataCollection;
		try {
			dataCollection = DummyDataCollectionFactory.readDataCollection(getAssets(), "pwgsc_pre-qualified_supplier_data.csv");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
		mDataRecordPagerAdapter = new DataCollectionPagerAdapter(
				getSupportFragmentManager(), dataCollection);

		// Set up the ViewPager with the data collection adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDataRecordPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
