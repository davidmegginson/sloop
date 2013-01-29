package com.megginson.sloop.model;

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
		setUrl(url);
		setTitle(url);
	}

	public Bookmark(Bookmark bookmark) {
		setUrl(bookmark.getUrl());
		setTitle(bookmark.getTitle());
	}

	public Bookmark(String url, String title) {
		setUrl(url);
		setTitle(title);
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
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
			return (ModelUtil.equals(getUrl(), b.getUrl()) && ModelUtil.equals(
					getTitle(), b.getTitle()));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ModelUtil.hashCode(getUrl()) + ModelUtil.hashCode(getTitle());
	}

	@Override
	public String toString() {
		return "{Bookmark|" + getUrl() + "|" + getTitle() + "}";
	}

}
