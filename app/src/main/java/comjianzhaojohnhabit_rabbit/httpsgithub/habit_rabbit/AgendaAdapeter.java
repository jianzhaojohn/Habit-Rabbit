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

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }


    // May need to use onBindViewHolder




    @Override
    public int getItemCount() {
        return mNumItems;
    }


    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView periodTextView;
        TextView freqTextView;
        TextView detailTextView;

        TextView dontchange1;
        TextView dontchange2;
        TextView dontchange3;

        TextView timecompeteTV;

        ProgressBar mProgress;
        TextView progressTxt;
        ImageView progressDone;

        public EventViewHolder(View itemView){
            super(itemView);

            titleTextView = (TextView)itemView.findViewById(R.id.tv_event_name);
//            periodTextView = (TextView)itemView.findViewById(R.id.tv_event_period);
//            freqTextView = (TextView)itemView.findViewById(R.id.tv_event_times);
            detailTextView=(TextView)itemView.findViewById(R.id.tv_event_detail);
//            dontchange1=(TextView)itemView.findViewById(R.id.habit_period);
//            dontchange2=(TextView)itemView.findViewById(R.id.time_tocomplete);
//            dontchange3=(TextView)itemView.findViewById(R.id.timealready);
//            timecompeteTV=(TextView)itemView.findViewById(R.id.complete_time);
            mProgress=(ProgressBar)itemView.findViewById(R.id.completion_progressBar);
            progressTxt=itemView.findViewById(R.id.progress_txt);
            progressDone =itemView.findViewById(R.id.done_img);
        }

        void bind(Habit event){
            titleTextView.setText(event.getName());
//            periodTextView.setText(event.getPeriod());
//            freqTextView.setText(""+event.getTimesPerPeriod());
            detailTextView.setText(event.getDescription());

            // dont change the following
//            dontchange1.setText("Habit period: ");
//            dontchange2.setText("Time to complete: ");
//            dontchange3.setText("Times already compete: ");

//            timecompeteTV.setText(""+event.getStreak());

            // set progress
            mProgress.setMax(event.getTimesPerPeriod());
            mProgress.setProgress(event.getStreak());
            progressTxt.setText(event.getStreak()+"/"+event.getTimesPerPeriod());

            // on click listener
            progressDone.setOnClickListener(v -> addRecordRequest(event));

            // listener
            itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, HabitDetailActivity.class);
                intent.putExtra(HabitDetailFragment.ARG_ITEM_ID, HabitList.HABITS_list.indexOf(event)+"");
                context.startActivity(intent);
                return true;
            });
        }

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
                                mProgress.setProgress(habit.getStreak());

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Adding Record")
                                        .setMessage("Adding record failed!")
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


