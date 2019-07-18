package com.jme.lsgoldtrade.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;

public class PayPopupwindow {

    public static void popupwindow(Activity context, int view) {
        final View popup = LayoutInflater.from(context).inflate(R.layout.popup_pay_with_ticket, null);
        ImageView iv_cancel_pay = (ImageView) popup.findViewById(R.id.iv_cancel_pay);
        RelativeLayout rl_gonghang = (RelativeLayout) popup.findViewById(R.id.rl_gonghang);
        RelativeLayout rl_zhifubao = (RelativeLayout) popup.findViewById(R.id.rl_zhifubao);
        RelativeLayout rl_weixin = (RelativeLayout) popup.findViewById(R.id.rl_weixin);
        final TextView tv_now_pay = (TextView) popup.findViewById(R.id.tv_now_pay);

        final PopupWindow window = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        iv_cancel_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        rl_gonghang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_now_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 实例化试图
        //设置SelectWindow的View
        window.setContentView(popup);
        //设置SelectWindow弹出窗体的宽
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectWindow弹出窗体的高
        window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        window.showAtLocation(context.findViewById(view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 设置layout在popupwindow中显示的位置
        //设置SelectWindow弹窗的可点击
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popup.findViewById(R.id.ll_pay).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        window.dismiss();
                    }
                }
                return true;
            }
        });
    }
}
