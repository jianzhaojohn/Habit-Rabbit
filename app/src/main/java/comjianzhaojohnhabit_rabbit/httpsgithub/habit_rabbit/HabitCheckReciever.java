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

import java.util.Calendar;

import static comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit.MainActivity.isRabbitAlive;


public class HabitCheckReciever extends BroadcastReceiver {
    private static boolean timerOn = false;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;
    private static Context shared_context;
    private static PendingIntent pendingIntent;
    private static NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm","reciever");
        checkHabits();
    }
    public static boolean isTimerOn(){
        return timerOn;
    }
    public static void initializationCheck(Context context){
        timerOn = true;
        Calendar currentCal = Calendar.getInstance();
        shared_context = context;
        sharedPref = shared_context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Intent intent = new Intent(shared_context,HabitCheckReciever.class);
        pendingIntent = PendingIntent.getActivity(shared_context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        notificationHelper = new NotificationHelper(context);

        int lastCheckedDay = sharedPref.getInt("DayOfYear",-1);

        Calendar rightNow = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR,0);
        midnight.set(Calendar.MINUTE,1);
        checkHabits();

        if (lastCheckedDay != currentCal.get(Calendar.DAY_OF_YEAR)){
            checkHabits();
            midnight.set(Calendar.DAY_OF_YEAR, Calendar.DAY_OF_YEAR + 2);

        }
        else{
            midnight.set(Calendar.DAY_OF_YEAR, Calendar.DAY_OF_YEAR + 1);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,midnight.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
    public static void checkHabits(){
        editor.putInt("DayOfYear",Calendar.DAY_OF_YEAR);
        editor.apply();
        int numberOfFailures = 0;
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

        String notificationMessage;
        boolean rabbitIsAlive = isRabbitAlive();
        if (rabbitIsAlive){
            notificationMessage = "Congrats! You completed all your habits for today! Your rabbits escape the slaughter! ヽ(•‿•)ノ";
        } else{
            notificationMessage = "MURDERER. Because you failed to complete your habits for today, your rabbit is going to die. His blood is on your hands!";
        }
        editor.putBoolean("RabbitAlive",rabbitIsAlive);
        editor.apply();
        //TODO send to notification helper
        notificationHelper.sendNotification(notificationMessage);
        /*
        Notification.Builder notification = new Notification.Builder(shared_context)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis()+ 1000)
                .setContentTitle("How did you do today?")
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.icon);


        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) shared_context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(100,notification.build());
        Log.d("alarm","notified");
        */

    }
}
