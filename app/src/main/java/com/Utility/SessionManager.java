package com.Utility;

import java.util.HashMap;

import com.google.gson.Gson;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;


	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "yolove";


	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_Access = "access_key";
    public static final String KEY_REGISTRATION = "registration_key";


	// Email address (make variable public to access from outside)
	// public static final String KEY_EMAIL = "email";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();

	}

	
	
	public void createLoginSession(String access_key) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);


		
		editor.putString(KEY_Access, access_key);


		editor.commit();


	}


    public void saveRegisterId(String id)
    {

        editor.putString(KEY_REGISTRATION, id );


        editor.commit();




    }


	public String getAccessToken()
	{
		
		String access_key=pref.getString(KEY_Access, "");
		

	    
	    return access_key;
	}

    public String getKeyRegistration()
    {

        String register_key=pref.getString(KEY_REGISTRATION, "");



        return register_key;
    }




	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public boolean checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {

			return false;
		} else {
			return true;
		}

	}



	/**
	 * Get stored session data
	 * */
	//	public HashMap<String, String> getUserDetails() {
	//		HashMap<String, String> user = new HashMap<String, String>();
	//		// user name
	//		user.put(KEY_ID, pref.getString(KEY_ID, null));
	//
	//		// user email id
	//		// user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
	//
	//		// return user
	//		return user;
	//	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

}
