package app.cave.medicinalertapp.recicver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.cave.medicinalertapp.R;
import app.cave.medicinalertapp.activity.NotificationActivity;
import app.cave.medicinalertapp.activity.NotificationDetailsActivity;
import app.cave.medicinalertapp.classfile.AlarmRing;
import app.cave.medicinalertapp.classfile.NotificationHelper;

public class AlarmReceiver  extends BroadcastReceiver {

    NotificationHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {

        String medName = intent.getStringExtra("medName");
        String time = intent.getStringExtra("time");
        String mealStatus = intent.getStringExtra("mealStatus");
        String type = intent.getStringExtra("medType");
        String med = intent.getStringExtra("med");




        helper = new NotificationHelper(context);

        int taken = intent.getIntExtra("taken", -5);
        int details = intent.getIntExtra("details", -7);
        int cancel = intent.getIntExtra("cancel", -9);


        int appOk = intent.getIntExtra("appOk", -88);
        int appCancel = intent.getIntExtra("appCancel", -99);

        int main = intent.getIntExtra("main", -100);


        if (main == 100){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(notificationIntent);


        }



        if (taken == 11){

            Toast.makeText(context, "You taken the medicine", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            helper.getManager().cancelAll();

        }


        if (cancel == 22){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "You cancel the medicine", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();
        }

        if (details == 33){
            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            helper.getManager().cancelAll();

            Intent activityIntent = new Intent(context, NotificationDetailsActivity.class);
            intent.putExtra("id", 0);
            context.startActivity(activityIntent);

        }

        if (appOk == 88){

            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();
        }

        if (appCancel == 99){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();

        }



        if ("true".equals(med) ){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            if (context.stopService(stopIntent)){

                context.stopService(stopIntent);
            }

            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Intent startIntent = new Intent(context, AlarmRing.class);
            startIntent.putExtra("ringtone_uri", ""+ringtoneUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                context.startForegroundService(startIntent);

                showNotification(medName.toUpperCase() + " (" + type + ")",
                        "it's time to take " +medName +" at " + time + " (" + mealStatus + ")");


            }else {

                showNotification(medName.toUpperCase() + " (" + type + ")",
                        "it's time to take " +medName +" at " + time + " (" + mealStatus + ")");
                context.startService(startIntent);


            }

        }

    }

    private void showNotification(String title, String message) {

        NotificationCompat.Builder notification = helper.getChannelNotification(title,message);
        helper.getManager().notify(1, notification.build());
    }


}
