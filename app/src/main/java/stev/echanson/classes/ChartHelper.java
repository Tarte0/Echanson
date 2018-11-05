package stev.echanson.classes;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class ChartHelper {

    public static void getBarChart (Context context, BarChart barChart){

        String[] nutrimentsNames = new String[] {"Sucre","Sel","Proteine","Gras"};
        double[] nutrimentsDailyValues = new double[] {50.0,5.0,55.0,93.0};
        double[] nutrimentsUserValues = new double[] {25.0,4.7,76.0,85.7};


        ArrayList<BarEntry> nutrimentsUtilisateur = new ArrayList<>();
        ArrayList<BarEntry> nutrimentsJournaliers = new ArrayList<>();

        for(int i=0; i<nutrimentsNames.length;i++){
            nutrimentsUtilisateur.add(new BarEntry(i,(float)nutrimentsUserValues[i]));
            nutrimentsJournaliers.add(new BarEntry(i,(float)nutrimentsDailyValues[i]));
        }

        BarDataSet set1 = new BarDataSet(nutrimentsUtilisateur, "Utilisateur");
        BarDataSet set2 = new BarDataSet(nutrimentsJournaliers, "Journaliers");


        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

        ArrayList<IBarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        BarData data = new BarData(sets);
        data.setBarWidth(barWidth); // set the width of each bar
        barChart.setData(data);
        barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.invalidate(); // refresh

    }

    public static void getBarChart2(Context context, HorizontalBarChart barChart){
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);

        barChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> SucreUtilisateur = new ArrayList<>();
        SucreUtilisateur.add(new BarEntry(0,(float)25.0));

        ArrayList<BarEntry> SucreJournaliers = new ArrayList<>();
        SucreJournaliers.add(new BarEntry(0,(float)50.0));

        ArrayList<BarEntry> SelUtilisateur = new ArrayList<>();
        SelUtilisateur.add(new BarEntry(0,(float)4.7));

        ArrayList<BarEntry> SelJournaliers = new ArrayList<>();
        SelJournaliers.add(new BarEntry(0,(float)5.0));

        ArrayList<BarEntry> ProteineUtilisateur = new ArrayList<>();
        ProteineUtilisateur.add(new BarEntry(0,(float)76.0));

        ArrayList<BarEntry> ProteineJournaliers = new ArrayList<>();
        ProteineJournaliers.add(new BarEntry(0,(float)55.0));

        ArrayList<BarEntry> GrasUtilisateur = new ArrayList<>();
        GrasUtilisateur.add(new BarEntry(0,(float)83.7));

        ArrayList<BarEntry> GrasJournaliers = new ArrayList<>();
        GrasJournaliers.add(new BarEntry(0,(float)93.0));

        BarDataSet set1 = new BarDataSet(SucreUtilisateur, "Sucre ");
        BarDataSet set2 = new BarDataSet(SucreJournaliers, "Seuil Sucre");
        BarDataSet set3 = new BarDataSet(SelUtilisateur, "Sel");
        BarDataSet set4 = new BarDataSet(SelJournaliers, "Seuil Sel");
        BarDataSet set5 = new BarDataSet(ProteineUtilisateur, "Proteine");
        BarDataSet set6 = new BarDataSet(ProteineJournaliers, "Seuil Proteine");
        BarDataSet set7 = new BarDataSet(GrasUtilisateur, "Gras");
        BarDataSet set8 = new BarDataSet(GrasJournaliers, "Seuil Gras");


        set1.setColor(Color.rgb(86,180,233));
        set2.setColor(Color.rgb(56,150,203));
        set3.setColor(Color.rgb(240,228,66));
        set4.setColor(Color.rgb(210,198,36));
        set5.setColor(Color.rgb(204,121,167));
        set6.setColor(Color.rgb(174,91,137));
        set7.setColor(Color.rgb(230,159,0));
        set8.setColor(Color.rgb(200,129,0));

        ArrayList<IBarDataSet> sets = new ArrayList<>();

        sets.add(set8);
        sets.add(set7);
        sets.add(set6);
        sets.add(set5);
        sets.add(set4);
        sets.add(set3);
        sets.add(set2);
        sets.add(set1);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.035f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData data = new BarData(sets);
        data.setBarWidth(barWidth); // set the width of each bar
        barChart.setData(data);
        barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.invalidate(); // refresh

        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum((float)0.0);
        xAxis.setEnabled(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setWordWrapEnabled(true);

    }
}
