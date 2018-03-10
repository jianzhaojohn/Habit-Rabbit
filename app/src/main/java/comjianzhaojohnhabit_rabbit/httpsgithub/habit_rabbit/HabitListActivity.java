package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

import comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit.dummy.DummyContent;

import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of Habits. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HabitDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HabitListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        // jump to newHabitActivity in responding to click the fab
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HabitListActivity.this, AddHabitActivity.class));
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_agenda);
        fab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HabitListActivity.this, CalendarActivity.class));
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.habit_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.habit_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, HabitList.HABITS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final HabitListActivity mParentActivity;
        private final List<Habit> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit item = (Habit) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(HabitDetailFragment.ARG_ITEM_ID, HabitList.HABITS.indexOf(item)+"");
                    HabitDetailFragment fragment = new HabitDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.habit_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, HabitDetailActivity.class);
//                    intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, item.getID());
                    intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, HabitList.HABITS.indexOf(item)+"");

                    context.startActivity(intent);
                }
            }
        };
        private  final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO
                return true;
            }
        };


        SimpleItemRecyclerViewAdapter(HabitListActivity parent,
                                      List<Habit> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.habit_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Context context = holder.itemView.getContext();
            int currentPosition = position;
            final Habit currentHabit = HabitList.HABITS.get(position);

//            holder.mIdView.setText(mValues.get(position).getID());
            holder.mIdView.setText(position+1+"");
            holder.mContentView.setText(mValues.get(position).getNameOfHabit());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.itemView.setOnLongClickListener(mOnLongClickListener);

            holder.mDeleteImg.setTag(mValues.get(currentPosition));
            holder.mDeleteImg.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // respond the delete button with a dialog box

//                    deleteHabitRequest(context, currentHabit);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Habit")
                        .setMessage("Do you want to delete this habit?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteHabitRequest(context, currentHabit);
                            }
                        })
                        .create()
                        .show();
                }
            });
        }

        private void deleteHabit(Habit habit) {
            //TODO: delete habit locally
            int currentPosition = HabitList.HABITS.indexOf(habit);
            HabitList.HABITS.remove(currentPosition);
            HabitList.habitlist.remove(habit);
            notifyItemRemoved(currentPosition);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        private void deleteHabitRequest(final Context context, final Habit habit) {
            // get params
            final String username = "test@example.com"; //TODO:
            final String habit_id = habit.getID();
            // send add new habit request
            RequestQueue queue = Volley.newRequestQueue(context);
            final String add_habit_url = "https://habit-rabbit.000webhostapp.com/delete_habit.php";

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
                                    deleteHabit(habit);

                                    // jump to habit list page
                                    context.startActivity(new Intent(context, HabitListActivity.class));
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Delete Habit")
                                            .setMessage("Delete habit failed!")
                                            .setNegativeButton("Retry", null)
                                            .setPositiveButton("OK", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    params.put("username", username);
                    params.put("habit_id", habit_id);
                    return params;
                }
            };

            queue.add(loginReq);

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mDeleteImg;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mDeleteImg = (ImageView) view.findViewById(R.id.delete_image);

            }
        }

    }
}
