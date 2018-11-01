package stev.echanson.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import stev.echanson.R;
import stev.echanson.classes.ImageAdapter;

public class GalleryFragment extends Fragment {
    ViewPager viewPager;

    View mainView;

    private boolean shouldRefreshOnResume = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_gallery, null);

            updateGallery();
        }

        return mainView;
    }

    public void updateGallery(){
        GridView gridView = mainView.findViewById(R.id.gallery_grid_view);
        gridView.setAdapter(new ImageAdapter(this.getContext()));
    }

    @Override
    public void onResume(){
        super.onResume();

        // Refresh de la gallery quand on revient de CameraActivity
        if(shouldRefreshOnResume){
            updateGallery();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

}
