package com.fifteentec.yoko;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.API.APIKey;
import com.fifteentec.Component.User.UserServer;

public class RegisterActivity extends BaseActivity {
    private static final String[] DUMMY_CREDENTIALS = new String[]{};

    private String mPhone;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mPassword2Et;
    private Button mRegisterBtn;

    private UserRegisterTask mRgstTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String message = getIntent().getStringExtra("FROM_WHERE");
        if (message != null && message.equals("VALIDATE_ACTIVITY")) {
            mPhone = APIKey.VALUE_ROLE_MOBILE_PREFIX + UserServer.getInstance().getMobile();
        } else {
            mPhone = "";
        }

        mUsernameEt = (EditText) findViewById(R.id.register_input_username_et);
        mPasswordEt = (EditText) findViewById(R.id.register_input_code_et);
        mPassword2Et = (EditText) findViewById(R.id.register_reinput_code_et);
        mRegisterBtn = (Button) findViewById(R.id.finish_btn);

        mRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    public void attemptRegister() {
        if (mRgstTask != null) {
            return;
        }

        // Reset errors.
        mUsernameEt.setError(null);
        mPasswordEt.setError(null);
        mPassword2Et.setError(null);

        // Store values at the time of the register attempt.
        String username = mUsernameEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        String password2 = mPassword2Et.getText().toString();

        boolean cancel = false;
        View focusView = null;

        while (true) {
            // Check for a valid username.
            if (TextUtils.isEmpty(username)) {
                mUsernameEt.setError(getString(R.string.error_field_required));
                focusView = mUsernameEt;
                cancel = true;
                break;
            } else if (!InfoValidate.isUsernameValid(username)) {
                mUsernameEt.setError(getString(R.string.error_invalid_username));
                focusView = mUsernameEt;
                cancel = true;
                break;
            }

            // Check for a valid password
            if (TextUtils.isEmpty(password)) {
                mPasswordEt.setError(getString(R.string.error_field_required));
                focusView = mPasswordEt;
                cancel = true;
                break;
            } else if (!InfoValidate.isPasswordValid(password)) {
                mPasswordEt.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordEt;
                cancel = true;
                break;
            }

            // Check for a valid password2
            if (TextUtils.isEmpty(password2)) {
                mPassword2Et.setError(getString(R.string.error_field_required));
                focusView = mPassword2Et;
                cancel = true;
                break;
            } else if (!InfoValidate.isPasswordValid(password2)) {
                mPassword2Et.setError(getString(R.string.error_invalid_password));
                focusView = mPassword2Et;
                cancel = true;
                break;
            }

            if (!password.equals(password2)) {
                mPasswordEt.setError(getString(R.string.error_inconsistent_register_passwords));
                focusView = mPasswordEt;
                cancel = true;
                break;
            }

            break;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user register attempt.
            mRgstTask = new UserRegisterTask(username, password);
            mRgstTask.execute((Void) null);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;

        UserRegisterTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            UserServer.getInstance().userRegister(RegisterActivity.this,
                    mPhone, mUsername, mPassword);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRgstTask = null;

            if (success) {
                finish();
            } else {
                mUsernameEt.setError(getString(R.string.error_invalid_username));
                mUsernameEt.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRgstTask = null;
        }
    }
}
