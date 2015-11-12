package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.com.workspace.activity.ManageEmployeeActivity;
import com.model.model.Member;
import com.model.model.PrefernceModel;
import com.service.BusinessManagerService;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/2/2015.
 */
public class GetEmployeeListAsyncTask extends AsyncTask<String, String, Boolean> {

    ProgressDialog dialog;
    private ArrayList<Member> mEmployeeList;
    private ManageEmployeeActivity context;
    private boolean result;
    private int page;

    public GetEmployeeListAsyncTask(ManageEmployeeActivity context,int page) {


        this.context = context;
        this.page = page;

    }

    @Override
    public void onPreExecute() {


        super.onPreExecute();


    }


    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        BusinessManagerService service = new BusinessManagerService();
        this.mEmployeeList = service.getEmployeeList(page);

        if(this.mEmployeeList.size()>0)
            result = true;
        else
            result = false;

        return result;
    }


    @Override
    public void  onPostExecute(Boolean result){

        //dialog.dismiss();

        this.context.onDataLoad(this.mEmployeeList);




    }


}
