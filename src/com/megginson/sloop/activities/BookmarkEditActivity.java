package com.megginson.sloop.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_edit);

		Intent intent = getIntent();
		if (intent != null) {
			mBookmark = new Bookmark(intent.getStringExtra("url"),
					intent.getStringExtra("title"));
		} else {
			mBookmark = new Bookmark();
		}

		setupForm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bookmark_edit, menu);
		return true;
	}

	private void setupForm() {
		
		// fill in the title field, if supplied
		EditText titleField = (EditText) findViewById(R.id.field_bookmark_title);
		titleField.setText(mBookmark.getTitle());
		
		// fill in the URL field, if supplied
		EditText urlField = (EditText) findViewById(R.id.field_bookmark_url);
		urlField.setText(mBookmark.getUrl());
		
		// add listener to the save button
		Button saveButton = (Button) findViewById(R.id.button_bookmark_save);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO validate and save the bookmark
				finish();
			}
		});
	}
}
