package com.qsjt.qingshan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class RetrievePasswordActivityViewModel : ViewModel() {

    var phoneNumber: String? = null

    var verificationCode: String? = null

    var password1: String? = null

    var password2: String? = null

    private var code: String? = null

    fun getVerificationCode(): MutableLiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = false

        val body = JSONObject()
        body.put("phoneNumber", phoneNumber)
        body.put("type", "1")
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .getVerificationCode(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<String>>() {
                    override fun onSuccess(response: BasicResponse<String>?) {
                        ToastUtils.show(response!!.result)
                        code = response!!.`object`
                        data.value = true
                    }
                })

        return data
    }

    fun resetPassword(): MutableLiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = false

        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.show("请输入手机号码")
            return data
        }
        if (TextUtils.isEmpty(verificationCode)) {
            ToastUtils.show("请输入验证码")
            return data
        }
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            ToastUtils.show("请输入密码")
            return data
        }
        if (password1 != password2) {
            ToastUtils.show("两次输入的密码不一致")
            return data
        }
        if (verificationCode != code) {
            ToastUtils.show("验证码不正确")
            return data
        }

        val body = JSONObject()
        body.put("phoneNumber", phoneNumber)
        body.put("password", Utils.encryptMD5(password2))
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .resetPassword(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<Any>>() {
                    override fun onSuccess(response: BasicResponse<Any>?) {
                        ToastUtils.show(response!!.result)
                        data.value = true
                    }
                })

        return data
    }
}
