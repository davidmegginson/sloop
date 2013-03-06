package com.megginson.sloop.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.impl.ContainsStringFilter;
import com.megginson.sloop.model.impl.EqualsStringFilter;
import com.megginson.sloop.ui.DataCollectionLoader;
import com.megginson.sloop.ui.DataCollectionResult;

/**
 * Sloop's main UI activity (browse a data set).
 * 
 * The activity consists of several components represented as {@link Fragment}
 * objects.
 * 
 * @author David Megginson
 */
@SuppressLint("DefaultLocale")
public class MainActivity extends FragmentActivity {

	public final static String ACTION_FILTER = "com.megginson.sloop.intent.FILTER";

	public final static String PARAM_URL = "url";

	public final static String PARAM_ENTRY = "entry";

	public final static String PARAM_FORCE_LOAD = "forceLoad";

	public final static String PREFERENCE_GROUP_MAIN = "main";

	public final static String PREFERENCE_URL = "url";

	public final static String DEFAULT_URL = "https://docs.google.com/spreadsheet/ccc?key=0AoDV0i2WefMXdEI2VV9Xb1I5eFpBeS1HYkw5NGNqR3c&output=csv#gid=0";

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
	 * The address action provider.
	 */
	private AddressActionProvider mAddressProvider;

	private MainDisplayFragment mMainDisplay;

	private TextFilterFragment mTextFilter;

	private InfoBarFragment mInfoBar;

	//
	// Activity lifecycle methods.
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// grab references to the fragments
		mTextFilter = (TextFilterFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_text_filter);
		mMainDisplay = (MainDisplayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_main_display);
		mInfoBar = (InfoBarFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_info_bar);

		// What are we supposed to be doing?
		doHandleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		// Set up the address bar action
		setupAddressProvider(menu.findItem(R.id.menu_address_bar));

		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// save the URL for the next invocation
		if (mUrl != null) {
			super.onSaveInstanceState(savedInstanceState);
			savedInstanceState.putString(PARAM_URL, mUrl);
			SharedPreferences.Editor editor = getSharedPreferences(
					PREFERENCE_GROUP_MAIN, MODE_PRIVATE).edit();
			editor.putString(PREFERENCE_URL, mUrl);
			editor.commit();
		}
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
		case R.id.menu_search:
			mTextFilter.setShown(!mTextFilter.isShown());
			return true;
		case R.id.menu_share:
			doShareUrl(mUrl);
			return true;
		case R.id.menu_bookmark_create:
			doLaunchBookmarkCreate(mUrl);
			return true;
		case R.id.menu_reload:
			doLoadDataCollection(mUrl, true);
			return true;
		case R.id.menu_help:
			ActivitiesUtil.doHelp(this);
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	public boolean onSearchRequested() {
		// The user has pressed the search button
		mTextFilter.setShown(!mTextFilter.isShown());
		return true;
	}

	/**
	 * Set up the address bar action provider.
	 * 
	 * @param item
	 *            The menu item for the address bar action.
	 */
	private void setupAddressProvider(MenuItem item) {
		mAddressProvider = (AddressActionProvider) item.getActionProvider();
		mAddressProvider.setUp(this, item);
		mAddressProvider.setUrl(mUrl);
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

		// ACTION_MAIN is the default
		if (action == null) {
			action = Intent.ACTION_MAIN;
		}

		if (Intent.ACTION_MAIN.equals(action)) {
			String url = intent.getStringExtra(PARAM_URL);
			// Restore the last URL
			if (url == null) {
				url = getSharedPreferences(PREFERENCE_GROUP_MAIN, MODE_PRIVATE)
						.getString(PREFERENCE_URL, null);
				url = DEFAULT_URL;
			}
			if (url != null && url.length() > 0) {
				doLoadDataCollection(url, false);
			}
		}

		else if (Intent.ACTION_VIEW.equals(action)) {
			String url = intent.getData().toString();
			if (url != null && url.length() > 0) {
				doLoadDataCollection(url, false);
			}
		}

		else if (ACTION_FILTER.equals(action)) {
			DataEntry entry = intent.getParcelableExtra(PARAM_ENTRY);
			doSetColumnFilter(entry);
		}
	}

	void doShareUrl(String url) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, url);
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, getString(R.string.menu_share)));
	}

	void doClearFilters() {
		DataCollection collection = mMainDisplay.getDataCollection();
		if (collection != null) {
			doSetTextFilter(null);
			collection.clearFilters();
			mTextFilter.clear();
			mMainDisplay.reset();
			doDisplayRecordNumber(mMainDisplay.getCurrentItem());
		}
	}

	/**
	 * Action: set a text filter for the data collection.
	 */
	void doSetTextFilter(String query) {
		DataCollection collection = mMainDisplay.getDataCollection();
		if (collection != null) {
			if (query == null) {
				collection.setTextFilter(null);
			} else {
				collection.setTextFilter(new ContainsStringFilter(query));
			}
			collection.setFilteringEnabled(true);
			mMainDisplay.reset();
			doDisplayRecordNumber(mMainDisplay.getCurrentItem());
		}
	}

	/**
	 * Action: add a column filter for the data collection.
	 * 
	 * @param entry
	 *            the data entry (soon to be the filter)
	 */
	private void doSetColumnFilter(DataEntry entry) {
		DataCollection collection = mMainDisplay.getDataCollection();
		if (collection.getColumnFilter(entry.getKey()) != null) {
			collection.putColumnFilter(entry.getKey(), null);
			if (!collection.hasFilters()) {
				collection.setFilteringEnabled(false);
			}
			Toast.makeText(
					this,
					String.format(getString(R.string.msg_filter_cleared),
							entry.getKey()), Toast.LENGTH_SHORT).show();
		} else {
			collection.setFilteringEnabled(true);
			collection.putColumnFilter(entry.getKey(), new EqualsStringFilter(
					entry.getValue()));
			Toast.makeText(
					this,
					String.format(getString(R.string.msg_filter_set),
							entry.getKey(), entry.getValue()),
					Toast.LENGTH_SHORT).show();
		}
		mMainDisplay.reset();
		doDisplayRecordNumber(mMainDisplay.getCurrentItem());
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
		builder.setNeutralButton(R.string.btn_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
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
		intent.putExtra(BookmarkEditActivity.PARAM_URL, url);
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
	void doLoadDataCollection(String url, boolean forceLoad) {
		if (url == null || url.length() == 0) {
			doDisplayError(getString(R.string.msg_web_address));
			return;
		}
		mUrl = url;
		Bundle args = new Bundle();
		args.putString(PARAM_URL, url);
		args.putBoolean(PARAM_FORCE_LOAD, forceLoad);
		getLoaderManager().restartLoader(0, args,
				new LoaderCallbacks<DataCollectionResult>() {

					@Override
					public Loader<DataCollectionResult> onCreateLoader(int id,
							Bundle args) {
						// only one loader for now, so ignore id
						DataCollectionLoader loader = new DataCollectionLoader(
								getApplicationContext());
						if (args != null) {
							loader.setURL(args.getString(PARAM_URL));
							loader.setForceLoad(args
									.getBoolean(PARAM_FORCE_LOAD));
						}
						return loader;
					}

					@Override
					public void onLoadFinished(
							Loader<DataCollectionResult> loader,
							DataCollectionResult result) {
						if (result.hasError()) {
							// if the load failed, show and error and stick
							// around
							doDisplayError(result.getThrowable().getMessage());
						} else if (result.getRedirectUrl() != null) {
							// if it was a non-CSV resource, launch the browser
							doLaunchBrowser(result.getRedirectUrl());
						} else {
							// succeeded - show the collection
							doUpdateDataCollection(result.getDataCollection());
							if (mAddressProvider != null) {
								mAddressProvider.setUrl(mUrl);
							}
						}
						mMainDisplay.setLoading(false);
					}

					@Override
					public void onLoaderReset(
							Loader<DataCollectionResult> loader) {
						// NO OP
					}

				});
		if (mAddressProvider != null) {
			mAddressProvider.setUrl(mUrl);
		}
		ActivitiesUtil.doHideKeyboard(this, mInfoBar.getView());
		mMainDisplay.setLoading(true);
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
		mMainDisplay.setDataCollection(dataCollection);
		if (dataCollection != null) {
			doDisplayRecordNumber(0);
		} else {
			mInfoBar.displayRecordCount(0, 0, 0);
		}
	}

	/**
	 * End this activity and launch a non-CSV URL.
	 * 
	 * This method invokes {@link #finish()}.
	 * 
	 * @param url
	 *            The URL to launch in the browser (etc.).
	 */
	private void doLaunchBrowser(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
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
	void doDisplayRecordNumber(int recordNumber) {
		DataCollection collection = mMainDisplay.getDataCollection();
		int filteredTotal = collection.getFilteredRecords().size();
		int unfilteredTotal = collection.getRecords().size();
		mMainDisplay.refresh();
		mInfoBar.displayRecordCount(recordNumber, filteredTotal,
				unfilteredTotal);
	}

}
