package vn.mog.app360.sdk.demo.payment;

import vn.mog.app360.sdk.demo.R;
import vn.mog.app360.sdk.payment.BankRequest;
import vn.mog.app360.sdk.payment.data.BankTransaction;
import vn.mog.app360.sdk.payment.interfaces.BankRequestListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static vn.mog.app360.sdk.demo.util.Constant.TAG;

public class BankingPaymentActivity extends BasePaymentActivity {
    private EditText mEdtAmount, mEdtUrl;
    private Button mBtnGetUrl, mBtnOpenBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking_payment);
        initView();
    }

    /**
     *
     */
    private void initView() {
        mEdtAmount = (EditText) findViewById(R.id.edtAmount);
        mEdtUrl = (EditText) findViewById(R.id.edtUrl);
        mBtnGetUrl = (Button) findViewById(R.id.btnGetUrl);
        mBtnOpenBrowser = (Button) findViewById(R.id.btnOpenBrowser);

        mBtnGetUrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openProgressDialog();
                getUrl();
            }
        });
    }

    /**
     *
     */
    private void getUrl() {
        int amount = -1;
        try {
            amount = Integer.parseInt(mEdtAmount.getText().toString());
        } catch (NumberFormatException e) {
            dismissProgressDialog();
            Log.w(TAG, getString(R.string.amount_not_valid), e);
            Toast.makeText(this, R.string.amount_not_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount < 10000) {
            dismissProgressDialog();
            Toast.makeText(this, R.string.warning_bank_amount,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        BankRequest bankRequest = new BankRequest.Builder().setAmount(amount)
                .setPayload("testpayload").setListener(new BankRequestListener() {

                    @Override
                    public void onSuccess(BankTransaction transaction) {
                        dismissProgressDialog();
                        mEdtUrl.setText(transaction.getPayUrl());
                        setupButtonOpenBrowser(transaction);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(),
                                R.string.get_pay_url_fail, Toast.LENGTH_SHORT)
                                .show();
                        Log.e(TAG, "Get URL Banking Payment fail", throwable);
                    }
                }).build();
        bankRequest.execute();
    }

    /**
     * @param transaction
     */
    private void setupButtonOpenBrowser(final BankTransaction transaction) {
        mBtnOpenBrowser.setEnabled(true);
        mBtnOpenBrowser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebBrowser(transaction.getPayUrl());
            }
        });
    }

    /**
     * @param url
     */
    private void openWebBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
