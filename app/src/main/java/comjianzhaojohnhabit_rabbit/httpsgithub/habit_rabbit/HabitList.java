package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;


public class HabitList {
    public static Hashtable<String,Habit> Habit_table;
    public static List<Habit> HABITS_list;
    public static Set<String> ID_set;

    private static String UserName;

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static void initialize(Context mContext) {
        Habit_table = new Hashtable<>();
        HABITS_list = new ArrayList<>();
        ID_set = SharedPref.getHabitList(mContext);
        Set<HabitList.Record> records = SharedPref.getRecords(mContext);

        // initialize habits
        for (String id:ID_set) {
            Habit habit = SharedPref.getHabit(mContext, id);
            Habit_table.put(id, habit);
            HABITS_list.add(habit);
        }

        // initialize streaks
        for (HabitList.Record record:records) {
            if (Habit_table.containsKey(record.habitID+"")) {
                Habit habit = Habit_table.get(record.habitID+"");
                //habit.updateStreaks(record.date, record.count);
            }
        }

        setUserName(SharedPref.getUser(mContext));
    }

    public static void deleteHabit(Habit habit) {
        HABITS_list.remove(habit);
        Habit_table.remove(habit.getHabitID());
        ID_set.remove(habit.getHabitID()+"");
    }

    public static void addHabit(Habit habit) {
        HABITS_list.add(habit);
        Habit_table.put(habit.getHabitID()+"", habit);
        ID_set.add(habit.getHabitID()+"");
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

    public class Record {
        @SerializedName("habit_id")
        public int habitID;
        public Date date;
        public int count;
    }
}