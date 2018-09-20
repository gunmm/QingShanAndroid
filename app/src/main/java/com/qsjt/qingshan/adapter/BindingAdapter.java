package com.qsjt.qingshan.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LiYouGui.
 */
public abstract class BindingAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;
        if (getMultiTypeDelegate() != null) {
            layoutId = getMultiTypeDelegate().getLayoutId(viewType);
        }
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutId, parent, false);
        return createBaseViewHolder(binding.getRoot());
    }
}
