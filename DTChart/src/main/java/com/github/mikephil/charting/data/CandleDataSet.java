
package com.github.mikephil.charting.data;

import android.graphics.Paint;

import androidx.annotation.ColorInt;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * DataSet for the CandleStickChart.
 * 
 * @author Philipp Jahoda
 */
public class CandleDataSet extends LineScatterCandleRadarDataSet<CandleEntry> {

    /** the width of the shadow of the candle */
    private float mShadowWidth = 3f;

    /** the space between the candle entries, default 0.1f (10%) */
    private float mBodySpace = 0.1f;

    /** use candle color for the shadow */
    private boolean mShadowColorSameAsCandle = false;

    /** paint style when open <= close */
    protected Paint.Style mIncreasingPaintStyle = Paint.Style.FILL;

    /** paint style when open > close */
    protected Paint.Style mDecreasingPaintStyle = Paint.Style.STROKE;

    /** 蜡烛图的颜色是使用In/DecreasingColor(默认),还是使用DataSet的Colors -- by yanmin*/
    protected boolean mUseDataSetColor = false;

    /** color for open <= close */
    protected int mIncreasingColor = ColorTemplate.COLOR_NONE;

    /** color for open > close */
    protected int mDecreasingColor = ColorTemplate.COLOR_NONE;

    /**
     * shadow line color, set -1 for backward compatibility and uses default
     * color
     */
    protected int mShadowColor = ColorTemplate.COLOR_NONE;

    public CandleDataSet(List<CandleEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public DataSet<CandleEntry> copy() {

        List<CandleEntry> yVals = new ArrayList<CandleEntry>();

        for (int i = 0; i < mYVals.size(); i++) {
            yVals.add(((CandleEntry) mYVals.get(i)).copy());
        }

        CandleDataSet copied = new CandleDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mShadowWidth = mShadowWidth;
        copied.mBodySpace = mBodySpace;
        copied.mHighLightColor = mHighLightColor;
        copied.mIncreasingPaintStyle = mIncreasingPaintStyle;
        copied.mDecreasingPaintStyle = mDecreasingPaintStyle;
        copied.mShadowColor = mShadowColor;

        return copied;
    }

    @Override
    protected void calcMinMax(int start, int end) {
        // super.calcMinMax();

        if (mYVals.size() == 0)
            return;

        List<CandleEntry> entries = mYVals;

        int endValue;

        if (end == 0 || end >= mYVals.size())
            endValue = mYVals.size() - 1;
        else
            endValue = end;

        mLastStart = start;
        mLastEnd = endValue;

        mYMin = Float.MAX_VALUE;
        mYMax = -Float.MAX_VALUE;

        for (int i = start; i <= endValue; i++) {

            CandleEntry e = entries.get(i);

            if (e.getLow() < mYMin)
                mYMin = e.getLow();

            if (e.getHigh() > mYMax)
                mYMax = e.getHigh();
        }
    }

    /**
     * Sets the space that is left out on the left and right side of each
     * candle, default 0.1f (10%), max 0.45f, min 0f
     * 
     * @param space
     */
    public void setBodySpace(float space) {

        if (space < 0f)
            space = 0f;
        if (space > 0.45f)
            space = 0.45f;

        mBodySpace = space;
    }

    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     * 
     * @return
     */
    public float getBodySpace() {
        return mBodySpace;
    }

    /**
     * Sets the width of the candle-shadow-line in pixels. Default 3f.
     * 
     * @param width
     */
    public void setShadowWidth(float width) {
        mShadowWidth = Utils.convertDpToPixel(width);
    }

    /**
     * Returns the width of the candle-shadow-line in pixels.
     * 
     * @return
     */
    public float getShadowWidth() {
        return mShadowWidth;
    }

    // TODO
    /**
     * It is necessary to implement ColorsList class that will encapsulate
     * colors list functionality, because It's wrong to copy paste setColor,
     * addColor, ... resetColors for each time when we want to add a coloring
     * options for one of objects
     * 
     * @author Mesrop
     */

    /** BELOW THIS COLOR HANDLING */

    /**
     * 设置蜡烛图的颜色是使用In/DecreasingColor,还是使用DataSet的Colors -- by yanmin
     *
     * @param useDataSetColor
     */
    public void setUseDataSetColor(boolean useDataSetColor) {
        mUseDataSetColor = useDataSetColor;
    }

    /**
     * 返回 蜡烛图的颜色是使用In/DecreasingColor,还是使用DataSet的Colors -- by yanmin
     *
     * @return
     */
    public boolean isUseDataSetColor() {
        return mUseDataSetColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open > close. 
     *
     * @param color
     */
    public void setDecreasingColor(int color) {
        mDecreasingColor = color;
    }
    
    /**
     * Returns the decreasing color.
     *
     * @return
     */
    public @ColorInt
    int getDecreasingColor() {
        return mDecreasingColor;
    }
    
    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open <= close. 
     *
     * @param color
     */
    public void setIncreasingColor(int color) {
        mIncreasingColor = color;
    }
    
    /**
     * Returns the increasing color.
     *
     * @return
     */
    public @ColorInt int getIncreasingColor() {
        return mIncreasingColor;
    }

    /**
     * Returns paint style when open > close
     * 
     * @return
     */
    public Paint.Style getDecreasingPaintStyle() {
        return mDecreasingPaintStyle;
    }

    /**
     * Sets paint style when open > close
     * 
     * @param decreasingPaintStyle
     */
    public void setDecreasingPaintStyle(Paint.Style decreasingPaintStyle) {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }

    /**
     * Returns paint style when open <= close
     * 
     * @return
     */
    public Paint.Style getIncreasingPaintStyle() {
        return mIncreasingPaintStyle;
    }

    /**
     * Sets paint style when open <= close
     * 
     * @param paintStyle
     */
    public void setIncreasingPaintStyle(Paint.Style paintStyle) {
        this.mIncreasingPaintStyle = paintStyle;
    }

    /**
     * Returns shadow color for all entries
     * 
     * @return
     */
    public @ColorInt int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Sets shadow color for all entries
     * 
     * @param shadowColor
     */
    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    public boolean getShadowColorSameAsCandle() {
        return mShadowColorSameAsCandle;
    }

    /**
     * Sets shadow color to be the same color as the candle color
     *
     * @param shadowColorSameAsCandle
     */
    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
