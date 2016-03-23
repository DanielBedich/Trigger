package com.example.danielbedich.trigger;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Adam on 3/23/2016.
 */
public class TriggerExecution extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Received", Toast.LENGTH_LONG).show();
        sendSms("3307745286", "hello");
    }

    /**
     protected void makeCall(View v, String contactNum) {
     //Call the specified number
     Intent call = new Intent(Intent.ACTION_CALL);
     call.setData(Uri.parse("tel:" + contactNum));
     if (PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE)) {
     startActivity(call);
     }
     }
     */
    protected void sendSms(String contactNum, String message){
        SmsManager smsMan = SmsManager.getDefault();
        smsMan.sendTextMessage(contactNum, null, message, null, null);
    }
/**
 protected void createNotif(View v, String message){
 //Creates a notification with the text entered into the message field
 NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(v.getContext());
 notifBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
 notifBuilder.setContentTitle("Trigger");
 notifBuilder.setTicker("New Trigger!");
 notifBuilder.setContentText(message);
 long[] vibrateTime = {2000};
 notifBuilder.setVibrate(vibrateTime);
 Intent resultIntent = new Intent(TriggerExecution.this, TriggerActivity.class);
 PendingIntent resultPendingIntent = PendingIntent.getActivity(TriggerExecution.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 notifBuilder.setContentIntent(resultPendingIntent);
 mNotificationManager.notify(1, notifBuilder.build());
 }
 */
}
