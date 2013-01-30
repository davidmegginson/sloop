package com.megginson.sloop.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.megginson.sloop.R;
import com.megginson.sloop.model.Bookmark;
import com.megginson.sloop.ui.BookmarkListAdapter;

public class BookmarkListActivity extends ListActivity {
	
	public final static String PREFERENCE_GROUP_BOOKMARKS = "bookmarks";

	private List<Bookmark> mBookmarks = new ArrayList<Bookmark>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		loadBookmarks();

		setListAdapter(new BookmarkListAdapter(this, mBookmarks));
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Bookmark bookmark = mBookmarks.get(position);
				Intent intent = new Intent(BookmarkListActivity.this, MainActivity.class);
				intent.putExtra("url", bookmark.getUrl());
				Toast.makeText(getApplicationContext(), bookmark.getTitle(), Toast.LENGTH_SHORT).show();
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bookmark_list, menu);
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
	
	private void loadBookmarks() {
		Map<String,?> preferences = getSharedPreferences(PREFERENCE_GROUP_BOOKMARKS, MODE_PRIVATE).getAll();
		for (String url : preferences.keySet()) {
			mBookmarks.add(new Bookmark(url, (String)preferences.get(url)));
		}
	}

}
