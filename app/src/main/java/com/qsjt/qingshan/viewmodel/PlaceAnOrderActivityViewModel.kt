package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.text.TextUtils
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.route.*
import com.qsjt.qingshan.config.Config
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class PlaceAnOrderActivityViewModel : ViewModel() {

    private val mConfig by lazy { Config.getInstance() }

    private val mRoutePlanSearch by lazy { RoutePlanSearch.newInstance(); }

    val drivingRouteLine by lazy { MutableLiveData<DrivingRouteLine>() }

    var data: HashMap<*, *>? = null

    val orderParam by lazy { JSONObject() }
    //预约时间
    var subscribeTime: String? = null
    //发货人
    var shipper: String? = null
    //发货人电话
    var shipperPhone: String? = mConfig.account
    //收货人
    var consignee: String? = null
    //收货人电话
    var receivePhone: String? = null
    //备注
    var note: String? = null
    //发票类型  1：个人  2：单位
    var invoiceType = "1"
    //收票人姓名
    val receiverName = ObservableField<String>()
    //收票人电话
    val receiverPhone = ObservableField<String>()
    //收票地址
    val receiverAddress = ObservableField<String>()
    //公司名称
    val companyName = ObservableField<String>()
    //公司纳税人识别号
    val companyNumber = ObservableField<String>()
    //订单距离
    val distance = ObservableField<Double>()
    //订单总运费
    val price = ObservableField<Double>()
    //车辆类型
    val vehicleType = ObservableField<String>()
    //起步价
    val startPrice = ObservableField<Double>()
    //超过里程
    val exceedMileage = ObservableField<Double>()
    //超过里程价钱
    val exceedMileagePrice = ObservableField<Double>()
    //订单总运费支付类型（1:支付宝支付    2:微信支付   3:现金支付）
    var freightFeePayType: String = "3"

    init {
        mRoutePlanSearch.setOnGetRoutePlanResultListener(object : OnGetRoutePlanResultListener {
            override fun onGetIndoorRouteResult(p0: IndoorRouteResult?) {
            }

            override fun onGetTransitRouteResult(p0: TransitRouteResult?) {
            }

            override fun onGetDrivingRouteResult(p0: DrivingRouteResult?) {
                if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                    drivingRouteLine.value = null
                    return
                }
                val routeLines = p0.routeLines
                if (routeLines == null || routeLines.isEmpty()) {
                    drivingRouteLine.value = null
                    return
                }
                drivingRouteLine.value = routeLines[0]
            }

            override fun onGetWalkingRouteResult(p0: WalkingRouteResult?) {
            }

            override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult?) {
            }

            override fun onGetBikingRouteResult(p0: BikingRouteResult?) {
            }
        })
    }

    fun routePlan() {
        val shipLocation = data!!["shipLocation"] as LatLng
        val receiveLocation = data!!["receiveLocation"] as LatLng
        mRoutePlanSearch.drivingSearch(DrivingRoutePlanOption()
                .from(PlanNode.withLocation(shipLocation))
                .to(PlanNode.withLocation(receiveLocation)))
    }

    fun checkFillIn(): Boolean {
        if (TextUtils.isEmpty(shipper)) {
            ToastUtils.show("发货人不能为空")
            return false
        }
        if (TextUtils.isEmpty(shipperPhone)) {
            ToastUtils.show("发货人电话不能为空")
            return false
        }
        if (!Utils.isMobileNumber(shipperPhone)) {
            ToastUtils.show("发货人电话号码格式不正确")
            return false
        }
        if (TextUtils.isEmpty(consignee)) {
            ToastUtils.show("收货人不能为空")
            return false
        }
        if (TextUtils.isEmpty(receivePhone)) {
            ToastUtils.show("收货人电话不能为空")
            return false
        }
        if (!Utils.isMobileNumber(receivePhone)) {
            ToastUtils.show("收货人电话号码格式不正确")
            return false
        }
        if (price.get() == 0.0) {
            return false
        }
        return true
    }

    fun placeOrder(): MutableLiveData<String> {
        val result = MutableLiveData<String>()

        val type = data!!["orderType"].toString()
        orderParam.put("type", type)
        val sendCity = data!!["city"].toString()
        orderParam.put("sendCity", sendCity)
        orderParam.put("appointTime", subscribeTime)
        orderParam.put("createManId", mConfig.userId)
        orderParam.put("linkMan", shipper)
        orderParam.put("linkPhone", shipperPhone)
        orderParam.put("receiveMan", consignee)
        orderParam.put("receivePhone", receivePhone)
        orderParam.put("note", note)
        val sendAddress = data!!["shipAddressName"].toString()
        orderParam.put("sendAddress", sendAddress)
        val sendDetailAddress = data!!["shipAddress"].toString()
        orderParam.put("sendDetailAddress", sendDetailAddress)
        val shipLocation = data!!["shipLocation"] as LatLng
        orderParam.put("sendLatitude", shipLocation.latitude)
        orderParam.put("sendLongitude", shipLocation.longitude)
        val receiveAddress = data!!["receiveAddressName"].toString()
        orderParam.put("receiveAddress", receiveAddress)
        val receiveDetailAddress = data!!["receiveAddress"].toString()
        orderParam.put("receiveDetailAddress", receiveDetailAddress)
        val receiveLocation = data!!["receiveLocation"] as LatLng
        orderParam.put("receiveLatitude", receiveLocation.latitude)
        orderParam.put("receiveLongitude", receiveLocation.longitude)
        orderParam.put("price", price.get())
        val servicePrice = price.get()!! * 0.03
        orderParam.put("servicePrice", if (servicePrice > 300.0) {
            300.0
        } else {
            servicePrice
        })
        orderParam.put("distance", distance.get())
        orderParam.put("freightFeePayType", freightFeePayType)
        orderParam.put("freightFeePayStatus", if ("3" == freightFeePayType) {
            "0"
        } else {
            orderParam.put("freightFeePayId", "dfhjksdhfjkhsdjkfhsdjkfhjksdhfjk")
            "1"
        })

        val jsonObject = JSONObject()
        jsonObject.put("orderParam", orderParam)

        if (!TextUtils.isEmpty(receiverName.get())) {
            val invoiceParam = JSONObject()
            invoiceParam.put("invoiceType", invoiceType)
            invoiceParam.put("receiverName", receiverName.get())
            invoiceParam.put("receiverPhone", receiverPhone.get())
            invoiceParam.put("receiverAddress", receiverAddress.get())
            invoiceParam.put("companyName", companyName.get())
            invoiceParam.put("companyNumber", companyNumber.get())
            jsonObject.put("orderParam", invoiceParam)
        }
        val requestBody = Utils.getRequestBody(jsonObject)

        RxRetrofit.getApiService()
                .placeAnOrder(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<String>>() {
                    override fun onSuccess(response: BasicResponse<String>?) {
                        result.value = response!!.`object`
                    }
                })

        return result
    }

    override fun onCleared() {
        super.onCleared()
        mRoutePlanSearch.destroy()
    }
}