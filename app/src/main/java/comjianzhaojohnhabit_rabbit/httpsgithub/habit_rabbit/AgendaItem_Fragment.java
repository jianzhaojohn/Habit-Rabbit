package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by john on 2018/3/22.
 */

public class AgendaItem_Fragment extends Fragment {
        RecyclerView mRecyclerView;
        AgendaAdapeter mAdapter;
        ArrayList<Habit> mEventList;
        public AgendaItem_Fragment() {

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_agenda_item, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.agenda_rv);
        mEventList = new ArrayList<Habit>();

        Bundle args = getArguments();
        mEventList = args.getParcelableArrayList("events");


        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setHasFixedSize(false);

        mAdapter = new AgendaAdapeter(mEventList);

        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }



}
