package vn.mog.app360.sdk.demo.payment;

import vn.mog.app360.sdk.demo.R;
import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Open and Dismiss dialog loading
 */
public abstract class BasePaymentActivity extends Activity {

	protected ProgressDialog mProgressDialog;

	/**
	 * 
	 */
	protected void openProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.loading));
		mProgressDialog.show();
	}

	/**
	 * 
	 */
	protected void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}
}
