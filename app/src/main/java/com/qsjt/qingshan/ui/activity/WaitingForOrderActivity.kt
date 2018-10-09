package com.qsjt.qingshan.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.baidu.mapapi.animation.AlphaAnimation
import com.baidu.mapapi.animation.AnimationSet
import com.baidu.mapapi.animation.ScaleAnimation
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.model.LatLng
import com.qsjt.qingshan.R
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.http.RxRetrofit
import com.qsjt.qingshan.model.BasicResponse
import com.qsjt.qingshan.model.response.Order
import com.qsjt.qingshan.model.response.Vehicle
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.MapUtils
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.ToolbarUtils
import com.qsjt.qingshan.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_waiting_for_order.*
import org.json.JSONObject

class WaitingForOrderActivity : BaseActivity() {

    private val mMap by lazy { this.mapView.map }

    private val order by lazy { intent.getSerializableExtra("order") as Order }

    private val mHandler by lazy { Handler() }

    private var mWaitTimeRunnable: Runnable? = null

    private var mRefreshDataRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        setContentView(R.layout.activity_waiting_for_order)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWaitTimeRunnable != null) {
            mHandler.removeCallbacks(mWaitTimeRunnable)
        }
        if (mRefreshDataRunnable != null) {
            mHandler.removeCallbacks(mRefreshDataRunnable)
        }
    }

    private fun initView() {
        val toolbarUtils = ToolbarUtils(this, this.in_toolbar)
                .setDisplayHomeAsUpEnabled()
                .setTitle("等待接单")
                .setRightText("取消订单") {
                    cancelOrder()
                }

        MapUtils.hideMapLogo(this.mapView)
        this.mapView.showZoomControls(false)
        mMap.uiSettings.isOverlookingGesturesEnabled = false

        if ("1" == order.status) {
            this.ll_tbc_tip.visibility = View.VISIBLE
            toolbarUtils.getView<TextView>(R.id.tv_right).visibility = View.GONE
        } else {
            this.ll_tbc_tip.visibility = View.GONE
            toolbarUtils.getView<TextView>(R.id.tv_right).visibility = View.VISIBLE

            initLocationMarker()
            loadVehicleList(true)
            mRefreshDataRunnable = object : Runnable {
                override fun run() {
                    loadVehicleList(false)
                    mHandler.postDelayed(this, 30000)
                }
            }
            mHandler.postDelayed(mRefreshDataRunnable, 30000)
        }
    }

    private fun cancelOrder() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("提示")
        dialog.setMessage("确认取消订单")
        dialog.setNegativeButton("取消", null)
        dialog.setPositiveButton("确认") { dialog, which ->
            val body = JSONObject()
            body.put("orderId", order.orderId)
            val requestBody = Utils.getRequestBody(body)

            RxRetrofit.getApiService()
                    .cancelOrder(requestBody)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseObserver<BasicResponse<Any>>() {
                        override fun onSuccess(response: BasicResponse<Any>?) {
                            ToastUtils.show("订单取消成功")
                            onBackPressed()
                        }
                    })
        }
        dialog.create().show()
    }

    private fun initLocationMarker() {
        val view = View.inflate(this, R.layout.map_item_waiting_location_marker, null)
        val tvWaitTime = view.findViewById<TextView>(R.id.tv_wait_time)

        var ms = System.currentTimeMillis() - order.createTime
        val position = LatLng(order.sendLatitude, order.sendLongitude)

        val markerOptions = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromView(view))
                .zIndex(9)
        val marker = mMap.addOverlay(markerOptions) as Marker
        marker.setToTop()
        mWaitTimeRunnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                tvWaitTime.text = "等待${getTime(ms)}"
                marker.icon = BitmapDescriptorFactory.fromView(view)
                ms += 1000
                mHandler.postDelayed(this, 1000)
            }
        }
        mHandler.post(mWaitTimeRunnable)

        val view1 = View.inflate(this, R.layout.map_item_circle_marker, null)
        val markerOptions1 = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromView(view1))
                .zIndex(9)
        val marker1 = mMap.addOverlay(markerOptions1) as Marker
        marker1.setAnchor(0.5f, 0.6f)
        val scaleAnimation = ScaleAnimation(0f, 1f)
        scaleAnimation.setRepeatCount(10000)
        val alphaAnimation = AlphaAnimation(1f, 0.1f)
        alphaAnimation.setRepeatCount(10000)
        val animationSet = AnimationSet()
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.setDuration(1000)
        animationSet.setAnimatorSetMode(0)
        animationSet.setInterpolator(LinearInterpolator())
        marker1.setAnimation(animationSet)
        marker1.startAnimation()

        MapUtils.animateMapStatus(mMap, position)
    }

    private fun loadVehicleList(showLoading: Boolean) {
        val body = JSONObject()
        body.put("orderId", order.orderId)
        val requestBody = Utils.getRequestBody(body)

        RxRetrofit.getApiService()
                .getVehicleList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BasicResponse<List<Vehicle>>>(showLoading) {
                    override fun onSuccess(response: BasicResponse<List<Vehicle>>?) {
                        val list = response!!.`object`
                        if (Utils.isCollectionEmpty(list)) {
                            return
                        }
                        list.forEach {
                            initVehicleMarker(it)
                        }
                    }
                })
    }

    private fun initVehicleMarker(it: Vehicle) {
        val position = LatLng(it.nowLatitude, it.nowLongitude)
        val markerOptions = MapUtils.initMarkerOptions(this, R.drawable.ic_vehicle_marker, 30)
        markerOptions.position(position)
        mMap.addOverlay(markerOptions)
    }

    private fun getTime(ms: Long): String {
        val ss = 1000
        val mi = ss * 60
        val hh = mi * 60
        val hour = ms / hh
        val minute = (ms - hour * hh) / mi
        val second = (ms - hour * hh - minute * mi) / ss
        return String.format("%d:%02d:%02d", hour, minute, second)
    }
}
