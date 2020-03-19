package com.jme.lsgoldtrade.ui.security;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.ihsg.patternlocker.CellBean;
import com.github.ihsg.patternlocker.IHitCellView;

import org.jetbrains.annotations.NotNull;

public class GestureHitCellView implements IHitCellView {

    private int mHitColor;
    private boolean isIndicatorView = false;

    public void setHitColor(int hitColor) {
        mHitColor = hitColor;
    }

    public void setIndicatorView(boolean indicatorView) {
        isIndicatorView = indicatorView;
    }

    @Override
    public void draw(@NotNull Canvas canvas, @NotNull CellBean cellBean, boolean flag) {
        int saveCount = canvas.save();

        Paint paint = new Paint();

        if (isIndicatorView) {
            paint.setColor(mHitColor);

            canvas.drawCircle(cellBean.getX(), cellBean.getY(), cellBean.getRadius() / 1.5f, paint);
        } else {
            paint.setColor(mHitColor);
            paint.setAlpha(50);
            canvas.drawCircle(cellBean.getX(), cellBean.getY(), cellBean.getRadius() * 2f / 3f, paint);

            paint.setColor(mHitColor);
            paint.setAlpha(255);
            canvas.drawCircle(cellBean.getX(), cellBean.getY(), cellBean.getRadius() / 3f, paint);
        }

        canvas.restoreToCount(saveCount);
    }
}
