package com.lucious.presence.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.lucious.presence.BaseApplication;

/**
 * Created by ztx on 2017/2/3.
 */

public class ToastUtils {
    private static Context context = BaseApplication.getInstance();
    public static void showToast(int resID) {
        showToast(context,Toast.LENGTH_SHORT,resID);
    }
    public static void showToast(String text) {
        showToast(context,Toast.LENGTH_SHORT,text);
    }
    public static void showToast(Context context,int resID) {
        showToast(context,Toast.LENGTH_SHORT,resID);
    }
    public static void showToast(Context context,String text) {
        showToast(context,Toast.LENGTH_SHORT,text);
    }
    public static void showLongToast(int resID) {
        showToast(context,Toast.LENGTH_LONG,resID);
    }
    public static void showLongToast(Context context,int resID) {
        showToast(context,Toast.LENGTH_LONG,resID);
    }
    public static void showLongToast(String text) {
        showToast(context,Toast.LENGTH_LONG,text);
    }
    public static void showLongToast(Context context,String text) {
        showToast(context,Toast.LENGTH_LONG,text);
    }
    public static void showToast(Context context,int duration,int resID) {
        showToast(context,duration,context.getString(resID));
    }
    public static void showToast(Context context,int duration,String text) {
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    public static void showToastOnUiThread(final Activity context,final String text) {
        if(context != null) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(context,text);
                }
            });
        }
    }
}
