package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;

/**
 * Created by Administrator on 2016/10/26
 */
public class ShareSelectWindow extends PopupWindow implements View.OnClickListener {

    private CheckBox cb_yingliqian, cb_kuisunqian, cb_yinglidian, cb_kunsundian;
    private RelativeLayout yingliPriceMinus, yingliPriceAdd, kuiSunPriceMinus, kuiSunPriceAdd, yingLiNumMinus, yingLiNumAdd, kuiSunNumMinus, kuiSunNumAdd;
    private Button btn_cancel, btn_sure;
    private EditText yinglipingcangxianjiage, kuisunpingcangxianjiage, yinglipingcangxiandianshu, kuisunpingcangxiandianshu;
    private ImageView jizhi, iv_cancel;
    private TextView price, num;
    private View mMenuView;
    private Context context;
    private String id;
    private String intro;
    private String title;
    private ProgressBar progress_bar;
    private String type;
    private String last_name;
    private String first_wen = "?from=android";
    private int platForm = 0;
    private String url = "";
    private String urlsix;
    private String contentTag;
    private String contentType;
    private String platFormName = "";

    public ShareSelectWindow(Context context) {
        this.context = context;
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflate.inflate(R.layout.alert_dialog_share, null);

        cb_yingliqian = mMenuView.findViewById(R.id.cb_yingliqian);
        cb_kuisunqian = mMenuView.findViewById(R.id.cb_kuisunqian);
        cb_yinglidian = mMenuView.findViewById(R.id.cb_yinglidian);
        cb_kunsundian = mMenuView.findViewById(R.id.cb_kunsundian);
        price = mMenuView.findViewById(R.id.price);
        num = mMenuView.findViewById(R.id.num);
        yingliPriceMinus = mMenuView.findViewById(R.id.yingliPriceMinus);
        yingliPriceAdd = mMenuView.findViewById(R.id.yingliPriceAdd);
        kuiSunPriceMinus = mMenuView.findViewById(R.id.kuiSunPriceMinus);
        btn_cancel = mMenuView.findViewById(R.id.btn_cancel);
        btn_sure = mMenuView.findViewById(R.id.btn_sure);
        kuiSunPriceAdd = mMenuView.findViewById(R.id.kuiSunPriceAdd);
        yingLiNumMinus = mMenuView.findViewById(R.id.yingLiNumMinus);
        jizhi = mMenuView.findViewById(R.id.jizhi);
        yingLiNumAdd = mMenuView.findViewById(R.id.yingLiNumAdd);
        iv_cancel = mMenuView.findViewById(R.id.iv_cancel);
        kuiSunNumMinus = mMenuView.findViewById(R.id.kuiSunNumMinus);
        kuiSunNumAdd = mMenuView.findViewById(R.id.kuiSunNumAdd);
        yinglipingcangxianjiage = mMenuView.findViewById(R.id.yinglipingcangxianjiage);
        kuisunpingcangxianjiage = mMenuView.findViewById(R.id.kuisunpingcangxianjiage);
        yinglipingcangxiandianshu = mMenuView.findViewById(R.id.yinglipingcangxiandianshu);
        kuisunpingcangxiandianshu = mMenuView.findViewById(R.id.kuisunpingcangxiandianshu);

        //给按钮设置点击监听
        price.setOnClickListener(this);
        num.setOnClickListener(this);
        yingliPriceMinus.setOnClickListener(this);
        yingliPriceAdd.setOnClickListener(this);
        kuiSunPriceMinus.setOnClickListener(this);
        kuiSunPriceAdd.setOnClickListener(this);
        yingLiNumMinus.setOnClickListener(this);
        yingLiNumAdd.setOnClickListener(this);
        kuiSunNumMinus.setOnClickListener(this);
        kuiSunNumAdd.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        jizhi.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        //设置SelectWindow的View
        this.setContentView(mMenuView);
        //设置SelectWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectWindow弹窗的可点击
        this.setFocusable(true);
        // 设置popWindow的显示和消失动画
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable cd = new ColorDrawable(0xb0000000);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#88000000"));
        //设置SelectWindow弹窗的背景
        this.setBackgroundDrawable(cd);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.price:

                break;
            case R.id.num:

                break;
            case R.id.yingliPriceMinus:

                break;
            case R.id.yingliPriceAdd:

                break;
            case R.id.kuiSunPriceMinus:

                break;
            case R.id.kuiSunPriceAdd:

                break;
            case R.id.yingLiNumMinus:

                break;
            case R.id.yingLiNumAdd:

                break;
            case R.id.kuiSunNumMinus:

                break;
            case R.id.kuiSunNumAdd:

                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                dismiss();
                break;
            case R.id.jizhi:

                break;
        }
    }

    private GetRewardsListener listener;

    public interface GetRewardsListener {
        void setListener();
    }

    public void setOnGetRewardsListener(GetRewardsListener listener) {
        this.listener = listener;
    }
}
