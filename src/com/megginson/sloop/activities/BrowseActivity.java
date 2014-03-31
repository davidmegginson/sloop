package com.megginson.sloop.activities;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;

public class BrowseActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		doOpen("Tab 1", "");
		doOpen("Tab 2", "");
		doOpen("Tab 3", "");
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		return super.onPrepareOptionsMenu(menu);
	}
	
	private void doOpen(String title, String url) {
		ActionBar actionBar = getActionBar();
		ActionBar.Tab tab = actionBar.newTab().setText(title);
		tab.setTabListener(new TabListener());
		actionBar.addTab(tab, true);
	}
	
	private class TabListener implements ActionBar.TabListener
	{

		@Override
		public void onTabSelected(ActionBar.Tab p1, FragmentTransaction p2)
		{
			// TODO: Implement this method
		}

		@Override
		public void onTabReselected(ActionBar.Tab p1, FragmentTransaction p2)
		{
			// TODO: Implement this method
		}

		@Override
		public void onTabUnselected(ActionBar.Tab p1, FragmentTransaction p2)
		{
			// TODO: Implement this method
		}
		
	};

}
