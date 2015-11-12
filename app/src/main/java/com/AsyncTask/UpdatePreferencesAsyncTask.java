package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.LoginActivity;
import com.com.workspace.activity.PreferencesActivity;
import com.model.model.User;
import com.service.LoginService;
import com.service.PreferencesService;

/**
 * Created by Fayme Shahriar on 9/1/2015.
 */
public class UpdatePreferencesAsyncTask extends AsyncTask<String, String, Boolean> {


    ProgressDialog dialog;
    boolean result;
    PreferencesActivity context;
    boolean email_preference;
    String search_preference;
    String zipcode;

    public UpdatePreferencesAsyncTask(PreferencesActivity context, boolean email_preference, String search_preference, String zipcode)
    {

        this.context = context;
        this.email_preference = email_preference;
        this.search_preference = search_preference;
        this.zipcode =zipcode;


    }

    @Override
    public void onPreExecute(){
        //Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();



        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("updating...");
        //dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();



    }
    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        PreferencesService service = new PreferencesService();
        result=service.updatePreferences(email_preference,search_preference,zipcode);


        return result;
    }
    @Override
    public void  onPostExecute(Boolean result){

        dialog.dismiss();

        if(result==false)
        {
            Toast.makeText(context,"Update cannot be done!!",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(context,"Saved successfully!!",Toast.LENGTH_SHORT).show();

        }

    }




}
