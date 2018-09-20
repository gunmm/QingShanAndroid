package com.qsjt.qingshan.ui.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import cn.jpush.android.api.JPushInterface
import com.google.gson.Gson
import com.qsjt.qingshan.application.MyApplication
import com.qsjt.qingshan.config.Config
import com.qsjt.qingshan.constant.Constants
import com.qsjt.qingshan.http.BaseObserver
import com.qsjt.qingshan.listener.OnJPushReceivedListener
import com.qsjt.qingshan.listener.OnRequestPermissionsListener
import com.qsjt.qingshan.model.RequestHead
import com.qsjt.qingshan.model.response.NotifyMessage
import com.qsjt.qingshan.receiver.JPushNotifyReceiver
import com.qsjt.qingshan.utils.KeyBoardUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


/**
 * @author LiYouGui.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    /**
     * Config
     */
    val mConfig: Config by lazy {
        Config.getInstance()
    }

    /**
     * JPush消息监听器
     */
    var onJPushReceivedListener: OnJPushReceivedListener? = null

    /**
     * 权限申请监听器
     */
    private lateinit var onRequestPermissionsListener: OnRequestPermissionsListener


    override fun onResume() {
        super.onResume()
        //设置JPush监听回调
        JPushNotifyReceiver.setOnJPushReceivedListener { message ->
            if (onJPushReceivedListener != null) {
                onJPushReceivedListener!!.onJPReceived(message)
            }
            showMsgDialog(message)
        }
    }

    /**
     * 设置竖屏
     */
    fun setOrientationPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 跳转到登录页面
     */
    private fun jump2Login() {
        clearUserInfo()

//        val intent = Intent(this, LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
    }

    /**
     * 清除用户信息
     */
    private fun clearUserInfo() {
        JPushInterface.deleteAlias(this, Constants.JP_ALIAS_SEQUENCE)
        mConfig.accessToken = null
        mConfig.userId = null
    }

    /**
     * 应用内推送消息弹窗
     */
    private fun showMsgDialog(message: NotifyMessage?) {


    }

    /**
     * 注册JPush 设置JPush别名
     */
    fun registerJPush() {
        val alias = mConfig.accessToken.replace("-", "_")
        JPushInterface.setAlias(this, Constants.JP_ALIAS_SEQUENCE, alias)
    }

//--------------------------------------------------------------------------------------------------
//动态申请权限
    /**
     * 申请权限授权
     */
    fun requestRunTimePermissions(permissions: Array<String>, listener: OnRequestPermissionsListener) {
        onRequestPermissionsListener = listener
        val permissionsList = ArrayList<String>()
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(it)
            }
        }
        if (permissionsList.isEmpty()) {
            listener.onGranted()
        } else {
            ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), Constants.PERMISSIONS_REQUEST_CODE)
        }
    }

    /**
     * 申请权限结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val deniedPermissions = java.util.ArrayList<String>()
                permissions.forEachIndexed { index, s ->
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(s)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    onRequestPermissionsListener.onGranted()
                } else {
                    onRequestPermissionsListener.onDenied(deniedPermissions)
                }
            }
        }
    }
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
//非输入框区域隐藏软键盘
    /**
     * 点击非输入框区域隐藏软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterView(), ev)) {
                return super.dispatchTouchEvent(ev)
            }
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                KeyBoardUtils.hideInputForce(this)
                v!!.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 是否应该隐藏软键盘
     */
    private fun isShouldHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val location = IntArray(2)
            v.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val right = left + v.width
            val bottom = top + v.height
            return !(ev.x > left && ev.x < right
                    && ev.y > top && ev.y < bottom)
        }
        return false
    }

    /**
     * 是否触摸在指定View上
     */
    private fun isTouchView(views: Array<View>?, ev: MotionEvent): Boolean {
        if (views == null || views.isEmpty()) {
            return false
        }
        val location = IntArray(2)
        for (v in views) {
            v.getLocationOnScreen(location)
            val left = location[0]
            val top = location[1]
            val right = left + v.width
            val bottom = top + v.height
            if (ev.x > left && ev.x < right
                    && ev.y > top && ev.y < bottom) {
                return true
            }
        }
        return false
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return View数组
     */
    open fun filterView(): Array<View>? {
        return null
    }
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
}