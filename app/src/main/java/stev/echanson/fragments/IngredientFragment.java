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
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Ingredient;
import stev.echanson.classes.Model;
import stev.echanson.classes.Regime;

public class IngredientFragment extends Fragment {
    private HealthConcernsUpdateActivity baseActivity;

    ViewPager viewPager;

    View mainView;

    private List<Categorie> categories;
    private List<Ingredient> ingredients;

    List<Model> ingredientNamesModels;
    private RecyclerView ingredientList;
    private RecyclerViewAdapter ingredientsRVAdapter;

    private FirebaseDatabase database;
    private String userID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_uhc_ingredient, null);
        }

        ingredientList = mainView.findViewById(R.id.ingredientList);

        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private Categorie getCategorieFromName(String name, List<Categorie> categories) {
        for(Categorie c : categories){
            if(c.getName().equals(name)) return c;
        }
        return null;
    }

    private Ingredient getIngredientFromName(String name, List<Ingredient> ingredients) {
        for(Ingredient i : ingredients){
            if(i.getName().equals(name)) return i;
        }
        return null;
    }

    public void update(FirebaseDatabase database, String userID, List<Categorie> selectedCategories) {
        this.database = database;
        this.userID = userID;
        this.categories = selectedCategories;

        getIngredients();
    }

    private void getIngredients() {
        FirebaseUtils fbu = new FirebaseUtils(database);

        //get ingredients and filter them
        ingredients = new ArrayList<>();

        DatabaseReference ingredientsRef = database.getReference(FirebaseUtils.INGREDIENTS_PATH);
        ingredientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredients = new ArrayList<>();
                List<Ingredient> CategoriesIngredients = new ArrayList<>();

                //get all ingredients
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String name;
                List<String> categoriesOfIng;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    categoriesOfIng = child.child("categories").getValue(gti);
                    ingredients.add(new Ingredient(name, categoriesOfIng));
                }

                //filter all ingredients from the categories
                List<Ingredient> filteredIngredients = new ArrayList<>();
                Categorie catChild;

                //is ing belonging to a selected categorie ?
                //if not we store it and display it
                boolean doesNotBelongToCategorie = true;
                for(Ingredient ing : ingredients){

                    if(ing.getCategories() != null){
                        for(String cn : ing.getCategories()){
                            catChild = getCategorieFromName(cn, categories);
                            doesNotBelongToCategorie = doesNotBelongToCategorie && (catChild==null);
                        }
                        if(doesNotBelongToCategorie) filteredIngredients.add(ing);
                    }else{
                        filteredIngredients.add(ing);
                    }

                    doesNotBelongToCategorie = true;

                }

                ingredientNamesModels = new ArrayList<>();

                for (Ingredient ing : filteredIngredients) {
                    ingredientNamesModels.add(new Model(ing.getName()));
                }

                ingredientsRVAdapter = new RecyclerViewAdapter(ingredientNamesModels,
                        getResources().getColor(R.color.colorPrimaryDark));

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                ingredientList.setHasFixedSize(true);
                ingredientList.setLayoutManager(manager);
                ingredientList.setAdapter(ingredientsRVAdapter);
                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public List<Ingredient> getSelectedIngredients(){
        List<Ingredient> selectedIngredients = new ArrayList<>();
        for (Model model : ingredientNamesModels) {
            if (model.isSelected()) {
                selectedIngredients.add(getIngredientFromName(model.getText(), ingredients));
            }
        }
        return selectedIngredients;
    }
}