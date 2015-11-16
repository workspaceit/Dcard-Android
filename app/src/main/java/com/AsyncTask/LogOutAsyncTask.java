package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;


import com.com.workspace.activity.HomeActivity;
import com.com.workspace.activity.LoginActivity;
import com.model.model.User;
import com.service.LogOutService;

/**
 * Created by rajib on 11/16/15.
 */
public class LogOutAsyncTask extends AsyncTask<String,String,Boolean> {

    ProgressDialog dialog;
    boolean result;
    HomeActivity context;

    public LogOutAsyncTask(HomeActivity context)
    {
        this.context=context;
    }

    @Override
    public void onPreExecute(){
        //Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Logout...");
        //dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();


    }

    @Override
    protected Boolean doInBackground(String... params) {

        LogOutService logOutService = new LogOutService();
        result = logOutService.logOutUser();
        return result;
    }

    @Override
    public void onPostExecute(Boolean result)
    {
        dialog.dismiss();
        if(result==true)
            this.context.logOutUser();
        else
            Toast.makeText(context, "LogOut failed", Toast.LENGTH_LONG).show();
    }


}
