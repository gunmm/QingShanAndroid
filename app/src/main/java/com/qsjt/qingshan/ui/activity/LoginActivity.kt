package com.qsjt.qingshan.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.qsjt.qingshan.R
import com.qsjt.qingshan.databinding.ActivityLoginBinding
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.LogUtils
import com.qsjt.qingshan.viewmodel.LoginActivityViewModel

class LoginActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginActivityViewModel::class.java) }

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        if (TextUtils.isEmpty(mConfig.userId)) {
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
            mBinding.viewModel = viewModel
            mBinding.setLifecycleOwner(this)

            initView()
        } else {
            jump()
        }
    }

    private fun initView() {
        mBinding.etPhoneNumber.setOnFocusChangeListener { v, hasFocus ->
            mBinding.ivClearPhone.visibility = if (hasFocus && !TextUtils.isEmpty(viewModel.phoneNumber)) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
        mBinding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.ivClearPhone.visibility = if (s!!.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        })
        mBinding.ivClearPhone.setOnClickListener {
            mBinding.etPhoneNumber.text = null
        }
    }

    fun login(v: View) {
        viewModel.login().observe(this, Observer {
            if (it!!) {
                jump()
            }
        })
    }

    private fun jump() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        onBackPressed()
    }

    fun retrievePassword(v: View) {
        val intent = Intent(this, RetrievePasswordActivity::class.java)
        startActivity(intent)
    }

    override fun filterView(): Array<View>? {
        return arrayOf(mBinding.ivClearPhone, mBinding.etPhoneNumber, mBinding.etPassword)
    }
}
