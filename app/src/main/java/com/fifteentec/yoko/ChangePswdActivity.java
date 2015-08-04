package com.fifteentec.yoko;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChangePswdActivity extends BaseActivity {
    private String mPhone;
    private EditText mPasswordEt;
    private EditText mPassword2Et;
    private Button mConfirmBtn;

    private UserChangePswdTask mChangePswdTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        String message = getIntent().getStringExtra("FROM_WHERE");
        if(message != null && message.equals("VALIDATE_ACTIVITY")) {
            mPhone = "0_" + getIntent().getStringExtra("PHONE");
        } else {
            mPhone = "";
        }

        mPasswordEt = (EditText)findViewById(R.id.changepswd_input_code_et);
        mPassword2Et = (EditText)findViewById(R.id.changepswd_reinput_code_et);
        mConfirmBtn = (Button)findViewById(R.id.confirm_btn);

        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptChangePswd();
            }
        });
    }

    public void attemptChangePswd() {
        if (mChangePswdTask != null) {
            return;
        }

        // Reset errors.
        mPasswordEt.setError(null);
        mPassword2Et.setError(null);

        // Store values at the time of the change password attempt.
        String password = mPasswordEt.getText().toString();
        String password2 = mPassword2Et.getText().toString();

        boolean cancel = false;
        View focusView = null;

        while(true)
        {
            // Check for a valid password
            if (TextUtils.isEmpty(password)) {
                mPasswordEt.setError(getString(R.string.error_field_required));
                focusView = mPasswordEt;
                cancel = true;
                break;
            } else if(!InfoValidate.isPasswordValid(password)) {
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
            } else if(!InfoValidate.isPasswordValid(password2)) {
                mPassword2Et.setError(getString(R.string.error_invalid_password));
                focusView = mPassword2Et;
                cancel = true;
                break;
            }

            if(!password.equals(password2)){
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
            mChangePswdTask = new UserChangePswdTask(password);
            mChangePswdTask.execute((Void) null);
        }
    }

    public class UserChangePswdTask extends AsyncTask<Void, Void, Boolean>{
        private final String mNewPassword;

        UserChangePswdTask(String newPassword){
            mNewPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            JSONObject changePswdParams = new JSONObject();
            try {
                changePswdParams.put(APIKey.KEY_USERNAME, mPhone);
                changePswdParams.put(APIKey.KEY_PASSWORD, mNewPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            APIServer.getInstance().sendJsonPut(APIUrl.URL_CHANGE_PASSWORD,
                    changePswdParams, null, new APIJsonCallbackResponse() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "恭喜你！修改密码成功！\n" + this.getResponse().toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }, getRequestQueue(), null);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mChangePswdTask = null;

            if (success) {
                finish();
            } else {
                mPasswordEt.setError(getString(R.string.error_invalid_password));
                mPasswordEt.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mChangePswdTask = null;
        }
    }
}