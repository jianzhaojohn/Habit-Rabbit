package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddHabitActivity extends Activity {

    private TextView mTitleView, mDesView, mTimesView;
    private Spinner mPeriodView;
    private Switch mReminder;

    private String username, title, description, times, period, reminder;
    private int valTimes;
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

        mTitleView = (TextView)findViewById(R.id.editText_title);
        mDesView = (TextView)findViewById(R.id.editText_description);
        mTimesView = (TextView)findViewById(R.id.editText_times);
        mPeriodView = (Spinner)findViewById(R.id.spinner_f);
        mReminder = (Switch)findViewById(R.id.switch_reminder);

        Button ok_button = (Button) findViewById(R.id.button_habit_ok);
        Button cancel_btn = (Button) findViewById(R.id.button_habit_cancel);

        ok_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            attemptAddHabit();
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

    private void attemptAddHabit() {
        // Reset errors.
        mTitleView.setError(null);
        mTimesView.setError(null);

        // get params
        username = SharedPref.getUser(this);
        title = mTitleView.getText().toString();
        times = mTimesView.getText().toString();
        description = mDesView.getText().toString();
        period = mPeriodView.getSelectedItem().toString();
        reminder = mReminder.isChecked()?"1":"0";

        boolean cancel = false;
        View focusView = null;

        // check the validation of times
        if(TextUtils.isEmpty(times)){
            mTimesView.setError("Enter a non-zero repeat times.");
            focusView = mTimesView;
            cancel = true;
        } else {
            try {
                valTimes = Integer.parseInt(times);
                if (valTimes == 0){
                    mTimesView.setError("Enter a non-zero integer.");
                    focusView = mTimesView;
                    cancel = true;
                }
            } catch (NumberFormatException e){
                mTimesView.setError("Enter a non-zero integer.");
                focusView = mTimesView;
                cancel = true;
            }
        }

        // check the validation of title
        if(TextUtils.isEmpty(title) || title == "") {
            mTitleView.setError("Habit title cannot be empty.");
            focusView = mTitleView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            addHabitRequest();
            Snackbar.make(mTitleView, "Adding new habit...", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void addHabitRequest() {

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
                                Habit habit = new Habit(habit_id, title, period, valTimes);
                                SharedPref.saveHabit(AddHabitActivity.this, habit);
                                HabitList.addHabit(habit);

                                finish();
                                int position = HabitList.HABITS_list.size() - 1;
                                HabitListActivity.adapter.notifyItemInserted(position);
                                HabitListActivity.recyclerView.scrollToPosition(position);
                            } else {
                                // show message when fails
                                Snackbar.make(mTitleView, "Adding new habit \"" + title + "\" failed!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException e) {
                            //show message when catch exception
                            Snackbar.make(mTitleView, "Response Error: " + e.toString(), Snackbar.LENGTH_SHORT)
                                    .show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            // on errorResponse
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mTitleView, "Volley Error! Please check your connection or try again later.", Snackbar.LENGTH_SHORT)
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
