package com.lucious.presence;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ztx on 2017/1/25.
 */

public abstract class BaseActivity extends Activity {
    protected BaseApplication mApplication;
    protected Resources mResources;

    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    protected List<AsyncTask<Void,Void,Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void,Void,Boolean>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        mResources = getResources();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    @Override
    protected void onDestroy() {
        clearAsyncTask();
        super.onDestroy();
    }

    protected abstract void initViews();
    protected abstract void initEvents();

    protected void putAsyncTask(AsyncTask<Void,Void,Boolean> asyncTask) {
        mAsyncTasks.add(asyncTask.execute());
    }

    protected void clearAsyncTask() {
        Iterator<AsyncTask<Void,Void,Boolean>> iterator = mAsyncTasks.iterator();
        while(iterator.hasNext()) {
            AsyncTask<Void,Void,Boolean> asyncTask = iterator.next();
            if(asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        mAsyncTasks.clear();
    }

    protected void startActivity(Class<?> cls) {
        startActivity(cls,null);
    }

    protected void startActivity(Class<?> cls,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this,cls);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivity(String action) {
        startActivity(action,null);
    }
    protected void startActivity(String action,Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected void defaultFinish() {
        super.fileList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(BaseApplication.isApplicationBroughtToBackground(this)){
            BaseApplication.isBroughtToBackground = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}