
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.CandleBodyBuffer;
import com.github.mikephil.charting.buffer.CandleShadowBuffer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.CandleDataProvider;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer {

    protected CandleDataProvider mChart;

    private CandleShadowBuffer[] mShadowBuffers;
    private CandleBodyBuffer[] mBodyBuffers;
    private DecimalFormat df = new DecimalFormat(".0");

    public CandleStickChartRenderer(CandleDataProvider chart, ChartAnimator animator,
                                    ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void initBuffers() {
        CandleData candleData = mChart.getCandleData();
        mShadowBuffers = new CandleShadowBuffer[candleData.getDataSetCount()];
        mBodyBuffers = new CandleBodyBuffer[candleData.getDataSetCount()];

        for (int i = 0; i < mShadowBuffers.length; i++) {
            CandleDataSet set = candleData.getDataSetByIndex(i);
            mShadowBuffers[i] = new CandleShadowBuffer(set.getValueCount() * 4);
            mBodyBuffers[i] = new CandleBodyBuffer(set.getValueCount() * 4);
        }
    }

    @Override
    public void drawData(Canvas c) {

        CandleData candleData = mChart.getCandleData();

        for (CandleDataSet set : candleData.getDataSets()) {

            if (set.isVisible() && set.getEntryCount() > 0)
                drawDataSet(c, set);
        }
    }

    protected void drawDataSet(Canvas c, CandleDataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        int dataSetIndex = mChart.getCandleData().getIndexOfDataSet(dataSet);

        List<CandleEntry> entries = dataSet.getYVals();

        int minx = Math.max(mMinX, 0);
        int maxx = Math.min(mMaxX + 1, entries.size());

        int range = (maxx - minx) * 4;
        int to = (int) Math.ceil((maxx - minx) * phaseX + minx);

        CandleBodyBuffer bodyBuffer = mBodyBuffers[dataSetIndex];
        bodyBuffer.setBodySpace(dataSet.getBodySpace());
        bodyBuffer.setPhases(phaseX, phaseY);
        bodyBuffer.limitFrom(minx);
        bodyBuffer.limitTo(maxx);
        bodyBuffer.feed(entries);

        trans.pointValuesToPixel(bodyBuffer.buffer);

        CandleShadowBuffer shadowBuffer = mShadowBuffers[dataSetIndex];
        shadowBuffer.setPhases(phaseX, phaseY);
        shadowBuffer.limitFrom(minx);
        shadowBuffer.limitTo(maxx);
        shadowBuffer.feed(entries);

        trans.pointValuesToPixel(shadowBuffer.buffer);

        mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

        // draw the body
        for (int j = 0; j < range; j += 4) {

            // get the entry
            CandleEntry e = entries.get(j / 4 + minx);

            if (!fitsBounds(e.getXIndex(), mMinX, to))
                continue;

            if (dataSet.getShadowColorSameAsCandle()) {

                if (e.getOpen() > e.getClose())
                    mRenderPaint.setColor(
                            dataSet.isUseDataSetColor() ?
                                    dataSet.getColor(j / 4 + minx) :
                                    dataSet.getDecreasingColor()
                    );

                else if (e.getOpen() < e.getClose())
                    mRenderPaint.setColor(
                            dataSet.isUseDataSetColor() ?
                                    dataSet.getColor(j / 4 + minx) :
                                    dataSet.getIncreasingColor()
                    );

                else
                    mRenderPaint.setColor(
                            dataSet.isUseDataSetColor() ?
                                    dataSet.getColor(j / 4 + minx) :
                                    dataSet.getShadowColor()
                    );

            } else {
                mRenderPaint.setColor(
                        dataSet.isUseDataSetColor() ?
                                dataSet.getColor(j / 4 + minx) :
                                dataSet.getShadowColor()
                );
            }

            mRenderPaint.setStyle(Paint.Style.STROKE);

            // draw the shadow
            c.drawLine(shadowBuffer.buffer[j], shadowBuffer.buffer[j + 1],
                    shadowBuffer.buffer[j + 2], shadowBuffer.buffer[j + 3],
                    mRenderPaint);

            float leftBody = bodyBuffer.buffer[j];
            float open = bodyBuffer.buffer[j + 1];
            float rightBody = bodyBuffer.buffer[j + 2];
            float close = bodyBuffer.buffer[j + 3];

            // draw body differently for increasing and decreasing entry
            if (open > close) { // decreasing

                if (dataSet.isUseDataSetColor()) {
                    mRenderPaint.setColor(dataSet.getColor(j / 4 + minx));
                } else {
                    mRenderPaint.setColor(dataSet.getDecreasingColor());
                }

                mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());
                // draw the body
                c.drawRect(leftBody, close, rightBody, open, mRenderPaint);
                // Modify by yanmin: 解决当Open和Close很接近，但不相等时，水平线太细的问题
                c.drawLine(leftBody, (open + close) / 2, rightBody, (open + close) / 2, mRenderPaint);

            } else if (open < close) {

                if (dataSet.isUseDataSetColor()) {
                    mRenderPaint.setColor(dataSet.getColor(j / 4 + minx));
                } else {
                    mRenderPaint.setColor(dataSet.getIncreasingColor());
                }

                mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());
                // draw the body
                c.drawRect(leftBody, open, rightBody, close, mRenderPaint);
                // Modify by yanmin: 解决当Open和Close很接近，但不相等时，水平线太细的问题
                c.drawLine(leftBody, (open + close) / 2, rightBody, (open + close) / 2, mRenderPaint);
            } else { // equal values
                if (dataSet.isUseDataSetColor()) {
                    mRenderPaint.setColor(dataSet.getColor(j / 4 + minx));
                } else {
                    mRenderPaint.setColor(dataSet.getShadowColor());
                }
                c.drawLine(leftBody, open, rightBody, close, mRenderPaint);
            }
        }
    }

    // /**
    // * Transforms the values of an entry in order to draw the candle-body.
    // *
    // * @param bodyPoints
    // * @param e
    // * @param bodySpace
    // */
    // private void transformBody(float[] bodyPoints, CandleEntry e, float
    // bodySpace, Transformer trans) {
    //
    // float phase = mAnimator.getPhaseY();
    //
    // bodyPoints[0] = e.getXIndex() - 0.5f + bodySpace;
    // bodyPoints[1] = e.getClose() * phase;
    // bodyPoints[2] = e.getXIndex() + 0.5f - bodySpace;
    // bodyPoints[3] = e.getOpen() * phase;
    //
    // trans.pointValuesToPixel(bodyPoints);
    // }
    //
    // /**
    // * Transforms the values of an entry in order to draw the candle-shadow.
    // *
    // * @param shadowPoints
    // * @param e
    // */
    // private void transformShadow(float[] shadowPoints, CandleEntry e,
    // Transformer trans) {
    //
    // float phase = mAnimator.getPhaseY();
    //
    // shadowPoints[0] = e.getXIndex();
    // shadowPoints[1] = e.getHigh() * phase;
    // shadowPoints[2] = e.getXIndex();
    // shadowPoints[3] = e.getLow() * phase;
    //
    // trans.pointValuesToPixel(shadowPoints);
    // }

    //Modify: 只画最大值和最小值 By yanmin
    @Override
    public void drawValues(Canvas c) {

        // if values are drawn
        // 源代码是当比例尺变大时，就不再画值，现改成不管图的比例尺是多大，都画，因为现在只画最大最小值
//        if (mChart.getCandleData().getYValCount() < mChart.getMaxVisibleCount()
        //        * mViewPortHandler.getScaleX()) {

        List<CandleDataSet> dataSets = mChart.getCandleData().getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {

            CandleDataSet dataSet = dataSets.get(i);

            if (!dataSet.isDrawValuesEnabled() || dataSet.getEntryCount() == 0)
                continue;

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            int originalColor = mValuePaint.getColor();

            int low = mChart.getLowestVisibleXIndex();
            int high = mChart.getHighestVisibleXIndex();

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

            List<CandleEntry> entries = dataSet.getYVals();

            int minx = Math.max(low, 0);
            //小米Note会产生数组越界，原因不详
            minx = Math.min(minx, entries.size() - 1);
            int maxx = Math.min(high, entries.size() - 1);

            CandleEntry minEntry = entries.get(minx);
            CandleEntry maxEntry = entries.get(minx);
            for (int t = minx + 1; t <= maxx; t++) {
                CandleEntry entry = entries.get(t);
                if (entry.getHigh() > maxEntry.getHigh())
                    maxEntry = entry;

                if (entry.getLow() < minEntry.getLow())
                    minEntry = entry;
            }

            float[] valuePoints = new float[2];
            valuePoints[0] = maxEntry.getXIndex();
            valuePoints[1] = maxEntry.getHigh() * mAnimator.getPhaseY();
            trans.getValueToPixelMatrix().mapPoints(valuePoints);

            mValuePaint.setColor(dataSet.getIncreasingColor());

            float yOffset = Utils.convertDpToPixel(1f);
            if (valuePoints[0] < mViewPortHandler.contentWidth() / 2) {
                mValuePaint.setTextAlign(Paint.Align.LEFT);
            } else {
                mValuePaint.setTextAlign(Paint.Align.RIGHT);
            }
            drawValue(c, dataSet.getValueFormatter(), maxEntry.getHigh(), maxEntry, i, valuePoints[0], valuePoints[1] - yOffset);

            valuePoints = new float[2];
            valuePoints[0] = minEntry.getXIndex();
            valuePoints[1] = minEntry.getLow() * mAnimator.getPhaseY();
            trans.getValueToPixelMatrix().mapPoints(valuePoints);

            mValuePaint.setColor(dataSet.getDecreasingColor());

            yOffset = Utils.convertDpToPixel(10f);
            if (valuePoints[0] < mViewPortHandler.contentWidth() / 2) {
                mValuePaint.setTextAlign(Paint.Align.LEFT);
            } else {
                mValuePaint.setTextAlign(Paint.Align.RIGHT);
            }
            drawValue(c, dataSet.getValueFormatter(), minEntry.getLow(), minEntry, i, valuePoints[0], valuePoints[1] + yOffset);
            mValuePaint.setColor(originalColor);
        }
        //}
    }

    //原函数，不使用
    //@Override
    public void drawValues2(Canvas c) {

        // if values are drawn
        if (mChart.getCandleData().getYValCount() < mChart.getMaxVisibleCount()
                * mViewPortHandler.getScaleX()) {

            List<CandleDataSet> dataSets = mChart.getCandleData().getDataSets();

            for (int i = 0; i < dataSets.size(); i++) {

                CandleDataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled() || dataSet.getEntryCount() == 0)
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                List<CandleEntry> entries = dataSet.getYVals();

                int minx = Math.max(mMinX, 0);
                int maxx = Math.min(mMaxX + 1, entries.size());

                float[] positions = trans.generateTransformedValuesCandle(
                        entries, mAnimator.getPhaseX(), mAnimator.getPhaseY(), minx, maxx);

                float yOffset = Utils.convertDpToPixel(5f);

                for (int j = 0; j < positions.length; j += 2) {

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                        continue;

                    CandleEntry entry = entries.get(j / 2 + minx);

                    drawValue(c, dataSet.getValueFormatter(), entry.getHigh(), entry, i, x, y - yOffset);
                }
            }
        }
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        for (int i = 0; i < indices.length; i++) {

            int xIndex = indices[i].getXIndex(); // get the
            // x-position

            CandleDataSet set = mChart.getCandleData().getDataSetByIndex(
                    indices[i].getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            CandleEntry e = set.getEntryForXIndex(xIndex);

            if (e == null)// || e.getXIndex() != xIndex
                continue;

            //Modify：Highlight的中心点设在蜡烛图的正中心 By yanmin
            float low = e.getOpen() * mAnimator.getPhaseY();
            float high = e.getClose() * mAnimator.getPhaseY();
            float y = (low + high) / 2f;

            float min = mChart.getYChartMin();
            float max = mChart.getYChartMax();

            float[] pts = new float[]{
                    xIndex, y
            };

            mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);

            if (set.isDrawHorizontalHighlightInTouchPoint()) {
                //Add By Yanmin 绘制HighLight在手指位置
//                pts[0] = indices[i].getX();
                pts[1] = indices[i].getY();
            }

            // draw the lines
            drawHighlightLines(c, pts, set);

            XAxis xAxis = mChart.getRendererXAxis().mXAxis;
            YAxis leftYAxis = mChart.getRendererLeftYAxis().mYAxis;
            YAxis rightYAxis = mChart.getRendererRightYAxis().mYAxis;

            //Add By yanmin: 显示当前HighLight对应的X值
            if (set.isVerticalHighlightIndicatorEnabled()
                    && xAxis.isDrawLabelsEnabled()) {
                String xVal = xAxis.getValues().get(xIndex);
                drawVerticalHighlightText(c, xVal, pts[0], mViewPortHandler.contentHeight(), mViewPortHandler.contentWidth(), set);
            }

            //Add By yanmin: 显示当前HighLight对应的Y值
            if (set.isHorizontalHighlightIndicatorEnabled()) {
                float y_position = pts[1];

                if (leftYAxis.isDrawLabelsEnabled() && y_position <= mViewPortHandler.contentHeight()) {
                    mChart.getTransformer(set.getAxisDependency()).pixelsToValue(pts);
                    float y_Val = pts[1];

                    drawHorizontalHighlightText(c, y_Val, 0, y_position, set);
                }
            }
        }
    }

}
