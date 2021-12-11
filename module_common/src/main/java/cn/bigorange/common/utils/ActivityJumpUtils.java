/**************************************************************************************
 *
 *
 * @(#) InternalExchangeService.java
 * @Package com.crc.crcgas.sts.service
 *
 * 此技术信息为华润燃气集团机密信息，未经本公司书面同意禁止向第三方披露
 * Copyright ? China Resources Gas Group Limited. All rights reserved.
 *
 **************************************************************************************/
package cn.bigorange.common.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * @Description: 页面跳转服务
 * @author: zhaorui
 * @date :   2019年1月22日
 */
public class ActivityJumpUtils {
    private static final Hashtable<String, Object> exchangeCache = new Hashtable<String, Object>();
    public static String KEY_EXCHANGE_PARAM = "_KEY_EXCHANGE_PARAM";
    public static String KEY_EXCHANGE_REQUEST_COODE = "KEY_EXCHANGE_REQUEST_COODE";
    private static int requestCodeSeed = 4;

    /**
     * @param intent
     * @param param
     * @Description: 向要跳转的intent意图中添加跳转参数
     * @return: void
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static void registerExchangeParam(Intent intent, Object param) {
        String key = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date());
        intent.putExtra(KEY_EXCHANGE_PARAM, key);
        synchronized (exchangeCache) {
            exchangeCache.put(key, param);
        }
    }

    /**
     * @param activity
     * @Description: 从intent意图中获取添加的参数值
     * @return: Object
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static Object getExchangeParam(Activity activity) {
        return getExchangeParam(activity.getIntent());
    }

    /**
     * @param fragment
     * @Description: 从intent意图中获取添加的参数值
     * @return: Object
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static Object getFragment(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) return null;
        return getExchangeParam(activity.getIntent());
    }

    /**
     * @param intent
     * @Description: 从intent意图中获取添加的参数值
     * @return: Object
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static Object getExchangeParam(Intent intent) {
        if (intent == null) {
            return null;
        }
        String key = intent.getStringExtra(KEY_EXCHANGE_PARAM);
        if (key == null || "".equals(key)) {
            return null;
        }
        synchronized (exchangeCache) {
            Object o = exchangeCache.get(key);
            exchangeCache.remove(key);
            return o;
        }
    }

    /**
     * @param activity
     * @param newIntent
     * @Description: 跳转页面并请求返回值
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static int startActivityForResult(Activity activity, Intent newIntent) {
        int requestCode = requestCodeSeed++;
        newIntent.putExtra(KEY_EXCHANGE_REQUEST_COODE, requestCode);
        activity.startActivityForResult(newIntent, requestCode);
        return requestCode;
    }

    /**
     * @param activity
     * @param intent
     * @Description: 跳转页面
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        //activity.overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
    }

    /**
     * @param context
     * @param intent
     * @Description: 跳转页面
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
        //activity.overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
    }

    /**
     * @param activity
     * @param toIntent
     * @Description: 结束当前页面并放置返回值
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static void finishActivityForResult(Activity activity, Intent toIntent) {
        int requestCode = activity.getIntent().getIntExtra(KEY_EXCHANGE_REQUEST_COODE, -1);
        int resultCode = 0;
        finishActivityForResult(activity, requestCode, resultCode, toIntent);
    }

    /**
     * @param activity
     * @param resultCode
     * @param toIntent
     * @Description: 结束当前页面并放置返回值
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    public static void finishActivityForResult(Activity activity, int resultCode, Intent toIntent) {
        int requestCode = activity.getIntent().getIntExtra(KEY_EXCHANGE_REQUEST_COODE, -1);
        finishActivityForResult(activity, requestCode, resultCode, toIntent);
    }

    /**
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param toIntent
     * @Description: 结束当前页面并放置返回值
     * @return: int
     * @Author: zhaorui
     * @date :   2019年1月22日
     */
    private static void finishActivityForResult(Activity activity, int requestCode, int resultCode, Intent toIntent) {
        if (toIntent == null)
            activity.setResult(resultCode);
        else activity.setResult(resultCode, toIntent);
        if (requestCode < 0)
            activity.finish();
        else {
            activity.finishActivity(requestCode);
            activity.finish();
        }
    }
}
