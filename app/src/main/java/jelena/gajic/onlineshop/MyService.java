package jelena.gajic.onlineshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Binder binder = null;
    private Thread thread = null;
    private String usr;

    @Override
    public boolean onUnbind(Intent intent) {
        thread.interrupt();
        binder.setSale(false);
        return super.onUnbind(intent);
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("ServiceTAG", "onCreate");
        thread = new Thread(new Runnable(){
            @Override
            public void run() {
                Log.d("ServiceTAG", "thread running");
                while(true) {
                    try{
                        LocalTime now = LocalTime.now();
                        if (/*now.getHour() == 15 && now.getMinute() == 00*/true) {
                            Thread.sleep(10000);
                            sendUsernameNotification();
                        }
                        Thread.sleep(5000);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }

                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.usr = intent.getStringExtra("username");
        if (binder == null) {
            binder = new Binder();
        }
        if (!thread.isAlive()) {
            Log.d("ServiceTAG", "starting thread");
            thread.start();
        }
        return binder;
    }
    public void sendUsernameNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("1",
                "myChannel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
        mNotificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("User logged in") // title for notification
                .setContentText(this.usr)// message for notification
                .setAutoCancel(true); // clear notification after click
        mNotificationManager.notify(0, mBuilder.build());
    }
}