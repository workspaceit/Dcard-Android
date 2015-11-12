package com.com.workspace.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Utility.Utility;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.model.dcard.R;

import org.w3c.dom.Text;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class CreateBarCode extends Activity implements View.OnClickListener{
    private String barcode_data;
    private ImageView iv;
    private TextView tv;
    private TextView userName;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_barcode_layout);
        initialize();


    }

    private void initialize() {

        // barcode data
        barcode_data = Utility.member.member_code;
        iv = (ImageView)findViewById(R.id.generatedBarcode);
        tv = (TextView) findViewById(R.id.memberIdBar);
        userName = (TextView) findViewById(R.id.userName);
        userName.setText(Utility.member.first_name + " "+Utility.member.last_name);
        this.back = (ImageButton)findViewById(R.id.back);
        this.back.setOnClickListener(this);


        // barcode image
        Bitmap bitmap = null;
        //iv = new ImageView(this);

        try {

            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }




        String text ="MEMBER ID: "+barcode_data;
        tv.setText(text);






    }


    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;

    }


    @Override
    public void onClick(View v) {

        if(v==back)
        {
            this.finish();

        }

    }
}
