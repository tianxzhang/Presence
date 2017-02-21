package com.lucious.presence.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lucious.presence.BaseActivity;
import com.lucious.presence.BaseApplication;
import com.lucious.presence.R;
import com.lucious.presence.config.Config;
import com.lucious.presence.util.LogUtils;
import com.lucious.presence.util.NetworkUtils.NetworkUtils;
import com.lucious.presence.util.SharedPreferencesUtil;
import com.lucious.presence.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ztx on 2017/1/26.
 */

public class WelcomeActivity extends BaseActivity {
    private static final String homeApi = Config.BASE_URL + "/u/api/home";
    private static final int SPLASH_DISPLAY_LENGTH = 2000;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();
        initEvents();
        startTime=System.currentTimeMillis();
        //loginCheck();
//        delayStartActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        startActivity(MainActivity.class);
        finish();
    }

    private void loginCheck() {
        AsyncHttpClient client = NetworkUtils.getAsyncHttpClientInstance();
        client.addHeader("Authorization","Bearer " + BaseApplication.token);
        RequestParams params = new RequestParams();
        params.put("rs",Config.RS);

        client.post(homeApi, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                ToastUtils.showToast("Error");
                loginCheck();
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    object = object.getJSONObject("datas");
                    boolean isLogin = true;
                    if(object.has("isLogin")) {
                        isLogin = object.getBoolean("isLogin");
                    }
                    if(isLogin) {
                        initEnter();
                    } else {
                        SharedPreferencesUtil.clear();
                        BaseApplication.token="";
                        BaseApplication.userId="";
                        startActivity(LoginAndRegisterActivity.class);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.i("logincheck: ","" + responseString);
            }
        });
    }

    private void initEnter() {
        delayStartActivity(new Intent(WelcomeActivity.this,MainActivity.class));
    }

    private void delayStartActivity(final Intent intent) {
        long restTime = SPLASH_DISPLAY_LENGTH - (System.currentTimeMillis() - startTime);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
                overridePendingTransition(R.anim.anim_splash_in,R.anim.anim_splash_out);
            }
        },(restTime > 0) ? restTime : 0);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }
}
