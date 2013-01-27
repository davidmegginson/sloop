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

/**
 * Sloop's main UI activity (browse a data set).
 * 
 * @author David Megginson
 */
public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<DataCollection> {

	//
	// Saveable state
	//

	/**
	 * The URL of the current data set.
	 */
	private String mUrl = null;

	//
	// UI components.
	//

	/**
	 * The {@link PagerAdapter} for the current data collection.
	 */
	private DataCollectionPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the data collection.
	 */
	private ViewPager mViewPager;

	/**
	 * The text field holding the selected URL.
	 */
	private EditText mUrlField;

	//
	// Activity lifecycle methods.
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);

		// Not necessary for 4.2, but for 4.0, this has to be called after the
		// content view has been set, or else we start with the spinner active.
		setProgressBarIndeterminateVisibility(false);

		// Set up the field where the user enters a URL.
		setupUrlField();
		
		// Set up the main display area
		setupPager();

		// Finally, restore any saved state
		if (savedInstanceState != null) {
			mUrl = savedInstanceState.getString("url");
			if (mUrl != null && mUrl.length() > 0) {
				loadData(mUrl);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("url", mUrl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	//
	// Loader callbacks
	//

	@Override
	public Loader<DataCollection> onCreateLoader(int id, Bundle args) {
		System.err.println("Create loader: "
				+ (args == null ? "[empty]" : args.getString("url")));
		// only one loader for now, so ignore id
		DataCollectionLoader loader = new DataCollectionLoader(
				getApplicationContext());
		if (args != null) {
			loader.setURL(args.getString("url"));
			loader.setResourceName(args.getString("resourceName"));
		}
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
	public void onLoaderReset(Loader<DataCollection> loader) {
		mPagerAdapter.setDataCollection(null);
	}

	//
	// Internal utility methods
	//
	
	/**
	 * Set up the URL text field and its clear button.
	 */
	private void setupUrlField() {
		// Handle the enter key in the URL field
		mUrlField = (EditText) findViewById(R.id.urlField);
		mUrlField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						v.clearFocus();
						loadData(v.getText().toString());
						return true;
					}
				});
		mUrlField.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Button clearUrlButton = (Button) findViewById(R.id.clearURL);
				if (hasFocus) {
					clearUrlButton.setVisibility(View.VISIBLE);
				} else {
					clearUrlButton.setVisibility(View.GONE);
				}
			}
		});
		
		Button clearButton = (Button)findViewById(R.id.clearURL);
		clearButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUrlField.setText(null);
			}
		});

	}

	/**
	 * Set up the main ViewPager.
	 */
	private void setupPager() {
		mPagerAdapter = new DataCollectionPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the data collection adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
	}

	/**
	 * Load data from a new URL.
	 * 
	 * @param url
	 *            the URL, or null (which will display an error).
	 */
	private void loadData(String url) {
		System.err.println("Load data: " + url);
		if (url != null && url.length() > 0) {
			mUrl = url;
			mUrlField.setText(url);
			Bundle args = new Bundle();
			args.putString("url", url);
			getLoaderManager().restartLoader(0, args, MainActivity.this);
			setProgressBarIndeterminateVisibility(true);
			hideKeyboard();
		} else {
			showError("Please enter a web address");
		}
	}

	/**
	 * Hide the soft keyboard.
	 */
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
	}

	/**
	 * Report an error message.
	 * 
	 * @param message
	 *            The error message as a string.
	 */
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
