package com.lucious.presence.activity;

import com.lucious.presence.BaseActivity;
import com.lucious.presence.config.Config;

import android.view.View;

/**
 * Created by ztx on 2017/2/3.
 */

public class LoginAndRegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String SEND_CODE_URL = Config.BASE_URL + "/presence/user/send_code";
    private static final String LOGIN_URL = Config.BASE_URL + "/presence/user/doMobileLogin";
    private static final String REG_URL = Config.BASE_URL + "/presence/user/regSub";

    private static final String TAG = "LoginAndregisterActivity";
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }
}
