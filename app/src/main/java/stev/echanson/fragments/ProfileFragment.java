package stev.echanson.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stev.echanson.R;

public class ProfileFragment extends Fragment {
    ViewPager viewPager;

    View mainView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_profile, null);
        }

       return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

}
