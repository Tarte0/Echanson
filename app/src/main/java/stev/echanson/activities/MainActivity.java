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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import stev.echanson.R;
import stev.echanson.fragments.AnalyticsFragment;
import stev.echanson.fragments.GalleryFragment;
import stev.echanson.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ProfileFragment pf;
    GalleryFragment gf;
    AnalyticsFragment af;

    LinearLayout mainLinearLayout;
    Toolbar toolbar;
    ImageButton toolbarCameraButton;

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

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        toolbar = findViewById(R.id.mainToolbar);
        toolbarCameraButton = toolbar.findViewById(R.id.toolbarCameraIB);

        toolbarCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCameraActivity(v);
            }
        });

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

    public void goToCameraActivity(View v) {
        Intent intent = new Intent(v.getContext(), ClassifierActivity.class);
        startActivity(intent);
    }

}
