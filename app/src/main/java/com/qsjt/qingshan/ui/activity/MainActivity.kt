package com.qsjt.qingshan.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qsjt.qingshan.R
import com.qsjt.qingshan.databinding.ActivityMainBinding
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.*
import com.qsjt.qingshan.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainActivityViewModel::class.java) }

    private lateinit var mBinding: ActivityMainBinding

    private val mLocationClient by lazy { BDLocationClient(MyLocationListener()) }

    private val mMap by lazy { mBinding.mapView.map }

    private val mIvLocationInWindow by lazy { IntArray(3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = viewModel
        mBinding.setLifecycleOwner(this)

        initMap()

        initView()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    private fun initMap() {
        mBinding.mapView.showZoomControls(false)
        mBinding.mapView.showScaleControl(false)
        MapUtils.hideMapLogo(mBinding.mapView)
        val uiSettings = mMap.uiSettings
        uiSettings.isOverlookingGesturesEnabled = false
        mMap.isMyLocationEnabled = true

        mMap.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(p0: MapStatus?) {
            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
            }

            override fun onMapStatusChange(p0: MapStatus?) {
            }

            override fun onMapStatusChangeFinish(p0: MapStatus?) {
                val point = Point(mIvLocationInWindow[0], mIvLocationInWindow[2])
                val location = mMap.projection.fromScreenLocation(point)
                viewModel.shipLocation = location
                viewModel.reverseGeoCode(location)
            }
        })
    }

    private fun initView() {
        val mTypeface = Typeface.createFromAsset(assets, "iconfont.ttf")
        ToolbarUtils(this, this.in_toolbar)
                .setTitle("主页面")
                .setLeftText(mTypeface, resources.getString(R.string.if_personal), 25f) {
                    mBinding.drawerLayout.openDrawer(GravityCompat.START)
                }
                .setRightText(mTypeface, resources.getString(R.string.if_message), 25f) {

                }
                .showMessageTip(true)

        mBinding.ivLocationPin.post {
            val loc = IntArray(2)
            mBinding.ivLocationPin.getLocationInWindow(loc)
            val point = Point()
            windowManager.defaultDisplay.getSize(point)
            val height = mBinding.ivLocationPin.height
            mIvLocationInWindow[0] = point.x / 2
            mIvLocationInWindow[1] = point.y - loc[1] - height
            mIvLocationInWindow[2] = loc[1] + height
            mLocationClient.start()
        }

        mBinding.tvLocation.setOnClickListener {
            mLocationClient.start()
        }

        Glide.with(this)
                .load(mConfig.server + mConfig.avatar)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_default_avatar)
                        .fallback(R.drawable.ic_default_avatar)
                        .error(R.drawable.ic_default_avatar))
                .into(mBinding.ivAvatar)

        mBinding.rbNow.paint.isFakeBoldText = true
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_now -> {
                    viewModel.orderType = "1"
                    mBinding.rbNow.paint.isFakeBoldText = true
                    mBinding.rbSubscribe.paint.isFakeBoldText = false
                }
                R.id.rb_subscribe -> {
                    viewModel.orderType = "2"
                    mBinding.rbNow.paint.isFakeBoldText = false
                    mBinding.rbSubscribe.paint.isFakeBoldText = true
                }
            }
        }

        mBinding.tvShipAddress.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.putExtra("city", viewModel.city)
            intent.putExtra("address_name", viewModel.shipAddressName.get())
            startActivityForResult(intent, 101)
        }

        mBinding.tvReceiveAddress.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.putExtra("city", viewModel.city)
            intent.putExtra("address_name", viewModel.receiveAddressName.get())
            startActivityForResult(intent, 102)
        }

        mBinding.gpPersonal.setOnClickListener {

        }
    }

    private inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            mLocationClient.stop()
            val locType = bdLocation!!.locType
            when (locType) {
                BDLocation.TypeGpsLocation,
                BDLocation.TypeOffLineLocation,
                BDLocation.TypeNetWorkLocation -> {
                    viewModel.city = bdLocation.city
                    val location = LatLng(bdLocation.latitude, bdLocation.longitude)
                    mMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(location, 18f))

                    Handler().postDelayed({
                        val point = Point()
                        point.x = mIvLocationInWindow[0]
                        point.y = mIvLocationInWindow[1]
                        val location = mMap.projection.fromScreenLocation(point)
                        MapUtils.animateMapStatus(mMap, location)
                    }, 100)

                    val locationData = MyLocationData.Builder()
                            .accuracy(bdLocation.radius)
                            .direction(bdLocation.direction)
                            .latitude(bdLocation.latitude)
                            .longitude(bdLocation.longitude)
                            .build()
                    mMap.setMyLocationData(locationData)
                    val configuration = MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                            false, null, Color.TRANSPARENT, Color.TRANSPARENT)
                    mMap.setMyLocationConfiguration(configuration)
                }
                else -> {
                    ToastUtils.show("无法获取您的定位")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101 && data != null) {
            val address = data.getStringExtra("address")
            val addressName = data.getStringExtra("address_name")
            viewModel.city = data.getStringExtra("city")
            val location = data.getParcelableExtra("lat_lng") as LatLng
            when (requestCode) {
                101 -> {
                    mMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(location, 18f))
                    Handler().postDelayed({
                        val point = Point()
                        point.x = mIvLocationInWindow[0]
                        point.y = mIvLocationInWindow[1]
                        val location = mMap.projection.fromScreenLocation(point)
                        mMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location))
                    }, 100)
                    viewModel.shipAddressName.set(addressName)
                    viewModel.shipAddress = address
                    viewModel.shipLocation = location
                }
                102 -> {
                    viewModel.receiveAddressName.set(addressName)
                    viewModel.receiveAddress = address
                    viewModel.receiveLocation = location
                    viewModel.loadDictionaryList().observe(this, Observer {
                        if (it != null) {
                            val intent = Intent(this, PlaceAnOrderActivity::class.java)
                            intent.putExtra("data", it)
                            startActivity(intent)
                        }
                    })
                }
            }
        }
    }
}
