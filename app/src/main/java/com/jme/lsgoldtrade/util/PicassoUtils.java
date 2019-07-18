package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoUtils {

    public static PicassoUtils instance = null;

    private PicassoUtils() {}

    public static synchronized PicassoUtils getInstance() {
        if (instance == null) {
            synchronized (PicassoUtils.class) {
                if (instance == null) {
                    instance = new PicassoUtils();
                }
            }
        }
        return instance;
    }

    public void loadImg(Context context, String img, View view) {
        Picasso.with(context).load(img).into((ImageView) view);
    }
}
