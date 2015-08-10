package vn.mog.app360.sdk.demo.payment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import vn.mog.app360.sdk.demo.R;
import vn.mog.app360.sdk.payment.CardRequest;
import vn.mog.app360.sdk.payment.data.CardTransaction;
import vn.mog.app360.sdk.payment.data.TransactionStatus;
import vn.mog.app360.sdk.payment.interfaces.CardRequestListener;

public class CardPaymentActivity extends BasePaymentActivity {
    private static final String TAG = "CardPaymentActivity";
    private Spinner mSpVendors;
    private EditText mEdtSerial, mEdtCodePin;
    private Button mBtnCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        initView();
    }

    /**
     *
     */
    private void initView() {
        mSpVendors = (Spinner) findViewById(R.id.spVendor);
        mEdtSerial = (EditText) findViewById(R.id.edtSerial);
        mEdtCodePin = (EditText) findViewById(R.id.edtCodePin);
        mBtnCharge = (Button) findViewById(R.id.btnCharge);
        mBtnCharge.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openProgressDialog();
                charge();
            }
        });
    }

    /**
     *
     */
    private void charge() {
        String vendor = mSpVendors.getSelectedItem().toString()
                .toLowerCase(Locale.US);

        CardRequest cardRequest = new CardRequest.Builder()
                .setCardCode(mEdtCodePin.getText().toString())
                .setCardSerial(mEdtSerial.getText().toString())
                .setCardVendor(vendor).setPayload("testpayload")
                .setSync(true)
                .setListener(new CardRequestListener() {

                    @Override
                    public void onSuccess(CardTransaction transaction) {
                        dismissProgressDialog();

                        if (transaction.getStatus() == TransactionStatus.COMPLETED) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.charge_success, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.charge_fail, transaction.getDetail().getDescription()),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.charge_fail, throwable.toString()), Toast.LENGTH_SHORT)
                                .show();
                        Log.e(TAG, "Card charge fail", throwable);
                    }
                }).build();
        cardRequest.execute();
    }
}
