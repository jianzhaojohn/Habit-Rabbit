package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

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

        public EventViewHolder(View itemView){
            super(itemView);

            titleTextView = (TextView)itemView.findViewById(R.id.tv_event_name);
            periodTextView = (TextView)itemView.findViewById(R.id.tv_event_period);
            freqTextView = (TextView)itemView.findViewById(R.id.tv_event_times);
            detailTextView=(TextView)itemView.findViewById(R.id.tv_event_detail);
            dontchange1=(TextView)itemView.findViewById(R.id.habit_period);
            dontchange2=(TextView)itemView.findViewById(R.id.time_tocomplete);
            dontchange3=(TextView)itemView.findViewById(R.id.timealready);
            timecompeteTV=(TextView)itemView.findViewById(R.id.complete_time);
            mProgress=(ProgressBar)itemView.findViewById(R.id.completion_progressBar);

        }

        void bind(Habit event){
            titleTextView.setText(event.getName());
            periodTextView.setText(event.getPeriod());
            freqTextView.setText(""+event.getTimesPerPeriod());
            detailTextView.setText(event.getDescription());

            // dont change the following
            dontchange1.setText("Habit period: ");
            dontchange2.setText("Time to complete: ");
            dontchange3.setText("Times already compete: ");

            timecompeteTV.setText(""+event.getStreak());

            mProgress.setMax(event.getTimesPerPeriod());
            mProgress.setProgress(event.getStreak());
        }
    }


}


