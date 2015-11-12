package com.com.workspace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.AsyncTask.GetMemberInfoAsyncTask;
import com.Utility.ConnectionDetector;
import com.model.dcard.R;
import com.model.model.Member;
import com.model.model.ScanDetails;
import com.service.BusinessManagerService;
import com.service.ScanService;

public class ScanDetailsActivity extends Activity implements View.OnClickListener {

    private Member member;
    private String member_code;
    private TextView memberName, memberCode;
    private EditText amount;
    private ImageButton back;
    private Button confirm, cancel, delete;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan_details);
        initialize();
    }

    private void initialize() {


        this.member = new Member();
        this.memberName = (TextView) findViewById(R.id.memberName);
        this.memberCode = (TextView) findViewById(R.id.memberCode);
        this.amount = (EditText) findViewById(R.id.editAmmount);
        this.back = (ImageButton) findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.confirm = (Button) findViewById(R.id.confirm);
        this.confirm.setOnClickListener(this);
        this.cancel = (Button) findViewById(R.id.cancel);
        this.cancel.setOnClickListener(this);
        this.delete = (Button) findViewById(R.id.delete);
        this.delete.setOnClickListener(this);

        this.cd = new ConnectionDetector(this);
        Intent intent = getIntent();

        String value = intent.getStringExtra("member_code");
        if (value != null) {
            member_code = intent.getStringExtra("member_code");
            System.out.println(member_code);
            if(cd.isConnectingToInternet())
            new GetMemberInfoAsyncTask(this, member_code).execute();

        } else {
            amount.setKeyListener(null);
            amount.setCursorVisible(false);
            amount.setPressed(false);
            amount.setFocusable(false);

        }


    }


    public void onDataLoad(Member member, boolean result) {

        if (result == false) {
            Toast.makeText(this, "It's not a valid member id", Toast.LENGTH_SHORT).show();

            this.amount.setCursorVisible(false);
            this.amount.setPressed(false);
            this.amount.setFocusable(false);
        } else {
            this.member = member;
            this.memberName.setText(this.member.first_name + " " + this.member.last_name);
            this.memberCode.setText(this.member.member_code);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == back) {
            finish();

        } else if (v == cancel) {
            finish();

        } else if (v == confirm) {

            if(amount.getText().length()>0)
            {
                if(cd.isConnectingToInternet())
                    new AddTransactionTask(this, this.member.id,Double.valueOf(amount.getText().toString())).execute();


            }

        }
        else if(v == delete)
        {

            if(cd.isConnectingToInternet())
                new DeleteLastTransactionTask(this,this.member.id).execute();

        }
    }


    public class DeleteLastTransactionTask extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        int member_id;


        public DeleteLastTransactionTask(Context c, int member_id) {
            this.mycontext = c;
            this.member_id = member_id;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Deleting....");
            // dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            ScanService service = new ScanService();

            result = service.deleteLastTransaction(this.member_id);




            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {

                Toast.makeText(ScanDetailsActivity.this, "Last transaction deleted!!", Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(ScanDetailsActivity.this, "Sorry it can't be done", Toast.LENGTH_LONG).show();

            }

        }

    }


    public class AddTransactionTask extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        int member_id;
        double amount;


        public AddTransactionTask(Context c, int member_id,double amount) {
            this.mycontext = c;
            this.member_id = member_id;
            this.amount = amount;
            this.result = false;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Adding....");
            // dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            ScanService service = new ScanService();
            System.out.println(this.member_id);
            result = service.addTransition(this.member_id,amount);




            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {

                Toast.makeText(ScanDetailsActivity.this, "Added!!", Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(ScanDetailsActivity.this, "Sorry it can't be done", Toast.LENGTH_LONG).show();

            }

        }

    }


}
