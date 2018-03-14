package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
/**
 * Created by Yun on 2018/3/13 0013.
 */

public class SharedPref {
    public static final String FILE_NAME = "HabitInfo";
    private static final String USER = "Username";
    private static final String DEFAULT_USER = "test@example.com";


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

    public static void saveHabitList(Context mContext, Set<String> ids) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putStringSet("habit_list", ids);
        editor.apply();
    }

    public static void saveHabitList(Context mContext, JSONArray jArray) {
        Set<String> list = new HashSet<>();
        for (int i = 0; i < jArray.length(); i++) {
            try {
                list.add(jArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        saveHabitList(mContext, list);
    }

        public static Set<String> getHabitList(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();
        return mPref.getStringSet("habit_list", null);
    }

    public static void saveHabit(Context mContext, JSONObject habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        try {
            // write new habit
            editor.putString("habit_"+habit.getInt("id"), habit.toString());
            editor.apply();

            // update habit_list
            Set<String> list = getHabitList(mContext);
            list.add(habit.getInt("id")+"");
            saveHabitList(mContext, list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void saveHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gson = new Gson();
        String jHabit = gson.toJson(habit);
        editor.putString("habit_"+habit.getId(), jHabit);

        // update habit_list
        Set<String> list = getHabitList(mContext);
        list.add(habit.getId()+"");
        saveHabitList(mContext, list);

        editor.apply();
    }

    public static void saveHabits(Context mContext, JSONArray habits) {
        for(int i = 0; i < habits.length(); i++) {
            try {
                saveHabit(mContext, (JSONObject) habits.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static Habit getHabit(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String jHabit = mPref.getString("habit_"+id, null);
        Gson gson = new Gson();
        Habit habit = gson.fromJson(jHabit, Habit.class);

        return habit;
    }

    public static String getHabitString(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String jHabit = mPref.getString("habit_"+id, null);

        return mPref.getString("habit_"+id, null);
    }

    public static Integer getStreak(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPref.edit();
//        mPref.getInt(id+STREAK, -1);

        return mPref.getInt(id+"_streak", -1);
    }

    public static void saveStreak(Context mContext, Habit habit) {
        // TODO: update streak
    }

    public static void editHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        Gson gson = new Gson();
        String jHabit = gson.toJson(habit);
        editor.putString("habit_"+habit.getId(), jHabit);

        editor.apply();
    }

    public static void deleteHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        String habit_id = habit.getId()+"";
        editor.remove("habit_"+habit_id);
        editor.apply();

        // update habit_list
        Set<String> list = getHabitList(mContext);
        list.remove(habit_id);
        saveHabitList(mContext, list);

    }

    public static void clearAll(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }
}
