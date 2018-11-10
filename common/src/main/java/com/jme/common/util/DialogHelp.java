package com.jme.common.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jme.common.R;

import java.util.HashMap;


/**
 * 对话框辅助类
 * Created by zhangzhongqiang on 16/3/22.
 */
public class DialogHelp {


    /***
     * 获取一个dialog
     *
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String title, String message, String positiveButtonStr, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonStr, onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String title, String message) {
        return getMessageDialog(context, title, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String message, String positiveButtonStr,
                                                       DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonStr, onOkClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String message, String positiveButtonStr, String negativeButtonStr,
                                                       DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonStr, onOkClickListener);
        builder.setNegativeButton(negativeButtonStr, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, SpannableString[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    public static AlertDialog.Builder getBtnCommitDialog(String type, Activity activity, String title, String commodityName, String price, String quantity, String bsflag, DialogInterface.OnClickListener onClickListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_open_close_order, (ViewGroup) activity.findViewById(R.id.dialog));
        TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        TextView tv_quantity = (TextView) dialog.findViewById(R.id.tv_quantity);
        TextView tv_bsflag = (TextView) dialog.findViewById(R.id.tv_bsflag);

        tv_name.setText(commodityName);
        tv_price.setText(price);
        tv_quantity.setText(quantity);
        if (type.equals("sjjc")) {
            if (bsflag.equals("1")) {
                tv_bsflag.setText("建仓买入");
            } else {
                tv_bsflag.setText("建仓卖出");
            }
        } else if (type.equals("zjjc")) {
            if (bsflag.equals("1")) {
                tv_bsflag.setText("指价买入");
            } else {
                tv_bsflag.setText("指价卖出");
            }
        } else if (type.equals("sjpc")) {
            if (bsflag.equals("1")) {
                tv_bsflag.setText("平仓买入");
            } else {
                tv_bsflag.setText("平仓卖出");
            }
        }

        AlertDialog.Builder builder = getDialog(activity);
        builder.setTitle(title);
        builder.setView(dialog);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);
        return builder;
    }

    public static AlertDialog.Builder getPuerDealItemDialog(Activity activity, String title, HashMap<String, Object> hashMap) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_puer_deal_item, null);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        TextView tv_code = (TextView) dialog.findViewById(R.id.tv_code);
        TextView tv_order_number = (TextView) dialog.findViewById(R.id.tv_order_number);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        TextView tv_amount = (TextView) dialog.findViewById(R.id.tv_amount);
        TextView tv_total_price = (TextView) dialog.findViewById(R.id.tv_total_price);
        TextView tv_trade_amount = (TextView) dialog.findViewById(R.id.tv_trade_amount);
        TextView tv_time = (TextView) dialog.findViewById(R.id.tv_time);
        TextView tv_trade_number = (TextView) dialog.findViewById(R.id.tv_trade_number);
        TextView tv_attribute = (TextView) dialog.findViewById(R.id.tv_attribute);
        LinearLayout layout_trade_amount = (LinearLayout) dialog.findViewById(R.id.layout_trade_amount);

        tv_dialog_title.setText(title);
        tv_name.setText(hashMap.get("Name").toString());
        tv_code.setText(hashMap.get("Code").toString());
        tv_order_number.setText(hashMap.get("OrderNumber").toString());
        tv_price.setText(hashMap.get("Price").toString());
        tv_amount.setText(hashMap.get("Amount").toString());
        tv_total_price.setText(hashMap.get("TotalPrice").toString());
        tv_time.setText(hashMap.get("Time").toString());

        if (hashMap.get("CounterFee") == null) {
            layout_trade_amount.setVisibility(View.GONE);
        } else {
            layout_trade_amount.setVisibility(View.VISIBLE);
            tv_trade_amount.setText(hashMap.get("CounterFee").toString());
        }

        tv_trade_number.setText(hashMap.get("TradeNumber").toString());
        tv_attribute.setText(hashMap.get("Attribute").toString());

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);

        return builder;
    }

    public static AlertDialog.Builder getPuerEntrustItemDialog(Activity activity, String title, HashMap<String, Object> hashMap) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_puer_entrust_item, null);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        TextView tv_code = (TextView) dialog.findViewById(R.id.tv_code);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        TextView tv_amount = (TextView) dialog.findViewById(R.id.tv_amount);
        TextView tv_time = (TextView) dialog.findViewById(R.id.tv_time);
        TextView tv_number = (TextView) dialog.findViewById(R.id.tv_number);
        TextView tv_total_price = (TextView) dialog.findViewById(R.id.tv_total_price);
        TextView tv_trade_amount = (TextView) dialog.findViewById(R.id.tv_trade_amount);
        TextView tv_attribute = (TextView) dialog.findViewById(R.id.tv_attribute);
        TextView tv_prop = (TextView) dialog.findViewById(R.id.tv_prop);

        tv_dialog_title.setText(title);
        tv_name.setText(hashMap.get("Name").toString());
        tv_code.setText(hashMap.get("Code").toString());
        tv_price.setText(hashMap.get("EntrustPrice").toString());
        tv_amount.setText(hashMap.get("EntrustAmount").toString());
        tv_total_price.setText(hashMap.get("DealPrice").toString());
        tv_trade_amount.setText(hashMap.get("DealAmount").toString());
        tv_time.setText(hashMap.get("Time").toString());
        tv_number.setText(hashMap.get("Number").toString());
        tv_attribute.setText(hashMap.get("Prop").toString());
        tv_prop.setText(hashMap.get("Status").toString());

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);

        return builder;
    }

    public static AlertDialog.Builder getPuerTradeInfoDialog(Activity activity, String title, HashMap<String, String> hashMap,
                                                             DialogInterface.OnClickListener onClickListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_puer_trade_info, null);
        TextView tv_dialog_code = (TextView) dialog.findViewById(R.id.tv_dialog_code);
        TextView tv_dialog_name = (TextView) dialog.findViewById(R.id.tv_dialog_name);
        TextView tv_dialog_price = (TextView) dialog.findViewById(R.id.tv_dialog_price);
        TextView tv_dialog_amount = (TextView) dialog.findViewById(R.id.tv_dialog_amount);
        TextView tv_dialog_total_price = (TextView) dialog.findViewById(R.id.tv_dialog_total_price);
        TextView tv_dialog_type = (TextView) dialog.findViewById(R.id.tv_dialog_type);

        tv_dialog_code.setText(hashMap.get("Code"));
        tv_dialog_name.setText(hashMap.get("Name"));
        tv_dialog_price.setText(hashMap.get("Price"));
        tv_dialog_amount.setText(hashMap.get("Amount"));
        tv_dialog_total_price.setText(hashMap.get("TotalPrice"));
        tv_dialog_type.setText(hashMap.get("Type"));

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);

        return builder;
    }

    public static AlertDialog.Builder getPuerPickInfoDialog(Activity activity, String title, HashMap<String, String> hashMap,
                                                             DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onCancleListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_puer_pick_info, null);
        TextView tv_dialog_name = (TextView) dialog.findViewById(R.id.tv_dialog_name);
        TextView tv_dialog_price = (TextView) dialog.findViewById(R.id.tv_dialog_price);
        TextView tv_dialog_amount = (TextView) dialog.findViewById(R.id.tv_dialog_amount);
        TextView tv_dialog_total_price = (TextView) dialog.findViewById(R.id.tv_dialog_total_price);

        tv_dialog_name.setText(hashMap.get("Name"));
        tv_dialog_price.setText(hashMap.get("Price"));
        tv_dialog_amount.setText(hashMap.get("Amount"));
        tv_dialog_total_price.setText(hashMap.get("TotalPrice"));

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);
        builder.setNegativeButton(R.string.dialog_common_cancle, onCancleListener);

        return builder;
    }

    public static AlertDialog.Builder getPuerPickResultInfoDialog(Activity activity, String title, HashMap<String, SpannableString> hashMap,
                                                            DialogInterface.OnClickListener onClickListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_puer_pick_result_info, null);
        TextView tv_dialog_name = (TextView) dialog.findViewById(R.id.tv_dialog_name);
        TextView tv_dialog_price = (TextView) dialog.findViewById(R.id.tv_dialog_price);
        TextView tv_dialog_amount = (TextView) dialog.findViewById(R.id.tv_dialog_amount);
        TextView tv_dialog_total_price = (TextView) dialog.findViewById(R.id.tv_dialog_total_price);

        tv_dialog_name.setText(hashMap.get("Name"));
        tv_dialog_price.setText(hashMap.get("Price"));
        tv_dialog_amount.setText(hashMap.get("Amount"));
        tv_dialog_total_price.setText(hashMap.get("TotalPrice"));

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);

        return builder;
    }

    public static AlertDialog.Builder getInOutTradeDialog(Activity activity, String title, HashMap<String, String> hashMap,
                                                          DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onCancleListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_inout_trade, null);
        LinearLayout layout_bankname = (LinearLayout) dialog.findViewById(R.id.layout_bankname);
        LinearLayout layout_card = (LinearLayout) dialog.findViewById(R.id.layout_card);
        TextView tv_inout_dialog_type = (TextView) dialog.findViewById(R.id.tv_inout_dialog_type);
        TextView tv_inout_dialog_bankname = (TextView) dialog.findViewById(R.id.tv_inout_dialog_bankname);
        TextView tv_inout_dialog_card = (TextView) dialog.findViewById(R.id.tv_inout_dialog_card);
        TextView tv_inout_dialog_money = (TextView) dialog.findViewById(R.id.tv_inout_dialog_money);

        tv_inout_dialog_type.setText(hashMap.get("Type").toString());
        if (hashMap.get("BankName") != null) {
            tv_inout_dialog_bankname.setText(hashMap.get("BankName").toString());
            layout_bankname.setVisibility(View.VISIBLE);
        } else {
            layout_bankname.setVisibility(View.GONE);
        }

        if (hashMap.get("Card") != null) {
            tv_inout_dialog_card.setText(hashMap.get("Card").toString());
            layout_card.setVisibility(View.VISIBLE);
        } else {
            layout_card.setVisibility(View.GONE);
        }

        tv_inout_dialog_money.setText(hashMap.get("Money").toString());

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);
        builder.setNegativeButton(R.string.dialog_common_cancle, onCancleListener);

        return builder;
    }

    public static AlertDialog.Builder getSpotOrderInfoItemDialog(Activity activity, String title, HashMap<String, Object> hashMap) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_spot_order_item, null);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        TextView tv_code = (TextView) dialog.findViewById(R.id.tv_code);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        TextView tv_amount = (TextView) dialog.findViewById(R.id.tv_amount);
        TextView tv_time = (TextView) dialog.findViewById(R.id.tv_time);
        TextView tv_number = (TextView) dialog.findViewById(R.id.tv_number);
//        TextView tv_total_price = (TextView) dialog.findViewById(R.id.tv_total_price);
        TextView tv_trade_amount = (TextView) dialog.findViewById(R.id.tv_trade_amount);
        TextView tv_attribute = (TextView) dialog.findViewById(R.id.tv_attribute);
        TextView tv_prop = (TextView) dialog.findViewById(R.id.tv_prop);
        TextView tv_tradeMarkName = (TextView) dialog.findViewById(R.id.tv_tradeMarkName);
        TextView tv_typeName = (TextView) dialog.findViewById(R.id.tv_typeName);
        TextView tv_freezeMargin = (TextView) dialog.findViewById(R.id.tv_freezeMargin);
        TextView tv_freezefees = (TextView) dialog.findViewById(R.id.tv_freezefees);

        tv_dialog_title.setText(title);
        tv_name.setText(hashMap.get("name").toString());
        tv_code.setText(hashMap.get("commodityId").toString());
        tv_tradeMarkName.setText(hashMap.get("tradeMarkName").toString());
        tv_typeName.setText(hashMap.get("typeName").toString());
        tv_price.setText(hashMap.get("orderPrice").toString());
        tv_amount.setText(hashMap.get("quantity").toString());
        tv_freezeMargin.setText(hashMap.get("freezeMargin").toString());
        tv_freezefees.setText(hashMap.get("freezefees").toString());
//        tv_total_price.setText(hashMap.get("DealPrice").toString());
        tv_trade_amount.setText(hashMap.get("tradedqty").toString());
        tv_time.setText(hashMap.get("orderTime").toString());
        tv_number.setText(hashMap.get("orderId").toString());
        tv_attribute.setText(hashMap.get("orderTypeName").toString());
        tv_prop.setText(hashMap.get("orderStateName").toString());

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);

        return builder;
    }

    public static AlertDialog.Builder getRedwineTradeInfoDialog(Activity activity, String title, HashMap<String, String> hashMap,
                                                                DialogInterface.OnClickListener onClickListener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialog = inflater.inflate(R.layout.dialog_redwine_trade_info, null);
        TextView tv_dialog_code = (TextView) dialog.findViewById(R.id.tv_dialog_code);
        TextView tv_dialog_name = (TextView) dialog.findViewById(R.id.tv_dialog_name);
        TextView tv_dialog_price = (TextView) dialog.findViewById(R.id.tv_dialog_price);
        TextView tv_dialog_amount = (TextView) dialog.findViewById(R.id.tv_dialog_amount);
        TextView tv_dialog_type = (TextView) dialog.findViewById(R.id.tv_dialog_type);
        TextView tv_dialog_firm = (TextView) dialog.findViewById(R.id.tv_dialog_firmid);

        tv_dialog_code.setText(hashMap.get("code"));
        tv_dialog_name.setText(hashMap.get("name"));
        tv_dialog_price.setText(hashMap.get("price"));
        tv_dialog_amount.setText(hashMap.get("amount"));
        tv_dialog_type.setText(hashMap.get("type"));
//        tv_dialog_firm.setText(hashMap.get("otherfirmid"));

        AlertDialog.Builder builder = getDialog(activity);
        builder.setView(dialog);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_common_confirm, onClickListener);
        builder.setNegativeButton(R.string.dialog_common_cancle, null);

        return builder;
    }

}
