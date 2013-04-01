package com.megginson.sloop.ui;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
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
		// Short click on an item means assign a filter
		// FIXME temporary kludge to make links clickable again.
		//
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String value = dataRecord.getEntries().get(position).getValue();
				if (Util.isUrl(value)) {
					if (value.toLowerCase(Locale.US).endsWith(".csv")) {
						Intent intent = new Intent(getActivity(),
								MainActivity.class);
						intent.setAction(Intent.ACTION_MAIN);
						intent.putExtra(MainActivity.PARAM_URL, value);
						startActivity(intent);
					} else {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(value));
						startActivity(intent);
					}
				}
			}
		});

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

}
