package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;


public class HabitCheckReciever extends BroadcastReceiver {
    private static boolean timerOn = false;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;
    private static Context shared_context;
    private static PendingIntent pendingIntent;
    private static NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm", "reciever");
        checkHabits();
    }

    public static boolean isTimerOn() {
        return timerOn;
    }

    public static void initializationCheck(Context context) {
        timerOn = true;
        shared_context = context;
        sharedPref = shared_context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Intent intent = new Intent(shared_context, HabitCheckReciever.class);
        pendingIntent = PendingIntent.getActivity(shared_context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationHelper = new NotificationHelper(context);

        int lastCheckedDay = sharedPref.getInt("DayOfYear", -1);

        Calendar currentCal = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.add(Calendar.DATE,1);
        midnight.set(Calendar.HOUR, 0);
        midnight.set(Calendar.MINUTE, 1);

        if (lastCheckedDay != currentCal.get(Calendar.DAY_OF_YEAR)) {
            checkHabits();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, midnight.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static boolean rabbitIsAlive() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int complete = 0;
        Calendar today = Calendar.getInstance();
        if (HabitList.HABITS_list.size() > 0) {
            for (Habit x : HabitList.HABITS_list) {
                Hashtable<String, Integer> rc = x.getRecords();
                String period = x.getPeriod();
                Calendar startDate = Calendar.getInstance();
                startDate.clear();
                startDate.setTime(x.getStartDate());
                if (period.equals("week") && today.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    Calendar yesterday = Calendar.getInstance();
                    if (startDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                            startDate.get(Calendar.WEEK_OF_YEAR) == yesterday.get(Calendar.WEEK_OF_YEAR))
                        break;
                    yesterday.add(Calendar.DATE, -8);
                    yesterday.set(Calendar.DAY_OF_WEEK, 1);
                    for (int i = 0; i < 7; i++) {
                        String dateString = dateFormat.format(yesterday.getTime());
                        if (rc.contains(dateString)) {
                            complete += rc.get(dateString);
                        }
                        yesterday.add(Calendar.DATE, 1);
                    }
                } else if (period.equals("month") && today.get(Calendar.DAY_OF_MONTH) == 1) {
                    Calendar yesterday = Calendar.getInstance();
                    if (startDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                            startDate.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH))
                        break;
                    yesterday.add(Calendar.MONTH, -1);
                    yesterday.set(Calendar.DAY_OF_MONTH, 1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.setTime(yesterday.getTime());
                    for (calendar.setTime(yesterday.getTime()); calendar.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH); calendar.add(Calendar.DATE, 1)) {
                        String dateString = dateFormat.format(calendar.getTime());
                        if (rc.contains(dateString)) {
                            complete += rc.get(dateString);
                        }
                        yesterday.add(Calendar.DATE, 1);
                    }
                } else {
                    Calendar yesterday = Calendar.getInstance();
                    if (startDate.get(Calendar.DATE) == yesterday.get(Calendar.DATE))
                        break;
                    yesterday.add(Calendar.DATE, -1);
                    String dateString = dateFormat.format(yesterday.getTime());
                    if (rc.contains(dateString)) {
                        complete = rc.get(dateString);
                    }
                }
                if (complete < x.getTimesPerPeriod()) {
                    return false;
                }
            }

            return true;
        }
        return true;
    }




    public static void checkHabits(){
        editor.putInt("DayOfYear",Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        editor.apply();


        //int numberOfFailures = 0;
        /*
        for (Habit habit : HabitList.HABITS_list) {
            String period = habit.getPeriod();
            if (period.equals("day") || period.equals("month") && Calendar.DAY_OF_MONTH == 1 || period.equals("week") && Calendar.DAY_OF_WEEK == 1){


                if (habit.getStreak() == 0){//I'm not sure if this part is right, getting only two failures
                    Log.d("alarm","failure"+habit.getName());
                    numberOfFailures++;
                }

            }
        }
        */
        boolean rabbitAlive = rabbitIsAlive();
        if (!rabbitAlive){
            String notificationMessage = "MURDERER. Because you failed to complete your habits for today, your rabbit is going to die. His blood is on your hands!";
            notificationHelper.sendNotification(notificationMessage);
        }
        editor.putBoolean("RabbitAlive",rabbitAlive);
        editor.apply();
        //TODO send to notification helper

    }
}
