package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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

    public static void saveHabit(Context mContext, JSONObject jHabit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        try {
            // write new habit
            editor.putString("habit_"+jHabit.getInt("habit_id"), jHabit.toString());
            editor.apply();

            // update habit_list
            Set<String> list = getHabitList(mContext);
            list.add(jHabit.getInt("habit_id")+"");
            saveHabitList(mContext, list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void saveHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        String jHabit = gson.toJson(habit);
        editor.putString("habit_"+habit.getHabitID(), jHabit);

        // update habit_list
        Set<String> list = getHabitList(mContext);
        list.add(habit.getHabitID()+"");
        saveHabitList(mContext, list);

        editor.apply();
    }

    public static void saveHabits(Context mContext, JSONArray habits) {
        for(int i = 0; i < habits.length(); i++) {
            try {
                saveHabit(mContext, habits.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static Habit getHabit(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String jHabit = mPref.getString("habit_"+id, null);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        Habit habit = gson.fromJson(jHabit, Habit.class);

        return habit;
    }

    public static String getHabitString(Context mContext, String id) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String jHabit = mPref.getString("habit_"+id, null);

        return mPref.getString("habit_"+id, null);
    }

    public static Set<HabitList.Record> getRecords(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Set<String> jRecords = mPref.getStringSet("records", null);

        Set<HabitList.Record> list = new HashSet<>();
        for (String jRecord:jRecords) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                        .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create();
                HabitList.Record record = gson.fromJson(jRecord, HabitList.Record.class);
                list.add(record);
        }
        return list;
    }

    public static String getRecordsString(Context mContext) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Set<String> jRecords = mPref.getStringSet("records", null);
        String res="";
        for (String s:jRecords) {
            res +=s;
        }
        return res;
    }

    public static void saveRecords(Context mContext, JSONArray jsonArray) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        Set<String> list = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putStringSet("records", list);

        editor.apply();
    }

    public static void editHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        String jHabit = gson.toJson(habit);
        editor.putString("habit_"+habit.getHabitID(), jHabit);

        editor.apply();
    }

    public static void deleteHabit(Context mContext, Habit habit) {
        SharedPreferences mPref = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        String habit_id = habit.getHabitID()+"";
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

    public static class DateDeserializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

        @Override
        public JsonElement serialize(Date arg0, Type arg1, JsonSerializationContext arg2) {
            return arg0 == null? null : new JsonPrimitive(arg0.getTime());
        }

        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

        @Override
        public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
            return new JsonPrimitive(Boolean.TRUE.equals(arg0));
        }

        @Override
        public Boolean deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            return arg0.getAsInt() == 1;
        }
    }

}
