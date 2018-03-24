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
  //  public static ArrayList<Habit> recentList=new ArrayList<>();
    public static ArrayList<Habit> weekList=new ArrayList<>();
    public static ArrayList<Habit> monthList=new ArrayList<>();

    public Agenda_Fragment() {
        generatelist();
    }

    public void generatelist(){
        todayList=new ArrayList<>();
    //    recentList=new ArrayList<>();
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

    public class Agadapter extends FragmentStatePagerAdapter {

        Context context = null;
        public Agadapter (Context context, FragmentManager fm){
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            ArrayList<Habit> eventList;
            Bundle args = new Bundle();
            fragment = new AgendaItem_Fragment();

            if (position == 0){
                eventList = todayList;

            }
            //else if(position==1){
              //  eventList = recentList;
            //}
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

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "today";
            }
            //else if(position==1){
             //   return "recent";
            //}
            else if(position==1){
                return "week";
            }
            else{
                return "month";
            }
        }
    }
}
