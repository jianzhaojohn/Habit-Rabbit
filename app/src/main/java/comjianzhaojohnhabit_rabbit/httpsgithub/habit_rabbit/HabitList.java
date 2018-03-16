package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;


public class HabitList {
    public static Hashtable<String,Habit> Habit_table;
    public static List<Habit> HABITS_list;
    public static Set<String> ID_set;

    private static String UserName;

    public String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static void initialize(Context mContext) {
        Habit_table = new Hashtable<>();
        HABITS_list = new ArrayList<>();
        ID_set = SharedPref.getHabitList(mContext);

        for (String id:ID_set) {
            Habit habit = SharedPref.getHabit(mContext, id);
            Habit_table.put(id, habit);
            HABITS_list.add(habit);
        }

        setUserName(SharedPref.getUser(mContext));
    }

    public static void deleteHabit(Habit habit) {
        HABITS_list.remove(habit);
        Habit_table.remove(habit.getId());
        ID_set.remove(habit.getId()+"");
    }

    public static void addHabit(Habit habit) {
        HABITS_list.add(habit);
        Habit_table.put(habit.getId()+"", habit);
        ID_set.add(habit.getId()+"");
    }

    /*public static void initialize(Context context){
        sharedContext = context;
        sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
//        Habit.setSharedContext(sharedContext);
        update();

    }
    public static Hashtable<String,Habit> update(){
        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        Habit_table = new Hashtable<String, Habit>();

        *//*for (String ID:habit_IDs){
            Habit_table.put(ID,new Habit(ID));
            HABITS_list.add(new Habit(ID));
        }*//*
        for (String ID:habit_IDs){
            Habit habit = SharedPref.getHabit(sharedContext, ID);
            Habit_table.put(ID, habit);
            HABITS_list.add(habit);
        }
        return Habit_table;
    }

    public static String addHabit(){
        String newHabitID = Habit.makeNewHabit();
        update();
        return newHabitID;
    }*/
}