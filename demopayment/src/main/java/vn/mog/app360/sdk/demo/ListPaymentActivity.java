package vn.mog.app360.sdk.demo;

import vn.mog.app360.sdk.demo.payment.BankingPaymentActivity;
import vn.mog.app360.sdk.demo.payment.CardPaymentActivity;
import vn.mog.app360.sdk.demo.payment.SMSPaymentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListPaymentActivity extends Activity {
	private static final String[] ARRAY_PAYMENT = new String[] {
			"SMS Payment API", "Card Payment API", "Bank Payment API" };

	private ListView mLvPayment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_payment);
		initView();
	}

	private void initView(){
    	mLvPayment = (ListView) findViewById(R.id.lvPayment);
    	ArrayAdapter<String> arrPayment = new ArrayAdapter<String>(this, 
    			android.R.layout.simple_list_item_1, ARRAY_PAYMENT);
    	mLvPayment.setAdapter(arrPayment);
    	
    	mLvPayment.setOnItemClickListener(clickListPayment);
    }
	
	OnItemClickListener clickListPayment = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent it = new Intent();
			switch (position) {
			case 0:
				it.setClass(getApplicationContext(), SMSPaymentActivity.class);
				break;
			case 1:
				it.setClass(getApplicationContext(), CardPaymentActivity.class);
				break;
			case 2:
				it.setClass(getApplicationContext(), BankingPaymentActivity.class);
				break;
			}
			
			startActivity(it);
			
		}
	};
}
