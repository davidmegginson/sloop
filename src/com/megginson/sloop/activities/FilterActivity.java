package com.megginson.sloop.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataEntry;

public class FilterActivity extends Activity {
	
	String mHeaders[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		DataEntry entry = intent.getParcelableExtra("entry");
		mHeaders = intent.getStringArrayExtra("headers");
		
		setupForm(entry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_filter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setupForm(DataEntry entry) {
		if (mHeaders == null) {
			return;
		}
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.activity_filter);
		for (String header : mHeaders) {
			LayoutInflater inflater = LayoutInflater.from(this);
			LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.component_filter, null);
			TextView label = (TextView)layout.findViewById(R.id.label_filter);
			label.setText(header);
			mainLayout.addView(layout);
		}
	}

}
