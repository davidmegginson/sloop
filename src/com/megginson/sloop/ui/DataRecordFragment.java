package com.megginson.sloop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.megginson.sloop.activities.MainActivity;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.model.Util;

/**
 * A view fragment wrapping a data record
 * 
 * @author David Megginson
 * @see DataRecord
 * @see DataCollectionPagerAdapter
 */
public class DataRecordFragment extends Fragment {

	public DataRecordFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		final DataRecord dataRecord = getArguments()
				.getParcelable("dataRecord");
		ListView listView = new ListView(getActivity());
		listView.setAdapter(new DataRecordListAdapter(getActivity(), dataRecord));
		
		//
		// Long click on an item means assign a filter
		//
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				doAssignFilter(dataRecord.getEntries().get(position));
				return true;
			}
		});
		
		//
		// Short click on an item looks for a URL to open
		//
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String value = dataRecord.getEntries().get(position).getValue();
				if (Util.isUrl(value)) {
					doOpenUrl(value);
				}
			}
		});
		
		return listView;
	}

	/**
	 * Send out the intent to create a filter.
	 * 
	 * This method sends a request to the existing {@link MainActivity} at the
	 * top of the stack to start filtering its results.
	 * 
	 * TODO does this belong here?
	 * 
	 * @param entry
	 *            the data entry to use as a model for the filter.
	 */
	private void doAssignFilter(DataEntry entry) {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setAction(MainActivity.ACTION_FILTER);
		// don't create a new instance of MainActivity
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(MainActivity.PARAM_ENTRY, entry);
		startActivity(intent);
	}

	/**
	 * Send out the intent to open a URL.
	 * 
	 * A new {@link MainActivity} will intercept the intent first, but if it
	 * turns out not to be a CSV resource, it will pass it on to other Android
	 * apps (e.g. the browser).
	 * 
	 * TODO does this belong here?
	 * 
	 * @param url
	 *            the URL to pass to {@link MainActivity}
	 */
	private void doOpenUrl(String url) {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.putExtra(MainActivity.PARAM_URL, url);
		startActivity(intent);
	}

}
