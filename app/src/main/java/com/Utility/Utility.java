package com.Utility;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.model.dcard.R;
import com.model.model.LoggedInMember;
import com.model.model.Member;
import com.model.model.Pagination;
import com.model.model.Store;
import com.model.model.User;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Toast;

public class Utility {

	public static String BASE_URL = "http://27.147.149.178:9070/dCard/public/";
    public static LoggedInMember member = new LoggedInMember();
    public static Store store = new Store();
    public static Pagination pg = new Pagination();
	public static String fbPicLink="";
    public static int total = 0;
    public static int current_number = 0;
    public static int page_number = 1;
	public static ArrayList<Boolean> categoryCheckList = new ArrayList<Boolean>();


	// public static String baseUrl="http://192.168.20.103/bioscope/mobile/";


	public static void setCheckList(int size)
	{
		for(int i = 0; i<size; i++)
		{

			categoryCheckList.add(false);

		}


	}

	public static void showMessage(Context c, String msg) {
		Toast.makeText(c, msg, Toast.LENGTH_LONG).show();

	}

	public static byte[] getBytesFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 70, stream);
		return stream.toByteArray();
	}

	public static String getBase64StringFromBitmap(Bitmap bitmap) {

		String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
				Base64.NO_WRAP);

		return imgString;
	}

	public static Bitmap getBitmapFromBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public static String md5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {
			int i = (b & 0xFF);
			if (i < 0x10)
				hex.append('0');
			hex.append(Integer.toHexString(i));
		}

		return hex.toString();
	}
	
	public static boolean isValidComment(String comment) {
//		String COMMENT_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		return Patterns.WEB_URL.matcher(comment).matches();

	}


    public static String getMapImageUrl(double lat,double lng)
    {
        System.out.println(lat +":"+lng);
        return "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=12&size=400x400&markers=color:blue%7clabel:D%7C"+lat+","+lng+"&maptype=roadmap&key=AIzaSyA57oHexKwRciIlKJ4eYIMWuhHQSO69cjQ";

        //return "";
    }
	

}