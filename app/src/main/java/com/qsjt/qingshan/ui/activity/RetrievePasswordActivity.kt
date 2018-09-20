package com.qsjt.qingshan.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.qsjt.qingshan.R
import com.qsjt.qingshan.databinding.ActivityRetrievePasswordBinding
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.ToastUtils
import com.qsjt.qingshan.utils.ToolbarUtils
import com.qsjt.qingshan.utils.Utils
import com.qsjt.qingshan.viewmodel.RetrievePasswordActivityViewModel
import kotlinx.android.synthetic.main.activity_retrieve_password.*

class RetrievePasswordActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(RetrievePasswordActivityViewModel::class.java) }

    private lateinit var mBinding: ActivityRetrievePasswordBinding

    private val mCountDownTimer by lazy {
        object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                mBinding.tvGetVerificationCode.text = "获取验证码"
                mBinding.tvGetVerificationCode.isClickable = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mBinding.tvGetVerificationCode.text = String.format("%dS", millisUntilFinished / 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retrieve_password)
        mBinding.viewModel = viewModel
        mBinding.setLifecycleOwner(this)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer.cancel()
    }

    private fun initView() {
        ToolbarUtils(this, this.in_toolbar)
                .setDisplayHomeAsUpEnabled()
                .setTitle("找回密码")

        val mTypeface = Typeface.createFromAsset(assets, "iconfont.ttf")
        mBinding.tvPhoneNumber.typeface = mTypeface
        mBinding.tvVerificationCode.typeface = mTypeface
        mBinding.tvPassword1.typeface = mTypeface
        mBinding.tvPassword2.typeface = mTypeface
        mBinding.tvPasswordVisibility.typeface = mTypeface

        mBinding.tvGetVerificationCode.setOnClickListener {
            if (!Utils.isMobileNumber(viewModel.phoneNumber)) {
                ToastUtils.show("请输入正确的手机号码")
                return@setOnClickListener
            }
            viewModel.getVerificationCode().observe(this, Observer {
                if (it!!) {
                    mCountDownTimer.start()
                    mBinding.tvGetVerificationCode.isClickable = false
                }
            })
        }

        mBinding.tvPasswordVisibility.setOnClickListener {
            mBinding.tvPasswordVisibility.text = if (mBinding.etPassword1.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                mBinding.etPassword1.transformationMethod = PasswordTransformationMethod.getInstance()
                mBinding.etPassword2.transformationMethod = PasswordTransformationMethod.getInstance()
                resources.getString(R.string.if_password_invisible)
            } else {
                mBinding.etPassword1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                mBinding.etPassword2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                resources.getString(R.string.if_password_visible)
            }
        }

        mBinding.btnResetPassword.setOnClickListener {
            viewModel.resetPassword().observe(this, Observer {
                if (it!!) {
                    onBackPressed()
                }
            })
        }
    }
}
