package com.megginson.sloop.ui;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.activities.MainActivity;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

/**
 * Adapt a single {@link DataRecordImpl} for display as a list of entries.
 * 
 * The {@link DataRecordFragment} class uses this adapter to display the data
 * record in a {@link ListView}.
 * 
 * @author David Megginson
 */
public class DataRecordListAdapter extends BaseAdapter {

	private Context mContext;

	private DataRecord mDataRecord;

	public DataRecordListAdapter(Context context, DataRecord dataRecord) {
		mContext = context;
		mDataRecord = dataRecord;
		System.err.println("Got a data record with "
				+ dataRecord.getEntries().size() + " entries");
	}

	@Override
	public int getCount() {
		return mDataRecord.getEntries().size();
	}

	@Override
	public Object getItem(int position) {
		return mDataRecord.getEntries().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View layout;
		ImageView checkView;
		TextView labelView;
		TextView valueView;

		// Reuse existing components if we can
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			layout = inflater.inflate(R.layout.component_data_entry, null);
		} else {
			layout = convertView;
		}

		// Set the appropriate text for this entry
		DataEntry entry = mDataRecord.getEntries().get(position);

		labelView = (TextView) layout.findViewById(R.id.field_name);
		labelView.setText(entry.getKey());

		final String value = entry.getValue();
		valueView = (TextView) layout.findViewById(R.id.field_value);
		valueView.setText(value);
		// replace some of the links with internal Sloop links
		stealURLClicks(valueView);

		checkView = (ImageView) layout.findViewById(R.id.image_checkbox);

		if (entry.hasFilter()) {
			checkView.setVisibility(View.VISIBLE);
		} else {
			checkView.setVisibility(View.INVISIBLE);
		}

		if (position % 2 == 1) {
			layout.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
		} else {
			layout.setBackgroundColor(Color.TRANSPARENT);
		}

		return layout;
	}

	/**
	 * Replace some URL spans in a TextView with internal links.
	 * 
	 * This function walks through all URLSpans in a text view, and replaces any
	 * ending in ".csv" with internal-linking spans.
	 * 
	 * @param textView
	 *            the text view to modify.
	 */
	private void stealURLClicks(TextView textView) {
		Spannable text = (Spannable) textView.getText();
		for (URLSpan span : textView.getUrls()) {
			// FIXME use a smarter regular expression
			if (span.getURL().toLowerCase(Locale.US).contains(".csv")) {
				int start = text.getSpanStart(span);
				int end = text.getSpanEnd(span);
				text.removeSpan(span);
				text.setSpan(new SloopURLSpan(span.getURL()), start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
	}

	/**
	 * Special class to replace URLSpan for an internal link.
	 * 
	 * When the Android libraries linkify a TextView, they provide no way to
	 * intercept some links for internal use in the application. This class
	 * provides an alternative span type that opens URLs in Sloop rather than
	 * sending out a view intent.
	 * 
	 * @author David Megginson
	 */
	private class SloopURLSpan extends ClickableSpan {
		private String mUrl;

		public SloopURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			Intent intent = new Intent(widget.getContext(), MainActivity.class);
			intent.setAction(Intent.ACTION_MAIN);
			intent.putExtra(MainActivity.PARAM_URL, mUrl);
			widget.getContext().startActivity(intent);
		}
	}

}
