package app.cave.medicinalertapp.classfile;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import app.cave.medicinalertapp.R;
import app.cave.medicinalertapp.activity.MainActivity;
import app.cave.medicinalertapp.activity.DetailsMedichineActivity;
import app.cave.medicinalertapp.recicver.AlarmReceiver;

public class NotificationHelper extends ContextWrapper {

    public static final String channelId = "channel ID";
    public static final String channelName = "channel Name";



    private NotificationManager manager;


    public NotificationHelper(Context base) {


        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {

        NotificationChannel channel1 = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);



    }

    public NotificationManager getManager(){

        if (manager == null){

            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }

        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message){

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("main", 100);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        Intent takenIntent = new Intent(this, AlarmReceiver.class);
        takenIntent.setAction("taken");
        takenIntent.putExtra("taken", 11);

        Intent cancelIntent = new Intent(this, AlarmReceiver.class);
        cancelIntent.setAction("cancel");
        cancelIntent.putExtra("cancel", 22);


        Intent detailsIntent = new Intent(this, DetailsMedichineActivity.class);
        detailsIntent.setAction("details");
        detailsIntent.putExtra("details", 33);
//        startActivity(detailsIntent);


        PendingIntent takenPendingIntent =
                PendingIntent.getBroadcast(this, 0, takenIntent, 0);

        PendingIntent cancelPendingIntent =
                PendingIntent.getBroadcast(this, 0, cancelIntent, 0);

        PendingIntent detailsPendingIntent =
                PendingIntent.getBroadcast(this, 0, detailsIntent, 0);




        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityIntent)
                .setSmallIcon(R.drawable.ic_medichine)
               .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(R.drawable.ic_check,
                        "Taken",
                        takenPendingIntent)
                .addAction(R.drawable.ic_details,
                        "Details",
                        detailsPendingIntent)
                .addAction(R.drawable.ic_cancel,
                        "Skip",
                        cancelPendingIntent)
                .setAutoCancel(true)
                .setOngoing(false);
    }





}
