package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by notachimo on 2/25/2018.
 */

public class Habit {
    //public enum Period {DAILY, WEEKLY, MONTHLY} was having trouble with enumeration
    public String description;
    public String period;
    public int timesPerPeriod;
    public int timesCompletedInPeriod;
    public int streak;
    private SharedPreferences.Editor editor;

    public Habit(String habit_ID){
        //"name,period,timesPerPeriod,timesCompletedInPeriod,streak"

        SharedPreferences sharedPref = getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        SharedPreferences temp =
        this.description = info[0];
        this.period = info[1];
        this.timesPerPeriod = Integer.parseInt(info[2]);
        this.timesCompletedInPeriod = Integer.parseInt(info[3]);
        this.streak = Integer.parseInt(info[4]);

    }
}
