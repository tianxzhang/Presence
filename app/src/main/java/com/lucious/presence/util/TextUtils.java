package com.lucious.presence.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by ztx on 2017/2/6.
 */

public class TextUtils {
    public static final String REGEX_EMAIL = "\\w[\\w.-]*@[\\w.]+\\.\\w+";
    private static final int DEF_DIV_SCALE = 10;

    public static String getJson(Context context, String name) {
        if(name != null) {
            String path = name;
            InputStream is = null;
            try {
                is = context.getAssets().open(path);
                return readTextFile(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String readTextFile(InputStream is) {
        String readedStr = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String tmp;
            while((tmp = br.readLine()) != null) {
                readedStr += tmp;
            }
            br.close();
            is.close();
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return readedStr;
    }

    public static String formatWithTwoDecimal(String string) {
        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.FLOOR);
        String temp = format.format(new BigDecimal(string));
        return temp;
    }

    public static String formatWithFourDecimal(String string) {
        DecimalFormat format = new DecimalFormat("0.0000");
        format.setRoundingMode(RoundingMode.FLOOR);
        String temp = format.format(new BigDecimal(string));
        return temp;
    }

    public static String formatWithTwoDecimalThroughF(Double d) {
        return String.format("%.2f",d);
    }

    public static String formatWithNDecimalThroughF(Double d,int n) {
        return String.format("%." + n + "f",d);
    }
    public static double div(double v1,double v2) {
        return div(v1,v2,DEF_DIV_SCALE);
    }

    private static double div(double v1, double v2, int defDivScale) {
        if(defDivScale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b0 = BigDecimal.ZERO;
        if(b2.equals(BigDecimal.ZERO) | b2.equals(new BigDecimal("0.0"))) {
            return 0;
        }
        return b1.divide(b2,defDivScale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double mul(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double add(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    public static String splitInteger(String string) {
        BigDecimal bd = new BigDecimal(string);
        String tempString = bd.toPlainString();
        String[] arr = tempString.split("[.]");
        if (arr[0] != null) {
            return arr[0];
        } else {
            return string;
        }
    }
    public static String splitDefaultDecimal(String string) {
        BigDecimal bd = new BigDecimal(string);
        String tempString = bd.toPlainString();
        String[] arr = tempString.split("[.]");
        if (arr.length != 2) {
            return "";
        } else {
            return "." + arr[1];
        }
    }
    public static String splitLongDecimal(String string, int n) {
        BigDecimal bd = new BigDecimal(string);
        String tempString = bd.toPlainString();
        String[] arr = tempString.split("[.]");
        if (arr.length != 2) {
            return "";
        } else if(arr[1].length() <= 5){
            return "." + arr[1];
        }else {
            return  "." + arr[1].substring(0, n) + "...";
        }
    }
    public static String splitOriginDecimal(String string) {
        BigDecimal bd = new BigDecimal(string);
        String result = bd.toPlainString();
        String[] arr = result.split("[.]");
        if (arr.length != 2) {
            return "";
        } else {
            return "." + arr[1];
        }
    }
    public static String splitDecimal(String string) {
        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.FLOOR);
        String temp = format.format(new BigDecimal(string));
        String[] arr = temp.split("[.]");
        if (arr.length != 2) {
            return "";
        } else {
            return "." + arr[1];
        }
    }
    public static String getApplicationVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }
    public static int getApplicationVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }
    public static Boolean versionCompare(int oldVersionCode,String newVersionCode) {
        int newv = Integer.parseInt(newVersionCode);
        return newv > oldVersionCode;
    }
}
