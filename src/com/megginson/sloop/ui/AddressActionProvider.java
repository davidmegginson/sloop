package com.megginson.sloop.ui;

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
 * This widget is designed for editing and displaying URLs and showing progress
 * during loading. It consists of a text field, a cancel button, and a progress
 * bar.
 * 
 * The parent activity (owner of the options menu) uses
 * {@link #setAddressBarListener(AddressBarListener)} to be informed of major
 * events coming out of this widget. It can also control the widget's state
 * using the {@link #setUrl(String)} method.
 * 
 * FIXME to support Android v14 (Ice Cream Sandwich), this class requires the
 * activity to pass it a reference to the {@link MenuItem} through
 * {@link #setMenuItem(MenuItem)}. See
 * http://stackoverflow.com/questions/14662384/ for more information about the
 * issue.
 * 
 * @author David Megginson
 */
public class AddressActionProvider extends ActionProvider {

	private String mUrl;

	private boolean mIsLoading = false;

	private AddressBarListener mListener;

	// FIXME just until I figure out how to get it
	private MenuItem mMenuItem;

	private Context mContext;

	private View mContentView;

	private EditText mUrlField;

	private Button mCancelButton;

	public AddressActionProvider(Context context) {
		super(context);
		mContext = context;
		System.err.println(context);
	}

	@Override
	public View onCreateActionView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mContentView = inflater.inflate(R.layout.widget_address_bar, null);
		setupUrlField();
		setupCancelButton();
		return mContentView;
	}

	/**
	 * Set the parent menu item.
	 * 
	 * The parent activity needs to call this to set the menu item.
	 * 
	 * FIXME this is just temporary, until we can get the MenuItem some other
	 * way.
	 * 
	 * @param menuItem
	 *            the menu item that owns this provider.
	 */
	public void setMenuItem(MenuItem menuItem) {
		mMenuItem = menuItem;
	}

	/**
	 * Set a listener to receive notifications of events from this widget.
	 * 
	 * @param l
	 *            The lister to receive address bar events.
	 */
	public void setAddressBarListener(AddressBarListener l) {
		mListener = l;
	}

	public void setUrl(String url) {
		mUrl = url;
		doUpdateStatus();
	}

	public void setIsLoading(boolean isLoading) {
		mIsLoading = isLoading;
		doUpdateStatus();
	}

	private void setupUrlField() {
		mUrlField = (EditText) mContentView.findViewById(R.id.urlField);
		mUrlField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						mListener.onLoadStarted(v.getText().toString());
						return true;
					}
				});

	}

	private void setupCancelButton() {
		mCancelButton = (Button) mContentView
				.findViewById(R.id.button_url_clear);
		mCancelButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				doCancel();
			}
		});
	}

	/**
	 * Update the loading status display.
	 */
	private void doUpdateStatus() {
		// update the URL field
		if (mUrlField != null) {
			mUrlField.setText(mUrl);
		}
	}

	/**
	 * Do a cancel action, depending on contenxt.
	 */
	private void doCancel() {
		if (mIsLoading) {
			// if loading, cancel the load
			mListener.onLoadCancelled(mUrl);
		} else if (mUrlField.getText() != null
				&& mUrlField.getText().length() > 0) {
			// if not loading but there's text, clear the field
			mUrlField.setText(null);
		} else {
			// if not loading and no text, collapse
			mMenuItem.collapseActionView();
		}
	}

	public interface AddressBarListener {

		public abstract void onLoadStarted(String url);

		public abstract void onLoadCancelled(String url);

	}

}
