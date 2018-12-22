
package com.github.mikephil.charting.components;

import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the x-axis labels settings. Only use the setter methods to
 * modify it. Do not access public variables directly. Be aware that not all
 * features the XLabels class provides are suitable for the RadarChart.
 *
 * @author Philipp Jahoda
 */
public class XAxis extends AxisBase {

    /**
     * the arraylist containing all the x-axis labels
     */
    protected List<String> mValues = new ArrayList<String>();

    /**
     * width of the x-axis labels in pixels - this is automatically
     * calculated by the computeAxis() methods in the renderers
     */
    public int mLabelWidth = 1;

    /**
     * height of the x-axis labels in pixels - this is automatically
     * calculated by the computeAxis() methods in the renderers
     */
    public int mLabelHeight = 1;

    /**
     * width of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeAxis() methods in the renderers
     */
    public int mLabelRotatedWidth = 1;

    /**
     * height of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeAxis() methods in the renderers
     */
    public int mLabelRotatedHeight = 1;

    /**
     * This is the angle for drawing the X axis labels (in degrees)
     */
    protected float mLabelRotationAngle = 0.f;

    /**
     * the space that should be left out (in characters) between the x-axis
     * labels
     */
    private int mSpaceBetweenLabels = 4;

    /**
     * the modulus that indicates if a value at a specified index in an
     * array(list) for the x-axis-labels is drawn or not. If index % modulus ==
     * 0 DRAW, else dont draw.
     */
    public int mAxisLabelModulus = 1;

    public boolean bNotAverage = false;

    public long[] mAxisLabelModulusList;

    /**
     * Is axisLabelModulus a custom value or auto calculated? If false, then
     * it's auto, if true, then custom. default: false (automatic modulus)
     */
    private boolean mIsAxisModulusCustom = false;

    /**
     * the modulus that indicates if a value at a specified index in an
     * array(list) for the y-axis-labels is drawn or not. If index % modulus ==
     * 0 DRAW, else dont draw. THIS IS ONLY FOR HORIZONTAL BARCHART.
     */
    public int mYAxisLabelModulus = 1;

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart
     */
    private boolean mAvoidFirstLastClipping = false;

    /**
     * Custom formatter for adjusting x-value strings
     */
    protected XAxisValueFormatter mXAxisValueFormatter = new DefaultXAxisValueFormatter();

    /**
     * the position of the x-labels relative to the chart
     */
    private XAxisPosition mPosition = XAxisPosition.TOP;

    /**
     * 只显示首尾数值，默认false代表数值都显示
     */
    private boolean mOnlyShowFirstLastValue = false;

    /**
     * 是否包含中午休市时间
     */
    private boolean mIsHaveCloseTime = false;
    private boolean mIsHaveMoreCloaseTime = false;

    /**
     * 显示的首尾数值
     */
    private long mFirstValue;
    private long mLastValue;
    private long mCloseTime;
    private List<long[]> mMoreCloseTime;

    /**
     * enum for the position of the x-labels relative to the chart
     */
    // Modify by yanmin: 新增BOTTOM_BOTHSIDE：只画显示区域内最小最大xIndex值
    public enum XAxisPosition {
        TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE, BOTTOM_BOTHSIDE
    }

    public XAxis() {
        super();

        mYOffset = Utils.convertDpToPixel(4.f);
    }

    /**
     * returns the position of the x-labels
     */
    public XAxisPosition getPosition() {
        return mPosition;
    }

    /**
     * sets the position of the x-labels
     *
     * @param pos
     */
    public void setPosition(XAxisPosition pos) {
        mPosition = pos;
    }

    /**
     * returns the angle for drawing the X axis labels (in degrees)
     */
    public float getLabelRotationAngle() {
        return mLabelRotationAngle;
    }

    /**
     * sets the angle for drawing the X axis labels (in degrees)
     *
     * @param angle the angle in degrees
     */
    public void setLabelRotationAngle(float angle) {
        mLabelRotationAngle = angle;
    }

    /**
     * Sets the space (in characters) that should be left out between the x-axis
     * labels, default 4. This only applies if the number of labels that will be
     * skipped in between drawn axis labels is not custom set.
     *
     * @param spaceCharacters
     */
    public void setSpaceBetweenLabels(int spaceCharacters) {
        mSpaceBetweenLabels = spaceCharacters;
    }

    /**
     * add by XuJun
     * 设置只显示XAxis的首尾数值
     */
    public void setOnlyShowFirstLastValue(boolean enable, boolean bHaveCloseTime, long firstValue, long lastValue, long clostTime) {
        mOnlyShowFirstLastValue = enable;
        mIsHaveCloseTime = bHaveCloseTime;
        mFirstValue = firstValue;
        mLastValue = lastValue;
        mCloseTime = clostTime;
    }

    public void setOnlyShowFirstLastValue(boolean enable, boolean bHaveCloseTime, boolean bHaveMoreCloseTime, List<long[]> moreCloseTime) {
        mOnlyShowFirstLastValue = enable;
        mIsHaveCloseTime = bHaveCloseTime;
        mIsHaveMoreCloaseTime = bHaveMoreCloseTime;
        mMoreCloseTime = moreCloseTime;
    }

    /**
     * add by XuJun
     * 获取是否只显示首尾值以及首尾的数值
     */
    public boolean isOnlyShowFirstLastValue() {
        return mOnlyShowFirstLastValue;
    }

    public boolean isHaveCloseTime() {
        return mIsHaveCloseTime;
    }

    public boolean isHaveMoreCloaseTime() {
        return mIsHaveMoreCloaseTime;
    }

    public long getFirstValue() {
        return mFirstValue;
    }

    public long getLastValue() {
        return mLastValue;
    }

    public long getClostValue() {
        return mCloseTime;
    }

    public List<long[]> getMoreCloseTime() {
        return mMoreCloseTime;
    }

    public long[] getAxisLabelModulusList() {
        return mAxisLabelModulusList;
    }

    /**
     * Sets the number of labels that should be skipped on the axis before the
     * next label is drawn. This will disable the feature that automatically
     * calculates an adequate space between the axis labels and set the number
     * of labels to be skipped to the fixed number provided by this method. Call
     * resetLabelsToSkip(...) to re-enable automatic calculation.
     *
     * @param count
     */
    public void setLabelsToSkip(int count) {

        if (count < 0)
            count = 0;

        mIsAxisModulusCustom = true;
        mAxisLabelModulus = count/* + 1*/;
    }

    public void setLabelsToSkip(long[] averageValues, boolean notAverage) {
        bNotAverage = notAverage;
        mIsAxisModulusCustom = true;
        mAxisLabelModulusList = averageValues;
    }

    /**
     * Calling this will disable a custom number of labels to be skipped (set by
     * setLabelsToSkip(...)) while drawing the x-axis. Instead, the number of
     * values to skip will again be calculated automatically.
     */
    public void resetLabelsToSkip() {
        mIsAxisModulusCustom = false;
    }

    /**
     * Returns true if a custom axis-modulus has been set that determines the
     * number of labels to skip when drawing.
     *
     * @return
     */
    public boolean isAxisModulusCustom() {
        return mIsAxisModulusCustom;
    }

    /**
     * Returns the space (in characters) that should be left out between the
     * x-axis labels
     */
    public int getSpaceBetweenLabels() {
        return mSpaceBetweenLabels;
    }

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart or the screen
     *
     * @param enabled
     */
    public void setAvoidFirstLastClipping(boolean enabled) {
        mAvoidFirstLastClipping = enabled;
    }

    /**
     * returns true if avoid-first-lastclipping is enabled, false if not
     *
     * @return
     */
    public boolean isAvoidFirstLastClippingEnabled() {
        return mAvoidFirstLastClipping;
    }

    /**
     * Sets the labels for this axis.
     *
     * @param values
     */
    public void setValues(List<String> values) {
        mValues = values;
    }

    /**
     * Returns the labels for this axis.
     *
     * @return
     */
    public List<String> getValues() {
        return mValues;
    }


    /**
     * Sets a custom XAxisValueFormatter for the data object that allows custom-formatting
     * of all x-values before rendering them. Provide null to reset back to the
     * default formatting.
     *
     * @param formatter
     */
    public void setValueFormatter(XAxisValueFormatter formatter) {
        if (formatter == null)
            mXAxisValueFormatter = new DefaultXAxisValueFormatter();
        else
            mXAxisValueFormatter = formatter;
    }

    /**
     * Returns the custom XAxisValueFormatter that is set for this data object.
     *
     * @return
     */
    public XAxisValueFormatter getValueFormatter() {
        return mXAxisValueFormatter;
    }

    @Override
    public String getLongestLabel() {

        String longest = "";

        for (int i = 0; i < mValues.size(); i++) {
            String text = mValues.get(i);

            if (longest.length() < text.length())
                longest = text;
        }

        return longest;
    }
}
