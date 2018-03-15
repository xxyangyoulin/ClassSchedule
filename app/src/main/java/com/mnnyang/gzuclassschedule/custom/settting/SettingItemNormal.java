package com.mnnyang.gzuclassschedule.custom.settting;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

/**
 * 设置选项
 * TODO 优化switch点击效果
 * Created by xxyangyoulin on 2018/3/13.
 */

public class SettingItemNormal extends LinearLayout {

    private TextView tvTitle;
    private TextView tvSummary;
    private SwitchCompat switchCompat;
    private boolean detach;

    public interface SettingOnClickListener {
        void onClick(View view, boolean checked);

        void onCheckedChanged(View view, boolean checked);
    }

    public void setSettingOnClickListener(SettingOnClickListener settingOnClickListener) {
        this.settingOnClickListener = settingOnClickListener;
    }

    private SettingOnClickListener settingOnClickListener;


    public SettingItemNormal(Context context) {
        super(context);
        init();
    }

    public SettingItemNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingItemNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemNormal);
        String title = typedArray.getString(R.styleable.SettingItemNormal_item_title);
        String summary = typedArray.getString(R.styleable.SettingItemNormal_summary);
        boolean checked = typedArray.getBoolean(R.styleable.SettingItemNormal_checked, false);
        boolean showSwitch = typedArray.getBoolean(R.styleable.SettingItemNormal_showSwitch, false);
        detach = typedArray.getBoolean(R.styleable.SettingItemNormal_detach, false);

        typedArray.recycle();

        init();


        setItemTitle(title);
        setSummary(summary);


        switchCompat.setChecked(checked);
        switchCompat.setVisibility(showSwitch ? VISIBLE : GONE);

        initEvent();
    }

    private void initEvent() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(this, v + "  clicked");

                if (detach) {
                    switchCompat.setChecked(!switchCompat.isChecked());
                }

                if (settingOnClickListener != null) {
                    settingOnClickListener.onClick(v, switchCompat.isChecked());
                }
            }
        });

        switchCompat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingOnClickListener != null) {
                    settingOnClickListener.onCheckedChanged(SettingItemNormal.this,
                            switchCompat.isChecked());
                }
            }
        });
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_setting_item, this)
                .findViewById(R.id.layout);

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_summary);
        switchCompat = findViewById(R.id.a_switch);
    }

    public void setItemTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(GONE);
        } else {
            if (tvTitle.getVisibility() == GONE) {
                tvTitle.setVisibility(VISIBLE);
            }
            tvTitle.setText(title);
        }
    }

    public void setSummary(String summary) {
        if (TextUtils.isEmpty(summary)) {
            tvSummary.setVisibility(GONE);
        } else {
            if (tvSummary.getVisibility() == GONE) {
                tvSummary.setVisibility(VISIBLE);
            }
            tvSummary.setText(summary);
        }

    }

    public void showSwitch(boolean show) {
        switchCompat.setVisibility(show ? VISIBLE : GONE);
    }

    public void setChecked(boolean checked) {
        switchCompat.setChecked(checked);
    }

    public boolean isChecked() {
        return switchCompat.isChecked();
    }
}
