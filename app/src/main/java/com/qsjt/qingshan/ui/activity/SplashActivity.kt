package com.qsjt.qingshan.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.qsjt.qingshan.R
import com.qsjt.qingshan.application.MyApplication
import com.qsjt.qingshan.listener.OnRequestPermissionsListener
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.Utils

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        Utils.setFullScreen(this)
        setContentView(R.layout.activity_splash)

        requestRunTimePermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        ), object : OnRequestPermissionsListener {
            override fun onGranted() {
                if (MyApplication.getMyApplication().isShowSplash) {
                    MyApplication.getMyApplication().isShowSplash = false
                    Handler().postDelayed({
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        onBackPressed()
                    }, 1000)
                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    onBackPressed()
                }
            }

            override fun onDenied(deniedPermissions: MutableList<String>?) {
                onBackPressed()
            }
        })
    }
}
