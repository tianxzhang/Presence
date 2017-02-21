package com.lucious.presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ztx on 2017/1/25.
 */

public abstract class BaseFragment extends Fragment {
    protected BaseApplication mApplication;
    protected Activity mActivity;
    protected Context mContext;
    protected View mView;

    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    protected List<AsyncTask<Void,Void,Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void,Void,Boolean>>();

    public BaseFragment() {
        super();
    }

    public BaseFragment(BaseApplication mApplication,Activity mActivity,Context mContext) {
        this.mApplication = mApplication;
        this.mActivity = mActivity;
        this.mContext = mContext;

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    public void onDestroy() {
        clearAsyncTask();
        super.onDestroy();
    }

    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void init();
    public View findViewById(int id) {return mView.findViewById(id);}
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
        Intent intent = new Intent();
        intent.setClass(getActivity(),cls);
        startActivity(intent);
    }
}
