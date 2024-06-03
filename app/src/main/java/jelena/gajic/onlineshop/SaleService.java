package jelena.gajic.onlineshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.time.LocalTime;

public class SaleService extends Service {

    private Binder binder = null;
    private Thread thread = null;

    public SaleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new Binder();
        }

        if (!thread.isAlive()) {
            Log.d("ServiceTAG", "starting thread");
            thread.start();
        }

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        thread.interrupt();
        binder.setSale(false);
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ServiceTAG", "onCreate");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ServiceTAG", "thread running");
                while (true) {
                    try {
                        LocalTime now = LocalTime.now();
                        if (/*now.getHour() == 15 && now.getMinute() == 00*/true) {
                            Thread.sleep(50);
                            sendNotification();
                            binder.setSale(true);
                            Thread.sleep(10000);
                            binder.setSale(false);
                            Intent finishIntent = new Intent("com.example.FINISH_ACTIVITY");
                            sendBroadcast(finishIntent);
                            sendEndNotification();
                            Thread.sleep(30000);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                }
            }
        });
    }

    public void sendNotification() {
        Log.d("ServiceTAG", "sending Notification....");
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("1",
                "myChannel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
        mNotificationManager.createNotificationChannel(channel);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("isSale", binder.getSale());
        intent.putExtra("username", binder.getUsername());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("SALE!") // title for notification
                .setContentText("The sale has just started!")// message for notification
                .setAutoCancel(true) // clear notification after click
                .setContentIntent(pendingIntent);


        mNotificationManager.notify(0, mBuilder.build());
    }

    public void sendEndNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("1",
                "myChannel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
        mNotificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("SALE HAS ENDED!") // title for notification
                .setContentText("Come back tomorrow for more...")// message for notification
                .setAutoCancel(true); // clear notification after click
        mNotificationManager.notify(0, mBuilder.build());
    }
}