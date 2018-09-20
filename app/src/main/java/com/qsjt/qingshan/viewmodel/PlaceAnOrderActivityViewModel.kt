package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.route.*
import com.qsjt.qingshan.config.Config
import org.json.JSONObject

class PlaceAnOrderActivityViewModel : ViewModel() {

    private val mConfig by lazy { Config.getInstance() }

    private val mRoutePlanSearch by lazy { RoutePlanSearch.newInstance(); }

    private val drivingRouteLine by lazy { MutableLiveData<DrivingRouteLine>() }

    var data: HashMap<*, *>? = null

    val requestBody by lazy { JSONObject() }

    var shipper: String? = null

    var shipPhone = mConfig.account

    var consignee: String? = null

    var receivePhone: String? = null

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

    fun routePlan(): MutableLiveData<DrivingRouteLine> {
        val shipLocation = data!!["shipLocation"] as LatLng
        val receiveLocation = data!!["receiveLocation"] as LatLng
        mRoutePlanSearch.drivingSearch(DrivingRoutePlanOption()
                .from(PlanNode.withLocation(shipLocation))
                .to(PlanNode.withLocation(receiveLocation)))
        return drivingRouteLine
    }

    override fun onCleared() {
        super.onCleared()
        mRoutePlanSearch.destroy()
    }
}