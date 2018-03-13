package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Yun on 2018/3/13 0013.
 */

public class SharedPref {
    private static final String FILE_NAME = "HabitInfo";
    private static final String USER = "Username";
    private static final String DEFAULT_USER = "test@example.com";
    private static final String ID = "_id";
    private static final String NAME = "_NameOfHabit";
    private static final String PERIOD = "_Period";
    private static final String TIMES = "_Times";
    private static final String STREAK = "_Streak";

    public static void saveUser(Context mContext, String user) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public static String getUser(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();

        return mPref.getString(USER,DEFAULT_USER);
    }

    public static void saveHabitList(Context mContext, Set<String> habits) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putStringSet("habit_list", habits);
        editor.apply();
    }

    public static Set<String> getHabitList(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();
        return mPref.getStringSet("habit_list", null);
    }

    public static void saveHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(habit.getID()+ID, habit.getID());
        editor.putString(habit.getID()+NAME, habit.getNameOfHabit());
        editor.putString(habit.getID()+PERIOD, habit.getPeriod());
        editor.putInt(habit.getID()+TIMES, habit.getTimesPerPeriod());
        editor.putInt(habit.getID()+STREAK, habit.getTimesCompletedInPeriod());
        editor.apply();
    }

    public static Habit getHabit(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();
        String habit_id = mPref.getString(id+ID, id);
        String habit_name = mPref.getString(id+NAME, "NA");
        String period = mPref.getString(id+PERIOD, "NA");
        Integer times = mPref.getInt(id+TIMES, -1);
//        mPref.getInt(id+STREAK, -1);

        return new Habit(habit_id, habit_name, period, times);
    }

    public static Integer getStreak(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();
//        mPref.getInt(id+STREAK, -1);

        return mPref.getInt(id+STREAK, -1);
    }

    public static void editHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        mPref.edit().putString(habit.getID()+NAME, habit.getNameOfHabit());
        mPref.edit().putString(habit.getID()+PERIOD, habit.getPeriod());
        mPref.edit().putInt(habit.getID()+TIMES, habit.getTimesPerPeriod());
        editor.apply();
    }

    public static void deleteHabit(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(id+ID);
        editor.remove(id+NAME);
        editor.remove(id+PERIOD);
        editor.remove(id+TIMES);
    }
}
