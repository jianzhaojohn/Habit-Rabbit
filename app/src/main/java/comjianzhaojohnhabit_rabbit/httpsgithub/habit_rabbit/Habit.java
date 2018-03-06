package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;


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
        this.ID = habit_ID;
        this.nameOfHabit = sharedPref.getString(habit_ID+"_NameOfHabit","");
        this.period = sharedPref.getString(habit_ID+"_Period","");
        this.timesPerPeriod = sharedPref.getInt(habit_ID+"_TimesToDoPerPeriod",-1);
        this.timesCompletedInPeriod = sharedPref.getInt(habit_ID+"_TimesCompletedSoFar",-1);
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
