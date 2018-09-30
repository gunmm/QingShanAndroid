package com.qsjt.qingshan.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.chad.library.adapter.base.BaseViewHolder
import com.qsjt.qingshan.R
import com.qsjt.qingshan.adapter.BindingAdapter
import com.qsjt.qingshan.constant.Constants
import com.qsjt.qingshan.databinding.ActivitySearchAddressBinding
import com.qsjt.qingshan.databinding.RecycleItemCityBinding
import com.qsjt.qingshan.databinding.RecycleItemSimpleListItem2Binding
import com.qsjt.qingshan.ui.base.BaseActivity
import com.qsjt.qingshan.utils.LogUtils
import com.qsjt.qingshan.viewmodel.SearchAddressActivityViewModel

class SearchAddressActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchAddressActivityViewModel::class.java) }

    private lateinit var mBinding: ActivitySearchAddressBinding

    private val mAdapter by lazy {
        object : BindingAdapter<PoiInfo>(R.layout.recycle_item_simple_list_item_2, null) {
            override fun convert(helper: BaseViewHolder?, item: PoiInfo?) {
                val mBinding = DataBindingUtil.getBinding<RecycleItemSimpleListItem2Binding>(helper!!.itemView)
                mBinding!!.data = item
                mBinding.executePendingBindings()
                mBinding.root.setOnClickListener {
                    val intent = Intent()
                    intent.putExtra("address", if ("北京市" == item!!.province ||
                            "天津市" == item!!.province ||
                            "上海市" == item!!.province ||
                            "重庆市" == item!!.province) {
                        item.city + item.area
                    } else {
                        item.province + item.city + item.area
                    })
                    intent.putExtra("address_name", item.name)
                    intent.putExtra("lat_lng", item.location)
                    intent.putExtra("city", item.city)
                    setResult(101, intent)
                    onBackPressed()
                }
            }
        }
    }

    private val cityList = Constants.CITY_LIST.split(",".toRegex())

    private val filterCityList by lazy { ArrayList<String>() }

    private val mCityAdapter by lazy {
        object : BindingAdapter<String>(R.layout.recycle_item_city, null) {
            @SuppressLint("SetTextI18n")
            override fun convert(helper: BaseViewHolder?, item: String?) {
                val mBinding = DataBindingUtil.getBinding<RecycleItemCityBinding>(helper!!.itemView)
                mBinding!!.data = item
                mBinding.executePendingBindings()
                mBinding.root.setOnClickListener {
                    this@SearchAddressActivity.mBinding.etAddress.text = null
                    viewModel.city = "${item!!}市"
                    tvCity.text = "${item}市"
                    mCityPopupWindow.dismiss()
                }
            }
        }
    }

    private lateinit var tvCity: TextView

    private val mCityPopupWindow by lazy { cityPopupWindow() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientationPortrait()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_address)
        mBinding.vm = viewModel
        mBinding.setLifecycleOwner(this)

        initView()
    }

    private fun initView() {
        val city = intent.getStringExtra("city")
        viewModel.city = if (TextUtils.isEmpty(city)) {
            "北京"
        } else {
            city
        }
        val addressName = intent.getStringExtra("address_name")
        viewModel.addressName = addressName

        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

        viewModel.poiSearch()
        viewModel.poiData.observe(this, Observer {
            mBinding.refreshLayout.finishLoadMore()
            if (it == null) {
                mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                mBinding.refreshLayout.setNoMoreData(false)
                if (viewModel.pageNum == 0) {
                    mAdapter.replaceData(it)
                } else {
                    mAdapter.addData(it)
                }
            }
        })

        val rv = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv.adapter = mAdapter
        mAdapter.bindToRecyclerView(rv)
//        mAdapter.setEmptyView(R.layout.recycle_empty_search_address)

        val headerView = View.inflate(this, R.layout.recycle_header_select_city, null)
        tvCity = headerView.findViewById(R.id.tv_city)
        tvCity.text = viewModel.city
        headerView.setOnClickListener {
            mCityPopupWindow.showAtLocation(mBinding.root, Gravity.NO_GRAVITY, 0, 0)
        }
        mAdapter.setHeaderView(headerView)

        mBinding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.pageNum = 0
                viewModel.addressName = s!!.toString()
                viewModel.poiSearch()
            }
        })

        mBinding.refreshLayout.setOnLoadMoreListener {
            viewModel.poiSearch()
        }
    }

    private fun cityPopupWindow(): PopupWindow {
        val view = View.inflate(this, R.layout.popup_select_city, null)
        val pw = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)
        pw.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pw.animationStyle = R.style.PopupAnimBottomToTop
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            pw.dismiss()
        }
        val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
        mCityAdapter.replaceData(cityList)
        rv.adapter = mCityAdapter
        mCityAdapter.bindToRecyclerView(rv)
        view.findViewById<EditText>(R.id.et_city).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    mCityAdapter.replaceData(cityList)
                    return
                }
                filterCityList.clear()
                cityList.forEach {
                    if (it.contains(s.toString())) {
                        filterCityList.add(it)
                    }
                }
                mCityAdapter.replaceData(filterCityList)
            }
        })
        return pw
    }
}
