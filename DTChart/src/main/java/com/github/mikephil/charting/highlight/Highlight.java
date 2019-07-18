
package com.github.mikephil.charting.highlight;

/**
 * Contains information needed to determine the highlighted value.
 * 
 * @author Philipp Jahoda
 */
public class Highlight {

    /** the x-index of the highlighted value */
    private int mXIndex;

    /** the index of the dataset the highlighted value is in */
    private int mDataSetIndex;

    /** index which value of a stacked bar entry is highlighted, default -1 */
    private int mStackIndex = -1;

    /** the range of the bar that is selected (only for stacked-barchart) */
    private Range mRange;

    //Add:记录当前HighLIght（Touch时的点）的X轴坐标值, Y轴坐标值 by yanmin
    private float mX;
    private float mY;

    /**
     * constructor
     * 
     * @param xIndex the index of the highlighted value on the x-axis
     * @param dataSet the index of the DataSet the highlighted value belongs to
     */
    public Highlight(int xIndex, int dataSet) {
        this.mXIndex = xIndex;
        this.mDataSetIndex = dataSet;
    }

    /**Add by yanmin
     * constructor
     *
     * @param xIndex the index of the highlighted value on the x-axis
     * @param dataSet the index of the DataSet the highlighted value belongs to
     * @param y y value of the highlighted value
     */
    public Highlight(int xIndex, int dataSet, float x, float y) {
        this.mXIndex = xIndex;
        this.mDataSetIndex = dataSet;
        this.mX = x;
        this.mY = y;
    }

    /**
     * Constructor, only used for stacked-barchart.
     * 
     * @param xIndex the index of the highlighted value on the x-axis
     * @param dataSet the index of the DataSet the highlighted value belongs to
     * @param stackIndex references which value of a stacked-bar entry has been
     *            selected
     */
    public Highlight(int xIndex, int dataSet, int stackIndex) {
        this(xIndex, dataSet);
        mStackIndex = stackIndex;
    }

    /**
     * Constructor, only used for stacked-barchart.
     *
     * @param xIndex the index of the highlighted value on the x-axis
     * @param dataSet the index of the DataSet the highlighted value belongs to
     * @param stackIndex references which value of a stacked-bar entry has been
     *            selected
     * @param range the range the selected stack-value is in
     */
    public Highlight(int xIndex, int dataSet, int stackIndex, Range range) {
        this(xIndex, dataSet, stackIndex);
        this.mRange = range;
    }

    /** Add by yanmin
     * returns x value of the highlighted value is in
     *
     * @return
     */
    public float getX() {
        return mX;
    }

    /** Add by yanmin
     * returns y value of the highlighted value is in
     *
     * @return
     */
    public float getY() {
        return mY;
    }

    /**
     * returns the index of the DataSet the highlighted value is in
     * 
     * @return
     */
    public int getDataSetIndex() {
        return mDataSetIndex;
    }

    /**
     * returns the index of the highlighted value on the x-axis
     * 
     * @return
     */
    public int getXIndex() {
        return mXIndex;
    }

    /**
     * Only needed if a stacked-barchart entry was highlighted. References the
     * selected value within the stacked-entry.
     * 
     * @return
     */
    public int getStackIndex() {
        return mStackIndex;
    }

    /**
     * Returns the range of values the selected value of a stacked bar is in. (this is only relevant for stacked-barchart)
     * @return
     */
    public Range getRange() {
        return mRange;
    }

    /**
     * returns true if this highlight object is equal to the other (compares
     * xIndex and dataSetIndex)
     * 
     * @param h
     * @return
     */
    public boolean equalTo(Highlight h) {

        if (h == null)
            return false;
        else {
            if (this.mDataSetIndex == h.mDataSetIndex && this.mXIndex == h.mXIndex
                    && this.mStackIndex == h.mStackIndex
                    && this.mX == h.mX && this.mY == h.mY)//Add By Yanmin
                return true;
            else
                return false;
        }
    }

    @Override
    public String toString() {
        return "Highlight, xIndex: " + mXIndex + ", dataSetIndex: " + mDataSetIndex
                + ", stackIndex (only stacked barentry): " + mStackIndex;
    }
}
