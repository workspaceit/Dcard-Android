package com.com.workspace.activity;

import android.content.Context;
import android.content.Intent;

import com.model.model.GCMData;

/**
 * Created by Fayme Shahriar on 9/9/2015.
 */
public class CommonUtilities {

    // give your server registration url here
    //static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php";

    // Google project id
    public static final String SENDER_ID = "48375300074";
    public static GCMData gcmData = new GCMData();

    /**
     * Tag used on log messages.
     */
    static final String TAG = "D-Card";

    static final String DISPLAY_MESSAGE_ACTION = "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {



        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


}
