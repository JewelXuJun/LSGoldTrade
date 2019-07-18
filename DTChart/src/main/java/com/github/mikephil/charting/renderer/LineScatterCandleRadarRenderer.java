package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
public abstract class LineScatterCandleRadarRenderer extends DataRenderer {

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param pts the transformed x- and y-position of the lines
     * @param set the currently drawn dataset
     */
    protected void drawHighlightLines(Canvas c, float[] pts, LineScatterCandleRadarDataSet set) {

        // set color and stroke-width
        mHighlightTextBgPaint.setColor(set.getHighLightColor());
        mHighlightTextBgPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightTextBgPaint.setPathEffect(set.getDashPathEffectHighlight());

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(pts[0], mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(pts[0], mViewPortHandler.contentBottom());

            //Modify by yanmin: 画HighLight的线改成DrawLine，因为用drawPath在BarChart不显示，原因不明
            // c.drawPath(mHighlightLinePath, mHighlightPaint);
            c.drawLine(pts[0], mViewPortHandler.contentTop(), pts[0], mViewPortHandler.contentBottom(), mHighlightTextBgPaint);
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            if (pts[1] >= mViewPortHandler.contentTop()
                    && pts[1] <= mViewPortHandler.contentBottom()) {
                // create horizontal path
                mHighlightLinePath.reset();
                mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
                mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

                //Modify by yanmin: 画HighLight的线改成DrawLine，因为用drawPath在BarChart不显示，原因不明
                // c.drawPath(mHighlightLinePath, mHighlightPaint);
                c.drawLine(mViewPortHandler.contentLeft(), pts[1], mViewPortHandler.contentRight(), pts[1], mHighlightTextBgPaint);
            }
        }

        // Modify by yanmin： 焦点位置画一个圆心
        if (set.isVerticalHighlightIndicatorEnabled()
                && set.isHorizontalHighlightIndicatorEnabled()
                && !set.isDrawHorizontalHighlightInTouchPoint()) {
            c.drawCircle(pts[0], pts[1], 5, mHighlightTextBgPaint);
        }
    }

    //Modify By yanmin: 画当前HighLight对应的Left-Y值
    protected void drawHorizontalHighlightText(Canvas c, float yVal, float x, float y, LineScatterCandleRadarDataSet set) {
        String valueStr = set.getHighlightValueFormatter().getFormattedValue
                (yVal, null);
        Rect rect = new Rect();
        mHighlightPaint.getTextBounds(valueStr, 0, valueStr.length(), rect);

        float x_position = x + 6;
        float y_position = y - 6;

        if (y_position <= rect.height() + 6)
            y_position = rect.height() + 6;

        rect.left += x_position - 4;
        rect.right += x_position + 4;
        rect.top += y_position - 3;
        rect.bottom += y_position + 3;
        c.drawRoundRect(new RectF(rect), 6, 6, mHighlightTextBgPaint);
        c.drawText(valueStr, x_position, y_position, mHighlightPaint);
    }

    //Modify By yanmin: 画当前HighLight对应的X值
    protected void drawVerticalHighlightText(Canvas c, String xValStr, float x, float y, float width, LineScatterCandleRadarDataSet set) {
        Rect rect = new Rect();
        mHighlightPaint.getTextBounds(xValStr, 0, xValStr.length(), rect);

        float x_position = x - rect.width() / 2;
        float y_position = y + rect.height() + 5;

        if (x_position < 3)
            x_position = 3;

        if (x_position > width - rect.width() - 3)
            x_position = width - rect.width() - 3;

        rect.left += x_position - 5;
        rect.right += x_position + 5;
        rect.top += y_position - 3;
        rect.bottom += y_position + 3;
        c.drawRoundRect(new RectF(rect), 6, 6, mHighlightTextBgPaint);
        c.drawText(xValStr, x_position, y_position, mHighlightPaint);
    }
}
