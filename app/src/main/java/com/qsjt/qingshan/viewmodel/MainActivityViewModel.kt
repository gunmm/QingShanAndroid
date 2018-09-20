package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.qsjt.qingshan.config.Config
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.model.response.Dictionary
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MainActivityViewModel : ViewModel() {

    private val geoCoder by lazy { GeoCoder.newInstance() }

    var nickName = Config.getInstance().nickname

    var orderType = "1"

    val shipAddressName = ObservableField<String>()

    var shipAddress: String? = null

    var shipLocation: LatLng? = null

    val receiveAddressName = ObservableField<String>()

    var receiveAddress: String? = null

    var receiveLocation: LatLng? = null

    var city: String? = null

    init {
        geoCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(p0: GeoCodeResult?) {
            }

            override fun onGetReverseGeoCodeResult(p0: ReverseGeoCodeResult?) {
                if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                    return
                }
                shipAddressName.set(p0.poiList[0].name)
                val poiInfo = p0.poiList[0]
//                shipAddressName.set(poiInfo.name)
                shipAddress = if ("北京市" == poiInfo.province ||
                        "天津市" == poiInfo.province ||
                        "上海市" == poiInfo.province ||
                        "重庆市" == poiInfo.province) {
                    poiInfo.city + poiInfo.area
                } else {
                    poiInfo.province + poiInfo.city + poiInfo.area
                }
            }
        })
    }

    fun reverseGeoCode(location: LatLng) {
        geoCoder.reverseGeoCode(ReverseGeoCodeOption().location(location))
    }

    fun loadDictionaryList(): MutableLiveData<HashMap<String, Any?>> {
        val data = MutableLiveData<HashMap<String, Any?>>()

        val body = JSONObject()
        body.put("name", "车辆类型")
        body.put("cityName", city)
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .getDictionaryList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<List<Dictionary>>>() {
                    override fun onSuccess(response: BasicResponse<List<Dictionary>>?) {
                        val map = HashMap<String, Any?>()
                        map["orderType"] = orderType
                        map["shipAddressName"] = shipAddressName.get()
                        map["shipAddress"] = shipAddress
                        map["shipLocation"] = shipLocation
                        map["receiveAddressName"] = receiveAddressName.get()
                        map["receiveAddress"] = receiveAddress
                        map["receiveLocation"] = receiveLocation
                        map["city"] = city
                        map["vehicleDictionary"] = response!!.`object`
                        data.value = map
                    }
                })

        return data
    }

    override fun onCleared() {
        super.onCleared()
        geoCoder.destroy()
    }
}
