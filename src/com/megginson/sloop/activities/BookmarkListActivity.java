package com.megginson.sloop.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.megginson.sloop.R;
import com.megginson.sloop.model.Bookmark;
import com.megginson.sloop.ui.BookmarkListAdapter;

public class BookmarkListActivity extends Activity
{

	public final static String PREFERENCE_GROUP_BOOKMARKS = "bookmarks";

	private ListView mBookmarksView;

	private List<Bookmark> mBookmarks = new ArrayList<Bookmark>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bookmark_list);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setupListView();
		doLoadBookmarks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bookmark_list, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_help:
				ActivitiesUtil.doHelp(this);
				return true;
			default:
				return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// react to menu selections
		switch (item.getItemId())
		{
			case android.R.id.home:
				doNavigateUp();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	//
	// UI component configuration
	//

	/**
	 * Set up the built-in list view.
	 */
	private void setupListView()
	{
		mBookmarksView = (ListView)findViewById(R.id.bookmarks);
		mBookmarksView.setAdapter(new BookmarkListAdapter(this, mBookmarks));
		mBookmarksView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id)
				{
					Bookmark bookmark = mBookmarks.get(position);
					Intent intent = new Intent(BookmarkListActivity.this,
											   MainActivity.class);
					intent.setAction(Intent.ACTION_MAIN);
					intent.putExtra(MainActivity.PARAM_URL, bookmark.getUrl());
					Toast.makeText(getApplicationContext(),
								   bookmark.getTitle(), Toast.LENGTH_SHORT).show();
					startActivity(intent);
				}
			});
		mBookmarksView.setOnItemLongClickListener(
			new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
				{
					doEditBookmark(position);
					return true;
				}

			}
		);
	}

	//
	// Abstracted UI actions
	//

	/**
	 * Action: return to home screen.
	 */
	private void doNavigateUp()
	{
		NavUtils.navigateUpFromSameTask(this);
	}

	/**
	 * Action: edit bookmark
	 */
	private void doEditBookmark(int position)
	{
		Bookmark bookmark = mBookmarks.get(position);
		Intent intent = new Intent(BookmarkListActivity.this,
								   BookmarkEditActivity.class);
		intent.putExtra(BookmarkEditActivity.PARAM_URL, bookmark.getUrl());
		startActivity(intent);
	}

	/**
	 * Action: load bookmarks from shared preferences.
	 */
	private void doLoadBookmarks()
	{
		Map<String, ?> preferences = getSharedPreferences(
			PREFERENCE_GROUP_BOOKMARKS, MODE_PRIVATE).getAll();
		for (String url : preferences.keySet())
		{
			mBookmarks.add(new Bookmark(url, (String) preferences.get(url)));
		}
		mBookmarksView.refreshDrawableState();
	}

}
