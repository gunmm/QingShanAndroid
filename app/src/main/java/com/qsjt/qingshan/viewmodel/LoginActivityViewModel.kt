package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.text.TextUtils
import com.google.gson.Gson
import com.qsjt.qingshan.config.Config
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.utils.LogUtils
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class LoginActivityViewModel : ViewModel() {

    var version = Utils.getAppVersionName()!!

    var phoneNumber: String? = Config.getInstance().account

    var password: String? = Config.getInstance().password

    fun login(): MutableLiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = false
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
            ToastUtils.show("手机号或密码不能为空")
            return data
        }

        val body = JSONObject()
        body.put("phoneNumber", phoneNumber)
        body.put("password", Utils.encryptMD5(password))
        body.put("plateform", "android")
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .login(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<Any>>() {
                    override fun onSuccess(response: BasicResponse<Any>?) {
                        val json = Gson().toJson(response!!.`object`)
                        val jsonObject = JSONObject(json)
                        val mConfig = Config.getInstance()
                        mConfig.account = phoneNumber
                        mConfig.password = password
                        mConfig.accessToken = jsonObject["accessToken"].toString()
                        mConfig.userId = jsonObject["userId"].toString()
                        mConfig.roleType = jsonObject["type"].toString()
                        if (json.contains("nickname")) {
                            mConfig.nickname = jsonObject["nickname"].toString()
                        }
                        if (json.contains("personImageUrl")) {
                            mConfig.avatar = jsonObject["personImageUrl"].toString()
                        }
                        data.value = true
                    }
                })

        return data
    }
}
