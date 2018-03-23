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
    public static ArrayList<Habit> recentList=new ArrayList<>();
    public static ArrayList<Habit> weekList=new ArrayList<>();
    public static ArrayList<Habit> monthList=new ArrayList<>();

    public Agenda_Fragment() {
        generatelist();
    }

    public void generatelist(){
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

/*        todayList.add(new Habit(1,"wake up at 6","week",7,"i want to wake up at 6 am every morning",false,null));


        recentList.add(new Habit(1,"complete project for cse442","week",1,"finish project with next week",false,null));
        recentList.add(new Habit(1,"play league of legend with friends","week",3,"play with friends 3 times per week",false,null));


        weekList.add(new Habit(1,"complete project for cse442","week",1,"finish project with next week",false,null));
        weekList.add(new Habit(1,"play league of legend with friends","week",3,"play with friends 3 times per week",false,null));
        weekList.add(new Habit(1,"week 4: idk, doing something","week",4,"no descripsion",false,null));

        monthList.add(new Habit(1,"month 2","month",2,"dance",false,null));
        monthList.add(new Habit(1,"month 2","month",3,"sing",false,null));
        monthList.add(new Habit(1,"month 3","month",4,"swim",false,null));
        monthList.add(new Habit(1,"month 4","month",5,"fly",false,null));
        monthList.add(new Habit(1,"month 5","month",6,"idk",false,null));*/

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

            } else if(position==1){
                eventList = recentList;
            }
            else if (position==2){
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
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "today";
            }else if(position==1){
                return "recent";
            }
            else if(position==2){
                return "week";
            }
            else{
                return "month";
            }
        }
    }
}
