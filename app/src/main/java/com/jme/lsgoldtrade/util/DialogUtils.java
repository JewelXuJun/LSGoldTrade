package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jme.lsgoldtrade.R;

/**
 * Created by Administrator on 2018/4/18.
 */

public class DialogUtils {

    public SetAlertDialogListener listener;

    public interface SetAlertDialogListener {

        void onPositive();

        void onNegative();
    }

    public static void alertDialog(Context context, String positive, String negative, String title, String content, final SetAlertDialogListener listener) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .positiveColor(context.getResources().getColor(R.color.color_0080ff))
                .negativeText(negative)
                .negativeColor(context.getResources().getColor(R.color.color_333))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onPositive();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                })
                .show();
    }

    public static void alertTitleDialog(Context context, String positive, String negative, String title, final SetAlertDialogListener listener) {
        new MaterialDialog.Builder(context)
                .title(title)
                .positiveText(positive)
                .positiveColor(context.getResources().getColor(R.color.color_0080ff))
                .negativeText(negative)
                .negativeColor(context.getResources().getColor(R.color.color_333))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onPositive();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                })
                .show();
    }

    public static void alertContentDialog(Context context, String positive, String negative, String content, final SetAlertDialogListener listener) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(positive)
                .positiveColor(context.getResources().getColor(R.color.color_0080ff))
                .negativeText(negative)
                .negativeColor(context.getResources().getColor(R.color.color_333))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onPositive();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                })
                .show();
    }
}
