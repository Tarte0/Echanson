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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import stev.echanson.R;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Ingredient;
import stev.echanson.classes.Regime;
import stev.echanson.fragments.CategorieFragment;
import stev.echanson.fragments.IngredientFragment;
import stev.echanson.fragments.RegimeFragment;
import stev.echanson.fragments.UserHealthConcernsSaveFragment;

public class HealthConcernsUpdateActivity extends AppCompatActivity {
    private ImageButton nextStepButton;
    private ImageButton previousStepButton;
    private ProgressBar progressBarStep;

    private ViewPager viewPager;
    private RegimeFragment regf;
    private CategorieFragment catf;
    private IngredientFragment ingf;
    private UserHealthConcernsSaveFragment savf;

    private FirebaseDatabase mDatabase;
    private FirebaseUser currentFirebaseUser;
    private String userID;

    private int currentStep = 0;
    private int stepNumber = 3;

    private List<Categorie> selectedCategories;

    private List<Regime> userRegimes;
    private List<Categorie> userCategories;
    private List<Ingredient> userIngredients;

    private int saveProgress = 0;
    private ProgressBar saveProgressBar;


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
                int previousStep = currentStep;
                if (currentStep < stepNumber){
                    currentStep++;
                    updateStep(previousStep, currentStep);
                }
            }
        });

        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousStep = currentStep;
                if (currentStep > 0){
                    currentStep--;
                    updateStep(previousStep, currentStep);
                }
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
        savf = new UserHealthConcernsSaveFragment();
        savf.setVp(viewPager);

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(regf, "Regimes");
        adapter.addFragment(catf, "Categories");
        adapter.addFragment(ingf, "Ingredients");
        adapter.addFragment(savf, "Save");

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

    private void updateStep(int previousStep, int currentStep) {
        updateProgressBar(currentStep);
        boolean isNext = previousStep < currentStep;
        switch (currentStep) {
            case 0:
                regimeStep();
                return;
            case 1:
                categorieStep(isNext);
                return;
            case 2:
                ingredientStep();
                return;
            case 3:
                saveStep();

        }
    }

    private void regimeStep() {
        viewPager.setCurrentItem(0);
        regf.update(mDatabase, userID);
    }

    private void categorieStep(boolean isNext) {
        if(isNext) userRegimes = regf.getSelectedRegimes();
        viewPager.setCurrentItem(1);
        catf.update(mDatabase, userID, userRegimes);
    }

    private void ingredientStep() {
        selectedCategories = catf.getSelectedCategories();
        userCategories = catf.getAllSelectedCategories();
        viewPager.setCurrentItem(2);
        ingf.update(mDatabase, userID, userCategories);
    }

    private void saveStep() {
        viewPager.setCurrentItem(3);
    }

    public List<Regime> getRegimes(){
        return userRegimes;
    }

    public List<Categorie> getCategories(){
        return userCategories;
    }

    public void saveHealthConcerns(TextView savingTV, ProgressBar progressBar){
        savingTV.setVisibility(View.VISIBLE);

        saveProgressBar = progressBar;

        saveProgressBar.setVisibility(View.VISIBLE);

        saveProgressBar.setProgress(0);
        userIngredients = ingf.getSelectedIngredients();
        saveProgress = 0;

        saveUserRegimes(userRegimes, userID);
        saveUserCategories(selectedCategories, userID);
        saveUserIngredients(userIngredients, userID);

        if(saveProgress >= 3){
            savingTV.setVisibility(View.INVISIBLE);
            saveProgressBar.setVisibility(View.INVISIBLE);
            finish();
        }
    }

    private void saveUserRegimes(List<Regime> userRegimes, String userID) {
        DatabaseReference userRegimesRef = mDatabase.getReference(FirebaseUtils.getUserRegimesPath(userID));
        userRegimesRef.setValue("");
        if(userRegimes != null) {
            DatabaseReference userNewRegimesRef;
            for (Regime r : userRegimes) {
                userNewRegimesRef = mDatabase.getReference(
                        FirebaseUtils.getUserNewRegimesPath(userID, r.getName()));
                userNewRegimesRef.setValue(r);
            }
        }
        updateProgress();
    }

    private void saveUserCategories(List<Categorie> userCategories, String userID) {
        DatabaseReference userCategoriesRef = mDatabase.getReference(FirebaseUtils.getUserCategoriesPath(userID));
        userCategoriesRef.setValue("");
        if(userCategories != null) {
            DatabaseReference userNewCategoriesRef;
            for (Categorie c : userCategories) {
                userNewCategoriesRef = mDatabase.getReference(
                        FirebaseUtils.getUserNewCategoriesPath(userID, c.getName()));
                userNewCategoriesRef.setValue(c);
            }
            updateProgress();
        }
    }

    private void saveUserIngredients(List<Ingredient> userIngredients, String userID) {
        DatabaseReference userIngredientsRef = mDatabase.getReference(FirebaseUtils.getUserIngredientsPath(userID));
        userIngredientsRef.setValue("");
        if(userIngredients != null) {
            DatabaseReference userNewIngredientsRef;
            for (Ingredient i : userIngredients) {
                userNewIngredientsRef = mDatabase.getReference(
                        FirebaseUtils.getUserNewIngredientsPath(userID, i.getName()));
                userNewIngredientsRef.setValue(i);
            }
            updateProgress();
        }
    }

    public void cancelHealthConcerns(){
        finish();
    }

    public int getSaveProgress(){
        return saveProgress;
    }

    public void updateProgress(){
        saveProgress++;
        saveProgressBar.setProgress(saveProgress*33);
    }

}
