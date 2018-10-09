package com.qsjt.qingshan.http;

import com.qsjt.qingshan.model.BasicResponse;
import com.qsjt.qingshan.model.response.Dictionary;
import com.qsjt.qingshan.model.response.Order;
import com.qsjt.qingshan.model.response.Vehicle;

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

    /**
     * 下单
     */
    @POST("mobile/addOrder")
    Observable<BasicResponse<String>> placeAnOrder(@Body RequestBody requestBody);

    /**
     * 获取客户订单列表
     */
    @POST("mobile/getOrderList")
    Observable<BasicResponse<List<Order>>> getClientOrderList(@Body RequestBody requestBody);

    /**
     * 获取范围内车辆列表
     */
    @POST("mobile/getOrderCarList")
    Observable<BasicResponse<List<Vehicle>>> getVehicleList(@Body RequestBody requestBody);

    /**
     * 客户取消订单
     */
    @POST("mobile/cancelOrder")
    Observable<BasicResponse<Object>> cancelOrder(@Body RequestBody requestBody);
}
