package vn.mog.app360.sdk.demo.payment;

import java.util.Locale;

import vn.mog.app360.sdk.demo.R;
import vn.mog.app360.sdk.payment.SmsRequest;
import vn.mog.app360.sdk.payment.data.SmsTransaction;
import vn.mog.app360.sdk.payment.interfaces.SmsRequestListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static vn.mog.app360.sdk.demo.util.Constant.TAG;

public class SMSPaymentActivity extends BasePaymentActivity {
	private EditText mEdtAmount, mEdtSyntax;
	private Button mBtnGetSyntax, mBtnSendSMS;
	private Spinner mSpVendors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_payment);
		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		mEdtAmount = (EditText) findViewById(R.id.edtAmount);
		mEdtSyntax = (EditText) findViewById(R.id.edtSyntax);
		mBtnGetSyntax = (Button) findViewById(R.id.btnGetSyntax);
		mBtnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		mSpVendors = (Spinner) findViewById(R.id.spVendor);

		mBtnGetSyntax.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openProgressDialog();
				getSyntax();
			}
		});

	}

	/**
	 * Get syntax from App360 API
	 */
	private void getSyntax() {
		String vendor = mSpVendors.getSelectedItem().toString()
				.toLowerCase(Locale.US);

		int amount = -1;
		try {
			amount = Integer.parseInt(mEdtAmount.getText().toString());
		} catch (NumberFormatException e){
			dismissProgressDialog();
			Log.w(TAG, getString(R.string.amount_not_valid), e);
			Toast.makeText(this, R.string.amount_not_valid, Toast.LENGTH_SHORT).show();
			return;
		}

		SmsRequest smsRequest = new SmsRequest.Builder().setAmounts(amount)
				.setPayload("testpayload").setSmsVendor(vendor)
				.setListener(new SmsRequestListener() {
					@Override
					public void onSuccess(SmsTransaction transaction) {
						dismissProgressDialog();
						mEdtSyntax.setText(transaction.getSyntax());
						setupButtonSendSMS(transaction);
					}

					@Override
					public void onFailure(Throwable throwable) {
						dismissProgressDialog();
						Toast.makeText(getApplicationContext(),
								R.string.get_syntax_fail, Toast.LENGTH_SHORT)
								.show();
						Log.e(TAG, "SMSPayment Get syntax fail", throwable);
					}
				}).build();
		smsRequest.execute();
	}

	/**
	 * 
	 * @param transaction
	 */
	private void setupButtonSendSMS(final SmsTransaction transaction) {
		mBtnSendSMS.setEnabled(true);
		mBtnSendSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendSMS(transaction.getRecipient(), transaction.getSyntax());
			}
		});
	}

	/**
	 * 
	 * @param phoneNumber
	 * @param content
	 */
	private void sendSMS(String phoneNumber, String content) {
		Uri smsUri = Uri.parse("smsto:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
		intent.putExtra("sms_body", content);
		startActivity(intent);
	}

}
