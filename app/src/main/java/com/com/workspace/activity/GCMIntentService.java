package com.com.workspace.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.AsyncTask.RegisterGCMAsyncTask;
import com.google.android.gcm.GCMBaseIntentService;
import com.model.dcard.R;
import com.model.model.GCMData;

/**
 * Created by Fayme Shahriar on 9/9/2015.
 */
public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";
    private GCMData gData;
    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
        Log.d("NAME","name");
        //ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
        new RegisterGCMAsyncTask(context,registrationId).execute();
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
        //ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        gData = new GCMData();


        gData.transactionCount = Integer.valueOf(intent.getExtras().getString("transactionCount"));
        gData.storeName = intent.getExtras().getString("storeName");
        gData.currentAmount = Double.valueOf(intent.getExtras().getString("currentAmount"));
        gData.totalSpent = Double.valueOf(intent.getExtras().getString("totalSpent"));
        gData.amountOff = Double.valueOf(intent.getExtras().getString("amountOff"));
        gData.discount = Double.valueOf(intent.getExtras().getString("discount"));
        gData.percentOff = Double.valueOf(intent.getExtras().getString("percentOff"));
        gData.onSpent = Double.valueOf(intent.getExtras().getString("onSpent"));

        CommonUtilities.gcmData = gData;

        System.out.println("jony: "+gData.storeName);
        System.out.println("jony: "+intent.getExtras().getString("transactionCount"));
        System.out.println("jony: "+gData.amountOff);

        String message = "Data found";

        CommonUtilities.displayMessage(context, message);

        generateOnPushNotification(context);
        // notifies user
        generateNotification(context);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    private void generateOnPushNotification(Context context)
    {

        Intent i = new Intent(context,PushNotificationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);


    }


    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, "D-Card Notification", when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, PushNotificationActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, "D-Card Notification", intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }

}
