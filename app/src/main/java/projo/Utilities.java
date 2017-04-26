package projo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userone.shoppingproject.DisplayItemsActivity;
import com.example.userone.shoppingproject.MainActivity;
import com.example.userone.shoppingproject.R;
import com.example.userone.shoppingproject.SingleItemActivity;
import com.example.userone.shoppingproject.SubCategoryActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user1 on 05-Dec-16.
 */

public class Utilities {
    public static String strUserName, last_name, first_name, profile_image, name, jsonResponse, email, link, url, gender, id = null, fb_profilepic_base64;
    public static boolean ishout;
    public static String[] categorynames;
    private static Utilities instance = null;
    private MainActivity.SomeFBListener mListener;

    private ProgressDialog progress;
    Context context;
    Singleton singleton;
    View fb_View;
    Button btn_login, btn_signup;
    RelativeLayout rll_popup;
    LinearLayout popup_layout;
    MainActivity main;
    ServerUtilites serverutilities;
    LinearLayout llv_footer;
    RelativeLayout rll_signin_fb;
    SessionManager session;
    HashMap<String, String> user;

    public Utilities(Context context) {
        this.context = context;
        singleton = Singleton.getInstance();
        serverutilities = new ServerUtilites(context);
        progress = new ProgressDialog(context);
        session = new SessionManager(context);
        main = new MainActivity();

    }

    public static synchronized Utilities getInstance(Context context) {
        if (null == instance)
            instance = new Utilities(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized Utilities getInstance() {
        if (null == instance) {
            throw new IllegalStateException(Utilities.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    //alert Dialog
    public void ShowAlert(String alertMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setMessage(alertMessage);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void showProgressDialog() {
        progress.setMessage("loading.......");
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        progress.setCancelable(true);
    }

    public void cancelProgressDialog() {

        // if ((context instanceof MainActivity) || (context instanceof DisplayItemsActivity) || (context instanceof SubCategoryActivity) || (context instanceof SingleItemActivity))
        progress.dismiss();
    }

    public void showPopup(final RelativeLayout rll_popup, LinearLayout popup_layout, LinearLayout llv_footer, RecyclerView popup_menu) {
        if (rll_popup.getVisibility() == View.GONE) {
            // animation for splash screen fad in and fad out
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(150);
                            main.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                    rll_popup.setVisibility(View.VISIBLE);
                                }
                            });


                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                ;
            };
            timer.start();
            if (context instanceof MainActivity) {
                if (!session.isLoggedIn()) {

                    popup_layout.setVisibility(View.VISIBLE);
                    popup_menu.setVisibility(View.GONE);


                } else {

                    popup_layout.setVisibility(View.GONE);
                    popup_menu.setVisibility(View.VISIBLE);


                }
            } else {
                if (!session.isLoggedIn()) {

                    popup_layout.setVisibility(View.VISIBLE);


                } else {

                    popup_layout.setVisibility(View.GONE);

                }
            }
            llv_footer.setVisibility(View.VISIBLE);

            Animation hide = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.sliding_from_botttom);
            llv_footer.startAnimation(hide);

        } else {
            Toast.makeText(context.getApplicationContext(), "already visible", Toast.LENGTH_SHORT).show();

        }


    }

    public void hidePopup(LinearLayout llv_footer, final RelativeLayout rll_popup) {
/*
        Toast.makeText(getApplicationContext(), "touchable", Toast.LENGTH_SHORT).show();
*/

        if (llv_footer.getVisibility() == View.VISIBLE) {
            Animation hide = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.sliding_to_bottom);
            llv_footer.startAnimation(hide);
            llv_footer.setVisibility(View.GONE);


            // animation for splash screen fad in and fad out
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            main.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                    rll_popup.setVisibility(View.GONE);
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                ;
            };
            timer.start();

        } else {
            Toast.makeText(context.getApplicationContext(), "alreay", Toast.LENGTH_SHORT).show();

        }


    }

    public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        Log.d("...............calling", "search");
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if ((view.getId() == R.id.btn_login) || (view.getId() == R.id.btn_signup) || (view.getId() == R.id.rll_signin_fb)) {
                view.setEnabled(true);

            } else {
                Log.d("view", view.toString());
                view.setEnabled(enabled);
                if (view instanceof ViewGroup) {
                    enableDisableViewGroup((ViewGroup) view, enabled);
                }
            }
        }
    }


    // fb graph request
    public void RequestData(final Context con, final MainActivity.SomeFBListener<String> listener) {
        this.mListener = listener;
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("utilities_respone_fb", " " + response);

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        Utilities.gender = json.getString("gender");
                        Log.d("gender", Utilities.gender);

                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        Log.d("text", text);
                        Utilities.id = json.getString("id");
                        Utilities.first_name = json.getString("first_name");
                        Utilities.last_name = json.getString("last_name");
                        Utilities.name = json.getString("name");
                        Log.d("strUserName", Utilities.first_name);
                        //tv_username.setText(Utilities.first_name);
                        Utilities.link = json.getString("link");
                        Utilities.gender = json.getString("gender");
                        Log.d("gender", Utilities.gender);
                        Log.d("link", Utilities.link);
                        JSONObject pic = json.getJSONObject("picture");
                        JSONObject data = pic.getJSONObject("data");
                        Utilities.ishout = data.getBoolean("is_silhouette");
                        Log.d("ishout", " " + Utilities.ishout);
                        Utilities.url = data.getString("url");
                        Utilities.email = json.getString("email");
                        if (Utilities.url != null) {
                            /*if (!TextUtils.isEmpty(Utilities.url.trim())) {
                                new FbNumberImage().
                                        execute();
                            }*/
                        }
                        Log.d("email", Utilities.email);
                        if (Utilities.email.equals(null) || (Utilities.email.length() == 0)) {
                            // here we called logout function because we dont want  users who doesn't have an email id
                            if (AccessToken.getCurrentAccessToken() != null) {

                                LoginManager.getInstance().logOut();
                                Toast.makeText(context.getApplicationContext(), "Email is mandatory,You should Login through email ", Toast.LENGTH_SHORT).show();

                            }


                        }
                        Log.d("url", Utilities.url);
                        Log.d("response", json.toString());

                    }

                } catch (JSONException e) {

                    if (Utilities.email == null) {
                        // here we called logout function because we dont want the users who doesn't have an email id
                        if (AccessToken.getCurrentAccessToken() != null) {

                            LoginManager.getInstance().logOut();
                            listener.getFBResult("FB error");
/*
                            cancelProgressDialog();
*/

                        }


                        Toast.makeText(context.getApplicationContext(), "Email is mandatory " + " " + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                    e.printStackTrace();
                }
                if (Utilities.email != null) {
                    // tv_username.setText(Utilities.first_name);
               /*     main.llv_footer = (LinearLayout) fb_View.findViewById(R.id.popup);
                    rll_popup = (RelativeLayout) fb_View.findViewById(R.id.rll_popup);
                    rll_popup.setVisibility(View.GONE);
                    llv_footer.setVisibility(View.GONE);*/

                    Toast.makeText(context.getApplicationContext(), "LoggedIn Successfully", Toast.LENGTH_SHORT).show();
                    sendFBDataToServer(context);


                } else {
                    if (AccessToken.getCurrentAccessToken() != null) {

                        LoginManager.getInstance().logOut();

                    }
                    //cancelProgressDialog();

                    Toast.makeText(context.getApplicationContext(), "Email is mandatory,You should Login through email ", Toast.LENGTH_SHORT).show();
                    mListener.getFBErrorResult("FB error");
                }

               /* Log.d("u_profile", " " + u_profile);


                if ((u_profile != null) && (Utilities.email.equals(null))) {
                    Utilities.first_name = u_profile.getFirstName();
                    Log.d("first_name", Utilities.first_name);

                    Utilities.last_name = u_profile.getLastName();
                    Log.d("lastname", Utilities.first_name);

                    Utilities.name = u_profile.getName();
                    Log.d("name", Utilities.name);

                    Utilities.profile_image = u_profile.getProfilePictureUri(100, 150).toString();
                    Log.d("profile_image", Utilities.profile_image);
                    Utilities.first_name = Utilities.first_name + " " + Utilities.last_name;
                }

                if (Utilities.first_name != null) {
                    tv_username.setText(Utilities.first_name);
                    rll_popup.setVisibility(View.GONE);
                    llv_footer.setVisibility(View.GONE);

                } else {

                    tv_username.setText(Utilities.first_name);


                }*/

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,email,gender,picture");
        request.setParameters(parameters);
        request.executeAsync();
        //tv_username.setText(Utilities.first_name);
        Log.d("first_name", " " + Utilities.first_name);


    }

    public void sendFBDataToServer(Context con) {
        String setFbdata = "http://www.dealsweb.com/deals/ws/FBLogin";

        try {
            Log.d("Utilities.email", Utilities.email);

            setFBProfileDataToServer(setFbdata, con);
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFBProfileDataToServer(String url, Context cont) throws AuthFailureError, JSONException {
        Log.d("profile request url", "--->" + url);
        this.context = cont;
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("Id", Utilities.id);
        jsonBody.put("First_name", Utilities.first_name);
        jsonBody.put("Last_name", Utilities.last_name);
        jsonBody.put("Email", Utilities.email);
        jsonBody.put("Name", Utilities.name);
        jsonBody.put("Gender", Utilities.gender);
        jsonBody.put("Image", "");
        final String mRequestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" profileData response", "--->" + response);

                        String DisplayName = null, LoginId = null, LoginEmail = null, Active = null, Status = null, ImagePath = null;
                        try {
                            JSONArray jArray = new JSONArray(response);
                            Log.d("join response12", "--->" + jArray.length());
                            JSONObject jsonobject = jArray.getJSONObject(0);

                            if (jArray.length() == 0) {
                                serverutilities.ShowAlert("Please enter valid E-mail!");
                            } else {
                                if (jArray.getJSONObject(0).has("DisplayName")) {

                                    DisplayName = jsonobject.getString("DisplayName");
                                    Log.d("DisplayName", "--->" + DisplayName);


                                }
                                if (jArray.getJSONObject(0).has("LoginId")) {

                                    LoginId = jsonobject.getString("LoginId");
                                    Log.d("LoginId", "--->" + LoginId);

                                }
                                if (jArray.getJSONObject(0).has("LoginEmail")) {

                                    LoginEmail = jsonobject.getString("LoginEmail");
                                    Log.d("LoginEmail", "--->" + LoginEmail);

                                }
                                if (jArray.getJSONObject(0).has("Active")) {

                                    Active = jsonobject.getString("Active");
                                    Log.d("Active", "--->" + Active);

                                }
                                if (jArray.getJSONObject(0).has("Status")) {

                                    Status = jsonobject.getString("Status");
                                    Log.d("Status", "--->" + Status);

                                }
                                if (jArray.getJSONObject(0).has("ImagePath")) {

                                    ImagePath = jsonobject.getString("ImagePath");

                                }
                                singleton.setLoginUserDisplayName(DisplayName);
                                singleton.setLoginUserEmailId(LoginEmail);
                                singleton.setLoginUserId(LoginId);
                                singleton.setLoginStatus(Status);
                                singleton.setLoginActiveStatus(Active);
                                singleton.setLoginImage("http://www.dealsweb.com" + ImagePath);
                                String fb_image_url="http://www.dealsweb.com" + ImagePath;

                                session.createLoginSession(LoginId, DisplayName, LoginEmail, fb_image_url);

                                mListener.getFBResult(response);
                               /* session.createLoginSession(LoginId, DisplayName, LoginEmail, fb_image_url);
                                Toast.makeText(context.getApplicationContext(), "LoggedIn Successfully", Toast.LENGTH_SHORT).show();
                                main.displayNameTextView.setText("Hi");
                                main.displayNameTextView1.setText(DisplayName);
                                Picasso.with(context.getApplicationContext()).load(fb_image_url).into(main.navigationProfileImageView);*/

                                // main.checkForUserLogin();
                                Log.d("22354y5666", "--->" + "you have your account with dealsweb");
                                Log.d("join response", "--->" + Utilities.first_name);
                                /*Intent i = new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(i);
                                finish();*/
                            }

                        } catch (Exception e) {
                            mListener.getFBErrorResult("FB error");

                            Log.d("Splash screen catch", "--->" + e);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mListener.getFBErrorResult("account error");
                Toast.makeText(context.getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("MainActivity)error", "--->" + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("body", "--->" + mRequestBody);
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

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
Log.d("response","--->"+response);
                    responseString = String.valueOf(response.statusCode);
Log.d("response string","--->"+responseString);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }*/
        };


        Volley.newRequestQueue(cont.getApplicationContext()).add(postRequest);


    }
    public   boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
    public void checkForUserLogin(Context cont) {
        SessionManager   session = new SessionManager(context.getApplicationContext());
        MainActivity main = new MainActivity();

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Singleton singleton = Singleton.getInstance();
            String loginStatus = user.get(SessionManager.KEY_EMAIL);
            singleton.setLoginUserDisplayName(user.get(SessionManager.KEY_NAME));
            singleton.setLoginUserEmailId(user.get(SessionManager.KEY_EMAIL));
            singleton.setLoginUserId(user.get(SessionManager.KEY_ID));
            singleton.setLoginImage(user.get(SessionManager.KEY_IMAGE));
            MainActivity.displayNameTextView1.setText("HI");
            Log.d("callinguserlogornot", user.get(SessionManager.KEY_EMAIL));
            Log.d("callinguserlogornot", user.get(SessionManager.KEY_NAME));
            Log.d("callinguserlogornot", user.get(SessionManager.KEY_EMAIL));
            Log.d("callinguserlogornot", user.get(SessionManager.KEY_IMAGE));
            Log.d("callinguserlogornot", user.get(SessionManager.KEY_ID));

            MainActivity.displayNameTextView.setText(user.get(SessionManager.KEY_NAME));
            MainActivity.displayNameTextView.setVisibility(View.VISIBLE);

            Picasso.with(cont).load(singleton.getLoginImage()).into(MainActivity.navigationProfileImageView);
            MainActivity.profile.setImageResource(R.drawable.profile_icon_login);
        } else {
            MainActivity.displayNameTextView.setVisibility(View.GONE);
            Log.d("callinguserlogornot", "else");

            MainActivity.displayNameTextView1.setText("Welcome");
            MainActivity.navigationProfileImageView.setImageResource(R.drawable.profe_icn);
            MainActivity.profile.setImageResource(R.drawable.profileone);
        }
    }
    public void DisplayToast(String toast) {

        Toast.makeText(context.getApplicationContext(), toast, Toast.LENGTH_LONG).show();
    }


}
