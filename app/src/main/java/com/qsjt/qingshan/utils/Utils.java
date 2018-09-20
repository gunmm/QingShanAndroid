package com.qsjt.qingshan.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.qsjt.qingshan.application.MyApplication;
import com.qsjt.qingshan.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author LiYouGui.
 */

public class Utils {


    /**
     * 设置全屏
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置沉浸式状态栏
     */
    public static boolean setTransparentNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            return true;
        }
        return false;
    }

    /**
     * 取消全屏
     */
    public static void clearFullScreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 文件转Base64
     *
     * @param file 文件
     * @return Base64
     */
    public static String fileToBase64(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            final StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                String encode = Base64.encodeToString(buffer, 0, len, Base64.DEFAULT);
                sb.append(encode);
            }
            inputStream.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存Bitmap到本地文件
     *
     * @param file   保存到本地为文件
     * @param bitmap bitmap
     */
    public static boolean saveBitmap(File file, Bitmap bitmap) {
        if (file == null || bitmap == null) {
            return false;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            int size = (int) (file.length() / 1024 / 1024);
            if (size > 0) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 - size * 10, outputStream);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isActivityForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(Integer.MAX_VALUE);
        if (isCollectionEmpty(list)) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).topActivity.getShortClassName().contains(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个Service是否在
     *
     * @param context   Context
     * @param className 服务的类名
     * @return 是否存在
     */
    public static boolean isServiceRuning(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(Integer.MAX_VALUE);
        if (isCollectionEmpty(list)) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).service.getShortClassName().contains(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用是否在前台
     *
     * @param context Context
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
            }
        }
        return false;
    }

    /**
     * @param date    日期
     * @param pattern 日期格式
     * @return 时间戳
     */
    public static long parseDate(String date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return 获取DP值
     */
    public static int getDP(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 格式消息内容
     *
     * @param msg 消息内容
     * @return 格式后的消息内容
     */
    public static String formatMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        String[] array = msg.split(" {2}");
        StringBuilder sb = new StringBuilder(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append("\n").append(array[i]);
        }
        return sb.toString();
    }

    /**
     * @param timestamp 时间戳
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getTime(long timestamp) {
        if (timestamp == 0) {
            return "——";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date(timestamp));
    }

    /**
     * @return 获取系统版本号
     */
    public static String getAppVersionName() {
        try {
            Context context = MyApplication.getMyApplication().getApplicationContext();
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0
     */
    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
        Window window = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }

    /**
     * MD5加密
     *
     * @param string 文本
     * @return MD5
     */
    public static String encryptMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 设置输入框保留小数位数
     *
     * @param editText 输入控件
     * @param number   保留小数点位数
     */
    public static void setEditTextPointNumber(final EditText editText, final int number) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (TextUtils.isEmpty(source)) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - number;
                    if (diff > 0) {
                        return source.subSequence(start, end - diff);
                    }
                }
                return null;
            }
        }});

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && s.toString().equals(".")) {
                    editText.setText("0.");
                    editText.setSelection(2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 是否为手机号
     */
    public static boolean isMobileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 应用包名
     * @return true|false
     */
    public static boolean isInstall(Context context, String packageName) {
        //获取PackageManager
        PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String packName = list.get(i).packageName;
                if (TextUtils.equals(packName, packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 集合是否为空
     *
     * @return true is empty
     */
    public static boolean isCollectionEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static RequestBody getRequestBody(JSONObject body) {
        JSONObject request = new JSONObject();
        try {
            JSONObject head = new JSONObject();
            head.put("token", Config.getInstance().getAccessToken());
            head.put("userId", Config.getInstance().getUserId());
            request.put("head", head);
            request.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("application/json"), request.toString());
    }
}