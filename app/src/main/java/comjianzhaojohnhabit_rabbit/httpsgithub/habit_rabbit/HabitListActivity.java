package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected static RecyclerView.Adapter adapter;

    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
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

    /**
     * Initialize the contents of the Activity's standard options menu. You should place your menu items in to menu.
     * @param menu-The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     *This hook is called whenever an item in your options menu is selected.
     * @param item-The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_Profile:
                startActivity(new Intent(HabitListActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
                startActivity(new Intent(HabitListActivity.this, CalendarActivity.class));
                break;
            case  R.id.nav_logout:
                try{
                    // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    // clear and delete data file, then logout
                    SharedPref.clearAll(HabitListActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
//                startActivity(new Intent(HabitListActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //set up the recycleView
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, HabitList.HABITS_list, mTwoPane));
        adapter = new SimpleItemRecyclerViewAdapter(this, HabitList.HABITS_list, mTwoPane);
        recyclerView.setAdapter(adapter);
    }


    //create a class which extends the RecyclerView.Adapter
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final HabitListActivity mParentActivity;
        private final List<Habit> mValues;
        private final boolean mTwoPane;
        //on clicklistener
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit item = (Habit) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(HabitDetailFragment.ARG_ITEM_ID, String.valueOf(item.getHabitID()));
                    HabitDetailFragment fragment = new HabitDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.habit_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, HabitDetailActivity.class);
                    intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, String.valueOf(item.getHabitID()));
//                    intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, HabitList.HABITS_list.indexOf(item)+"");

                    context.startActivity(intent);
                }
            }
        };

        //onLongClickListener
        private  final View.OnLongClickListener mOnLongClickListener = v -> {
            // respond the delete button with a dialog box
            final Context context = v.getContext();
            final Habit currentHabit = (Habit) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit Habit")
                    .setMessage("Do you want to delete this habit?")
                    .setNegativeButton("NO", null)
                    .setPositiveButton("YES", (dialog, which) -> deleteHabitRequest(context, currentHabit))
                    .create()
                    .show();
            return true;
        };


        SimpleItemRecyclerViewAdapter(HabitListActivity parent,
                                      List<Habit> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        /**
         *Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.habit_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should update the contents of the itemView to reflect the item at the given position.
         * @param holder-The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
         * @param position-The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Context context = holder.itemView.getContext();
            int currentPosition = position;
            final Habit currentHabit = HabitList.HABITS_list.get(position);

//            holder.mIdView.setText(mValues.get(position).getHabitID()+"");
            holder.mIdView.setText(position+1+"");
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.itemView.setOnLongClickListener(mOnLongClickListener);

            holder.mDeleteImg.setTag(mValues.get(currentPosition));
            holder.mDeleteImg.setOnClickListener(v -> {
                // respond the delete button with a dialog box
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
            });
        }

        //deleted the database
        private void deleteHabit(Context mContext, Habit habit) {
            int currentPosition = HabitList.HABITS_list.indexOf(habit);
            // delete habit locally
            SharedPref.deleteHabit(mContext, habit);
            HabitList.deleteHabit(habit);
            notifyItemRemoved(currentPosition);
            if (currentPosition != mValues.size()) {
                notifyItemRangeChanged(currentPosition, mValues.size()-currentPosition);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        //update the deletion to the database
        private void deleteHabitRequest(final Context context, final Habit habit) {
            // get params
            final String username = SharedPref.getUser(context);
            final String habit_id = habit.getHabitID()+"";
            // send delete habit request
            RequestQueue queue = VolleySingleton.getInstance(context)
                    .getRequestQueue(context);
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
                                    deleteHabit(context, habit);

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

        // viewHolder class
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
