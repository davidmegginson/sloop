package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtering wrapper around a {@link List}.
 * 
 * This class wraps an existing list, and makes it appear as if the list
 * contains only items that match the {@link ListItemFilter} provided.
 * 
 * This is a read-only version of the list — it is not possible to add, remove,
 * or modify list items through the filter interface. For efficiency, this
 * implementation caches the results of filtering. If you make changes to the
 * underlying list, then you need to invoke the {@link #invalidateCache()}
 * method to clear and rebuild the cache.
 * 
 * Note that the implementation is lazy; the filter won't actually be applied
 * until it is needed. If you want to build the cache early (say, to avoid a
 * pause later), use the {@link #buildCache()} method.
 * 
 * To apply multiple filters, create a top-level filter that combines the
 * others.
 * 
 * @author David Megginson
 */
public class FilteredList<T> extends AbstractList<T> {

	private List<T> mList;

	private ListItemFilter<T> mFilter;

	private List<Integer> mMatchCache;

	/**
	 * Create a filtered list wrapper.
	 * 
	 * @param filter
	 *            the filter to apply to the list.
	 * @param list
	 */
	public FilteredList(ListItemFilter<T> filter, List<T> list) {
		super();
		mFilter = filter;
		mList = list;
	}

	@Override
	public T get(int location) {
		buildCache();
		return mList.get(mMatchCache.get(location));
	}

	@Override
	public int size() {
		buildCache();
		return mMatchCache.size();
	}

	/**
	 * Clear the internal filtering cache.
	 * 
	 * Call this method if the underlying list (might have) changed.
	 */
	public void invalidateCache() {
		mMatchCache = null;
	}

	/**
	 * Build the internal filtering cache.
	 * 
	 * Call this method if you want to force the filtered list to build its
	 * cache early (say, to avoid a delay later).
	 */
	public void buildCache() {
		if (mMatchCache == null) {
			mMatchCache = new ArrayList<Integer>();
			for (int i = 0; i < mList.size(); i++) {
				if (mFilter.isMatch(mList.get(i))) {
					mMatchCache.add(i);
				}
			}
		}
	}

}
