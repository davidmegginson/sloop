package com.megginson.sloop.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.megginson.sloop.activities.BookmarkListActivity;
import com.megginson.sloop.model.Bookmark;

/**
 * Adapt a list of {@link Bookmark} objects for display.
 * 
 * The {@link BookmarkListActivity} class uses this adapter to display the
 * bookmarks in a {@link ListView}.
 * 
 * @author David Megginson
 */
public class BookmarkListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Bookmark> mBookmarks;

	public BookmarkListAdapter(Context context, List<Bookmark> bookmarks) {
		mContext = context;
		mBookmarks = bookmarks;
	}

	@Override
	public int getCount() {
		return mBookmarks.size();
	}

	@Override
	public Object getItem(int position) {
		return mBookmarks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO what can we do better?
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;

		// reuse an existing view if possible
		if (convertView != null) {
			textView = (TextView) convertView;
		} else {
			textView = makeText();
		}

		Bookmark bookmark = mBookmarks.get(position);
		textView.setText(bookmark.getTitle());
		return textView;
	}

	/**
	 * Create a new text view to hold a bookmark.
	 * 
	 * @return a fully-configured text view.
	 */
	private TextView makeText() {
		TextView textView = new TextView(mContext);
		textView.setTextSize(20);
		textView.setPadding(10, 10, 10, 10);
		return textView;
	}

}
