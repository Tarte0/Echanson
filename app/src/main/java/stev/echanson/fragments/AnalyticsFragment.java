package stev.echanson.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import stev.echanson.R;
import stev.echanson.classes.ChartHelper;

public class AnalyticsFragment extends Fragment {
    ViewPager viewPager;

    View mainView;

    private HorizontalBarChart barChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_analytics, null);
        }

        barChart = mainView.findViewById(R.id.bar_chart);

        ChartHelper.getBarChart2(getContext(),barChart);

        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

}
