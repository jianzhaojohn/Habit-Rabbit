package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by notachimo on 3/7/2018.
 */

public class HabitList {
    public static Hashtable<String,Habit> Habit_table;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPref;
    private static Context sharedContext;

    public static List<Habit> HABITS_list = new ArrayList<Habit>();


    public static void initialize(Context context){
        sharedContext = context;
        sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
//        Habit.setSharedContext(sharedContext);
        update();

    }
    public static Hashtable<String,Habit> update(){
        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        Habit_table = new Hashtable<String, Habit>();

        /*for (String ID:habit_IDs){
            Habit_table.put(ID,new Habit(ID));
            HABITS_list.add(new Habit(ID));
        }*/
        for (String ID:habit_IDs){
            Habit habit = SharedPref.getHabit(sharedContext, ID);
            Habit_table.put(ID, habit);
            HABITS_list.add(habit);
        }
        return Habit_table;
    }

    /*public static String addHabit(){
        String newHabitID = Habit.makeNewHabit();
        update();
        return newHabitID;
    }*/
}