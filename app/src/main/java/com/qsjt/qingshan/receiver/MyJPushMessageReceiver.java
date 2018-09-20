package com.qsjt.qingshan.receiver;

import android.content.Context;

import com.qsjt.qingshan.utils.LogUtils;
import com.qsjt.qingshan.utils.ToastUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private int mJPushCount = 0;

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);

        if (jPushMessage.getErrorCode() == 0) {
            LogUtils.i("JPush", "set alias successfully!");
            LogUtils.i("JPush", "alias:" + jPushMessage.getAlias());
        } else {
            LogUtils.i("JPush", "mJPushCount:" + mJPushCount);
            LogUtils.i("JPush", "return code:" + jPushMessage.getErrorCode());

            if (mJPushCount / 3 < 5) {
                if (0 == mJPushCount % 3 && mJPushCount / 3 > 0 && mJPushCount / 3 < 5) {
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new JPushAliasThread(context, jPushMessage.getAlias()).start();
            } else {
                ToastUtils.show("推送服务注册失败，请检查网络再次登录!");
            }
            mJPushCount++;
        }
    }

    /**
     * 设置JPush别名
     */
    private class JPushAliasThread extends Thread {

        private final Context context;

        private final String alias;

        private JPushAliasThread(Context context, String alias) {
            this.context = context;
            this.alias = alias;
        }

        @Override
        public void run() {
            super.run();
            JPushInterface.setAlias(context, 9999, alias);
        }
    }
}
