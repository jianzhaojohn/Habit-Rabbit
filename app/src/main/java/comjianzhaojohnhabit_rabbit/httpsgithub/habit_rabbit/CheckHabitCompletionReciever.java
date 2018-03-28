
package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.Calendar;
import java.util.Set;

public class CheckHabitCompletionReciever extends BroadcastReceiver {
    public static Calendar lastCheckedCal;
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean rabbitDead = false;
        Log.d("alarm","Broadcast Reciever");
        //FILENAME = HabitInfo
        lastCheckedCal = Calendar.getInstance();
        Set<String> habits = SharedPref.getHabitList(context);
        for (String habitID:habits){
            Habit habit = SharedPref.getHabit(context,habitID);
            char period = habit.getPeriod().charAt(0);
            if (period == 'd' || (period == 'm' && lastCheckedCal.get(Calendar.DAY_OF_MONTH) == 1) || (period == 'w' && lastCheckedCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)){
                //if it's daily or Monday for weekly or first day of month for monthly
                Log.d("alarm",habit.getPeriod()+habit.getTimesPerPeriod()+"  "+habit.getStreak());
                if (habit.getTimesPerPeriod() > habit.getStreak()){
                    rabbitDead = true;
                }
                habit.setStreak(0);
            }
        }

        SharedPreferences sharedPrefences = context.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefences.edit();
        editor.putBoolean("RabbitAlive",!rabbitDead);
        editor.commit();
        Log.d("alarm",rabbitDead+"");
    }
}
