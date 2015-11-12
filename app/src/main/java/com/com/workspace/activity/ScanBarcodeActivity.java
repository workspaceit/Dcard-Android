package com.com.workspace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.model.dcard.R;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class ScanBarcodeActivity extends CaptureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scan_layout);
        initialize();
    }

    private void initialize() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan your barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.initiateScan();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result

            String re = scanResult.getContents();
            //System.out.println("this is the code: " + re);
           // Log.d("code", re);

            if(re!=null) {
                Intent intent1 = new Intent(ScanBarcodeActivity.this, ScanDetailsActivity.class);
                intent1.putExtra("member_code", re);
                startActivity(intent1);
                finish();
            }
            else
                finish();

        }

    }


}
