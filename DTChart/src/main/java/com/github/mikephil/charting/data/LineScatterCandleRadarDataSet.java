package com.github.mikephil.charting.data;

import android.graphics.DashPathEffect;

import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.List;

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
public abstract class LineScatterCandleRadarDataSet<T extends Entry> extends BarLineScatterCandleBubbleDataSet<T> {

    protected boolean mDrawVerticalHighlightIndicator = true;
    protected boolean mDrawHorizontalHighlightIndicator = true;

    /**
     * the width of the highlight indicator lines
     */
    protected float mHighlightLineWidth = 0.5f;

    /**
     * the path effect for dashed highlight-lines
     */
    protected DashPathEffect mHighlightDashPathEffect = null;

    // Add By Yanmin: Highlight Value formatter that is used instead of the auto-formatter if set .
    protected YAxisValueFormatter mHighlightValueFormatter;
    // Add By Yanmin: 如果为true：Highlight水平线在手指按压的位置，false：则Highlight水平线处于所选Entry的Y值线上
    protected boolean mDrawHorizontalHighlightInTouchPoint;

    public LineScatterCandleRadarDataSet(List<T> yVals, String label) {
        super(yVals, label);
        mHighlightLineWidth = Utils.convertDpToPixel(0.5f);
    }

    /**
     * Enables / disables the horizontal highlight-indicator. If disabled, the indicator is not drawn.
     *
     * @param enabled
     */
    public void setDrawHorizontalHighlightIndicator(boolean enabled) {
        this.mDrawHorizontalHighlightIndicator = enabled;
    }

    /**
     * Enables / disables the vertical highlight-indicator. If disabled, the indicator is not drawn.
     *
     * @param enabled
     */
    public void setDrawVerticalHighlightIndicator(boolean enabled) {
        this.mDrawVerticalHighlightIndicator = enabled;
    }

    /**
     * Enables / disables both vertical and horizontal highlight-indicators.
     *
     * @param enabled
     */
    public void setDrawHighlightIndicators(boolean enabled) {
        setDrawVerticalHighlightIndicator(enabled);
        setDrawHorizontalHighlightIndicator(enabled);
    }

    public boolean isVerticalHighlightIndicatorEnabled() {
        return mDrawVerticalHighlightIndicator;
    }

    public boolean isHorizontalHighlightIndicatorEnabled() {
        return mDrawHorizontalHighlightIndicator;
    }

    /**
     * Sets the width of the highlight line in dp.
     *
     * @param width
     */
    public void setHighlightLineWidth(float width) {
        mHighlightLineWidth = Utils.convertDpToPixel(width);
    }

    /**
     * Returns the line-width in which highlight lines are to be drawn.
     *
     * @return
     */
    public float getHighlightLineWidth() {
        return mHighlightLineWidth;
    }

    /**
     * Enables the highlight-line to be drawn in dashed mode, e.g. like this "- - - - - -"
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space inbetween the line-pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableDashedHighlightLine(float lineLength, float spaceLength, float phase) {
        mHighlightDashPathEffect = new DashPathEffect(new float[]{
                lineLength, spaceLength
        }, phase);
    }

    /**
     * Disables the highlight-line to be drawn in dashed mode.
     */
    public void disableDashedHighlightLine() {
        mHighlightDashPathEffect = null;
    }

    /**
     * Returns true if the dashed-line effect is enabled for highlight lines, false if not.
     * Default: disabled
     *
     * @return
     */
    public boolean isDashedHighlightLineEnabled() {
        return mHighlightDashPathEffect == null ? false : true;
    }

    public DashPathEffect getDashPathEffectHighlight() {
        return mHighlightDashPathEffect;
    }

    /**
     * Sets the formatter to be used for formatting the Highlight labels. If no formatter is set, the chart will
     * automatically determine a reasonable formatting (concerning decimals) for all the values that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter calculated by the chart.
     *
     * @param f
     */
    public void setHighlightValueFormatter(YAxisValueFormatter f) {

        if (f == null)
            mHighlightValueFormatter = new DefaultYAxisValueFormatter(2);
        else
            mHighlightValueFormatter = f;
    }

    /**
     * Returns the formatter used for formatting the Highlight labels.
     *
     * @return
     */
    public YAxisValueFormatter getHighlightValueFormatter() {

        if (mHighlightValueFormatter == null)
            mHighlightValueFormatter = new DefaultYAxisValueFormatter(2);

        return mHighlightValueFormatter;
    }

    public void setDrawHorizontalHighlightInTouchPoint(boolean enable) {
        mDrawHorizontalHighlightInTouchPoint = enable;
    }

    public boolean isDrawHorizontalHighlightInTouchPoint() {
        return mDrawHorizontalHighlightInTouchPoint;
    }
}
