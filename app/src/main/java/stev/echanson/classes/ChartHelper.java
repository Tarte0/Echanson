package stev.echanson.classes;

import android.content.Context;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

import stev.echanson.R;


public class ChartHelper {

    // Nombre de catégories du radarChart
    public static final int NB_CATEGORIES = 6;

    public static final double MAX = 5.0, MIN = 0.00;

    public static void getRadarChart(Context context, RadarChart radarChart){
        radarChart.setTouchEnabled(false);

        // Animation de l'affichage
        //radarChart.animateXY(1400,1400, Easing.EasingOption.EaseInOutQuad,Easing.EasingOption.EaseInOutQuad);


        ArrayList<RadarEntry> nutriments = new ArrayList<>();


        for(int i = 0; i<NB_CATEGORIES; i++){
            double val = (Math.random() * MAX) + MIN;
            nutriments.add(new RadarEntry((float)val));
        }

        RadarDataSet set = new RadarDataSet(nutriments,"Nutriments");
        set.setColor(context.getResources().getColor(R.color.colorPrimary));
        set.setFillColor(context.getResources().getColor(R.color.colorPrimary));
        set.setDrawFilled(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set);

        RadarData data =  new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);

        radarChart.setData(data);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0);
        xAxis.setXOffset(0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] categories = new String[] {"Energie", "Gras", "Sucre", "Proteine", "Sel", "Fibre"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return categories[(int) value % categories.length];
            }
        });

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(NB_CATEGORIES,false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMaximum((float)MAX);
        yAxis.setAxisMinimum((float)MIN);
        yAxis.setDrawLabels(false);

        // Hide Description Label
        radarChart.getDescription().setEnabled(false);

        // Hide Legend
        radarChart.getLegend().setEnabled(false);
        /*
        legend.setTextSize(15f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(5f);*/



    }
}
