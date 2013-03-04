package com.megginson.sloop.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.ui.DataCollectionPagerAdapter;

/**
 * Activity fragment for the main browser display area.
 * 
 * This fragment encapsulates part of the layout for MainActivity, including the
 * main display area for data records.
 * 
 * @author David Megginson
 */
public class MainDisplayFragment extends Fragment {

	private MainActivity mActivity;
	private ViewGroup mLayout;
	private DataCollectionPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private SeekBar mSeekBar;
	private ProgressBar mProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_main_display,
				container);
		setupPager();
		setupProgressBar();
		setupSeekBar();
		return mLayout;
	}

	/**
	 * Force the display to reset itself (usually after a filter change).
	 */
	public void reset() {
		mViewPager.setAdapter(mPagerAdapter);
	}

	/**
	 * Toggle showing the loading animation.
	 * 
	 * @param isLoading
	 *            true to show the loading animation.
	 */
	public void setLoading(boolean isLoading) {
		if (isLoading) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * Get the underlying data collection.
	 * 
	 * @return the data collection, or null if none is loaded.
	 */
	public DataCollection getDataCollection() {
		return mPagerAdapter.getDataCollection();
	}

	/**
	 * Set the underlying data collection.
	 * 
	 * @param dataCollection
	 *            the data collection to display.
	 */
	public void setDataCollection(DataCollection dataCollection) {
		mPagerAdapter.setDataCollection(dataCollection);
		if (dataCollection != null) {
			mSeekBar.setMax(mPagerAdapter.getCount() - 1);
		} else {
			mSeekBar.setProgress(0);
			mSeekBar.setMax(0);
		}
	}

	/**
	 * Get the current item displayed.
	 * 
	 * @return the zero-based index of the current item.
	 */
	public int getCurrentItem() {
		return mViewPager.getCurrentItem();
	}

	/**
	 * Update the {@link SeekBar} to the current position.
	 */
	public void refresh() {
		DataCollection collection = mPagerAdapter.getDataCollection();
		int filteredTotal = collection.getFilteredRecords().size();
		mSeekBar.setProgress(getCurrentItem());
		mSeekBar.setMax(filteredTotal - 1);
	}

	/**
	 * Set up the main {@link ViewPager}.
	 */
	private void setupPager() {
		mPagerAdapter = new DataCollectionPagerAdapter(
				mActivity.getSupportFragmentManager());

		// Set up the ViewPager with the data collection adapter.
		mViewPager = (ViewPager) mLayout.findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						mActivity.doDisplayRecordNumber(position);
					}
				});
	}

	/**
	 * Set up the {@link ProgressBar} loading animation (initially hidden).
	 */
	private void setupProgressBar() {
		mProgressBar = (ProgressBar) mLayout.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * Set up the {@link SeekBar}.
	 */
	private void setupSeekBar() {
		mSeekBar = (SeekBar) mLayout.findViewById(R.id.page_seek_bar);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// NO OP
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// NO OP
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mViewPager.setCurrentItem(progress, false);
			}
		});
	}

}
