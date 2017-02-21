package com.lucious.presence.config;

import java.util.Locale;

/**
 * Created by ztx on 2017/1/26.
 */

public class Config {
    public static final String BASE_URL="http://tianxzhang.com";

    public static String RS = "1";
    public static void setApiLanguage() {
        if(isZh()) {
            RS = "1";
        } else {
            RS = "2";
        }
    }

    public static boolean isZh() {
        String lan = Locale.getDefault().getLanguage();
        return lan.equals("zh");
    }
}
