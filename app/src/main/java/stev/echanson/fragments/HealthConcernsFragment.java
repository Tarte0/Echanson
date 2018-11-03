package stev.echanson.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import stev.echanson.R;
import stev.echanson.activities.ClassifierActivity;
import stev.echanson.activities.HealthConcernsUpdateActivity;
import stev.echanson.adapters.ChipGridViewAdapter;
import stev.echanson.adapters.ChipRecyclerViewAdapter;
import stev.echanson.adapters.TextChipRecyclerViewAdapter;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Ingredient;
import stev.echanson.classes.Model;
import stev.echanson.classes.Regime;

public class HealthConcernsFragment extends Fragment {
    private ViewPager viewPager;
    private View mainView;
    private TextView regimesTitleTV;
    private TextView categoriesTitleTV;
    private TextView ingredientsTitleTV;

    private FirebaseDatabase mDatabase;
    private FirebaseUser currentFirebaseUser;
    private String userID;

    private RecyclerView regimesLayout;
    private RecyclerView categoriesLayout;
    private RecyclerView ingredientsLayout;

    private ChipRecyclerViewAdapter regimesRVAdapter;
    private TextChipRecyclerViewAdapter categoriesRVAdapter;
    private TextChipRecyclerViewAdapter ingredientsRVAdapter;

    private ArrayList<Regime> regimes;
    private ArrayList<Categorie> categories;
    private ArrayList<Ingredient> ingredients;

    private Button updateHealthConcernsButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_health_concerns, null);
        }

        mDatabase = FirebaseDatabase.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();

        //   REGIMES      //////////////////////////////////////////////////////////////////////////
        regimesTitleTV = mainView.findViewById(R.id.regimesTextView);
        regimesLayout = mainView.findViewById(R.id.regimeChipsLayout);
        regimes = new ArrayList<>();

        DatabaseReference userRegimesRef = mDatabase.getReference(FirebaseUtils.getUserRegimesPath(userID));
        userRegimesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                regimes = new ArrayList<>();
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String name;
                String icon;
                List<String> forbids;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    icon = child.child("icon").getValue(String.class);
                    forbids = child.child("forbids").getValue(gti);
                    regimes.add(new Regime(name, icon, forbids));
                }

                HashMap<String, Drawable> regimeNamesIcons = new HashMap<>();

                Resources resources = mainView.getContext().getResources();

                for(Regime r : regimes){
                    regimeNamesIcons.put(r.getName(),
                            resources.getDrawable(resources.getIdentifier(r.getIcon(), "drawable",
                            mainView.getContext().getPackageName())));
                }

                Iterator it = regimeNamesIcons.entrySet().iterator();
                List<Map.Entry> regimesRecyclerModels = new ArrayList<>();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    regimesRecyclerModels.add(pair);
                    it.remove(); // avoids a ConcurrentModificationException
                }

                regimesRVAdapter = new ChipRecyclerViewAdapter(regimesRecyclerModels);

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                regimesLayout.setHasFixedSize(true);
                regimesLayout.setLayoutManager(manager);
                regimesLayout.setAdapter(regimesRVAdapter);
                //notifyDataSetChanged();

                if(regimesRecyclerModels.size() <=0) regimesTitleTV.setText(R.string.noReg);
                if(regimesRecyclerModels.size() >0) regimesTitleTV.setText(R.string.reg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //   CATEGORIES   //////////////////////////////////////////////////////////////////////////
        categoriesTitleTV = mainView.findViewById(R.id.categorieTextView);
        categoriesLayout = mainView.findViewById(R.id.categorieChipsLayout);
        categories = new ArrayList<>();

        DatabaseReference userCategoriesRef = mDatabase.getReference(FirebaseUtils.getUserCategoriesPath(userID));
        userCategoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories = new ArrayList<>();
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String name;
                List<String> children;
                List<String> parent;
                List<String> relatedRegime;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    children = child.child("children").getValue(gti);
                    parent = child.child("parent").getValue(gti);
                    relatedRegime = child.child("relatedRegime").getValue(gti);
                    categories.add(new Categorie(name, children, parent, relatedRegime));
                }

                List<String> categorieNames = new ArrayList<>();

                for(Categorie c : categories){
                    categorieNames.add(c.getName());
                }

                categoriesRVAdapter = new TextChipRecyclerViewAdapter(categorieNames);

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                categoriesLayout.setHasFixedSize(true);
                categoriesLayout.setLayoutManager(manager);
                categoriesLayout.setAdapter(categoriesRVAdapter);
                //notifyDataSetChanged();

                if(categorieNames.size() <=0) categoriesTitleTV.setText(R.string.noCatNoIng);
                if(categorieNames.size() >0) categoriesTitleTV.setText(R.string.catOrIng);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //   INGREDIENTS   /////////////////////////////////////////////////////////////////////////
        ingredientsTitleTV = mainView.findViewById(R.id.ingredientTextView);
        ingredientsLayout = mainView.findViewById(R.id.ingredientChipsLayout);
        ingredients = new ArrayList<>();

        DatabaseReference userIngredientsRef = mDatabase.getReference(FirebaseUtils.getUserIngredientsPath(userID));
        userIngredientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredients = new ArrayList<>();
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String name;
                List<String> categoriesOfIng;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    categoriesOfIng = child.child("categories").getValue(gti);
                    ingredients.add(new Ingredient(name, categoriesOfIng));
                }

                List<String> ingredientsNames = new ArrayList<>();

                for(Ingredient i: ingredients){
                    ingredientsNames.add(i.getName());
                }

                ingredientsRVAdapter = new TextChipRecyclerViewAdapter(ingredientsNames);

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                ingredientsLayout.setHasFixedSize(true);
                ingredientsLayout.setLayoutManager(manager);
                ingredientsLayout.setAdapter(ingredientsRVAdapter);
                //notifyDataSetChanged();

                if(ingredientsNames.size() <=0) ingredientsTitleTV.setText(R.string.noIng);
                if(ingredientsNames.size() >0) ingredientsTitleTV.setText(R.string.ing);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateHealthConcernsButton = mainView.findViewById(R.id.updateHealthConcernsButton);
        updateHealthConcernsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HealthConcernsUpdateActivity.class);
                startActivity(intent);
            }
        });

        return mainView;
    }

    private List<Model> getListData(ArrayList<String> list) {
        List<Model> mModelList = new ArrayList<>();

        for(int i=0; i<list.size();++i){
            mModelList.add(new Model(list.get(i)));
        }

        return mModelList;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
