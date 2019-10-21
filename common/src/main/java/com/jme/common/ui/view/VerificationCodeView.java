package com.jme.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jme.common.R;
import com.jme.common.util.ScreenUtil;

public class VerificationCodeView extends RelativeLayout {

    private LinearLayout containerEt;

    private EditText et;
    private Drawable mEtDividerDrawable;
    private Drawable mEtBackgroundDrawable;

    private int mEtNumber;
    private int mEtHeight;
    private int mEtTextColor;
    private float mEtTextSize;

    private TextView[] mTextViews;

    private EdittextTextWatcher mEdittextTextWatcher = new EdittextTextWatcher();

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_identifying_code, this);
        containerEt = this.findViewById(R.id.container_et);
        et = this.findViewById(R.id.et);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyleAttr, 0);
        mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeView_icv_et_number, 1);
        mEtHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_icv_et_height, (int) dp2px(50, context));
        mEtDividerDrawable = typedArray.getDrawable(R.styleable.VerificationCodeView_icv_et_divider_drawable);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_icv_et_text_size, (int) sp2px(16, context));
        mEtTextColor = typedArray.getColor(R.styleable.VerificationCodeView_icv_et_text_color, Color.BLACK);
        mEtBackgroundDrawable = typedArray.getDrawable(R.styleable.VerificationCodeView_icv_et_bg);

        typedArray.recycle();

        initUI();
    }

    private void initUI() {
        initTextViews(getContext(), mEtNumber, mEtHeight, mEtDividerDrawable, mEtTextSize, mEtTextColor);
        initEtContainer(mTextViews);
        setListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeightMeasureSpec = heightMeasureSpec;

        int heightMode = MeasureSpec.getMode(mHeightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) dp2px(50, getContext()), MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
    }

    private void initTextViews(Context context, int etNumber, int etHeight, Drawable etDividerDrawable, float etTextSize, int etTextColor) {
        et.setCursorVisible(false);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etNumber)});

        if (etDividerDrawable != null) {
            etDividerDrawable.setBounds(0, 0, etDividerDrawable.getMinimumWidth(), etDividerDrawable.getMinimumHeight());
            containerEt.setDividerDrawable(etDividerDrawable);
        }

        mTextViews = new TextView[etNumber];
        for (int i = 0; i < mTextViews.length; i++) {
            TextView textView = new TextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, etTextSize);
            textView.setTextColor(etTextColor);
            textView.setWidth((int) dp2px(40, context));
            textView.setHeight(etHeight);
            textView.setBackground(mEtBackgroundDrawable);
            textView.setGravity(Gravity.CENTER);
            textView.setFocusable(false);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) dp2px(5, context), 0, (int) dp2px(5, context), 0);
            textView.setLayoutParams(layoutParams);

            mTextViews[i] = textView;
        }
    }

    private void initEtContainer(TextView[] mTextViews) {
        for (int i = 0; i < mTextViews.length; i++) {
            containerEt.addView(mTextViews[i]);
        }
    }

    private void setListener() {
        et.addTextChangedListener(mEdittextTextWatcher);
        et.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDelete();
                    return true;
                }
                return false;
            }
        });
    }

    private void setText(String inputContent) {
        for (int i = 0; i < mTextViews.length; i++) {
            TextView tv = mTextViews[i];

            if (tv.getText().toString().trim().equals("")) {
                tv.setText(inputContent);
                if (inputCompleteListener != null)
                    inputCompleteListener.inputComplete();

                break;
            }
        }
    }

    private void onKeyDelete() {
        for (int i = mTextViews.length - 1; i >= 0; i--) {
            TextView tv = mTextViews[i];

            if (!tv.getText().toString().trim().equals("")) {
                tv.setText("");
                if (inputCompleteListener != null)
                    inputCompleteListener.deleteContent();

                break;
            }
        }
    }

    public String getInputContent() {
        StringBuffer buffer = new StringBuffer();

        for (TextView tv : mTextViews) {
            buffer.append(tv.getText().toString().trim());
        }
        return buffer.toString();
    }

    public void clearInputContent() {
        for (int i = 0; i < mTextViews.length; i++) {
            mTextViews[i].setText("");
        }
    }

    public void setEtNumber(int etNumber) {
        this.mEtNumber = etNumber;
        et.removeTextChangedListener(mEdittextTextWatcher);
        containerEt.removeAllViews();
        initUI();
    }

    public int getEtNumber() {
        return mEtNumber;
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete();

        void deleteContent();
    }


    public float dp2px(float dpValue, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public float sp2px(float spValue, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

    private class EdittextTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String inputStr = editable.toString();

            if (inputStr != null && !inputStr.equals("")) {
                setText(inputStr);
                et.setText("");
            }
        }
    }


}
