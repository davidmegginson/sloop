package com.megginson.sloop.ui;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<DataCollection> {

	public static String SAMPLE_FILE = "pwgsc_pre-qualified_supplier_data.csv";

	/**
	 * The {@link PagerAdapter} for the current data collection.
	 */
	private DataCollectionPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the data collection.
	 */
	private ViewPager mViewPager;

	private EditText mUrlField;

	private Button mGoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);

		// Not necessary for 4.2, but for 4.0, this has to be called after the
		// content view has been set, or else we start with the spinner active.
		setProgressBarIndeterminateVisibility(false);

		mPagerAdapter = new DataCollectionPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the data collection adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);

		mUrlField = (EditText) findViewById(R.id.urlField);
		mUrlField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						loadData(v.getText().toString());
						return true;
					}
				});

		mGoButton = (Button) findViewById(R.id.goButton);
		mGoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData(mUrlField.getText().toString());
			}
		});
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
		DataCollectionLoader loader = new DataCollectionLoader(
				getApplicationContext());
		loader.setURL(args.getString("url"));
		loader.setResourceName(args.getString("resourceName"));
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<DataCollection> loader,
			DataCollection dataCollection) {
		MainActivity.this.setProgressBarIndeterminateVisibility(false);
		if (dataCollection == null) {
			showError("Error loading data");
		} else {
			mPagerAdapter.setDataCollection(dataCollection);
		}
	}

	@Override
	public void onLoaderReset(Loader<DataCollection> dataCollection) {
		// TODO clear old data from the viewpager
	}

	private void loadData(String url) {
		if (url != null && url.length() > 0) {
			Bundle args = new Bundle();
			args.putString("url", url);
			getLoaderManager().restartLoader(0, args, MainActivity.this);
			setProgressBarIndeterminateVisibility(true);
			hideKeyboard();
		} else {
			showError("Please enter a web address");
		}
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
	}

	private void showError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

}
