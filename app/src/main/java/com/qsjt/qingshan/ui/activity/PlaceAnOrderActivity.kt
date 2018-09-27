package com.qsjt.qingshan.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import com.baidu.mapapi.model.LatLng
import com.qsjt.qingshan.R
import com.qsjt.qingshan.adapter.SpinnerAdapter
import com.qsjt.qingshan.adapter.ViewHolder
import com.qsjt.qingshan.databinding.ActivityPlaceAnOrderBinding
import com.qsjt.qingshan.databinding.PopupInvoiceBinding
import com.qsjt.qingshan.databinding.PopupPaymentBinding
import com.qsjt.qingshan.databinding.PopupPriceDetailBinding
import com.qsjt.qingshan.model.response.Dictionary
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.MapUtils
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.ToolbarUtils
import com.qsjt.qingshan.utils.Utils
import com.qsjt.qingshan.viewmodel.PlaceAnOrderActivityViewModel

class PlaceAnOrderActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(PlaceAnOrderActivityViewModel::class.java) }

    private lateinit var mBinding: ActivityPlaceAnOrderBinding

    private val mVehicleSpinnerAdapter by lazy {
        object : SpinnerAdapter<Dictionary>(this,
                R.layout.recycle_item_simple_list_item_1, viewModel.data!!["vehicleDictionary"] as List<Dictionary>) {
            override fun convert(helper: ViewHolder?, item: Dictionary?) {
                helper!!.getView<TextView>(R.id.text1).text = item!!.description
            }
        }
    }

    private val mInvoicePopupWindow by lazy { invoicePopupWindow() }

    private val mPricePopupWindow by lazy { pricePopupWindow() }

    private val mMap by lazy { mBinding.mapView.map }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_an_order)
        mBinding.vm = viewModel
        mBinding.setLifecycleOwner(this)

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

    private fun initView() {
        ToolbarUtils(this, mBinding.inToolbar)
                .setDisplayHomeAsUpEnabled()
                .setTitle("下单")

        val data = intent.getSerializableExtra("data") as HashMap<*, *>
        viewModel.data = data

        val orderType = data["orderType"].toString()
        mBinding.llSubscribeTime.visibility = if ("1" == orderType) {
            View.GONE
        } else {
            mBinding.tvSubscribeTime.setOnClickListener {
                Utils.chooseDataTime(this, mBinding.tvSubscribeTime)
            }
            View.VISIBLE
        }

        initMap(data)

        mBinding.spVehicleType.adapter = mVehicleSpinnerAdapter
        mBinding.spVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = mVehicleSpinnerAdapter.getItem(position)
                setPriceDetail(item)
            }
        }

        viewModel.routePlan()
        viewModel.drivingRouteLine.observe(this, Observer {
            if (it != null) {
                viewModel.distance.set((it.distance / 1000).toDouble())
                val item = mVehicleSpinnerAdapter.getItem(0)
                setPriceDetail(item)
            }
        })

        mBinding.tvInvoice.setOnClickListener {
            showInvoiceDialog()
        }

        mBinding.tvPriceDetail.setOnClickListener {
            mPricePopupWindow.showAtLocation(mBinding.root, Gravity.BOTTOM, 0, 0)
            Utils.setBackgroundAlpha(this, 0.5f)
        }

        mBinding.btnPlaceOrder.setOnClickListener {
            if (!mBinding.checkBox.isChecked) {
                ToastUtils.show("请先同意协议")
                return@setOnClickListener
            }
            if (viewModel.checkFillIn()) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("提示")
                dialog.setMessage("请核对好所选择的车辆类型的\n运载量 和 容量\n下单后不可修改")
                dialog.setNegativeButton("重新选择", null)
                dialog.setPositiveButton("继续下单") { dialog, which ->
                    if (mBinding.tvInvoice.isSelected) {
                        showPaymentPopupWindow()
                    } else {
                        viewModel.placeOrder().observe(this, Observer {
                            jump2WaitingForOrder(it)
                        })
                    }
                }
                dialog.create().show()
            }
        }
    }

    private fun initMap(data: HashMap<*, *>) {
        mBinding.mapView.showZoomControls(false)
        mBinding.mapView.showScaleControl(false)
        MapUtils.hideMapLogo(mBinding.mapView)
        val uiSettings = mMap.uiSettings
        uiSettings.isOverlookingGesturesEnabled = false

        mMap.setOnMapTouchListener {
            mBinding.scrollView.requestDisallowInterceptTouchEvent(it.action != MotionEvent.ACTION_UP)
        }

        val shipLocation = data["shipLocation"] as LatLng
        val shipAddressName = data["shipAddressName"].toString()
        val shipMarker = MapUtils.initMarkerOptions(this, shipAddressName, R.drawable.ic_begin_marker, 30)
        shipMarker.position(shipLocation)
        mMap.addOverlay(shipMarker)

        val receiveLocation = data["receiveLocation"] as LatLng
        val receiveAddressName = data["receiveAddressName"].toString()
        val receiveMarker = MapUtils.initMarkerOptions(this, receiveAddressName, R.drawable.ic_end_marker, 30)
        receiveMarker.position(receiveLocation)
        mMap.addOverlay(receiveMarker)

        MapUtils.setZoomAndPoint(mMap, shipLocation, receiveLocation)
    }

    private fun showInvoiceDialog() {
        if (mBinding.tvInvoice.isSelected) {
            mInvoicePopupWindow.showAtLocation(mBinding.root, Gravity.BOTTOM, 0, 0)
            Utils.setBackgroundAlpha(this, 0.5f)
        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("提示")
            dialog.setMessage("需要发票\n必须预先线上支付运输费用")
            dialog.setNegativeButton("取消", null)
            dialog.setPositiveButton("确定") { dialog, which ->
                mInvoicePopupWindow.showAtLocation(mBinding.root, Gravity.BOTTOM, 0, 0)
                Utils.setBackgroundAlpha(this, 0.5f)
            }
            dialog.create().show()
        }
    }

    private fun invoicePopupWindow(): PopupWindow {
        val mBinding = DataBindingUtil
                .inflate<PopupInvoiceBinding>(layoutInflater,
                        R.layout.popup_invoice, null, false)
        mBinding.vm = viewModel
        val pw = PopupWindow(mBinding.root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        pw.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pw.animationStyle = R.style.PopupAnimBottomToTop
        pw.setOnDismissListener {
            Utils.setBackgroundAlpha(this, 1f)
        }
        mBinding.tvPersonal.isSelected = true
        mBinding.tvPersonal.setOnClickListener {
            viewModel.invoiceType = "1"
            mBinding.tvPersonal.isSelected = true
            mBinding.tvUnit.isSelected = false
            if (mBinding.gpUnitEt.visibility != View.GONE) {
                mBinding.gpUnitEt.visibility = View.GONE
            }
        }
        mBinding.tvUnit.setOnClickListener {
            viewModel.invoiceType = "2"
            mBinding.tvPersonal.isSelected = false
            mBinding.tvUnit.isSelected = true
            if (mBinding.gpUnitEt.visibility != View.VISIBLE) {
                mBinding.gpUnitEt.visibility = View.VISIBLE
            }
        }
        mBinding.tvCancel.setOnClickListener {
            viewModel.companyName.set(null)
            viewModel.companyNumber.set(null)
            viewModel.receiverName.set(null)
            viewModel.receiverPhone.set(null)
            viewModel.receiverAddress.set(null)
            PlaceAnOrderActivity@ this.mBinding.tvInvoice.isSelected = false
            PlaceAnOrderActivity@ this.mBinding.tvInvoice.text = "发票"
            pw.dismiss()
        }
        mBinding.btnConfirm.setOnClickListener {
            if ("2" == viewModel.invoiceType) {
                if (TextUtils.isEmpty(viewModel.companyName.get())) {
                    ToastUtils.show("公司名称不能为空")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(viewModel.companyNumber.get())) {
                    ToastUtils.show("纳税人识别号不能为空")
                    return@setOnClickListener
                }
            }
            if (TextUtils.isEmpty(viewModel.receiverName.get())) {
                ToastUtils.show("收票人姓名不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(viewModel.receiverPhone.get())) {
                ToastUtils.show("收票人电话不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(viewModel.receiverAddress.get())) {
                ToastUtils.show("收票人地址不能为空")
                return@setOnClickListener
            }
            PlaceAnOrderActivity@ this.mBinding.tvInvoice.isSelected = true
            PlaceAnOrderActivity@ this.mBinding.tvInvoice.text = "已选发票"
            pw.dismiss()
        }
        return pw
    }

    private fun pricePopupWindow(): PopupWindow {
        val mBinding = DataBindingUtil
                .inflate<PopupPriceDetailBinding>(layoutInflater,
                        R.layout.popup_price_detail, null, false)
        mBinding.vm = viewModel
        val pw = PopupWindow(mBinding.root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pw.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pw.animationStyle = R.style.PopupAnimBottomToTop
        pw.setOnDismissListener {
            Utils.setBackgroundAlpha(this, 1f)
        }
        mBinding.tvCancel.setOnClickListener {
            pw.dismiss()
        }
        return pw
    }

    private fun setPriceDetail(item: Dictionary?) {
        if (item == null) {
            return
        }
        viewModel.orderParam.put("carType", item.keyText)
        viewModel.vehicleType.set(item.valueText)
        viewModel.startPrice.set(item.startPrice)
        if (viewModel.distance.get() == null) {
            return
        }
        if (viewModel.distance.get()!! > item.startDistance) {
            val exceedMileage = viewModel.distance.get()!! - item.startDistance
            viewModel.exceedMileage.set(exceedMileage)
            viewModel.exceedMileagePrice.set(exceedMileage * item.unitPrice)
            viewModel.price.set(item.startPrice + viewModel.exceedMileagePrice.get()!!)
        } else {
            viewModel.exceedMileage.set(0.0)
            viewModel.exceedMileagePrice.set(0.0)
            viewModel.price.set(item.startPrice)
        }
    }

    private fun showPaymentPopupWindow() {
        val mBinding = DataBindingUtil
                .inflate<PopupPaymentBinding>(layoutInflater,
                        R.layout.popup_payment, null, false)
        mBinding.vm = viewModel
        val pw = PopupWindow(mBinding.root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pw.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pw.animationStyle = R.style.PopupAnimBottomToTop
        pw.setOnDismissListener {
            Utils.setBackgroundAlpha(this, 1f)
        }
        mBinding.btnPayment.setOnClickListener {
            viewModel.freightFeePayType = if (mBinding.radioGroup.checkedRadioButtonId == R.id.rb_alipay) {
                "1"
            } else {
                "2"
            }
            viewModel.placeOrder().observe(this, Observer {
                jump2WaitingForOrder(it)
            })
            pw.dismiss()
        }
        pw.showAtLocation(this.mBinding.root, Gravity.BOTTOM, 0, 0)
        Utils.setBackgroundAlpha(this, 0.5f)
    }

    private fun jump2WaitingForOrder(it: String?) {
        if (!TextUtils.isEmpty(it)) {
            val intent = Intent(this, WaitingForOrderActivity::class.java)
            intent.putExtra("order_id", it)
            startActivity(intent)
            onBackPressed()
        }
    }
}
