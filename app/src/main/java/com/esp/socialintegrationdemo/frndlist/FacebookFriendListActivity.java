package com.esp.socialintegrationdemo.frndlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.esp.socialintegrationdemo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by admin on 22/4/16.
 */
public class FacebookFriendListActivity extends Activity {

    private Button btngetList;
    private ListView lvfblist;
    private ArrayList<userBean> friends_list;
    private CallbackManager callbackManager;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_facebookfriendlist);

        friends_list = new ArrayList<userBean>();

        btngetList = (Button) findViewById(R.id.btngetList);
        lvfblist = (ListView) findViewById(R.id.lvfblist);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        System.out.println("onSuccess");
                        System.out.println("==========AccessToken.getCurrentAccessToken()==================" + AccessToken.getCurrentAccessToken());
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/taggable_friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        try {
                                            System.out.println("===========response=============" + response);
                                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                            String res = response.toString().replace(rawName.toString(), "@@@");
                                            System.out.println("===========>>> res  <<========" + res);
                                            if (rawName != null && rawName.length() > 0) {

                                                System.out.println("========data.length()===========" + rawName.length());
                                                for (int i = 0; i < rawName.length(); i++) {
                                                    JSONObject jsonObject1 = rawName.getJSONObject(i);
                                                    JSONObject picObj = jsonObject1.getJSONObject("picture");
                                                    JSONObject dataObj = picObj.getJSONObject("data");

                                                    friends_list.add(new userBean(dataObj.getString("url").toString(), jsonObject1.getString("name")));
                                                }
                                                JSONObject pagingObj = response.getJSONObject().getJSONObject("paging");

                                                if (pagingObj.has("next")) {

                                                    JSONObject cursorsJson = pagingObj.getJSONObject("cursors");
                                                    setPaging(AccessToken.getCurrentAccessToken(), cursorsJson.getString("after").toString());
                                                } else {

                                                    Collections.sort(friends_list, new Comparator<userBean>() {
                                                        @Override
                                                        public int compare(userBean lhs, userBean rhs) {
                                                            return lhs.getName().compareTo(rhs.getName().toString());
                                                        }
                                                    });

                                                    userAdapter = new UserAdapter(FacebookFriendListActivity.this, R.layout.row_user, friends_list);
                                                    lvfblist.setAdapter(userAdapter);
                                                }

                                            }

                                        } catch (Exception e) {
                                            System.out.println("=====Error========" + e.toString());
                                        }
                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

        btngetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions((Activity) FacebookFriendListActivity.this, Arrays.asList("email", "public_profile"));
            }
        });
    }

    void setPaging(AccessToken accessToken, String after) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/taggable_friends",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        try {
                            System.out.println("===========response=============" + response);
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            String res = response.toString().replace(rawName.toString(), "@@@");
                            System.out.println("===========>>> res  <<========" + res);

                            if (rawName != null && rawName.length() > 0) {

                                System.out.println("========data.length()===========" + rawName.length());
                                for (int i = 0; i < rawName.length(); i++) {
                                    JSONObject jsonObject1 = rawName.getJSONObject(i);
                                    JSONObject picObj = jsonObject1.getJSONObject("picture");
                                    JSONObject dataObj = picObj.getJSONObject("data");

                                    friends_list.add(new userBean(dataObj.getString("url").toString(), jsonObject1.getString("name")));
                                }
                                JSONObject pagingObj = response.getJSONObject().getJSONObject("paging");

                                if (pagingObj.has("next")) {

                                    JSONObject cursorsJson = pagingObj.getJSONObject("cursors");
                                    setPaging(AccessToken.getCurrentAccessToken(), cursorsJson.getString("after").toString());
                                } else {

                                    Collections.sort(friends_list, new Comparator<userBean>() {
                                        @Override
                                        public int compare(userBean lhs, userBean rhs) {
                                            return lhs.getName().compareTo(rhs.getName().toString());
                                        }
                                    });

                                    userAdapter = new UserAdapter(FacebookFriendListActivity.this, R.layout.row_user, friends_list);
                                    lvfblist.setAdapter(userAdapter);
                                }

                            }


                        } catch (Exception e) {

                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("pretty", "0");
        parameters.putString("limit", "100");
        parameters.putString("after", after);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
