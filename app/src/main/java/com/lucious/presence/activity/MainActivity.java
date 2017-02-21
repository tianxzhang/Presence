package com.lucious.presence.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lucious.presence.BaseApplication;
import com.lucious.presence.R;
import com.lucious.presence.config.Config;
import com.lucious.presence.fragment.maintabs.CourseFragment;
import com.lucious.presence.fragment.maintabs.HomeFragment;
import com.lucious.presence.fragment.maintabs.PlaygroundFragment;
import com.lucious.presence.util.LogUtils;
import com.lucious.presence.util.NetworkUtils.NetworkUtils;
import com.lucious.presence.util.TextUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ztx on 2017/2/2.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String UPDATE_URL = Config.BASE_URL + "/u/api/update";

    private Fragment fragmentPlayground;
    private Fragment fragmentCourse;
    private Fragment fragmentHome;
    private LinearLayout mTabPlayground;
    private ImageButton mTabPlaygroundIco;
    private LinearLayout mTabCourse;
    private ImageButton mTabCourseIco;
    private LinearLayout mTabHome;
    private ImageButton mTabHomeIco;

    private String presentVersion = "";
    private String versionCode = "";
    private String versionName = "";
    private String newVersionUrl = "";
    private String newDes = "";

    private Boolean flagFrontOrBg = false;
    private Boolean flagIsGoToActivity = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvents();
        setSelect(getIntent().getIntExtra("Tag",0));
        httpsUpdateRequest();
    }

    private void httpsUpdateRequest() {
        AsyncHttpClient client = NetworkUtils.getAsyncHttpClientInstance();
        RequestParams params = new RequestParams();
        client.addHeader("Authorization","Bearer " + BaseApplication.token);
        params.put("rs",Config.RS);
        client.post(UPDATE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.i("APITEST","关于界面更新: " + "responseBody:" + responseString);
                resolveAboutData(responseString);
            }
        });
    }

    
    private void resolveAboutData(String responseString) {
        if(responseString.equals("")) {
            return;
        }
        try {
            JSONObject object = new JSONObject(responseString);
            object = object.getJSONObject("datas");
            versionCode = object.getString("Build");
            versionName = object.getString("Version");
            newVersionUrl = object.getString("Url");
            newDes = object.getString("Description");
            if(TextUtils.versionCompare(TextUtils.getApplicationVersionCode(MainActivity.this),versionCode)) {
                showUpdateAlertDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setSelect(int tag) {
        resetImgs();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (tag) {
            case 0:
                if(fragmentPlayground == null) {
                    fragmentPlayground = new PlaygroundFragment();
                    transaction.add(R.id.main_framelayout,fragmentPlayground);
                } else {
                    transaction.show(fragmentPlayground);
                }
                mTabPlaygroundIco.setImageResource(R.drawable.ico_playground_selected);
                break;
            case 1:
                if(fragmentCourse == null) {
                    fragmentCourse = new CourseFragment();
                    transaction.add(R.id.main_framelayout,fragmentCourse);
                } else {
                    transaction.show(fragmentCourse);
                }
                mTabCourseIco.setImageResource(R.drawable.ico_course_selected);
                break;
            case 2:
                if(fragmentHome == null) {
                    fragmentHome = new HomeFragment();
                    transaction.add(R.id.main_framelayout,fragmentHome);
                } else {
                    transaction.show(fragmentHome);
                }
                mTabHomeIco.setImageResource(R.drawable.ico_home_selected);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(fragmentPlayground != null)
            transaction.hide(fragmentPlayground);
        if(fragmentCourse != null)
            transaction.hide(fragmentCourse);
        if(fragmentHome != null)
            transaction.hide(fragmentHome);
    }

    private void resetImgs() {
        mTabPlaygroundIco.setImageResource(R.drawable.ico_playground);
        mTabCourseIco.setImageResource(R.drawable.ico_course);
        mTabHomeIco.setImageResource(R.drawable.ico_home);
    }

    private void initEvents() {
        mTabPlayground.setOnClickListener(this);
        mTabCourse.setOnClickListener(this);
        mTabHome.setOnClickListener(this);
    }

    private void initViews() {
        mTabPlayground = (LinearLayout) findViewById(R.id.id_tab_playground);
        mTabCourse = (LinearLayout) findViewById(R.id.id_tab_course);
        mTabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        mTabPlaygroundIco = (ImageButton) findViewById(R.id.id_tab_playground_ico);
        mTabCourseIco = (ImageButton) findViewById(R.id.id_tab_course_ico);
        mTabHomeIco = (ImageButton) findViewById(R.id.id_tab_home_ico);
//        showUpdateAlertDialog();
    }

    private void showUpdateAlertDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialog_update,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setView(view,0,0,0,0);
        TextView mTvTitle = (TextView) view.findViewById(R.id.tv_dialog_update_title);
        TextView mTvDes = (TextView) view.findViewById(R.id.tv_dialog_update_des);
        TextView mTvUpdateNow = (TextView) view.findViewById(R.id.tv_dialog_update_right_now);
        TextView mTvUpdateLater = (TextView) view.findViewById(R.id.tv_dialog_update_later);
        mTvDes.setText(newDes);
        mTvUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newVersionUrl));
                startActivity(browserIntent);
            }
        });
        mTvUpdateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }

    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_playground:
                setSelect(0);
                break;
            case R.id.id_tab_course:
                setSelect(1);
                break;
            case R.id.id_tab_home:
                setSelect(2);
                break;
        }
    }
}
