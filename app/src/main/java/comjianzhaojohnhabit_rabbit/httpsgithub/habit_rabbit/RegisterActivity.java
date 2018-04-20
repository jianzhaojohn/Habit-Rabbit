package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.email);
        mPassword_1View = (EditText) findViewById(R.id.password_1);
        mPassword_2View = (EditText) findViewById(R.id.password2);

        //cretae a editorActionListener
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

    // method call when user try to register
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
        } else if (!isPasswordValid(password1,email)) {
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
            requestRegister(email, password1);
        }
    }

    //update the data to database
    private void requestRegister(String email, String password) {
        // get email and password
        final String mEmail = email;
        final String mPassword = password;

        // send login request
        RequestQueue queue = VolleySingleton.getInstance(this)
                .getRequestQueue(this);
        final String url_reg = "https://habit-rabbit.000webhostapp.com/Register_encrypt.php";

        StringRequest loginReq = new StringRequest(Request.Method.POST, url_reg,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the response
                            JSONObject jsonRes = new JSONObject(response);
                            Boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                // jump to next page
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();

                            } else {
                                mEmailView.setError("Email already exist");
                                mEmailView.requestFocus();
                            }
                        } catch (JSONException e) {
                            //display the error message when catch a exception
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(e.toString())
                                    .setTitle("Response error")
                                    .setNegativeButton("", null)
                                    .create()
                                    .show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage(error.toString())
                        .setTitle("Volley Error")
                        .setNegativeButton("OK", null)
                        .create()
                        .show();
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

    /**
     * check if a email is valid
     * @param email- input string
     * @return true if emial is valid, false otherwise
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * check if your passwrod is valid
     * @param password- your passowrd
     * @return true if passwrods is valid, false otherwise
     */
    private boolean isPasswordValid(String password, String email) {

        boolean len=false;
        boolean uppercase = false;
        boolean lowercase = false;
        boolean specialcase = false;
        //boolean user = false;
        if(password.equals(email)){
            //user = true;
            return false;
        }
        int pass_length = password.length();
        if(pass_length > 8) len=true;
        for(int i = 0; i< pass_length; i++){

            if(password.charAt(i) >= 65 && password.charAt(i) <=90) {
                uppercase = true;
            }
            else if(password.charAt(i) >= 97 && password.charAt(i) <=122) {
                lowercase = true;
            }
            else if((password.charAt(i) >=33 && password.charAt(i) <= 47) || (password.charAt(i) >=58 && password.charAt(i) <= 64) || (password.charAt(i) >=91 && password.charAt(i) <= 97) || (password.charAt(i) >=123 && password.charAt(i) <= 126))
            {
                specialcase = true;
            }

        }
        return (len && uppercase && lowercase && specialcase);
    }

    //clickListener, which open the login page when clicked
    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
