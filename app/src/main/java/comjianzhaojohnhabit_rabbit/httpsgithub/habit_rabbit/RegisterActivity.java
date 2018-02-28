package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    // UI references.
    private EditText mEmailView;
    private EditText mPassword_1View;
    private EditText mPassword_2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.email);
        mPassword_1View = (EditText) findViewById(R.id.password_1);
        mPassword_2View = (EditText) findViewById(R.id.password2);
        mPassword_2View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.register_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPassword_1View.setError(null);
        mPassword_2View.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password1 = mPassword_1View.getText().toString();
        String password2 = mPassword_2View.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password1)) {
            mPassword_1View.setError(getString(R.string.error_field_required));
            focusView = mPassword_1View;
            cancel = true;
        } else if (!isPasswordValid(password1)) {
            mPassword_1View.setError(getString(R.string.error_invalid_password));
            focusView = mPassword_1View;
            cancel = true;
        }

        // Check for consistent password
        if (!password1.equals(password2)) {
            mPassword_2View.setError("Two passwords do not match");
            focusView = mPassword_2View;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);
            requestRegister(email, password1);
        }
    }

    private void requestRegister(String email, String password) {
        // get email and password
        final String mEmail = email;
        final String mPassword = password;

        // send login request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_reg = "https://habit-rabbit.000webhostapp.com/Register.php";

        StringRequest loginReq = new StringRequest(Request.Method.POST, url_reg,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the response
                            JSONObject jsonRes = new JSONObject(response);
                            Boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                // jump to login page
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                mEmailView.setError("Email already exist");
                                mEmailView.requestFocus();
                            }
                        } catch (JSONException e) {
                            mEmailView.requestFocus();
                            Snackbar.make(mEmailView, e.toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            mEmailView.setText(e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mEmailView.requestFocus();
                Snackbar.make(mEmailView, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", mEmail);
                params.put("password", mPassword);
                return params;
            }
        };

        queue.add(loginReq);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic

        return password.length() > 3;
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
//        getIntent().putExtra("email", mEmailView.getText().toString());
        startActivity(intent);
    }
}
