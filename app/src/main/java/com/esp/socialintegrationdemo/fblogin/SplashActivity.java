package com.esp.socialintegrationdemo.fblogin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esp.socialintegrationdemo.R;
import com.esp.socialintegrationdemo.frndlist.FacebookFriendListActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SplashActivity extends Activity {

    private Button btnFB, btnFrndList;
    private ImageView imgProfile;
    private TextView txtUserDetail;

    /*for handle clikc of FB button*/
    private boolean clickSearch = false;

    // For Facebook
    private CallbackManager callbackManager;
    // private LoginWithSocialAPI loginwithsocialAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_splash);

        btnFB = (Button) findViewById(R.id.btnFB);
        btnFrndList = (Button) findViewById(R.id.btnFrndList);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtUserDetail = (TextView) findViewById(R.id.txtUserDetail);


        btnFrndList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, FacebookFriendListActivity.class);
                startActivity(intent);
            }
        });

        getKeyHash(SplashActivity.this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult result) {
                        System.out.println("============onSuccess==========");
                        System.out.println("============result.getAccessToken()==========" + result.getAccessToken().getToken());
                        GraphRequest request = GraphRequest.newMeRequest(
                                result.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        try {
                                            String[] name = object.getString(
                                                    "name").split(" ");
                                            UserBean userbean = new UserBean();
                                            userbean.fname = name[0].toString();
                                            userbean.lname = name[1].toString();
                                            userbean.email = object
                                                    .getString("email");
                                            userbean.socialId = object
                                                    .getString("id").toString();
                                            userbean.socialType = 1;
                                            userbean.gender = object.getString("gender");
//                                            userbean.profilePic = Profile.getCurrentProfile().getProfilePictureUri(400, 400).toString();
                                            userbean.profilePic = "https://graph.facebook.com/" + userbean.socialId + "/picture?type=large";
                                            /*set ProfilePicture & UserDetail*/
                                            imgProfile.setVisibility(View.VISIBLE);
                                            Picasso.with(SplashActivity.this).load(userbean.profilePic).into(imgProfile);

                                            txtUserDetail.setVisibility(View.VISIBLE);
                                            txtUserDetail.setText(Html.fromHtml("Name : " + userbean.fname + userbean.lname + "<br />" + "Gender : " + userbean.gender + "<br />" +
                                                    "Email : " + userbean.email + "<br />" + "SocialID : " + userbean.socialId));

                                            // loginwithsocialAPI = new
                                            // LoginWithSocialAPI(
                                            // context, responseListener,
                                            // userbean);
                                            // loginwithsocialAPI.execute();

                                            System.out.println("=========JSONObject======" + object);
                                            System.out.println("=========response======" + response.getJSONObject());
                                            System.out.println("=========userbean.fname=======" + userbean.fname);
                                            System.out.println("============userbean.lname==========" + userbean.lname);
                                            System.out.println("=========userbean.email=======" + userbean.email);
                                            System.out.println("============userbean.socialId==========" + userbean.socialId);
                                            System.out.println("=========userbean.gender======" + userbean.gender);
                                            System.out.println("=========userbean.profilePic ======" + userbean.profilePic);

                                            btnFB.setText("LogOut");
//                                            facebookLogout();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickSearch) {
                    facebookLogout();
                    clickSearch = false;
                } else {
                    if (Utils.isOnline(SplashActivity.this)) {
                        System.out.println("============LoginManager.logInWithReadPermissions==========");
                        LoginManager.getInstance().logInWithReadPermissions(
                                SplashActivity.this,
                                Arrays.asList("email", "public_profile"));
                    } else {
                        Toast.makeText(SplashActivity.this, "Connection Error!",
                                Toast.LENGTH_LONG).show();
                    }
                    clickSearch = true;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("===========onActivityResult================");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void getKeyHash(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("com.esp.socialintegrationdemo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println("--------------KeyHash:--------------" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void facebookLogout() {
        System.out.println("===========facebookLogout()================");
        LoginManager.getInstance().logOut();
        btnFB.setText("Login with Facebook");
        imgProfile.setVisibility(View.GONE);
        txtUserDetail.setVisibility(View.GONE);
    }
}
