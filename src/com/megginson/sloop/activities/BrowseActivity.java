package com.megginson.sloop.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BrowseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		doOpen("Tab 1", "");
		doOpen("Tab 2", "");
		doOpen("Tab 3", "");
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO: Implement this method
		return super.onPrepareOptionsMenu(menu);
	}

	private void doOpen(String title, String url) {
		ActionBar actionBar = getActionBar();
		ActionBar.Tab tab = actionBar.newTab().setText(title);
		tab.setTabListener(new TabListener(new DummyFragment()));
		actionBar.addTab(tab, true);
	}

	public static class DummyFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(container.getContext());
			textView.setText("Hello, world! " + Math.random());
			return textView;
		}

	}

	private class TabListener implements ActionBar.TabListener {

		private Fragment mFragment;
		private boolean mFragmentAdded;

		public TabListener(Fragment fragment) {
			super();
			mFragment = fragment;
			mFragmentAdded = false;
		}

		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			if (!mFragmentAdded) {
				ft.add(android.R.id.content, mFragment);
				mFragmentAdded = true;
			} else {
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			ft.detach(mFragment);
		}

	};

}
