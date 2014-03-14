package com.megginson.sloop.activities;

import android.content.Context;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.megginson.sloop.R;

/**
 * Collapsible URL address field for the action bar.
 * 
 * This action provider edits and displays URLs: it consists of a text field and
 * a clear/cancel button.
 * 
 * @author David Megginson
 */
public class AddressActionProvider extends ActionProvider
{

	// Contextual items
	private MainActivity mActivity;
	private MenuItem mMenuItem;
	private Context mContext;

	// Child views
	private View mContentView;
	private EditText mUrlField;
	private Button mCancelButton;

	// Internal state
	private String mUrl;

	/**
	 * Standard constructor.
	 * 
	 * @param context
	 *            the context for the action provider.
	 */
	public AddressActionProvider(Context context)
	{
		super(context);
		mContext = context;
		System.err.println(context);
	}

	/**
	 * Setup the action provider with information about the main activity.
	 * 
	 * We can't call the constructor directly, so we have to set extra arguments
	 * here instead.
	 * 
	 * @param activity
	 *            the referring activity
	 * @param menuItem
	 *            the menu item that owns this provider.
	 */
	public void setUp(MainActivity activity, MenuItem menuItem)
	{
		mActivity = activity;
		mMenuItem = menuItem;
	}

	@Override
	public View onCreateActionView()
	{
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mContentView = inflater.inflate(R.layout.widget_address_bar, null);
		setupUrlField();
		setupCancelButton();
		return mContentView;
	}

	/**
	 * Set the URL for the action provider.
	 * 
	 * We can't simply use the state of the EditText view, because we need to be
	 * able to restore the URL on a cancel.
	 * 
	 * @param url
	 *            the URL in the address bar.
	 */
	public void setUrl(String url)
	{
		mUrl = url;
		doUpdateStatus();
	}

	/**
	 * Set up the text field with a callback.
	 */
	private void setupUrlField()
	{
		mUrlField = (EditText) mContentView.findViewById(R.id.urlField);
		// Enter key submits the URL
		mUrlField
			.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
											  KeyEvent event)
				{
					mUrl = mUrlField.getText().toString();
					mActivity.doLoadDataCollection(mUrl, true);
					return true;
				}
			});

	}

	/**
	 * Set up the cancel button with a callback.
	 */
	private void setupCancelButton()
	{
		mCancelButton = (Button) mContentView
			.findViewById(R.id.button_url_clear);
		// Cancel button clears or closes
		mCancelButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					doCancel();
				}
			});
	}

	/**
	 * Update the loading status display.
	 */
	private void doUpdateStatus()
	{
		// update the URL field
		if (mUrlField != null)
		{
			mUrlField.setText(mUrl);
		}
	}

	/**
	 * Do a cancel action, depending on context.
	 * 
	 * If the field is not empty, empty it; otherwise, close the action
	 * provider.
	 */
	private void doCancel()
	{
		if (mUrlField.getText() != null && mUrlField.getText().length() > 0)
		{
			mUrlField.setText(null);
		}
		else
		{
			doUpdateStatus();
			ActivitiesUtil.doHideKeyboard(mContext, mUrlField);
			mMenuItem.collapseActionView();
		}
	}

}
