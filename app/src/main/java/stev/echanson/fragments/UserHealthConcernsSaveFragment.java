package stev.echanson.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import stev.echanson.R;
import stev.echanson.activities.HealthConcernsUpdateActivity;

public class UserHealthConcernsSaveFragment extends Fragment {

    private ViewPager viewPager;
    private View mainView;

    private Button saveButton;
    private Button cancelButton;
    private TextView savingTV;
    private ProgressBar saveProgressBar;
    private HealthConcernsUpdateActivity baseActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_uhc_save, null);
        }

        baseActivity = (HealthConcernsUpdateActivity) getActivity();

        saveButton = mainView.findViewById(R.id.saveButton);
        cancelButton = mainView.findViewById(R.id.cancelButton);

        savingTV = mainView.findViewById(R.id.saveCancelTV);
        saveProgressBar = mainView.findViewById(R.id.saveProgressBar);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.saveHealthConcerns(savingTV, saveProgressBar);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.cancelHealthConcerns();
            }
        });

        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
