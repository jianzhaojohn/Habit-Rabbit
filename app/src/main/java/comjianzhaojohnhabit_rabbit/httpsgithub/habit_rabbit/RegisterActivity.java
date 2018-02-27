package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

    // UI references.
    private EditText mEmailView;
    private EditText mPassword1View;
    private EditText mPassword2View;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.email);
        mPassword1View = (EditText) findViewById(R.id.password1);
        String psw = mPassword1View.getText().toString();
        mPassword2View = (EditText) findViewById(R.id.password2);
        mPassword2View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
//                    if (mPassword1View.getText().toString() == mPassword2View.getText().toString())
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
        Snackbar.make(mEmailView, "Attempt to Register", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
//        getIntent().putExtra("email", mEmailView.getText().toString());
        startActivity(intent);
    }
}
