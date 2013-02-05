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
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.ui.DataCollectionLoader;
import com.megginson.sloop.ui.DataCollectionPagerAdapter;
import com.megginson.sloop.ui.DataCollectionResult;
import com.megginson.sloop.util.ListItemFilter;
import com.megginson.sloop.widgets.AddressActionProvider;

/**
 * Sloop's main UI activity (browse a data set).
 * 
 * @author David Megginson
 */
public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<DataCollectionResult> {

	public final static String ACTION_MAIN = "com.megginson.sloop.intent.MAIN";

	public final static String ACTION_FILTER = "com.megginson.sloop.intent.FILTER";

	public final static String PREFERENCE_GROUP_MAIN = "main";

	public final static String PREFERENCE_URL = "url";

	//
	// Saveable state
	//

	/**
	 * The URL of the current data set.
	 */
	private String mUrl = null;

	private boolean mIsLoading = false;

	//
	// UI components.
	//

	/**
	 * The address action provider.
	 */
	private AddressActionProvider mAddressProvider;

	/**
	 * The {@link PagerAdapter} for the current data collection.
	 */
	private DataCollectionPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the data collection.
	 */
	private ViewPager mViewPager;

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
		System.err.println("onCreate");

		setContentView(R.layout.activity_main);

		// Set up the main display area
		setupPager();

		// Set up the seek bar.
		setupSeekBar();

		// What are we supposed to be doing?
		doHandleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		System.err.println("onCreateOptionsMenu");

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		// Set up the address bar action
		setupAddressProvider(menu.findItem(R.id.menu_address_bar));

		// Register this activity to handle searches
		setupSearchManager(menu.findItem(R.id.menu_search));

		return true;
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
		doHandleIntent(intent);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Items from the main menu
		switch (item.getItemId()) {
		case R.id.menu_bookmark_list:
			doLaunchBookmarkList();
			return true;
		case R.id.menu_bookmark_create:
			doLaunchBookmarkCreate(mUrl);
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
		if (result.hasError()) {
			doDisplayError(result.getThrowable().getMessage());
		} else {
			doUpdateDataCollection(result.getDataCollection());
			mIsLoading = false;
			if (mAddressProvider != null) {
				mAddressProvider.setUrl(mUrl);
				mAddressProvider.setIsLoading(false);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<DataCollectionResult> loader) {
		doUpdateDataCollection(null);
	}

	//
	// Configuration functions for UI components.
	//
	// Each of these functions sets listeners, etc. for its component. The
	// listeners use the do*() action methods to perform actions.
	//

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
						doDisplayRecordNumber(position);
					}
				});
	}

	private void setupSeekBar() {
		mSeekBar = (SeekBar) findViewById(R.id.page_seek_bar);
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
	 * Set up the address bar action provider.
	 * 
	 * @param item
	 *            The menu item for the address bar action.
	 */
	private void setupAddressProvider(MenuItem item) {
		mAddressProvider = (AddressActionProvider) item.getActionProvider();
		mAddressProvider.setMenuItem(item);
		mAddressProvider.setUrl(mUrl);
		mAddressProvider.setIsLoading(mIsLoading);
		mAddressProvider
				.setAddressBarListener(new AddressActionProvider.AddressBarListener() {
					@Override
					public void onLoadStarted(String url) {
						doLoadDataCollection(url);
					}

					@Override
					public void onLoadCancelled(String url) {

					}
				});
	}

	/**
	 * Set up the search view action provider.
	 * 
	 * @param item
	 *            The menu item for the search view.
	 */
	private void setupSearchManager(MenuItem item) {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) item.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
	}

	//
	// Abstracted UI actions
	//
	// These are separate functions to avoid direct dependencies among UI
	// components and their listeners. Each action represents something that can
	// happen in the activity, and the functions take care of figuring out what
	// to do with different components.
	//

	/**
	 * Action: handle any special intents.
	 * 
	 * @param intent
	 *            The intent passed to the activity.
	 */
	private void doHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (action == null) {
			action = ACTION_MAIN;
		}

		if (ACTION_MAIN.equals(action)) {
			// ACTION_MAIN is the default
			String url = intent.getStringExtra("url");
			// Restore the last URL
			if (url == null) {
				url = getSharedPreferences(PREFERENCE_GROUP_MAIN, MODE_PRIVATE)
						.getString(PREFERENCE_URL, null);
			}

			if (url != null && url.length() > 0) {
				doLoadDataCollection(url);
			}
		}

		else if (ACTION_FILTER.equals(action)) {
			// TODO set a filter
			DataEntry entry = intent.getParcelableExtra("entry");
			doSetFilter(entry);
		}

		else if (Intent.ACTION_SEARCH.equals(action)) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doSearch(query);
		}
	}
	
	/**
	 * Action: set the filter for the data collection.
	 * 
	 * @param entry the data entry (soon to be the filter)
	 */
	private void doSetFilter(final DataEntry entry) {
		mPagerAdapter.setFilter(new ListItemFilter<DataRecord>() {
			@Override
			public boolean isMatch(DataRecord dataRecord) {
				return entry.getValue().equals(
						dataRecord.get(entry.getKey()));
			}
		});
		System.err.println("Set filter " + entry.toString());
		Toast.makeText(this, "Filter " + entry.toString(),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Action: report an error message.
	 * 
	 * @param message
	 *            The error message as a string.
	 */
	private void doDisplayError(String message) {
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

	/**
	 * Action: search for a substring inside a record field.
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
				doDisplayError("No results found for " + query);
			} else {
				mViewPager.setCurrentItem(position);
			}
		}
	}

	/**
	 * Action: launch the bookmark list activity.
	 */
	private void doLaunchBookmarkList() {
		Intent intent = new Intent(this, BookmarkListActivity.class);
		startActivity(intent);
	}

	/**
	 * Action: launch the bookmark create activity.
	 * 
	 * @param url
	 *            The URL of the bookmark to be created or edited.
	 */
	private void doLaunchBookmarkCreate(String url) {
		Intent intent = new Intent(this, BookmarkEditActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	/**
	 * Load a data collection from a URL.
	 * 
	 * When the load is complete, the loader will invoke the
	 * {@link #onLoadFinished(Loader, DataCollectionResult)} method with the
	 * result.
	 * 
	 * @param url
	 *            the URL of the data collection.
	 */
	private void doLoadDataCollection(String url) {
		if (url == null || url.length() == 0) {
			doDisplayError("Please enter a web address");
			return;
		}
		mUrl = url;
		mIsLoading = true;
		Bundle args = new Bundle();
		args.putString("url", url);
		getLoaderManager().restartLoader(0, args, MainActivity.this);
		if (mAddressProvider != null) {
			mAddressProvider.setIsLoading(true);
			mAddressProvider.setUrl(mUrl);
		}
		doHideKeyboard();
	}

	/**
	 * Action: update the data collection displayed.
	 * 
	 * Assigns the new data collection to the pager adapter, and updates the
	 * seekbar and the info bar.
	 * 
	 * @param dataCollection
	 *            the new data collection, or null to clear.
	 */
	private void doUpdateDataCollection(DataCollection dataCollection) {
		mPagerAdapter.setDataCollection(dataCollection);
		if (dataCollection != null) {
			mSeekBar.setMax(mPagerAdapter.getCount());
			doDisplayRecordNumber(0);
		} else {
			mSeekBar.setProgress(0);
			mSeekBar.setMax(0);
			doDisplayInfo("No data collection loaded");
		}
	}

	/**
	 * Action: display the current record number.
	 * 
	 * Updates the position of the horizontal seekbar, and displays the record
	 * number in the info bar.
	 * 
	 * @param recordNumber
	 *            the record number to display (zero-based).
	 */
	private void doDisplayRecordNumber(int recordNumber) {
		mSeekBar.setProgress(recordNumber);
		doDisplayInfo(String.format("Record %,d/%,d", recordNumber + 1,
				mPagerAdapter.getCount()));
	}

	/**
	 * Action: update text in the info bar.
	 * 
	 * @param message
	 *            The message to display.
	 */
	private void doDisplayInfo(String message) {
		TextView infoBar = (TextView) findViewById(R.id.info_bar);
		infoBar.setText(message);
	}

	/**
	 * Action: hide the soft keyboard.
	 */
	private void doHideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
	}

}
