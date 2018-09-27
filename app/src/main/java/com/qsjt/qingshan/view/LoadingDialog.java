package com.qsjt.qingshan.view;

import android.app.ProgressDialog;

import com.qsjt.qingshan.application.MyApplication;
import com.qsjt.qingshan.utils.LogUtils;


public class LoadingDialog {

    public static ProgressDialog getInstance() {
        LoadingDialog instance = new LoadingDialog();
        return instance.mPDialog;
    }

    private ProgressDialog mPDialog;

    private LoadingDialog() {
        mPDialog = new ProgressDialog(MyApplication.getMyApplication().getTopActivity());
        mPDialog.setMessage("正在加载");
        mPDialog.setIndeterminate(true);
        mPDialog.setCancelable(false);
    }
}
