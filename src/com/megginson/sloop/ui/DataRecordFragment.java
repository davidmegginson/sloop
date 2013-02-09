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
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				doAssignFilter(dataRecord.get(position));
				return true;
			}
		});
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String value = dataRecord.get(position).getValue();
				if (Util.isUrl(value)) {
					doOpenUrl(value);
				}
			}
		});
		return listView;
	}

	// TODO does this belong here?
	private void doAssignFilter(DataEntry entry) {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setAction(MainActivity.ACTION_FILTER);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(MainActivity.PARAM_ENTRY, entry);
		startActivity(intent);
	}

	private void doOpenUrl(String url) {
		Intent intent;
		// FIXME this logic should be in the main activity
		// FIXME Google Doc CSV won't end with the extension
		if (url.toLowerCase(Locale.ENGLISH).endsWith(".csv")) {
			intent = new Intent(getActivity(), MainActivity.class);
			intent.setAction(Intent.ACTION_MAIN);
			intent.putExtra(MainActivity.PARAM_URL, url);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
		}
		startActivity(intent);
	}

}
