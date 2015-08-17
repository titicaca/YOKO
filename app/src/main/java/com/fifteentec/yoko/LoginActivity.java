package com.fifteentec.yoko;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.API.APIServer;
import com.fifteentec.Component.User.UserServer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends LoaderActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{};
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mPhoneView;

    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone_actv);
        mPhoneView.setText(UserServer.getInstance().getMobile());
        mPasswordView = (EditText) findViewById(R.id.password_et);
        mPasswordView.setText(UserServer.getInstance().getPassword());
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInBtn = (Button) findViewById(R.id.sign_in_btn);
        mSignInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView mForgetCodeTv = (TextView) findViewById(R.id.forget_code_tv);
        mForgetCodeTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        TextView mRegisterTv = (TextView) findViewById(R.id.register_tv);
        mRegisterTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mLoginFormView = findViewById(R.id.user_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString().replaceAll("[\\s\\-]+", "");
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        while (true) {
            // Check for a valid phone.
            if (TextUtils.isEmpty(phone)) {
                mPhoneView.setError(getString(R.string.error_field_required));
                focusView = mPhoneView;
                cancel = true;
                break;
            } else if (!InfoValidate.isPhoneValid(phone)) {
                mPhoneView.setError(getString(R.string.error_invalid_phone));
                focusView = mPhoneView;
                cancel = true;
                break;
            }

            // Check for a valid password
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
                break;
            } else if (!InfoValidate.isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
                break;
            }

            break;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(phone, password);
            mAuthTask.execute((Void) null);
        }
    }

    public void changePassword() {
        Intent intent = new Intent(LoginActivity.this,
                ValidateActivity.class);
        intent.putExtra("FROM_WHERE", "LOGIN_ACTIVITY");
        startActivity(intent);
    }

    public void register() {
        Intent intent = new Intent(LoginActivity.this,
                ValidateActivity.class);
        startActivity(intent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int animTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(animTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(animTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void setAdapter(ArrayAdapter<String> adapter) {
        mPhoneView.setAdapter(adapter);
    }

    public UserLoginTask getAuthTask() {
        return mAuthTask;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mPassword;

        UserLoginTask(String phone, String password) {
            mPhone = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(0);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mPhone)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            UserServer.getInstance().userLogin(LoginActivity.this, mPhone, mPassword);

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            afterPostExecute(success);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        public void afterPostExecute(final Boolean success, JSONObject error_response) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                try {
                    if (error_response.has(APIServer.STRING_ERROR_STATUS_CODE) &&
                            Integer.parseInt(error_response.get(APIServer.STRING_ERROR_STATUS_CODE).toString())
                                    == APIServer.VALUE_BAD_REQUEST) {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}