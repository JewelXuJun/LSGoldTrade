package com.jme.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ToastUtils {

    private static Toast instance = null;

    public static void setToast(Context context, String msg) {
        if (instance == null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else {
            instance.setText(msg);
        }
    }
}
