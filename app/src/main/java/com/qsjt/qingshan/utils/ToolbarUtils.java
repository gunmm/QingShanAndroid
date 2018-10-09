package com.qsjt.qingshan.utils;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qsjt.qingshan.R;

/**
 * @author LiYouGui.
 */
public class ToolbarUtils {

    private final ToolbarUtils instance;

    private final AppCompatActivity activity;

    private final View view;

    private final SparseArray<View> mViews;

    public ToolbarUtils(final AppCompatActivity activity, View view) {
        instance = this;
        this.activity = activity;
        this.view = view;
        this.mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View v = mViews.get(viewId);
        if (v == null) {
            v = view.findViewById(viewId);
            mViews.put(viewId, v);
        }
        return (T) v;
    }

    public ToolbarUtils setDisplayHomeAsUpEnabled() {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        return instance;
    }

    public ToolbarUtils showMessageTip(boolean b) {
        ImageView ivMessageTip = view.findViewById(R.id.iv_message_tip);
        if (b) {
            ivMessageTip.setVisibility(View.VISIBLE);
        } else {
            ivMessageTip.setVisibility(View.GONE);
        }
        return instance;
    }

    public ToolbarUtils setTitle(String title) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        return instance;
    }

    public ToolbarUtils setLeftText(String text, View.OnClickListener listener) {
        TextView tvLeft = view.findViewById(R.id.tv_left);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(text);
        tvLeft.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setLeftText(Typeface typeface, String text, float size, View.OnClickListener listener) {
        TextView tvLeft = view.findViewById(R.id.tv_left);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setTypeface(typeface);
        tvLeft.setText(text);
        tvLeft.setTextSize(size);
        tvLeft.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setLeftIcon(int resId, View.OnClickListener listener) {
        AppCompatImageView ivLeft = view.findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(resId);
        ivLeft.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setRightText(String text, View.OnClickListener listener) {
        TextView tvRight = view.findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
        tvRight.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setRightText(Typeface typeface, String text, float size, View.OnClickListener listener) {
        TextView tvRight = view.findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTypeface(typeface);
        tvRight.setText(text);
        tvRight.setTextSize(size);
        tvRight.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setRightIcon(int resId, View.OnClickListener listener) {
        AppCompatImageView ivRight = view.findViewById(R.id.iv_right);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
        ivRight.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setRight2Icon(int resId, View.OnClickListener listener) {
        AppCompatImageView ivRight = view.findViewById(R.id.iv_right2);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
        ivRight.setOnClickListener(listener);
        return instance;
    }

    public ToolbarUtils setTab(String leftText, String rightText, final OnTabSelectedListener listener) {
        view.findViewById(R.id.ll_tab).setVisibility(View.VISIBLE);
        final TextView tvTabLeft = view.findViewById(R.id.tv_tab_left);
        tvTabLeft.setSelected(true);
        tvTabLeft.setText(leftText);
        final TextView tvTabRight = view.findViewById(R.id.tv_tab_right);
        tvTabRight.setText(rightText);
        tvTabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTabLeft.setSelected(true);
                tvTabRight.setSelected(false);
                listener.onTabSelected(0);
            }
        });
        tvTabRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTabRight.setSelected(true);
                tvTabLeft.setSelected(false);
                listener.onTabSelected(1);
            }
        });
        return instance;
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int tabIndex);
    }
}