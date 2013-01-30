package com.megginson.sloop.ui;

import java.util.List;

import com.megginson.sloop.model.Bookmark;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookmarkListAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Bookmark> mBookmarks;
	
	public BookmarkListAdapter(Context
			context, List<Bookmark> bookmarks) {
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
		Bookmark bookmark = mBookmarks.get(position);
		if (convertView == null) {
			convertView = new TextView(mContext);
			((TextView)convertView).setTextSize(20);
			((TextView)convertView).setPadding(10, 10, 10, 10);
		}
		((TextView)convertView).setText(bookmark.getTitle());
		return convertView;
	}

}
