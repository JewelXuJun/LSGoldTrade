package com.jme.common.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jme.common.R;

public class SearchView extends LinearLayout implements View.OnClickListener {
    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }

    /**
     * 输入框
     */
    private EditText et_input;

    /**
     * 删除键
     */
    private ImageView iv_delete;

    /**
     * 返回按钮
     */
    private Button btn_search;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 弹出列表
     */
    private ListView lv_tips;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_search_view, this);

        et_input = (EditText) findViewById(R.id.et_input);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        btn_search = (Button) findViewById(R.id.btn_search);
        lv_tips = (ListView) findViewById(R.id.lv_tips);

        lv_tips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lv_tips.getAdapter().getItem(i).toString();
                et_input.setText(text);
                et_input.setSelection(text.length());
                //hint list view gone and result list view show
                lv_tips.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        iv_delete.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        et_input.addTextChangedListener(new EditChangedListener());
        et_input.setOnClickListener(this);
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lv_tips.setVisibility(GONE);
                    notifyStartSearching(et_input.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     *
     * @param text
     */
    private void notifyStartSearching(String text) {
        if (mListener != null) {
            mListener.onSearch(et_input.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lv_tips.getAdapter() == null) {
            lv_tips.setAdapter(mHintAdapter);
        }
    }

    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    public void setHintText(String hitText) {
        et_input.setHint(hitText);
    }

    public void setSearchButton(String text) {
        btn_search.setVisibility(View.VISIBLE);
        btn_search.setText(text);
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                iv_delete.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lv_tips.getAdapter() != mAutoCompleteAdapter) {
                    lv_tips.setVisibility(VISIBLE);
                    lv_tips.setAdapter(mAutoCompleteAdapter);
                }
            } else {
                iv_delete.setVisibility(GONE);
                if (mHintAdapter != null) {
                    lv_tips.setAdapter(mHintAdapter);
                }
                lv_tips.setVisibility(GONE);
            }
            //更新autoComplete数据
            if (mListener != null) {
                mListener.onRefreshAutoComplete(charSequence + "");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_input) {
        } else if (id == R.id.iv_delete) {
            et_input.setText("");
            iv_delete.setVisibility(GONE);
        } else if (id == R.id.btn_search) {
            lv_tips.setVisibility(GONE);
            notifyStartSearching(et_input.getText().toString());
        }
    }
}