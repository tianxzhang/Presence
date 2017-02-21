package com.lucious.presence;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.lucious.presence.util.ImageUtils.ImageFileCache;
import com.lucious.presence.util.ImageUtils.ImageMemoryCache;
import com.lucious.presence.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ztx on 2016/12/29.
 */

public class BaseApplication extends Application {
    public static Boolean isBroughtToBackground = false;
    public static Boolean hasPin = false;
    public static Boolean enablePin = true;
    public static int pinErrorTimes;
    public static String localPin;
    public static boolean isSetAlias = false;

    public static ImageMemoryCache memoryCache;
    public static ImageFileCache fileCache;

    public static String token;
    public static String userId;

    public static List<String> mEmoticons = new ArrayList<String>();
    public static Map<String,Integer> mEmoticonsId = new HashMap<String,Integer>();
    public static List<String> mEmoticons_Zem = new ArrayList<String>();
    public static List<String> mEmoticons_Zemji = new ArrayList<String>();

    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        memoryCache = new ImageMemoryCache(this);
        fileCache = new ImageFileCache();
        mInstance = this;
        token = SharedPreferencesUtil.getString("token","");
        userId = SharedPreferencesUtil.getString("userId","");

        for(int i = 1;i < 64;i++) {
            String emoticonsName = "[zem" + i + "]";
            int emoticonsId = getResources().getIdentifier("zem" + i,"drawable",getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zem.add(emoticonsName);
            mEmoticonsId.put(emoticonsName,emoticonsId);
        }
        for(int i = 1;i < 59;i++) {
            String emoticonsName = "[zemoji" + i + "]";
            int emoticonsId = getResources().getIdentifier("zemoji_e" + i,"drawable",getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zemji.add(emoticonsName);
            mEmoticonsId.put(emoticonsName,emoticonsId);
        }
    }

    private String getAppName(int Pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if(info.pid == Pid) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName,PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {

            }
        }
        return processName;
    }

    public static BaseApplication getInstance() {return mInstance;}

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if(!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if(!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
