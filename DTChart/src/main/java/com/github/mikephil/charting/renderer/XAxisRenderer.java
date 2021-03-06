
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;

import com.datai.common.charts.common.Config;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.List;

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;

    private int mLowestIndex;
    private int mHighestIndex;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, trans);

        this.mXAxis = xAxis;

        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextAlign(Align.CENTER);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }

    public void computeAxis(float xValAverageLength, List<String> xValues) {

        //Bug: 画X轴Label时设置其颜色 By yanmin
        mAxisLabelPaint.setColor(mXAxis.getTextColor());
        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        StringBuilder widthText = new StringBuilder();

        int max = Math.round(xValAverageLength
                + mXAxis.getSpaceBetweenLabels());

        for (int i = 0; i < max; i++) {
            widthText.append('h');
        }

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, widthText.toString());

        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");

        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(
                labelWidth,
                labelHeight,
                mXAxis.getLabelRotationAngle());

        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        mXAxis.setValues(xValues);
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yoffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        if (mXAxis.getPosition() == XAxisPosition.TOP) {

            drawLabels(c, mViewPortHandler.contentTop() - yoffset,
                    new PointF(0.5f, 1.0f));

        } else if (mXAxis.getPosition() == XAxisPosition.TOP_INSIDE) {

            drawLabels(c, mViewPortHandler.contentTop() + yoffset + mXAxis.mLabelRotatedHeight,
                    new PointF(0.5f, 1.0f));

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {

            drawLabels(c, mViewPortHandler.contentBottom() + yoffset,
                    new PointF(0.5f, 0.0f));

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE) {

            drawLabels(c, mViewPortHandler.contentBottom() - yoffset - mXAxis.mLabelRotatedHeight,
                    new PointF(0.5f, 0.0f));

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_BOTHSIDE) {
            //Modify by yanmin: 只画显示区域内最小最大xIndex值
            drawBothSideLabels(c, mViewPortHandler.contentBottom() + yoffset,
                    new PointF(0.0f, 0.0f));

        } else { // BOTH SIDED

            drawLabels(c, mViewPortHandler.contentTop() - yoffset,
                    new PointF(0.5f, 1.0f));
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset,
                    new PointF(0.5f, 0.0f));
        }
    }

    @Override
    public void renderAxisLine(Canvas c) {

        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());

        if (mXAxis.getPosition() == XAxisPosition.TOP
                || mXAxis.getPosition() == XAxisPosition.TOP_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mAxisLinePaint);
        }

        if (mXAxis.getPosition() == XAxisPosition.BOTTOM
                || mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED
                || mXAxis.getPosition() == XAxisPosition.BOTTOM_BOTHSIDE) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    //Modify by yanmin:记录当前显示区域的最小和最大Index
    public void setXIndexRange(int lowestIndex, int highestIndex) {
        mLowestIndex = lowestIndex;
        if (mLowestIndex < 0)
            mLowestIndex = 0;
        mHighestIndex = highestIndex;
    }

    //Modify by yanmin: 当mXAxis.getPosition() == XAxisPosition.BOTTOM_BOTHSIDE，只画最小和最大Index对应X值
    protected void drawBothSideLabels(Canvas c, float pos, PointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();

        if (mLowestIndex >= 0 && mLowestIndex < mXAxis.getValues().size()) {
            String lowestLabel = mXAxis.getValues().get(mLowestIndex);
            drawLabel(c, lowestLabel, mLowestIndex, mXAxis.getXOffset(), pos, anchor, labelRotationAngleDegrees);
        }

        if (mHighestIndex >= 0 && mHighestIndex < mXAxis.getValues().size()) {
            String highestLabel = mXAxis.getValues().get(mHighestIndex);
            drawLabel(c, highestLabel, mHighestIndex, mViewPortHandler.getChartWidth() - Utils.calcTextWidth(mAxisLabelPaint, highestLabel) - mXAxis.getXOffset(),
                    pos, anchor, labelRotationAngleDegrees);
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected void drawLabels(Canvas c, float pos, PointF anchor) {

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();

        // pre allocate to save performance (dont allocate in loop)
        float[] position = new float[]{
                0f, 0f
        };

        int count = mMaxX + 1;

        int average = mXAxis.mAxisLabelModulus /*- 1*/;

        List<long[]> timeLists = mXAxis.getMoreCloseTime();
        long timeListsCount = 0;
        int timeListsPosition = 0;

        for (int i = 0; i < count; i++) {
            if (i > mMaxX) {
                position[0] = count - (mXAxis.mAxisLabelModulus - mMaxX % mXAxis.mAxisLabelModulus);
            } else {
                position[0] = i;
            }

            mTrans.pointValuesToPixel(position);

            if (mViewPortHandler.isInBoundsX(position[0])) {
                String label;

                if (mXAxis.isOnlyShowFirstLastValue()) {
                    if (mXAxis.isHaveCloseTime()) {
                        if (mXAxis.isHaveMoreCloaseTime()) {
                            if (i == 0) {
                                label = sdf.format(timeLists.get(0)[0]);

                                position[0] = position[0] + Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴首数值显示靠左对齐
                            } else if (i == count - 1) {
                                label = sdf.format(timeLists.get(timeLists.size() - 1)[1]);

                                position[0] = position[0] - Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴尾数值显示靠右对齐
                            } else if (i == (timeLists.get(timeListsPosition)[1] - timeLists.get(timeListsPosition)[0]) / Config.TimeInterval_OneMinute + timeListsCount
                                    && timeListsPosition < timeLists.size() - 1) {
                                label = sdf.format(timeLists.get(timeListsPosition)[1]) + "/" + sdf.format(timeLists.get(timeListsPosition + 1)[0]);
                                timeListsCount = timeListsCount + (timeLists.get(timeListsPosition)[1] - timeLists.get(timeListsPosition)[0]) / Config.TimeInterval_OneMinute;

                                timeListsPosition++;

                                position[0] = position[0] + 1;
                            } else {
                                label = "";
                            }
                        } else {
                            if (i == 0) {
                                label = sdf.format(mXAxis.getFirstValue());

                                position[0] = position[0] + Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴首数值显示靠左对齐
                            } else if (i == count - 1) {
                                label = sdf.format(mXAxis.getLastValue());

                                position[0] = position[0] - Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴尾数值显示靠右对齐
                            } else if (i == count / 2) {
                                label = sdf.format(mXAxis.getFirstValue() + Config.TimeInterval_OneMinute * i)
                                        + "/" + sdf.format(mXAxis.getFirstValue() + Config.TimeInterval_OneMinute * i + mXAxis.getClostValue());

                                position[0] = position[0] + 8;
                            } else {
                                label = "";
                            }
                        }
                    } else {
                        if (i == 0) {
                            label = sdf.format(mXAxis.getFirstValue());

                            position[0] = position[0] + Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴首数值显示靠左对齐
                        } else if (i == count - 1) {
                            label = sdf.format(mXAxis.getLastValue());

                            position[0] = position[0] - Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴尾数值显示靠右对齐
                        } else if (i % average == 0 && !(count - i + 1 < average)) {
                            label = sdf.format(mXAxis.getFirstValue() + Config.TimeInterval_FiveMinute * i);

                            position[0] = position[0] + 8;
                        } else {
                            label = "";
                        }
                    }

                } else {
                    if (i > count) {
                        label = "";
                    } else if (i == 0) {
                        label = sdf.format(timeLists.get(0)[0]);

                        position[0] = position[0] + Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴首数值显示靠左对齐
                    } else if (i == count - 1) {
                        label = sdf.format(timeLists.get(0)[1]);

                        position[0] = position[0] - Utils.calcTextWidth(mAxisLabelPaint, label) / 2;  //Add by XuJun 使X轴尾数值显示靠右对齐
                    } /*else if (i % mXAxis.mAxisLabelModulus == 0) {
                        label = mXAxis.getValues().get(i);
                    }*/ else {
                        label = "";
                    }
                }

                if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                    // avoid clipping of the last
                    if (i == mXAxis.getValues().size() - 1 && mXAxis.getValues().size() > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                        if (width > mViewPortHandler.offsetRight() * 2
                                && position[0] + width > mViewPortHandler.getChartWidth())
                            position[0] -= width / 2;

                        // avoid clipping of the first
                    } else if (i == 0) {

                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        position[0] += width / 2;
                    }
                }

                drawLabel(c, label, i, position[0], pos, anchor, labelRotationAngleDegrees);
            }
        }
    }

    protected void drawLabel(Canvas c, String label, int xIndex, float x, float y, PointF anchor, float angleDegrees) {
        String formattedLabel = mXAxis.getValueFormatter().getXValue(label, xIndex, mViewPortHandler);
        Utils.drawText(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }

    @Override
    public void renderGridLines(Canvas c) {

        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        // pre alloc
        float[] position = new float[]{
                0f, 0f
        };

        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());

        Path gridLinePath = new Path();

        // Modify by yanmin:不画最左最右的两条线，防止跟边框重合
        if (mXAxis.bNotAverage) {
            long[] value = mXAxis.getAxisLabelModulusList();
            long time = 0;

            for (int i = 0; i < value.length - 1; i++) {
                time = time + value[i];
                position[0] = time;
                mTrans.pointValuesToPixel(position);

                if (position[0] >= mViewPortHandler.offsetLeft()
                        && position[0] <= mViewPortHandler.getChartWidth()) {
                    gridLinePath.moveTo(position[0], mViewPortHandler.contentBottom());
                    gridLinePath.lineTo(position[0], mViewPortHandler.contentTop());

                    // draw a path because lines don't support dashing on lower android versions
                    c.drawPath(gridLinePath, mGridPaint);
                }

                gridLinePath.reset();
            }
        } else {
            for (int i = mMinX + mXAxis.mAxisLabelModulus; i < mMaxX; i += mXAxis.mAxisLabelModulus) {

                position[0] = i;
                mTrans.pointValuesToPixel(position);

                if (position[0] >= mViewPortHandler.offsetLeft()
                        && position[0] <= mViewPortHandler.getChartWidth()) {

                    gridLinePath.moveTo(position[0], mViewPortHandler.contentBottom());
                    gridLinePath.lineTo(position[0], mViewPortHandler.contentTop());

                    // draw a path because lines don't support dashing on lower android versions
                    c.drawPath(gridLinePath, mGridPaint);
                }

                gridLinePath.reset();
            }
        }
    }

    //Add:无数据的时候，画默认GridLine By yanmin
    @Override
    public void renderNodataGridLines(Canvas c) {

        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());

       /* Path gridLinePath = new Path();
        int count = 3;
        float itemWidth = mViewPortHandler.getChartWidth() / (count + 1);

        // draw the vertical grid
        for (int i = 1; i <= count; i++) {

            float x = mViewPortHandler.contentTop() + itemWidth * i;

            gridLinePath.moveTo(x, mViewPortHandler.contentBottom());
            gridLinePath.lineTo(x, mViewPortHandler.contentTop());

            // draw a path because lines don't support dashing on lower android versions
            c.drawPath(gridLinePath, mGridPaint);

            gridLinePath.reset();
        }*/
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mXAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] position = new float[2];

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled())
                continue;

            position[0] = l.getLimit();
            position[1] = 0.f;

            mTrans.pointValuesToPixel(position);

            renderLimitLineLine(c, l, position);
            renderLimitLineLabel(c, l, position, 2.f + l.getYOffset());
        }
    }

    float[] mLimitLineSegmentsBuffer = new float[4];
    private Path mLimitLinePath = new Path();

    public void renderLimitLineLine(Canvas c, LimitLine limitLine, float[] position) {
        mLimitLineSegmentsBuffer[0] = position[0];
        mLimitLineSegmentsBuffer[1] = mViewPortHandler.contentTop();
        mLimitLineSegmentsBuffer[2] = position[0];
        mLimitLineSegmentsBuffer[3] = mViewPortHandler.contentBottom();

        mLimitLinePath.reset();
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1]);
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3]);

        mLimitLinePaint.setStyle(Paint.Style.STROKE);
        mLimitLinePaint.setColor(limitLine.getLineColor());
        mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());

        c.drawPath(mLimitLinePath, mLimitLinePaint);
    }

    public void renderLimitLineLabel(Canvas c, LimitLine limitLine, float[] position, float yOffset) {
        String label = limitLine.getLabel();

        // if drawing the limit-value label is enabled
        if (label != null && !label.equals("")) {

            mLimitLinePaint.setStyle(limitLine.getTextStyle());
            mLimitLinePaint.setPathEffect(null);
            mLimitLinePaint.setColor(limitLine.getTextColor());
            mLimitLinePaint.setStrokeWidth(0.5f);
            mLimitLinePaint.setTextSize(limitLine.getTextSize());

            float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();

            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();

            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {

                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                mLimitLinePaint.setTextAlign(Align.LEFT);
                c.drawText(label, position[0] + xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {

                mLimitLinePaint.setTextAlign(Align.LEFT);
                c.drawText(label, position[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {

                mLimitLinePaint.setTextAlign(Align.RIGHT);
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else {

                mLimitLinePaint.setTextAlign(Align.RIGHT);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            }
        }
    }

}
