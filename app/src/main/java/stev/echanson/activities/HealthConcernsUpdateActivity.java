package stev.echanson.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import stev.echanson.R;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.Ingredient;
import stev.echanson.classes.Regime;
import stev.echanson.fragments.CategorieFragment;
import stev.echanson.fragments.IngredientFragment;
import stev.echanson.fragments.RegimeFragment;

public class HealthConcernsUpdateActivity extends AppCompatActivity {
    private ImageButton nextStepButton;
    private ImageButton previousStepButton;
    private ProgressBar progressBarStep;

    private ViewPager viewPager;
    private RegimeFragment regf;
    private CategorieFragment catf;
    private IngredientFragment ingf;

    private FirebaseDatabase mDatabase;
    private FirebaseUser currentFirebaseUser;
    private String userID;

    private int currentStep = 0;
    private int stepNumber = 3;

    private List<Regime> regimes;
    private List<Categorie> categories;
    private List<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_concerns_update);

        mDatabase = FirebaseDatabase.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();

        nextStepButton = findViewById(R.id.nextBottomStepper);
        previousStepButton = findViewById(R.id.previousBottomStepper);
        progressBarStep = findViewById(R.id.progressBarBottomStepper);

        viewPager = findViewById(R.id.userHealtParamsViewPager);
        setupViewPager(viewPager);

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < stepNumber) currentStep++;
                updateStep(currentStep);
            }
        });

        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) currentStep--;
                updateStep(currentStep);
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {

        regf = new RegimeFragment();
        regf.setVp(viewPager);
        catf = new CategorieFragment();
        catf.setVp(viewPager);
        ingf = new IngredientFragment();
        ingf.setVp(viewPager);

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(regf, "Regimes");
        adapter.addFragment(catf, "Categories");
        adapter.addFragment(ingf, "Ingredients");

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

    private void updateProgressBar(int currentStep) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBarStep.setProgress(currentStep * 34, true);
        } else {
            progressBarStep.setProgress(currentStep * 34);
        }
    }

    private void updateStep(int currentStep) {
        updateProgressBar(currentStep);
        switch (currentStep) {
            case 0:
                regimeStep();
                return;
            case 1:
                categorieStep();
                return;
            case 2:
                ingredientStep();

        }
    }

    private void regimeStep() {
        viewPager.setCurrentItem(0);
        regf.update(mDatabase, userID);
    }

    private void categorieStep() {
        regimes = regf.getSelectedRegimes();
        viewPager.setCurrentItem(1);
        catf.update(mDatabase, userID, regimes);
    }

    private void ingredientStep() {
        viewPager.setCurrentItem(2);
    }

    public List<Regime> getRegimes(){
        return regimes;
    }



}
