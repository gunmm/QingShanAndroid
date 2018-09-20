package com.qsjt.qingshan.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.qsjt.qingshan.application.MyApplication;
import com.qsjt.qingshan.config.Config;
import com.qsjt.qingshan.constant.Constants;
import com.qsjt.qingshan.listener.OnJumpLoginListener;
import com.qsjt.qingshan.model.BasicResponse;
import com.qsjt.qingshan.utils.ToastUtils;
import com.qsjt.qingshan.view.LoadingDialog;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static com.qsjt.qingshan.http.BaseObserver.ExceptionReason.CONNECT_ERROR;
import static com.qsjt.qingshan.http.BaseObserver.ExceptionReason.CONNECT_TIMEOUT;
import static com.qsjt.qingshan.http.BaseObserver.ExceptionReason.PARSE_ERROR;
import static com.qsjt.qingshan.http.BaseObserver.ExceptionReason.UNKNOWN_ERROR;


/**
 * @author LiYouGui
 */

public abstract class BaseObserver<T extends BasicResponse> implements Observer<T> {

    private ProgressDialog mPDialog;

    private Disposable mDisposable;

    public BaseObserver() {
        this(true);
    }

    public BaseObserver(boolean isShowLoading) {
        if (isShowLoading) {
            mPDialog = LoadingDialog.getInstance();
            mPDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mDisposable != null) {
                        mDisposable.dispose();
                    }
                }
            });
            mPDialog.show();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        progressDialogDismiss();
        switch (t.getResult_code()) {
            case "1":   //请求成功
                onSuccess(t);
                break;
            case "-9":  //登录超时
                onFail(t);
                jump2Login();
                break;
            default:   //请求错误
                onFail(t);
                break;
        }
    }

    private void jump2Login() {
        Context context = MyApplication.getMyApplication().getApplicationContext();
        JPushInterface.deleteAlias(context, Constants.JP_ALIAS_SEQUENCE);
        Config.getInstance().setAccessToken(null);
        Config.getInstance().setUserId(null);
        Intent intent = new Intent("com.qsjt.qingshan.LoginActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    public abstract void onSuccess(T response);

    /**
     * 服务器返回错误数据
     *
     * @param response 服务器返回的数据
     */
    public void onFail(T response) {
        ToastUtils.show(response.getResult());
    }

    @Override
    public void onError(Throwable e) {
        progressDialogDismiss();
        e.printStackTrace();
        if (e instanceof HttpException) {                       // HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {         // 连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {       // 连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {               // 解析错误
            onException(PARSE_ERROR);
        } else {                                                // 未知错误
            onException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {
        progressDialogDismiss();
    }

    private void progressDialogDismiss() {
        if (mPDialog != null) {
            mPDialog.dismiss();
        }
    }

    /**
     * 请求异常
     */
    private void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show("网络连接错误");
                break;
            case CONNECT_TIMEOUT:
                ToastUtils.show("连接超时,请稍后再试");
                break;
            case BAD_NETWORK:
                ToastUtils.show("网络状态异常");
                break;
            case PARSE_ERROR:
                ToastUtils.show("数据解析错误");
                break;
            case UNKNOWN_ERROR:
                ToastUtils.show("未知的错误");
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
