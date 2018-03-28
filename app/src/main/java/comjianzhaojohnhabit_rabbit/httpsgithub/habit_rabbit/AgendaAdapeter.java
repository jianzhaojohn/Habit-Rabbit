package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 2018/3/22.
 */

public class AgendaAdapeter extends RecyclerView.Adapter<AgendaAdapeter.EventViewHolder>{

    private ArrayList<Habit> mList;
    private int mNumItems;

    public AgendaAdapeter( ArrayList<Habit> list){
        mList = list;
        mNumItems = list.size();
    }

    /**
     *
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.

     This new ViewHolder should be constructed with a new View that can represent the items of the given type. You can either create a new View manually or inflate it from an XML layout file.

     The new ViewHolder will be used to display items of the adapter using onBindViewHolder(ViewHolder, int, List). Since it will be re-used to display different items in the data set, it is a good idea to cache references to sub views of the View to avoid unnecessary findViewById(int) calls.
     *
     * @param parent-The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType-The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public AgendaAdapeter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.actiity_agenda_item_detail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;

    }

    /**
     *
     * Called by RecyclerView to display the data at the specified position. This method should update the contents of the itemView to reflect the item at the given position.
     *
     * @param holder-The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position-The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumItems;
    }


    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView detailTextView;
        ProgressBar mProgress;
        TextView progressTxt;
        ImageView progressDone;

        //constructor
        public EventViewHolder(View itemView){
            super(itemView);
            titleTextView = (TextView)itemView.findViewById(R.id.tv_event_name);
            detailTextView=(TextView)itemView.findViewById(R.id.tv_event_detail);
            mProgress=(ProgressBar)itemView.findViewById(R.id.completion_progressBar);
            progressTxt=itemView.findViewById(R.id.progress_txt);
            progressDone =itemView.findViewById(R.id.done_img);
        }

        /**
         *
         * bind the information to the view and display them in the fragement
         *
         * @param event- user habbit
         */
        void bind(Habit event){
            titleTextView.setText(event.getName());
            detailTextView.setText(event.getDescription());

            // set progress
            mProgress.setMax(event.getTimesPerPeriod());
            mProgress.setProgress(event.getStreak());
            progressTxt.setText(event.getStreak()+"/"+event.getTimesPerPeriod());

            // on click listeners
            progressDone.setOnClickListener(v -> addRecordRequest(event));

//            itemView.setOnClickListener(v -> {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, HabitDetailActivity.class);
//                intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, event.getHabitID()+"");
//                context.startActivity(intent);
//            });

            // listener
            itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, HabitDetailActivity.class);
                intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, event.getHabitID()+"");
                context.startActivity(intent);
                return true;
            });
        }

        /**
         * update the local change to dtatbase
         *
         * @param habit- user habbit
         */
        private void addRecordRequest(Habit habit) {
            // get params
            final String username = HabitList.getUserName();
            final String habit_id = habit.getHabitID()+"";
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            final String date = dateFormat.format(currentDate);
            Context context = itemView.getContext();
            // send delete habit request
            RequestQueue queue = Volley.newRequestQueue(context);
            final String add_record_url = "https://habit-rabbit.000webhostapp.com/add_record.php";

            // request server to add this habit to database
            StringRequest loginReq = new StringRequest(Request.Method.POST, add_record_url,
                    response -> {
                        try {
                            // parse the response
                            JSONObject jsonRes = new JSONObject(response);
                            Boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                // update local records
                                habit.updateStreaks(currentDate, 1);
                                SharedPref.saveRecords(context,jsonRes.getJSONArray("records"));
                                notifyItemChanged(mList.indexOf(habit));
//                                mProgress.setProgress(habit.getStreak());
//                                progressTxt.setText(habit.getStreak()+"/"+habit.getTimesPerPeriod());
                            } else {
                                //show message when fails
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Adding Record")
                                        .setMessage("Adding record failed!")
                                        .setNegativeButton("Retry", null)
                                        .setPositiveButton("OK", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            //show message when catch exception
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Response error")
                                    .setMessage(e.toString())
                                    .setNegativeButton("OK", null)
                                    .create()
                                    .show();
                            e.printStackTrace();
                        }
                    }, error -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Volley Error")
                                .setMessage(error.toString())
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("habit_id", habit_id);
                    params.put("date", date);
                    return params;
                }
            };

            queue.add(loginReq);
        }


    }


}


