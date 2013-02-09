package com.megginson.sloop.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.megginson.sloop.R;
import com.megginson.sloop.model.Bookmark;

/**
 * Form to create or edit a bookmark.
 * 
 * Will check the intent for "url" and "title" parameters.
 * 
 * TODO warn of cancel on back button
 * 
 * @author David Megginson
 * 
 */
public class BookmarkEditActivity extends Activity {

	private Bookmark mBookmark;

	private EditText mTitleField;

	private EditText mUrlField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_edit);

		doInitializeBookmark();

		setupForm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bookmark_edit, menu);
		return true;
	}

	/**
	 * Set up the bookmark form.
	 */
	private void setupForm() {
		// fill in the title field, if supplied
		mTitleField = (EditText) findViewById(R.id.field_bookmark_title);
		mTitleField.setText(mBookmark.getTitle());		

		// fill in the URL field, if supplied
		mUrlField = (EditText) findViewById(R.id.field_bookmark_url);
		mUrlField.setText(mBookmark.getUrl());		

		// set up the save button
		Button saveButton = (Button) findViewById(R.id.button_bookmark_save);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSubmitForm();
			}
		});		
	}

	/**
	 * Action: initialize the bookmark from the intent.
	 */
	private void doInitializeBookmark() {
		Intent intent = getIntent();
		if (intent != null) {
			String url = intent.getStringExtra("url");
			String title = getSharedPreferences(
						BookmarkListActivity.PREFERENCE_GROUP_BOOKMARKS,
						MODE_PRIVATE).getString(url, url);
			mBookmark = new Bookmark(url, title);
		} else {
			mBookmark = new Bookmark();
		}

	}

	/**
	 * Action: save the bookmark from the URL field.
	 */
	private void doSubmitForm() {
		// TODO validate
		mBookmark.setUrl(mUrlField.getText().toString());
		mBookmark.setTitle(mTitleField.getText().toString());
		doSaveAndExit();
	}
	
	private void doSaveAndExit() {
		SharedPreferences.Editor editor = getSharedPreferences(
				BookmarkListActivity.PREFERENCE_GROUP_BOOKMARKS,
				MODE_PRIVATE).edit();
		editor.putString(mBookmark.getUrl(), mBookmark.getTitle());
		editor.commit();
		Toast.makeText(getApplicationContext(), "Bookmark saved",
				Toast.LENGTH_SHORT).show();
		finish();		
	}
}
