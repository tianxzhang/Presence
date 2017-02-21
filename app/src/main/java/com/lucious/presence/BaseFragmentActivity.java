package com.lucious.presence;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ztx on 2017/1/25.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {
    protected BaseApplication mApplication;
    protected float mDensity;
    protected int mScreenHeight;
    protected int mScreenWidth;

    protected List<AsyncTask<Void,Void,Boolean>> mAsyncTasks = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mApplication = ((BaseApplication)getApplication());
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
    protected void putAsyncTask(AsyncTask<Void,Void,Boolean> paramAsyncTask) {
        this.mAsyncTasks.add(paramAsyncTask.execute(new Void[0]));
    }
    protected void clearAsyncTask() {
        Iterator localIterator = this.mAsyncTasks.iterator();
        while(localIterator.hasNext()) {
            AsyncTask localAsyncTask = (AsyncTask) localIterator.next();
            if((localAsyncTask != null) && (!localAsyncTask.isCancelled()))
                localAsyncTask.cancel(true);
        }
        this.mAsyncTasks.clear();
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
        super.finish();
    }
}
