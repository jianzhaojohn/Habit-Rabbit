package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

/**
 * An activity representing a single Habit detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HabitListActivity}.
 */
public class HabitDetailActivity extends AppCompatActivity {
    private TextView mTitleView, mDesView, mTimesView;
    private Spinner mPeriodView;
    private Switch mReminder;

    private Habit currentHabit;
    private String username, title, description, times, period, reminder, habit_id;
    private int valTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);

        // show alert dialog in responding to click the fab
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
                builder.setTitle("Edit Habit")
                        .setMessage("Do you want to save your changes on this habit?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preRequest();
                            }
                        })
                        .create()
                        .show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(HabitDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(HabitDetailFragment.ARG_ITEM_ID));
            HabitDetailFragment fragment = new HabitDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.habit_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, HabitListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preRequest(){
        // get params

        mTitleView = findViewById(R.id.title_txt);
        mDesView = findViewById(R.id.detail_txt);
        mTimesView = findViewById(R.id.times_txt);
        mPeriodView = (Spinner)findViewById(R.id.period_spinner);
        mReminder = (Switch)findViewById(R.id.reminder_switch);

        username = SharedPref.getUser(HabitDetailActivity.this);
        currentHabit = HabitList.Habit_table.get(getIntent().getStringExtra(HabitDetailFragment.ARG_ITEM_ID));
        habit_id = currentHabit.getHabitID()+"";
        description = mDesView.getText().toString();
        title = mTitleView.getText().toString();
        times = mTimesView.getText().toString();
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
            editHabitRequest();
            Snackbar.make(mTitleView, "Saving changes...", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void editHabitRequest() {

        // send edit habit request
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue(this);
        final String add_habit_url = "https://habit-rabbit.000webhostapp.com/edit_habit.php";

        // request server to add this habit to database
        StringRequest loginReq = new StringRequest(Request.Method.POST, add_habit_url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the response
                            JSONObject jsonRes = new JSONObject(response);
                            Boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                // update local file
                                currentHabit.setName(title);
                                currentHabit.setTimesPerPeriod(valTimes);
                                currentHabit.setPeriod(period);
                                currentHabit.setReminder(mReminder.isChecked());
                                currentHabit.setDescription(description);
                                SharedPref.editHabit(HabitDetailActivity.this, currentHabit);

                                // alert user
                                Snackbar.make(mTitleView, "Habit has been saved.", Snackbar.LENGTH_SHORT)
                                        .show();
                            } else {
                                //show message when fails
                                Snackbar.make(mTitleView, "Failed on Saving this habit!", Snackbar.LENGTH_SHORT)
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
            //On errorResponse
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mTitleView, "Volley Error! Please check your connection or try again later.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }) {
            //return the habit information in Map form
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("habit_id", habit_id);
                params.put("username", username);
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
