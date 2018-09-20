package com.qsjt.qingshan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.qsjt.qingshan.application.MyApplication;

/**
 * Toast
 *
 * @author LiYouGui
 */

public class ToastUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context = MyApplication.getMyApplication().getBaseContext();

    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static Toast show(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        ((TextView) mToast.getView().findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
        mToast.show();
        return mToast;
    }
}
