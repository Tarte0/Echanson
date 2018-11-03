package stev.echanson.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import stev.echanson.R;
import stev.echanson.activities.HealthConcernsUpdateActivity;
import stev.echanson.adapters.RecyclerViewAdapter;
import stev.echanson.adapters.TextChipRecyclerViewAdapter;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Model;
import stev.echanson.classes.Regime;

public class CategorieFragment extends Fragment {
    private HealthConcernsUpdateActivity baseActivity;
    private ViewPager viewPager;

    private View mainView;

    private List<Regime> regimes;
    private List<Categorie> categories;

    List<Model> categorieNamesModels;
    private RecyclerView categorieList;
    private RecyclerViewAdapter categoriesRVAdapter;

    private FirebaseDatabase database;
    private String userID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_uhc_categorie, null);
        }

        baseActivity = (HealthConcernsUpdateActivity) getActivity();

        regimes = baseActivity.getRegimes();

        categorieList = mainView.findViewById(R.id.categorieList);

        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void update(FirebaseDatabase database, String userID, List<Regime> selectedRegimes) {
        this.database = database;
        this.userID = userID;
        this.regimes = selectedRegimes;

        getCategories();
    }

    private void getCategories() {
        FirebaseUtils fbu = new FirebaseUtils(database);

        //get categories and filter them from the regimes
        categories = new ArrayList<>();

        DatabaseReference categoriesRef = database.getReference(FirebaseUtils.CATEGORIES_PATH);
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories = new ArrayList<>();
                List<Categorie> regimesCategories = new ArrayList<>();

                //get all categories
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

                //get all categories from the regimes
                Categorie cat;
                Categorie catChild;
                if(regimes != null){
                    for(Regime r : regimes){
                        if(r.getForbids() != null){
                            for(String cn : r.getForbids()){
                                cat = getCategorieFromName(cn, categories);

                                //get all categorie's children and add them to the filter
                                if(cat.getChildren() != null) {
                                    for (String catChildName : cat.getChildren()) {
                                        catChild = getCategorieFromName(catChildName, categories);
                                        if (!regimesCategories.contains(catChild))
                                            regimesCategories.add(catChild);
                                    }
                                }

                                //add the category itself
                                if(!regimesCategories.contains(cat))
                                    regimesCategories.add(cat);
                            }
                        }
                    }
                }

                //filter all the categories from the regimes categories
                List<Categorie> filteredCategories = new ArrayList<>();
                if(categories != null) {
                    for(Categorie c : categories){
                        if(!regimesCategories.contains(c))
                            filteredCategories.add(c);
                    }
                }

                categorieNamesModels = new ArrayList<>();

                for (Categorie c : filteredCategories) {
                    categorieNamesModels.add(new Model(c.getName()));
                }

                categoriesRVAdapter = new RecyclerViewAdapter(categorieNamesModels,
                        getResources().getColor(R.color.colorPrimaryDark));

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                categorieList.setHasFixedSize(true);
                categorieList.setLayoutManager(manager);
                categorieList.setAdapter(categoriesRVAdapter);
                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Categorie getCategorieFromName(String name, List<Categorie> categories) {
        for(Categorie c : categories){
            if(c.getName().equals(name)) return c;
        }
        return null;
    }
}
