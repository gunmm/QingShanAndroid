package com.qsjt.qingshan.http;

import com.qsjt.qingshan.model.BasicResponse;
import com.qsjt.qingshan.model.response.Dictionary;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author LiYouGui
 */

public interface ApiService {

    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 60000;

    /**
     * 登录
     */
    @POST("login")
    Observable<BasicResponse<Object>> login(@Body RequestBody requestBody);

    /**
     * 获取验证码
     */
    @POST("getCode")
    Observable<BasicResponse<String>> getVerificationCode(@Body RequestBody requestBody);

    /**
     * 重置密码
     */
    @POST("resetPassword")
    Observable<BasicResponse<Object>> resetPassword(@Body RequestBody requestBody);

    /**
     * 获取字典表
     */
    @POST("mobile/getDictionaryList")
    Observable<BasicResponse<List<Dictionary>>> getDictionaryList(@Body RequestBody requestBody);
}