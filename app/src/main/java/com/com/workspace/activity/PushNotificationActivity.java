package com.com.workspace.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Utility.Utility;
import com.google.android.gcm.GCMRegistrar;
import com.journeyapps.barcodescanner.Util;
import com.model.dcard.R;


public class PushNotificationActivity extends Activity implements View.OnClickListener {


    PowerManager pm;
    private PowerManager.WakeLock wl;
    private ImageButton back;
    private TextView storeName, spenditure, transactionNumber, totalSpent, currentSpent, pay, save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_push_notification);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNjfdhotDimScreen");


        NotificationManager notifManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        this.back = (ImageButton) findViewById(R.id.back);
        this.back.setOnClickListener(this);

        this.storeName = (TextView) findViewById(R.id.storeTitle);
        this.spenditure = (TextView) findViewById(R.id.spenditurePush);

        this.transactionNumber = (TextView) findViewById(R.id.transactionNumber);
        this.totalSpent = (TextView) findViewById(R.id.totalSpent);

        this.currentSpent = (TextView) findViewById(R.id.currentSpent);
        this.pay = (TextView) findViewById(R.id.pay);
        this.save = (TextView) findViewById(R.id.savePush);
        System.out.println("jony: "+CommonUtilities.gcmData.storeName);
        System.out.println("jony: "+CommonUtilities.gcmData.transactionCount);
        System.out.println("jony: "+CommonUtilities.gcmData.amountOff);
        System.out.println("jony: "+CommonUtilities.gcmData.currentAmount);
        System.out.println("jony: "+CommonUtilities.gcmData.discount);





        //  try {


            storeName.setText(CommonUtilities.gcmData.storeName);
            spenditure.setText("This store offers " + CommonUtilities.gcmData.percentOff + " % on every $" + CommonUtilities.gcmData.onSpent + " spent");
            transactionNumber.setText("Transaction Number: " + CommonUtilities.gcmData.transactionCount);
            totalSpent.setText("Total spent at this store so far: $" + CommonUtilities.gcmData.totalSpent);
            double currentAmount = CommonUtilities.gcmData.currentAmount + CommonUtilities.gcmData.discount;
            currentSpent.setText("This transaction total: $" + currentAmount);
            pay.setText("You will pay: $" + CommonUtilities.gcmData.currentAmount);
            save.setText("You saved: $" + CommonUtilities.gcmData.discount);




//        } catch (Exception ex) {
//
//            //System.out.print(ex.getMessage());
//        }


    }


    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            wl.acquire();

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            // lblMessage.append(newMessage + "\n");

            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            wl.release();
        }
    };

    @Override
    protected void onDestroy() {

        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (v == back) {
            finish();

        }

    }
}
