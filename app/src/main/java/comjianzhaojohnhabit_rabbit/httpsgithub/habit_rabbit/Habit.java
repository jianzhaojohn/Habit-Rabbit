package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Habit {

    private String ID;
    private String nameOfHabit;
    private String period;
    private int timesPerPeriod;
    private transient int timesCompletedInPeriod;

    // temp constructor
    public Habit(String id, String name, String period, int times){
        this.ID = id;
        this.nameOfHabit = name;
        this.period = period;
        this.timesPerPeriod = times;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setNameOfHabit(String nameOfHabit) {
        this.nameOfHabit = nameOfHabit;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setTimesPerPeriod(int timesPerPeriod) {
        this.timesPerPeriod = timesPerPeriod;
    }

    public void setTimesCompletedInPeriod(int timesCompletedInPeriod) {
        this.timesCompletedInPeriod = timesCompletedInPeriod;
    }

    public String getNameOfHabit() {
        return nameOfHabit;
    }

    public String getPeriod() {
        return period;
    }

    public int getTimesPerPeriod() {
        return timesPerPeriod;
    }

    public int getTimesCompletedInPeriod() {
        return timesCompletedInPeriod;
    }


    /*
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPref;
    private static Context sharedContext;
    public Habit(String habit_ID){
        //"name,period,timesPerPeriod,timesCompletedInPeriod,streak"
        this.ID = habit_ID;
        this.nameOfHabit = sharedPref.getString(this.ID + "_NameOfHabit", "error");
        this.period = sharedPref.getString(this.ID + "_Period", "error");
        this.timesPerPeriod = sharedPref.getInt(this.ID + "_TimesToDoPerPeriod", -1);
        this.timesCompletedInPeriod = sharedPref.getInt(this.ID + "_TimesCompletedSoFar", -1);

    }


    public static String makeNewHabit() {
        String newID = UUID.randomUUID().toString();
        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        habit_IDs.add(newID);
        editor.putStringSet("habit_IDs", habit_IDs);
        editor.commit();
        HabitList.update();
        return newID;
    }
    public String getNameOfHabit() {
        return nameOfHabit;
    }
    public void setNameOfHabit(String nameOfHabit) {
        editor.putString(ID+"_NameOfHabit",nameOfHabit);
        editor.commit();
        this.nameOfHabit = nameOfHabit;
        HabitList.update();
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        editor.putString(ID+"_Period",period);
        editor.commit();
        this.period = period;
        HabitList.update();
    }

    public int getTimesPerPeriod() {
        return timesPerPeriod;
    }
    public void setTimesPerPeriod(int timesPerPeriod) {
        editor.putInt(ID+"_TimesToDoPerPeriod",timesPerPeriod);
        editor.commit();
        this.timesPerPeriod = timesPerPeriod;
        HabitList.update();
    }

    public int getTimesCompletedInPeriod() {
        return timesCompletedInPeriod;
    }
    public void setTimesCompletedInPeriod(int timesCompletedInPeriod) {
        editor.putInt(ID+"_TimesToDoPerPeriod",timesCompletedInPeriod);
        editor.commit();
        this.timesCompletedInPeriod = timesCompletedInPeriod;
        HabitList.update();
    }

    public void delete(){
        editor.remove(ID + "_NameOfHabit");
        editor.remove(ID + "_Period");
        editor.remove(ID + "_TimesToDoPerPeriod");
        editor.remove(ID + "_TimesCompletedSoFar");

        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        habit_IDs.remove(ID);
        editor.putStringSet("habit_IDs", habit_IDs);

        editor.commit();
        HabitList.update();

    }

    public static void setSharedContext(Context inputContext){
        sharedContext = inputContext;
        sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
*/
    public String makeString(){
        return ID + ',' + nameOfHabit + ',' + this.period;
    }

}