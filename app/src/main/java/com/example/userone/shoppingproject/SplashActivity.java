package com.example.userone.shoppingproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import database.DatabaseHandler;
import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;

import static android.R.id.message;

/**
 * Created by userone on 12/5/2016.
 */

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    ServerUtilites serverUtilites;
    Singleton singleton;
    Boolean subApiStatus = false, categoryApiStatus = false;
    Utilities Utilities;
    DatabaseHandler db;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Utilities = new Utilities(this);
        db = new DatabaseHandler(this);
        serverUtilites = new ServerUtilites(SplashActivity.this);
        singleton = Singleton.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //api calls for categories
                boolean networkStatus = isNetworkAvailable();
                if (networkStatus) {

                    singleton.setOnline(networkStatus);
                    Map<String, String> categoryHeader = new HashMap<String, String>();
                    categoryHeader.put("ApiKey ", serverUtilites.apiHeader);

                    String categoryapi = serverUtilites.serverUrl + "" + serverUtilites.category;
                    getDealsResponse(categoryHeader, categoryapi);


//latest Deals

/*
                    String latestString = serverUtilites.serverUrl + "" + serverUtilites.latestDeals;
                    getLatestDeals(categoryHeader, latestString);*/


                    String subCategoryApi = serverUtilites.serverUrl + "" + serverUtilites.subCategory;
                    getDealsResponse1(categoryHeader, subCategoryApi);


                } else {
                    singleton.setOnline(networkStatus);
                   /* Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();*/
                    Utilities.ShowAlert("No Internet Connection");
                }


            }
        }, SPLASH_TIME_OUT);

    }


    public void getDealsResponse(final Map<String, String> mHeaders,
                                 String url) {

        singleton.categoriesData.clear();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String sectionId = null, sectionName = null,ImageUrl=null;


                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("CategoryId")) {

                                    sectionId = jsonobject.getString("CategoryId");
                                } else {

                                }
                                if (jArray.getJSONObject(i).has("CategoryName")) {
                                    sectionName = jsonobject.getString("CategoryName");
                                }
                                if (jArray.getJSONObject(i).has("ImageUrl")) {
                                    ImageUrl = jsonobject.getString("ImageUrl");
                                }


                                singleton.categoriesData.add(sectionId + "-~-" + sectionName+"-~-"+ImageUrl);
                                Log.d("apicategories", "--->" + sectionName);


                            }

                            Log.d("splashscreen", "--->" + singleton.categoriesData.size());
                            categoryApiStatus = true;
                            if(subApiStatus)
                            {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            }

                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message =serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                Log.d("category error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SplashActivity.this).add(postRequest);

    }

    public void getDealsResponse1(final Map<String, String> mHeaders, String url) {



      /*  private Void makeJsonObjectRequest1(final Map<String, String> params1,
        String string) {

        String url = ServerUtilites.serverUrl + "" + string;*/
      /*  String url = ServerUtilites.serverUrl + "" + string;*/
       /* final Map<String, String> mHeaders = new HashMap<String, String>();
        mHeaders.put("ApiKey ", "3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF");*/
        Log.d("request url", "--->" + url);
        singleton.subCategoriesData.clear();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        subApiStatus=true;
                        Log.d("response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String sectionId = null, sectionName = null,SubCategoryId=null;


                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("CategoryId")) {

                                    sectionId = jsonobject.getString("CategoryId");
                                }
                                if (jArray.getJSONObject(i).has("SubCategoryId")) {

                                    SubCategoryId = jsonobject.getString("SubCategoryId");
                                }
                                if (jArray.getJSONObject(i).has("SubCategoryName")) {
                                    sectionName = jsonobject.getString("SubCategoryName");
                                }


                                singleton.subCategoriesData.add(sectionId + "-~-" + sectionName+"-~-"+SubCategoryId);

                            }

                            if(categoryApiStatus)
                            {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message =serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {

                Log.d("param", "--->" + mHeaders);
                return mHeaders;
            }
        };

        Volley.newRequestQueue(SplashActivity.this).add(postRequest);


    }


    //check network connections
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
