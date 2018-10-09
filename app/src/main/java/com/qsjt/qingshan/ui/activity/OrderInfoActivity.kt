package com.qsjt.qingshan.ui.activity

import android.os.Bundle
import com.qsjt.qingshan.R
import com.qsjt.qingshan.ui.base.BaseActivity

class OrderInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        setContentView(R.layout.activity_order_info)

        initView()
    }

    private fun initView() {


    }
}
