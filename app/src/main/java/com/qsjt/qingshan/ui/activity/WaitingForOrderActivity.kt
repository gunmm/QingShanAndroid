package com.qsjt.qingshan.ui.activity

import android.os.Bundle
import com.qsjt.qingshan.R
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.ToolbarUtils
import kotlinx.android.synthetic.main.activity_waiting_for_order.*

class WaitingForOrderActivity : BaseActivity() {


    private val mMap by lazy { this.mapView.map }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_order)

        initView()
    }

    private fun initView() {
        ToolbarUtils(this, this.in_toolbar)
                .setDisplayHomeAsUpEnabled()
                .setTitle("等待接单")
                .setRightText("取消订单") {

                }


    }
}
