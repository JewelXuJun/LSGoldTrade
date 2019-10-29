package com.jme.lsgoldtrade.ui.security;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.ihsg.patternlocker.CellBean;
import com.github.ihsg.patternlocker.INormalCellView;

import org.jetbrains.annotations.NotNull;

public class GestureNormalCellView implements INormalCellView {

    private int mDefaultColor;
    private float mDivide;

    public void setDefaultColor(int color) {
        mDefaultColor = color;
    }

    public void setDivide(float divide) {
        mDivide = divide;
    }

    @Override
    public void draw(@NotNull Canvas canvas, @NotNull CellBean cellBean) {
        int saveCount = canvas.save();

        Paint paint = new Paint();
        paint.setColor(mDefaultColor);

        canvas.drawCircle(cellBean.getX(), cellBean.getY(), cellBean.getRadius() / mDivide, paint);
        canvas.restoreToCount(saveCount);
    }
}
