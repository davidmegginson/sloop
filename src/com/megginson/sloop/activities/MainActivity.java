package com.megginson.sloop.activities;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.ui.DataCollectionLoader;
import com.megginson.sloop.ui.DataCollectionPagerAdapter;
import com.megginson.sloop.ui.DataCollectionResult;

/**
 * Sloop's main UI activity (browse a data set).
 * 
 * @author David Megginson
 */
public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<DataCollectionResult> {

	public final static String PREFERENCE_GROUP_MAIN = "main";

	public final static String PREFERENCE_URL = "url";

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
	
	/**
	 * The seek bar for scrolling through the collection.
	 */
	private SeekBar mSeekBar;

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
		
		// Set up the seek bar.
		setupSeekBar();

		// Handle any intent
		handleIntent(getIntent());

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("url", mUrl);

		// save the URL for the next invocation
		SharedPreferences.Editor editor = getSharedPreferences(
				PREFERENCE_GROUP_MAIN, MODE_PRIVATE).edit();
		editor.putString(PREFERENCE_URL, mUrl);
		editor.commit();
	}

	@Override
	public void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		// Register this activity to handle searches
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		Intent intent;

		switch (item.getItemId()) {

		case R.id.menu_bookmark_list:
			intent = new Intent(this, BookmarkListActivity.class);
			startActivity(intent);
			return true;

		case R.id.menu_bookmark_create:
			intent = new Intent(this, BookmarkEditActivity.class);
			intent.putExtra("url", mUrl);
			startActivity(intent);
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);

		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// Toggle the search menu item when the user presses the search button
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			SearchView searchView = (SearchView) findViewById(R.id.menu_search);
			searchView.setIconified(!searchView.isIconified());
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	//
	// Loader callbacks
	//

	@Override
	public Loader<DataCollectionResult> onCreateLoader(int id, Bundle args) {
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
	public void onLoadFinished(Loader<DataCollectionResult> loader,
			DataCollectionResult result) {
		MainActivity.this.setProgressBarIndeterminateVisibility(false);
		if (result.hasError()) {
			showError(result.getThrowable().getMessage());
		} else {
			DataCollection dataCollection = result.getDataCollection();
			mPagerAdapter.setDataCollection(dataCollection);
			mSeekBar.setProgress(0);
			mSeekBar.setMax(dataCollection.size());
			updateInfoBar(0);
		}
	}

	@Override
	public void onLoaderReset(Loader<DataCollectionResult> loader) {
		mPagerAdapter.setDataCollection(null);
	}

	//
	// Internal utility methods
	//

	/**
	 * Handle any special intents.
	 * 
	 * @param intent
	 *            The intent passed to the activity.
	 */
	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (Intent.ACTION_SEARCH.equals(action)) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doSearch(query);
		} else {
			mUrl = intent.getStringExtra("url");
			// Restore the last URL
			if (mUrl == null) {
				mUrl = getSharedPreferences(PREFERENCE_GROUP_MAIN, MODE_PRIVATE)
						.getString(PREFERENCE_URL, null);
			}

			if (mUrl != null && mUrl.length() > 0) {
				loadData(mUrl);
			}
		}
	}

	/**
	 * Search for a substring inside a record field.
	 * 
	 * Advances the view pager to the first result.
	 * 
	 * @param query
	 *            the string query (currently case-sensitive).
	 */
	private void doSearch(String query) {
		DataCollection dataCollection = mPagerAdapter.getDataCollection();
		if (dataCollection != null) {
			int position = dataCollection.search(query, 0);
			if (position == -1) {
				showError("No results found for " + query);
			} else {
				mViewPager.setCurrentItem(position);
			}
		}
	}

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
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mSeekBar.setProgress(position);
						updateInfoBar(position);
					}

				});
	}
	
	private void setupSeekBar() {
		mSeekBar = (SeekBar)findViewById(R.id.page_seek_bar);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// NO OP
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// NO OP
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
					mViewPager.setCurrentItem(progress, false);
			}
		});
	}

	/**
	 * Load data from a new URL.
	 * 
	 * @param url
	 *            the URL, or null (which will display an error).
	 */
	private void loadData(String url) {
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
	 * Update the info bar.
	 * 
	 * @param position
	 *            The position in the data collection.
	 */
	private void updateInfoBar(int position) {
		TextView infoBar = (TextView) findViewById(R.id.info_bar);
		DataCollection dataCollection = mPagerAdapter.getDataCollection();
		if (dataCollection == null) {
			infoBar.setText("No data collection loaded");
		} else {
			infoBar.setText(String.format("Record %,d/%,d", position + 1,
					dataCollection.size()));
		}
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
