package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by notachimo on 2/25/2018.
 */
//TODO: Make a local var in database holding whether or not the database has been changed, have changes in Habit changed the variable
public class Habit {
    private String ID;
    private String nameOfHabit;
    private String period;
    private int timesPerPeriod;
    private int timesCompletedInPeriod;
    private SharedPreferences.Editor editor;
    private static Context sharedContext;
    public Habit(String habit_ID){
        //"name,period,timesPerPeriod,timesCompletedInPeriod,streak"
        SharedPreferences sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
        if (habit_ID == "new habit"){
            generateUniqueID();
            Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
            habit_IDs.add(this.ID);
            editor.putStringSet("habit_IDs", habit_IDs);
            editor.commit();
        }
        else {
            this.ID = habit_ID;
        }
        this.nameOfHabit = sharedPref.getString(this.ID + "_NameOfHabit", "");
        this.period = sharedPref.getString(this.ID + "_Period", "");
        this.timesPerPeriod = sharedPref.getInt(this.ID + "_TimesToDoPerPeriod", -1);
        this.timesCompletedInPeriod = sharedPref.getInt(this.ID + "_TimesCompletedSoFar", -1);

    }

    private void generateUniqueID() {
       this.ID = UUID.randomUUID().toString();
    }
    public String getNameOfHabit() {
        return nameOfHabit;
    }
    public void setNameOfHabit(String nameOfHabit) {
        editor.putString(ID+"_NameOfHabit",nameOfHabit);
        editor.commit();
        this.nameOfHabit = nameOfHabit;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        editor.putString(ID+"_Period",period);
        editor.commit();
        this.period = period;
    }

    public int getTimesPerPeriod() {
        return timesPerPeriod;
    }

    public void setTimesPerPeriod(int timesPerPeriod) {
        editor.putInt(ID+"_TimesToDoPerPeriod",timesPerPeriod);
        editor.commit();
        this.timesPerPeriod = timesPerPeriod;
    }

    public int getTimesCompletedInPeriod() {
        return timesCompletedInPeriod;
    }

    public void setTimesCompletedInPeriod(int timesCompletedInPeriod) {
        editor.putInt(ID+"_TimesToDoPerPeriod",timesCompletedInPeriod);
        editor.commit();
        this.timesCompletedInPeriod = timesCompletedInPeriod;
    }

    public void delete(){
        //TODO: make a method that deletes all necessary info about the habit
    }
    public static void setSharedContext(Context inputContext){
        sharedContext = inputContext;
    }
}
