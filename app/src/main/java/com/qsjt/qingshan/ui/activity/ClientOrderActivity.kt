package com.qsjt.qingshan.ui.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import com.qsjt.qingshan.R
import com.qsjt.qingshan.adapter.BindingAdapter
import com.qsjt.qingshan.databinding.RecycleItemClientOrderBinding
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.model.response.Order
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.ToolbarUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_client_order.*
import org.json.JSONObject

class ClientOrderActivity : BaseActivity() {

    private val mAdapter by lazy {
        object : BindingAdapter<Order>(R.layout.recycle_item_client_order, null) {
            override fun convert(helper: BaseViewHolder?, item: Order?) {
                if (item == null) return
                val bd = DataBindingUtil.getBinding<RecycleItemClientOrderBinding>(helper!!.itemView)
                bd!!.vm = item
                bd.executePendingBindings()
                bd.root.setOnClickListener {
                    when (item.status) {
                        "0", "1" -> {
                            val intent = Intent(this@ClientOrderActivity, WaitingForOrderActivity::class.java)
                            intent.putExtra("order", item)
                            startActivity(intent)
                        }
                        else -> {
                            
                        }
                    }
                }
            }
        }
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
            page = 0
            loadData()
        }
        this.refreshLayout.setOnLoadMoreListener {
            loadData()
        }

        val rv = this.recyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv.adapter = mAdapter
        mAdapter.bindToRecyclerView(rv)
    }

    private fun loadData() {
        val body = JSONObject()
        body.put("userId", mConfig.userId)
        body.put("page", page)
        body.put("rows", "10")
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .getClientOrderList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<List<Order>>>(false) {
                    override fun onSuccess(response: BasicResponse<List<Order>>?) {
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore()
                        val list = response!!.`object`
                        if (Utils.isCollectionEmpty(list)) {
                            return
                        }
                        if (list.size < 10) {
                            refreshLayout.finishLoadMoreWithNoMoreData()
                        } else {
                            refreshLayout.setNoMoreData(false)
                        }
                        if (page == 0) {
                            mAdapter.replaceData(list)
                        } else {
                            mAdapter.addData(list)
                        }
                        page++
                    }

                    override fun onFail(response: BasicResponse<List<Order>>?) {
                        super.onFail(response)
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore(false)
                    }
                })
    }
}
