package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit.Habit;

/**
 * Created by notachimo on 3/7/2018.
 */

public class HabitList {
    public static Hashtable<String,Habit> habitlist;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPref;
    private static Context sharedContext;

    public static List<Habit> HABITS = new ArrayList<Habit>();


    public static void initialize(Context context){
        sharedContext = context;
        sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Habit.setSharedContext(sharedContext);
        update();

    }
    public static Hashtable<String,Habit> update(){
        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        habitlist = new Hashtable<String, Habit>();

        for (String ID:habit_IDs){
            habitlist.put(ID,new Habit(ID));
        }
        return habitlist;
    }
    public static String addHabit(){
        String newHabitID = Habit.makeNewHabit();
        update();
        return newHabitID;
    }
}