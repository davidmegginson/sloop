package com.megginson.sloop.model;

import com.megginson.sloop.util.Util;

/**
 * A saved reference to an external data set.
 * 
 * @author David Megginson
 */
public class Bookmark {

	private String mTitle;

	private String mUrl;

	public Bookmark() {
		super();
	}

	public Bookmark(String url) {
		this(url, null);
	}

	public Bookmark(String url, String title) {
		setUrl(url);
		setTitle(title);
	}

	public Bookmark(Bookmark bookmark) {
		setUrl(bookmark.getUrl());
		setTitle(bookmark.getTitle());
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		if (title == null) {
			mTitle = getUrl();
		} else {
			mTitle = title;
		}
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Bookmark(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o != null && o instanceof Bookmark) {
			Bookmark b = (Bookmark) o;
			return (Util.equals(getUrl(), b.getUrl()) && Util.equals(
					getTitle(), b.getTitle()));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Util.hashCode(getUrl()) + Util.hashCode(getTitle());
	}

	@Override
	public String toString() {
		return "{Bookmark|" + getUrl() + "|" + getTitle() + "}";
	}

}
