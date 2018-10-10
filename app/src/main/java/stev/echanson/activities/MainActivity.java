package stev.echanson.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import stev.echanson.R;
import stev.echanson.classes.Classifier;
import stev.echanson.views.LeftActivityViewPager;
import stev.echanson.fragments.AnalyticsFragment;
import stev.echanson.fragments.GalleryFragment;
import stev.echanson.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ProfileFragment pf;
    GalleryFragment gf;
    AnalyticsFragment af;

    // -1 - left, 0 - center, 1 - right
    private int scroll = 0;
    // set only on `onPageSelected` use it in `onPageScrolled`
    // if currentPage < page - we swipe from left to right
    // if currentPage == page - we swipe from right to left  or centered
    private int currentPage = 0;
    // if currentPage < page offset goes from `screen width` to `0`
    // as you reveal right fragment.
    // if currentPage == page , offset goes from `0` to `screen width`
    // as you reveal right fragment
    // You can use it to see
    //if user continue to reveal next fragment or moves it back
    private int currentOffset = 0;
    // behaves similar to offset in range `[0..1)`
    private float currentScale = 0;

    private boolean isLeftActivityRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_profile:
                        viewPager.setCurrentItem(0);
                        item.setChecked(true);
                        break;
                    case R.id.bottom_nav_gallery:
                        viewPager.setCurrentItem(1);
                        item.setChecked(true);
                        break;
                    case R.id.bottom_nav_analytics:
                        viewPager.setCurrentItem(2);
                        item.setChecked(true);
                        break;
                }
                return false;

            }
        });

        //viewPager.setCurrentItem(0);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        pf = new ProfileFragment();
        pf.setVp(viewPager);
        gf = new GalleryFragment();
        gf.setVp(viewPager);
        af = new AnalyticsFragment();
        af.setVp(viewPager);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(pf, "Profile");
        adapter.addFragment(gf, "Gallery");
        adapter.addFragment(af, "Analytics");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void goToCameraActivity() {
        Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isLeftActivityRunning = false;
    }
}
