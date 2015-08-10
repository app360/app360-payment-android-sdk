package vn.mog.app360.sdk.demo;

import static vn.mog.app360.sdk.demo.util.Constant.TAG;
import vn.mog.app360.sdk.App360SDK;
import vn.mog.app360.sdk.InitListener;
import vn.mog.app360.sdk.scopedid.SessionManager;
import vn.mog.app360.sdk.scopedid.SessionService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button mBtnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mBtnLogin = (Button) findViewById(R.id.btnLogin);

		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openProgressDialog();
				//Call login method
				login();
			}
		});
	}

	private void login() {
		// TODO login with username and password
		//Do login stuff here..
		//.........
		// After login success, app receives user-id. Use it to create session with App360 SDK
		onLoginSuccess("user-id");
	}
	
	private void onLoginSuccess(String userId) {

		// When login success, Init App360SDK
		initApp360SDK(userId);
	}

	/**
	 * 
	 * @param userId
	 */
	private void initApp360SDK(final String userId) {
		App360SDK.initialize(getString(R.string.appId),
				getString(R.string.appSecretKey), this, new InitListener() {
					@Override
					public void onSuccess() {
						//Check if init method re-open cached session successfully or not
						SessionService.Session currentSession = SessionManager
								.getCurrentSession();

						if (currentSession == null) {
							
							createApp360Session(userId);
						} else {
							Toast.makeText(getApplication(), "Re-open cached session successfully!",
									Toast.LENGTH_SHORT).show();
							Log.d(TAG, currentSession.toString());

							openListPaymentActivity();
							dissmissProgressDialog();
						}
					}

					@Override
					public void onFailure(Exception e) {
						Toast.makeText(getApplication(),
								"Init fail\n" + "See Logcat for more detail.",
								Toast.LENGTH_SHORT).show();
						Log.e(TAG, "Init fail", e);
						dissmissProgressDialog();
					}
				});
	}

	/**
	 * 
	 * @param userId
	 */
	private void createApp360Session(String userId) {
		SessionManager.createSession(userId,
				new SessionManager.SessionCallback() {
					@Override
					public void onSuccess() {
						Toast.makeText(getApplication(),
								"Create session success", Toast.LENGTH_SHORT)
								.show();

						SessionService.Session newSession = SessionManager
								.getCurrentSession();
						Log.d(TAG, "Created session:" + newSession.toString());

						openListPaymentActivity();
						dissmissProgressDialog();
					}

					@Override
					public void onFailure(Exception e) {
						Toast.makeText(
								getApplication(),
								"Create session fail\n"
										+ "See Logcat for more detail.",
								Toast.LENGTH_SHORT).show();
						Log.e(TAG, "Create session fail", e);
						dissmissProgressDialog();
					}
				});
	}

	/**
	 * 
	 */
	private void openListPaymentActivity() {
		Intent it = new Intent(this, ListPaymentActivity.class);
		startActivity(it);
		finish();
	}

	private ProgressDialog mProgressDialog;

	private void openProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.loading));
		mProgressDialog.show();
	}

	private void dissmissProgressDialog() {
		mProgressDialog.dismiss();
	}
	
}
