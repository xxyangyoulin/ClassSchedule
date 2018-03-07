package com.mnnyang.gzuclassschedule.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.R;

/**
 * Created by mnnyang on 17-11-5.
 */

public class EditTextLayout extends LinearLayout {

    private EditText mEtText;
    private ImageView mIvIcon;
    private ImageView mIvClear;
    private String mHint;

    public EditTextLayout(Context context) {
        super(context);
        init();
    }

    public EditTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public EditTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextLayout);
        mHint = typedArray.getString(R.styleable.EditTextLayout_hint);
        String text = typedArray.getString(R.styleable.EditTextLayout_text);
        Drawable icon = typedArray.getDrawable(R.styleable.EditTextLayout_icon);
        Boolean inputEnabled = typedArray.getBoolean(R.styleable.EditTextLayout_input_enabled, true);
        int textColor = typedArray.getColor(R.styleable.EditTextLayout_textColor, Color.BLACK);
        int hintColor = typedArray.getColor(R.styleable.EditTextLayout_hintColor, Color.GRAY);

        typedArray.recycle();

        init();
        setHint(mHint);
        setText(text);
        setIcon(icon);
        setTextColor(textColor);
        setHintColor(hintColor);
        setInputEnabled(inputEnabled);
    }

    private void setHintColor(int color) {
        mEtText.setHintTextColor(color);
    }

    private void setTextColor(int color) {
        mEtText.setTextColor(color);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_edit_text, this);
        mEtText = findViewById(R.id.et_text);
        mIvIcon = findViewById(R.id.iv_icon);
        mIvClear = findViewById(R.id.iv_clear);

        mIvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtText.setText("");
            }
        });

        mEtText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mIvClear.setVisibility(hasFocus ? VISIBLE : INVISIBLE);
                mEtText.setHint(hasFocus?"":mHint);
            }
        });
    }

    /**
     *
     * @param inputType definition of EditorInfo class
     */
    public EditTextLayout setInputType(int inputType){
        mEtText.setInputType(inputType);
        return this;
    }

    public EditTextLayout setInputEnabled(boolean enabled) {
        mEtText.setFocusable(enabled);
        return this;
    }

    public EditTextLayout setHint(String hint) {
        mHint = hint;
        mEtText.setHint(hint);
        return this;
    }

    public EditTextLayout setText(String text) {
        mEtText.setText(text);
        return this;
    }

    public String getText() {
        return mEtText.getText().toString();
    }

    public EditTextLayout setIcon(Drawable icon) {
        if (icon != null) {
            mIvIcon.setImageDrawable(icon);
        }
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(l);
        mEtText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onClick(EditTextLayout.this);
                }
            }
        });
    }
}
