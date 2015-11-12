package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.ManageEmployeeActivity;
import com.com.workspace.activity.ScanDetailsActivity;
import com.model.model.Member;
import com.model.model.ScanResult;
import com.service.BusinessManagerService;
import com.service.ScanService;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/7/2015.
 */
public class GetMemberInfoAsyncTask extends AsyncTask<String, String, Boolean> {

    ProgressDialog dialog;
    private ScanResult sr;
    private ScanDetailsActivity context;
    private boolean result;
    private String member_code;

    public GetMemberInfoAsyncTask(ScanDetailsActivity context,String member_code)
    {
        this.context = context;
        this.member_code = member_code;

    }

    @Override
    public void onPreExecute() {


        super.onPreExecute();


    }


    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        ScanService service = new ScanService();
        this.sr = service.getMember(member_code);

        if(this.sr.rObj.status == true) {
            result = true;
            System.out.println("True");
        }
        else  if(this.sr.rObj.status == false) {
            result = false;
            System.out.println("false");
        }
        return result;
    }


    @Override
    public void  onPostExecute(Boolean result){

        //dialog.dismiss();

        this.context.onDataLoad(this.sr.member,result);




    }



}
