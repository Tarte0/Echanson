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
import stev.echanson.fragments.HealthConcernsFragment;
import stev.echanson.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfileFragment pf;
    private GalleryFragment gf;
    private AnalyticsFragment af;
    private HealthConcernsFragment hcf;

    private LinearLayout mainLinearLayout;
    private Toolbar toolbar;
    private ImageButton toolbarCameraButton;

    private  MenuItem prevMenuItem;


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
                    case R.id.bottom_nav_analytics:
                        viewPager.setCurrentItem(0);
                        item.setChecked(true);
                        break;
                    case R.id.bottom_nav_gallery:
                        viewPager.setCurrentItem(1);
                        item.setChecked(true);
                        break;
                    case R.id.bottom_nav_health:
                        viewPager.setCurrentItem(2);
                        item.setChecked(true);
                        break;
                    case R.id.bottom_nav_profile:
                        viewPager.setCurrentItem(3);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        viewPager = findViewById(R.id.viewpager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(prevMenuItem != null){
                    prevMenuItem.setChecked(false);
                }
                else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        bottomNavigationView.getMenu().getItem(0).setChecked(true);


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


        af = new AnalyticsFragment();
        af.setVp(viewPager);
        gf = new GalleryFragment();
        gf.setVp(viewPager);
        hcf = new HealthConcernsFragment();
        hcf.setVp(viewPager);
        pf = new ProfileFragment();
        pf.setVp(viewPager);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(af, "Analytics");
        adapter.addFragment(gf, "Gallery");
        adapter.addFragment(hcf, "Health Concerns");
        adapter.addFragment(pf, "Profile");
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
