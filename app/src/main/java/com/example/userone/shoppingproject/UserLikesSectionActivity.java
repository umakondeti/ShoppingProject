package com.example.userone.shoppingproject;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import adapters.DataAdapter;
import adapters.UserDetailsAdapter;
import projo.ServerUtilites;
import projo.Singleton;


/**
 * Created by user1 on 12-Jan-17.
 */

public class UserLikesSectionActivity extends AppCompatActivity {
    RecyclerView rv_user_likes_section;
    GridLayoutManager gridLayoutManager;
    DataAdapter mAdapter;
    Singleton singleton;
    public static ProgressBar progressBar_UserLikesSection;
    ServerUtilites serverUtilites;
    String loginUserId;
    ImageView backarrow3;
    TextView toobarTitle;
    boolean stop = true;
    TextView emptyLikesTextView;
    int count = 1, responseDataSize;
    public static String liked_products = "Liked Products";

    private static String LikDislkId = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_likes_section_layout);
        rv_user_likes_section = (RecyclerView) findViewById(R.id.rv_user_liked_deals);
        gridLayoutManager = new GridLayoutManager(UserLikesSectionActivity.this, 2);
        progressBar_UserLikesSection=(ProgressBar)findViewById(R.id.progressbar_user_likes) ;
        rv_user_likes_section.setLayoutManager(gridLayoutManager);
        backarrow3 = (ImageView) findViewById(R.id.backarrow);
        emptyLikesTextView=(TextView)findViewById(R.id.tv_likes);
        singleton = Singleton.getInstance();
        serverUtilites = new ServerUtilites(this);
        toobarTitle=(TextView)findViewById(R.id.tab_text);
        toobarTitle.setText("Likes");
        getLikesData();
        Log.d("likes_data_size", " " + singleton.user_likes_data.size());
        backarrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLikesData() {
        progressBar_UserLikesSection.setVisibility(View.VISIBLE);
        Log.d("calling likespage", "gettings likes_data ");

        //  if (singleton.isOnline()) {

        Log.d("calling likespage", "gettings likes_data ");

        //api call for hotestDeals

        Map<String, String> apiHeader = new HashMap<String, String>();
        apiHeader.put("ApiKey ", serverUtilites.apiHeader);
        loginUserId = singleton.getLoginUserId();
        if (loginUserId != null) {
            getLikesDataFromServer(apiHeader, loginUserId, LikDislkId);
        } else {
            Toast.makeText(getApplicationContext(), "your id is null,please login again", Toast.LENGTH_SHORT).show();
        }


    }

    public void getLikesDataFromServer(final Map<String, String> mHeaders, String loginuserid, String LikeDislikeId) {
        String url = serverUtilites.likesSession + loginuserid + "/" + LikeDislikeId;
        Log.d("request url", "--->" + url);

        stop = true;
        count = 1;
        responseDataSize = 0;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            singleton.user_likes_data.clear();
                            JSONArray likes_section_JArray = new JSONArray(response);
                            String StoreName = null, DiscountedPrice = null, RetailPrice = null, PercentDiscount = null, ProductName = null, ImageName = null, CategoryId = null, Description = null, ViewsCount = null, LikesCount = null;
                            String EndDate = null;
                            String SubCategoryId = null, DealType = null, DealId = null;

                            if(likes_section_JArray.length()==0)
                            {
                                emptyLikesTextView.setVisibility(View.VISIBLE);
                            }

                            for (int j = 0; j < likes_section_JArray.length(); j++) {

                                JSONObject jsonobject = likes_section_JArray.getJSONObject(j);

                                //check the condition key exists in jsonResponse
                                if (likes_section_JArray.getJSONObject(j).has("StoreName")) {

                                    StoreName = jsonobject.getString("StoreName");
                                    Log.d("user_StoreName",StoreName);
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DiscountedPrice")) {
                                    DiscountedPrice = jsonobject.getString("DiscountedPrice");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("RetailPrice")) {

                                    RetailPrice = jsonobject.getString("RetailPrice");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("PercentDiscount")) {
                                    PercentDiscount = jsonobject.getString("PercentDiscount");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ProductName")) {

                                    ProductName = jsonobject.getString("ProductName");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ImageName")) {
                                    ImageName = jsonobject.getString("ImageName");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("CategoryId")) {
                                    CategoryId = jsonobject.getString("CategoryId");
                                }

                                if (likes_section_JArray.getJSONObject(j).has("Description")) {

                                    Description = jsonobject.getString("Description");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ViewsCount")) {
                                    if (!jsonobject.getString("ViewsCount").equals("null")) {
                                        ViewsCount = jsonobject.getString("ViewsCount");
                                    } else {
                                        ViewsCount = "0";
                                    }

                                }
                                if (likes_section_JArray.getJSONObject(j).has("LikesCount")) {

                                    if (!jsonobject.getString("LikesCount").equals("null")) {
                                        LikesCount = jsonobject.getString("LikesCount");
                                    } else {
                                        LikesCount = "0";
                                    }

                                }
                                if (likes_section_JArray.getJSONObject(j).has("EndDate")) {

                                    EndDate = jsonobject.getString("EndDate");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("SubCategoryId")) {

                                    SubCategoryId = jsonobject.getString("SubCategoryId");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DealType")) {

                                    DealType = jsonobject.getString("DealType");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DealId")) {

                                    DealId = jsonobject.getString("DealId");
                                }
                                String imageLink = CategoryId + "/" + ImageName;


                                String totalApiData = imageLink + "-~-" + StoreName + "-~-" + DiscountedPrice + "-~-" + RetailPrice +
                                        "-~-" + PercentDiscount + "-~-" + ProductName + "-~-" + Description + "-~-" + ViewsCount + "-~-"
                                        + LikesCount + "-~-" + EndDate + "-~-" + CategoryId + "-~-" + SubCategoryId + "-~-" + DealType + "-~-" + DealId;

                                singleton.user_likes_data.add(totalApiData);
                                Log.d("likes_data_size(anm)", " " + singleton.user_likes_data.size());

                                Log.d("likes_section_data", "--->" + totalApiData);

                            }
                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }
                        progressBar_UserLikesSection.setVisibility(View.GONE);

                        Log.d("likes_data_size(a)", " " + singleton.user_likes_data.size());
                        mAdapter = new DataAdapter(singleton.user_likes_data, rv_user_likes_section, UserLikesSectionActivity.this, "UserLikesSectionActivity");
                        rv_user_likes_section.setAdapter(mAdapter);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar_UserLikesSection.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(UserLikesSectionActivity.this).add(postRequest);


    }
}