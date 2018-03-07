package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
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

public class AddHabitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);

        Button ok_button = (Button) findViewById(R.id.button_habit_ok);
        final Button cancel_btn = (Button) findViewById(R.id.button_habit_cancel);

        ok_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addHabitRequest();
                                        }
                                    }
        );

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddHabitActivity.this, HabitListActivity.class));
            }
        });
    }


    private void addHabitRequest() {
        // send login request
        RequestQueue queue = Volley.newRequestQueue(this);
        String add_habit_url = "https://habit-rabbit.000webhostapp.com/AddHabit.php";

        // get params
        TextView mTitleView = (TextView)findViewById(R.id.editText_title);
        TextView mDesView = (TextView)findViewById(R.id.editText_description);
        TextView mTimesView = (TextView)findViewById(R.id.editText_times);
        Spinner mPeriodView = (Spinner)findViewById(R.id.spinner_f);
        Switch mRemider = (Switch)findViewById(R.id.switch_reminder);

        final String title = mTitleView.getText().toString();
        final String description = mDesView.getText().toString();
        final String times = mTimesView.getText().toString();
        final String period = mPeriodView.getSelectedItem().toString();
        final String reminder = mRemider.isChecked()?"true":"false";

        StringRequest loginReq = new StringRequest(Request.Method.POST, add_habit_url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the response
                            JSONObject jsonRes = new JSONObject(response);
                            Boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                // get habit id
                                int habit_id = jsonRes.getInt("habit_id");

                                // TODO: update habit_id in local file

                                // jump to habit list page
                                startActivity(new Intent(AddHabitActivity.this, HabitListActivity.class));
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddHabitActivity.this);
                                builder.setTitle("Add New Habit")
                                        .setMessage("Add new habit failed!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddHabitActivity.this);
                            builder.setTitle("Response error")
                                    .setMessage(e.toString())
                                    .setNegativeButton("OK", null)
                                    .create()
                                    .show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddHabitActivity.this);
                builder.setTitle("Volley Error")
                        .setMessage(error.toString())
                        .setNegativeButton("OK", null)
                        .create()
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                TODO:params.put("ussername", username);
                params.put("habit", title);
                params.put("description", description);
                params.put("times", times);
                params.put("period", period);
                params.put("reminder", reminder);
                return params;
            }
        };

        queue.add(loginReq);
    }
}
