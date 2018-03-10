package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        /*GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph.addSeries(series);
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Jump to edit this habit activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // show alert dialog in responding to click the fab
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
                builder.setTitle("Edit Habit")
                        .setMessage("Do you want to save your change on this habit?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editHabitRequest();
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

    private void editHabitRequest() {
        //TODO: send request
        // get params
        TextView mTitleView = (TextView)findViewById(R.id.title_txt);
        TextView mDesView = (TextView)findViewById(R.id.detail_txt);
        TextView mTimesView = (TextView)findViewById(R.id.times_txt);
        Spinner mPeriodView = (Spinner)findViewById(R.id.period_spinner);
        Switch mRemider = (Switch)findViewById(R.id.reminder_switch);

        //TODO: get username
        final String username = "test@example.com";
        final String habit_id = "6";
        final String title = mTitleView.getText().toString();
        final String description = mDesView.getText().toString();
        final String times = mTimesView.getText().toString();
        final String period = mPeriodView.getSelectedItem().toString();
        final String reminder = mRemider.isChecked()?"1":"0";

        // send edit habit request
        RequestQueue queue = Volley.newRequestQueue(this);
        final String add_habit_url = "https://habit-rabbit.000webhostapp.com/edit_habit.php";

        // TODO: update local file to store this new habit

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
                                builder.setTitle("Edit Habit")
                                        .setMessage("Changes have been saved!")
                                        .setPositiveButton("OK", null)
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
                                builder.setTitle("Edit Habit")
                                        .setMessage("Edit habit failed!")
                                        .setNegativeButton("Retry", null)
                                        .setPositiveButton("OK", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailActivity.this);
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
