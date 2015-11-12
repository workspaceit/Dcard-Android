package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.com.workspace.activity.LoginActivity;
import com.com.workspace.activity.PreferencesActivity;
import com.model.model.PrefernceModel;
import com.model.model.User;
import com.service.LoginService;
import com.service.PreferencesService;

/**
 * Created by Fayme Shahriar on 8/31/2015.
 */
public class GetPreferencesAsyncTask extends AsyncTask<String, String, Boolean> {

    ProgressDialog dialog;
    PrefernceModel pf;
    User user;
    PreferencesActivity context;

    public GetPreferencesAsyncTask(PreferencesActivity context)
    {
        this.context = context;


    }

    @Override
    public void onPreExecute(){


        super.onPreExecute();



    }

    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        PreferencesService pService = new PreferencesService();
        this.pf=pService.getPreferences();




        return this.pf.state;
    }


    @Override
    public void  onPostExecute(Boolean result){

        //dialog.dismiss();

        this.context.onDataLoad(this.pf);




    }


}
