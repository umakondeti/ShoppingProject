package com.example.userone.shoppingproject;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;


/**
 * Created by userone on 12/8/2016.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextView showDescription;
    Singleton singleton;
    ServerUtilites serverUtilites;
    Button changePasswordButton;
    ImageView backArrowImageView;
    Utilities utilities;
    boolean showOrHideCurrentPasswordStatus=false,showOrHideNewPasswordStatus=false,showOrHideConfirmPasswordStatus=false;
    ImageView showOrHideCurrentPasswordImageView,showOrHideNewPasswordImageView,showOrHideConfirmPasswordImageView;
    TextView currentpasswordTextview,newpasswordTextview,confirmpasswordTextview;
    String currentPassword, newPassword, confirmPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow_change_password);
        changePasswordButton = (Button) findViewById(R.id.btn_save);
        showDescription = (TextView) findViewById(R.id.show_description);
        singleton = Singleton.getInstance();
        serverUtilites = new ServerUtilites(this);
        utilities=new Utilities(this);
        showOrHideCurrentPasswordImageView=(ImageView)findViewById(R.id.current_password_eye_icon);
        showOrHideNewPasswordImageView=(ImageView)findViewById(R.id.new_password_eye_icon);
        showOrHideConfirmPasswordImageView=(ImageView)findViewById(R.id.confirm_password_eye_icon);
        //showDescription.setText(singleton.getDescriptionData());
        currentpasswordTextview = (TextView) findViewById(R.id.current_password_editext);
        newpasswordTextview = (TextView) findViewById(R.id.new_password_editext);
        confirmpasswordTextview = (TextView) findViewById(R.id.confirm_pass_editext);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleton.isOnline()) {
                    currentPassword = currentpasswordTextview.getText().toString().trim();
                    newPassword=newpasswordTextview.getText().toString().trim();
                    confirmPassword=confirmpasswordTextview.getText().toString().trim();
                    if(currentPassword.length()!=0)
                    {
                        if(newPassword.length()!=0)
                        {
                            if(confirmPassword.length()!=0)
                            {
                                if(newPassword.equals(confirmPassword))
                                {
                                    if((newPassword.length()>=6)&&(newPassword.length()>=6)) {
                                        String reviewsString = serverUtilites.serverUrl + serverUtilites.changePassword;
                                        Map<String, String> apiHeader = new HashMap<String, String>();
                                        apiHeader.put("ApiKey ", serverUtilites.apiHeader);
                                        getChangePasswordData(reviewsString);
                                    }else
                                    {
                                        utilities.ShowAlert("Password length should not be lessthan 6 characters");
                                    }
                                }else
                                {
                                    utilities.ShowAlert("New Password and Confirm Password should be equal");
                                }

                            }else
                            {
                                utilities.ShowAlert("Confirm Password should not be empty");

                            }
                        }else
                        {
                            utilities.ShowAlert("New Password should not be empty");

                        }
                    }else
                    {
                        utilities.ShowAlert("Current Password should not be empty");

                    }


                }
            }
        });
        showOrHideCurrentPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showOrHideCurrentPasswordStatus) {
                    showOrHideCurrentPasswordImageView.setImageResource(R.drawable.eye_icon);
                    // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    currentpasswordTextview.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showOrHideCurrentPasswordStatus=false;
                } else {
                    showOrHideCurrentPasswordImageView.setImageResource(R.drawable.hide_pasd_icon);
                    //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    currentpasswordTextview.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    showOrHideCurrentPasswordStatus=true;

                }
            }
        });
        showOrHideNewPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showOrHideNewPasswordStatus) {
                    showOrHideNewPasswordImageView.setImageResource(R.drawable.eye_icon);
                    // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newpasswordTextview.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showOrHideNewPasswordStatus=false;
                } else {
                    showOrHideNewPasswordImageView.setImageResource(R.drawable.hide_pasd_icon);
                    //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newpasswordTextview.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    showOrHideNewPasswordStatus=true;

                }
            }
        });

        showOrHideConfirmPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showOrHideConfirmPasswordStatus) {
                    showOrHideConfirmPasswordImageView.setImageResource(R.drawable.eye_icon);
                    // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmpasswordTextview.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showOrHideConfirmPasswordStatus=false;
                } else {
                    showOrHideConfirmPasswordImageView.setImageResource(R.drawable.hide_pasd_icon);
                    //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmpasswordTextview.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    showOrHideConfirmPasswordStatus=true;

                }
            }
        });

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
    }


    public void getChangePasswordData(String url) {

        Log.d("profile url", "--->" + url);
        String loginUserId = singleton.getLoginUserId();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("LgnId", loginUserId);
            jsonBody.put("CurrentPwd", currentPassword);
            jsonBody.put("NewPwd", newPassword);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ChangePassword response", "--->" + response);
                        String CurrentPassword = null, NewPassword = null, ConfirmPassword = null;
                        if(response.trim().equals("\"Y\""))
                        {
                            Toast.makeText(ChangePasswordActivity.this," Your Password updated Successfully!",Toast.LENGTH_SHORT).show();
                        }else if(response.trim().equals("\"N\""))
                        {
                            utilities.ShowAlert("Current Password is wrong,please enter correct password");
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("category error", "--->" + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                return mRequestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();


                params1.put("ApiKey ", "3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF");
                        /*params1.put("Content-Type ","application/json");
                params1.put("Accept","application/json");*/

                return params1;
            }


        };


        Volley.newRequestQueue(getApplicationContext()).add(postRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
