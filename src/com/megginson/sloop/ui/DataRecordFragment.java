package com.megginson.sloop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.megginson.sloop.activities.FilterActivity;
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
	
	// TODO does this belong here?
	private void doAssignFilter(DataEntry entry){
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setAction(MainActivity.ACTION_FILTER);
		intent.putExtra("entry", entry);
		getActivity().startActivity(intent);
	}

}
