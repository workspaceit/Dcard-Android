package com.com.workspace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;


import com.AsyncTask.LoginAsyncTask;
import com.Utility.SessionManager;
import com.Utility.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.model.dcard.R;
import com.model.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends Activity {

    private LoginButton fbLoginBtn;
    private CallbackManager callbackManager;
    private SessionManager mSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_layout);


//		try {
//		    PackageInfo info = getPackageManager().getPackageInfo(
//		            "com.workspace.dcard", PackageManager.GET_SIGNATURES);
//		    for (Signature signature: info.signatures)  {
//		        MessageDigest md = MessageDigest.getInstance("SHA");
//		        md.update(signature.toByteArray());
//		        System.out.println("FACEBOOK APP SIGNATURE " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
//		    }
//		} catch (Exception e) {
//		    // TODO: handle exception
//		    e.printStackTrace();
//		}

        mSession = new SessionManager(this);
        fbLoginBtn = (LoginButton) findViewById(R.id.login_button);
        fbLoginBtn.setReadPermissions(Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                //final AccessToken accessToken = loginResult.getAccessToken();


                GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                //System.out.println("Check: " + response.toString());
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    String birthday = "";
//                                    if(object.getString("birthday")!=null) {
//                                        birthday = object.getString("birthday");
//                                    }
//                                    else
//                                    {
//                                        birthday = "";
//                                    }
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");

                                    System.out.println(id + ", " + name + ", " + email + ", " + gender + ", " + birthday);
                                    User user = new User();
                                    user.email = email;
                                    user.fbId = id;
                                    user.first_name = firstName;
                                    user.last_name = lastName;

                                    new LoginAsyncTask(LoginActivity.this,user).execute();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void onDataLoad() {
        LoginManager.getInstance().logOut();
        mSession.createLoginSession(Utility.member.access_token);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }


}
