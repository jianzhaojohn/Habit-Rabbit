package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;


// convert the data from data base into a habit List
//a class which repsonse for whats above
public class HabitList {
    public static Hashtable<String,Habit> Habit_table;
    public static List<Habit> HABITS_list;
    public static Set<String> ID_set;
    private static String UserName;

    //getter
    public static String getUserName() {
        return UserName;
    }
    //setter
    public static void setUserName(String userName) {
        UserName = userName;
    }
    //initialize the data
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

    //deleted habit
    public static void deleteHabit(Habit habit) {
        HABITS_list.remove(habit);
        Habit_table.remove(habit.getHabitID());
        ID_set.remove(habit.getHabitID()+"");
    }

    //add habit
    public static void addHabit(Habit habit) {
        HABITS_list.add(habit);
        Habit_table.put(habit.getHabitID()+"", habit);
        ID_set.add(habit.getHabitID()+"");
    }


    // create a Record class
    public class Record {
        @SerializedName("habit_id")
        public int habitID;
        public Date date;
        public int count;
    }
}