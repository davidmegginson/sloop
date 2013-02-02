package com.megginson.sloop.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.megginson.sloop.activities.MainActivity;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final DataRecord dataRecord = getArguments().getParcelable("dataRecord");
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
		return listView;
	}
	
	private void doAssignFilter(DataEntry entry){
		// TODO send an intent to the FilterActivity
		Toast.makeText(getActivity(), "Filter: " + entry.getKey() + "=" + entry.getValue(), Toast.LENGTH_SHORT).show();
	}

}
