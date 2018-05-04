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
 * this is the fragment for the item iside the agenda fragment
 */

public class AgendaItem_Fragment extends Fragment {
        RecyclerView mRecyclerView;
        AgendaAdapeter mAdapter;
        ArrayList<Habit> mEventList;

        //requered a contructor
        public AgendaItem_Fragment() {}

    /**
     *
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). This will be called between onCreate(Bundle) and onActivityCreated(Bundle).

       If you return a View from here, you will later be called in onDestroyView() when the view is being released.
     *
     * @param inflater-The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container-If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState- If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
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
