package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by john on 2018/3/22.
 */

public class Agenda_Fragment extends Fragment {

    private TabLayout mTabLayout;
    public static ArrayList<Habit> todayList=new ArrayList<>();
    public static ArrayList<Habit> weekList=new ArrayList<>();
    public static ArrayList<Habit> monthList=new ArrayList<>();

    //constructor
    public Agenda_Fragment() {
        generatelist();
    }

    /**
     * get data from database and store them in the ArrayList
     */
    public void generatelist(){
        todayList=new ArrayList<>();
        weekList=new ArrayList<>();
        monthList=new ArrayList<>();

        for (int i = 0; i < HabitList.HABITS_list.size(); i++) {
            Habit habit = HabitList.HABITS_list.get(i);

            if (habit.getPeriod().equals("day")) {
                todayList.add(habit);
            } else if (habit.getPeriod().equals("week")) {
                weekList.add(habit);
            } else if (habit.getPeriod().equals("month")) {
                monthList.add(habit);
            }
        }


    }

    /**
     *
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null
     * (which is the default implementation). This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     *
     * @param inflater-The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container-If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState- If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result=inflater.inflate(R.layout.activity_agenda, container, false);
        ViewPager pager=(ViewPager)result.findViewById(R.id.agenda_content);
        pager.setAdapter(new Agadapter(getActivity(), getChildFragmentManager()));
        mTabLayout = (TabLayout)result.findViewById(R.id.agenda_tab);
        mTabLayout.setupWithViewPager(pager);
        return(result);
    }


    //create a class which extends FragmentStatePagerAdapter
    public class Agadapter extends FragmentStatePagerAdapter {
        Context context = null;

        //constructor
        public Agadapter (Context context, FragmentManager fm){
            super(fm);
            this.context = context;
        }

        /**
         *Return the Fragment associated with a specified position.
         *
         * @param position
         * @return Return the Fragment associated with a specified position.
         */
        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            ArrayList<Habit> eventList;
            Bundle args = new Bundle();
            fragment = new AgendaItem_Fragment();

            if (position == 0){
                eventList = todayList;
            }
            else if (position==1){
                eventList = weekList;
            }
            else{
                eventList = monthList;
            }
            args.putParcelableArrayList("events", eventList);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         *
         * @return number of element in that fragment
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         *
         * @param position
         * @return the name of Fragment associated with a specified position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "today";
            }
            else if(position==1){
                return "week";
            }
            else{
                return "month";
            }
        }
    }
}
