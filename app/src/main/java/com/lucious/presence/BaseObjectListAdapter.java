package com.lucious.presence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztx on 2017/1/25.
 */

public class BaseObjectListAdapter extends BaseAdapter {
    protected BaseApplication mApplication;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<?> mDatas = new ArrayList<>();

    public BaseObjectListAdapter(BaseApplication application,Context context,List<?> datas) {
        mApplication = application;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if(datas != null) {
            mDatas = datas;
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<?> getDatas() {
        return mDatas;
    }
}
