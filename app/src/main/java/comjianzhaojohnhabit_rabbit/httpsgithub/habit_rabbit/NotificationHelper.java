package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

public class NotificationHelper extends ContextWrapper {
    public static String CHANNEL_ID = "comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit.ANDROID";
    public static String CHANNEL_NAME = "Habit Completion";
    private boolean oreo = false;
    private NotificationManager manager;
    private Context context;
    public NotificationHelper(Context base) {
        super(base);
        context = base;

        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT > 25 ){
            NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            androidChannel.enableLights(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.enableVibration(true);
            androidChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(androidChannel);
        }
        getManager();
    }
    public NotificationManager getManager(){
        if (manager==null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public void sendNotification(String message){
        Notification.Builder notification = new Notification.Builder(context)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis()+ 1000)
                .setContentTitle("How did you do today?")
                .setContentText(message)
                .setSmallIcon(R.drawable.icon);
        if (Build.VERSION.SDK_INT > 25 ) {
            Log.d("alarm","setting channel ID");
            notification.setChannelId(CHANNEL_ID);

        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(100,notification.build());
        Log.d("alarm","notified");

    }
}
