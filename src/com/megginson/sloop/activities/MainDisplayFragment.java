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
	
	public void reset() {
		mViewPager.setAdapter(mPagerAdapter);
		updateSeekBar();
	}
	
	public void setLoading(boolean isLoading) {
		if (isLoading) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	public DataCollection getDataCollection() {
		return mPagerAdapter.getDataCollection();
	}
	
	public void setDataCollection(DataCollection dataCollection) {
		mPagerAdapter.setDataCollection(dataCollection);
		if (dataCollection != null) {
			mSeekBar.setMax(mPagerAdapter.getCount() - 1);
		} else {
			mSeekBar.setProgress(0);
			mSeekBar.setMax(0);
		}
	}
	
	public int getCurrentItem(){
		return mViewPager.getCurrentItem();
	}

	/**
	 * Set up the main ViewPager.
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

	private void setupProgressBar() {
		mProgressBar = (ProgressBar) mLayout.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.GONE);
	}

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
	
	private void updateSeekBar(){
		DataCollection collection = mPagerAdapter.getDataCollection();
		int filteredTotal = collection.getFilteredRecords().size();
		mSeekBar.setProgress(getCurrentItem());
		mSeekBar.setMax(filteredTotal - 1);
	}

}
