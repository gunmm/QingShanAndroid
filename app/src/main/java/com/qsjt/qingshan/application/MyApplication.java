package com.qsjt.qingshan.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.qsjt.qingshan.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiYouGui.
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MyApplication myApplication;

    private final List<Activity> activities = new ArrayList<>();

    private boolean showSplash = true;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        registerActivityLifecycleCallbacks(this);

        //百度地图SDK初始化
        SDKInitializer.initialize(this);
    }

    /**
     * @return MyApplication
     */
    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public boolean isShowSplash() {
        return showSplash;
    }

    public void setShowSplash(boolean showSplash) {
        this.showSplash = showSplash;
    }

    /**
     * @return true is foreground
     */
    public boolean isForeground() {
        return !activities.isEmpty();
    }

    /**
     * @param className activityClassName
     * @return true is foreground
     */
    public boolean isForeground(String className) {
        if (!isForeground()) {
            return false;
        }
        if (TextUtils.isEmpty(className)) {
            return false;
        }
        for (Activity activity : activities) {
            if (activity.getClass().getSimpleName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return TopActivity
     */
    public Activity getTopActivity() {
        return activities.get(0);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        activities.add(0, activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
