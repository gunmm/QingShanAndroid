package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*

class SearchAddressActivityViewModel : ViewModel() {

    private val poiSearch by lazy { PoiSearch.newInstance() }

    val poiData by lazy { MutableLiveData<List<PoiInfo>>() }

    var addressName: String? = null

    var city: String? = null

    var pageNum = 0

    init {
        poiSearch.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
            }

            override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
            }

            override fun onGetPoiResult(p0: PoiResult?) {
                if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                    poiData.value = null
                    return
                }
                poiData.value = p0.allPoi
                pageNum++
            }

            override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
            }
        })
    }

    fun poiSearch() {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(addressName)) {
            return
        }
        poiSearch.searchInCity(PoiCitySearchOption()
                .city(city)
                .keyword(addressName)
                .pageNum(pageNum)
        )
    }

    override fun onCleared() {
        super.onCleared()
        poiSearch.destroy()
    }
}