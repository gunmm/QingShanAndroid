package com.qsjt.qingshan.ui.activity

import android.os.Bundle
import com.qsjt.qingshan.R
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.ToolbarUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_client_order.*
import org.json.JSONObject

class ClientOrderActivity : BaseActivity() {

    private val mAdapter by lazy {
    }

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_order)

        initView()
    }

    private fun initView() {
        ToolbarUtils(this, this.in_toolbar)
                .setDisplayHomeAsUpEnabled()
                .setTitle("我的订单")

        this.refreshLayout.autoRefresh()
        this.refreshLayout.setOnRefreshListener {
            loadData()
        }
        this.refreshLayout.setOnLoadMoreListener {

        }
    }

    private fun loadData() {
        val body = JSONObject()
        body.put("userId", mConfig.userId)
        body.put("page", page.toString())
        body.put("rows", "10")
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .getClientOrderList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<Any>>(false) {
                    override fun onSuccess(response: BasicResponse<Any>?) {
                        refreshLayout.finishRefresh()
                    }

                    override fun onFail(response: BasicResponse<Any>?) {
                        super.onFail(response)
                        refreshLayout.finishRefresh()
                    }
                })
    }
}
