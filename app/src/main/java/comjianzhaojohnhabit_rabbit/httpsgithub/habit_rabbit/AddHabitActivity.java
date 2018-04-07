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
    /**
     *
     * Called when the activity is starting. This is where most initialization should go: calling setContentView(int) to inflate the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri, String[], String, String[], String) to retrieve cursors for data being displayed, etc.

     You can call finish() from within this function, in which case onDestroy() will be immediately called without any of the rest of the activity lifecycle (onStart(), onResume(), onPause(), etc) executing.

     Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.

     This method must be called from the main thread of your app.

     If you override this method you must call through to the superclass implementation.
     *
     * @param savedInstanceState-  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
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
                //start new activity
                startActivity(new Intent(AddHabitActivity.this, HabitListActivity.class));
                finish();
            }
        });
    }


    private void addHabitRequest() {
        // get params
        TextView mTitleView = (TextView)findViewById(R.id.editText_title);
        TextView mDesView = (TextView)findViewById(R.id.editText_description);
        TextView mTimesView = (TextView)findViewById(R.id.editText_times);
        Spinner mPeriodView = (Spinner)findViewById(R.id.spinner_f);
        Switch mRemider = (Switch)findViewById(R.id.switch_reminder);

        final String username = SharedPref.getUser(this);
        final String title = mTitleView.getText().toString();
        final String description = mDesView.getText().toString();
        final String times;
        if(mTimesView.getText().toString()=="0"){
            times="1";
        }else{
            times = mTimesView.getText().toString();
        }
        final String period = mPeriodView.getSelectedItem().toString();
        final String reminder = mRemider.isChecked()?"1":"0";

        // send add new habit request
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue(this);
        final String add_habit_url = "https://habit-rabbit.000webhostapp.com/add_habit.php";

        // request server to add this habit to database
        StringRequest addHabitReq = new StringRequest(Request.Method.POST, add_habit_url,
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

                                // update local file to store this new habit
                                Habit habit = new Habit(habit_id, title, period, Integer.parseInt(times));
                                SharedPref.saveHabit(AddHabitActivity.this, habit);
                                HabitList.addHabit(habit);
                                //TODO: notify adapter

                                // jump to habit list page
                                startActivity(new Intent(AddHabitActivity.this, HabitListActivity.class));
                                finish();
                            } else {
                                // show message when fails
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddHabitActivity.this);
                                builder.setTitle("Add New Habit")
                                        .setMessage("Add new habit failed!")
                                        .setNegativeButton("Retry", null)
                                        .setPositiveButton("OK", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            //show message when catch exception
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

            // on errorResponse
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
            /**
             *
             * @return the information of your habitt in Map form
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("habit", title);
                params.put("description", description);
                params.put("times", times);
                params.put("period", period);
                params.put("reminder", reminder);
                return params;
            }
        };

        queue.add(addHabitReq);
    }
}
