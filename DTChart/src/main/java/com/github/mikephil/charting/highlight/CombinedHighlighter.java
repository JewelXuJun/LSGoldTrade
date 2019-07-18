package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.utils.SelectionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Jahoda on 12/09/15.
 */
public class CombinedHighlighter extends ChartHighlighter<BarLineScatterCandleBubbleDataProvider> {

    public CombinedHighlighter(BarLineScatterCandleBubbleDataProvider chart) {
        super(chart);
    }

    /**
     * Returns a list of SelectionDetail object corresponding to the given xIndex.
     *
     * @param xIndex
     * @return
     */
    @Override
    protected List<SelectionDetail> getSelectionDetailsAtIndex(int xIndex) {

        CombinedData data = (CombinedData) mChart.getData();

        // get all chartdata objects
        List<ChartData> dataObjects = data.getAllData();

        List<SelectionDetail> vals = new ArrayList<SelectionDetail>();

        float[] pts = new float[2];

        for (int i = 0; i < dataObjects.size(); i++) {

            for (int j = 0; j < dataObjects.get(i).getDataSetCount(); j++) {

                DataSet<?> dataSet = dataObjects.get(i).getDataSetByIndex(j);

                // dont include datasets that cannot be highlighted
                if (!dataSet.isHighlightEnabled())
                    continue;

                // extract all y-values from all DataSets at the given x-index
                // Modity by yanmin: 修改为找到最接近xIndex的Entry，并且返回Entry对象
                final Entry entry = dataSet.getClosestEntryForXIndex(xIndex);
                if (entry == null)
                    continue;

                pts[1] = entry.getVal();//yVal

                mChart.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);

                if (!Float.isNaN(pts[1])) {
                    vals.add(new SelectionDetail(pts[1], j, entry.getXIndex(), dataSet));
                }
            }
        }

        return vals;
    }
}
