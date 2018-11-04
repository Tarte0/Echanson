package stev.echanson.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.PopupWindow;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import stev.echanson.R;
import stev.echanson.adapters.RecyclerViewAdapter;
import stev.echanson.adapters.TextChipRecyclerViewAdapter;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Ingredient;
import stev.echanson.classes.Model;
import stev.echanson.classes.Nourriture;
import stev.echanson.classes.NutrimentsUnits;
import stev.echanson.classes.Regime;

public class ClassifyPopupActivity extends AppCompatActivity {

    private Button saveButton;
    private Button discardButton;

    private TextView nourritureScName;
    private TextView nourritureScEnergieValue;
    private TextView nourritureScGrasValue;
    private TextView nourritureScSucreValue;
    private TextView nourritureScProteineValue;
    private TextView nourritureScSelValue;

    private RecyclerView okNourritureScIngredientsList;
    private RecyclerView warningNourritureScIngredientsList;

    private static FirebaseDatabase mDatabase;
    private static FirebaseUser currentFirebaseUser;
    private static String userID;
    private FirebaseUtils fbu;

    private Nourriture distantNourriture;
    private NutrimentsUnits distantNutriments;
    private TextChipRecyclerViewAdapter okIngredientsRVAdapter;
    private TextChipRecyclerViewAdapter warningIngredientsRVAdapter;

    private List<Ingredient> ingredients;
    private List<String> userWarningIngredients;
    private List<String> nourritureWarningIngredients;
    private List<String> nourritureOkIngredients;
    private List<String> userWarningCategories;
    private ArrayList<Ingredient> nourritureIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_popup);

        Intent i = getIntent();

        String date = i.getExtras().getString("date");
        final String nourritureName = i.getExtras().getString("nourritureName").replace(' ', '_');
        String pictureB64 = i.getExtras().getString("pictureB64");

        nourritureScName = findViewById(R.id.nourritureScName);
        nourritureScEnergieValue = findViewById(R.id.nourritureScEnergieValue);
        nourritureScGrasValue = findViewById(R.id.nourritureScGrasValue);
        nourritureScSucreValue = findViewById(R.id.nourritureScSucreValue);
        nourritureScProteineValue = findViewById(R.id.nourritureScProteineValue);
        nourritureScSelValue = findViewById(R.id.nourritureScSelValue);

        okNourritureScIngredientsList = findViewById(R.id.okNourritureScIngredientsList);
        warningNourritureScIngredientsList = findViewById(R.id.warningNourritureScIngredientsList);

        LinearLayoutManager managerOk = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager managerWar = new LinearLayoutManager(getApplicationContext());

        okNourritureScIngredientsList.setHasFixedSize(true);
        warningNourritureScIngredientsList.setHasFixedSize(true);
        okNourritureScIngredientsList.setLayoutManager(managerOk);
        warningNourritureScIngredientsList.setLayoutManager(managerWar);

        mDatabase = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        fbu = new FirebaseUtils(mDatabase);


        //Fetching specific "nourriture" on FireBase
        DatabaseReference nourritureRef = mDatabase.getReference(FirebaseUtils.NOURRITURE_PATH);
        DatabaseReference targetNourriture = nourritureRef.child(nourritureName);
        targetNourriture.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distantNourriture = dataSnapshot.getValue(Nourriture.class);
                updateViews(getApplicationContext(), distantNourriture, nourritureName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fetching all ingredients
        DatabaseReference ingredientsRef = mDatabase.getReference(FirebaseUtils.INGREDIENTS_PATH);
        ingredientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredients = new ArrayList<>();

                //get all ingredients
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {
                };
                String name;
                List<String> categoriesOfIng;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    categoriesOfIng = child.child("categories").getValue(gti);
                    ingredients.add(new Ingredient(name, categoriesOfIng));
                }

                handleWarnings(getApplicationContext());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateViews(final Context c, final Nourriture distantNourriture, final String nourritureName) {

        //fetching nutriments units
        DatabaseReference nutrimentsRef = mDatabase.getReference(FirebaseUtils.NUTRIMENTS_PATH);
        nutrimentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distantNutriments = dataSnapshot.getValue(NutrimentsUnits.class);

                NumberFormat nm = NumberFormat.getNumberInstance();
                nourritureScName.setText(nourritureName);
                if (distantNourriture != null) {
                    nourritureScEnergieValue.setText(nm.format(distantNourriture.getEnergie())
                            .concat(" ")
                            .concat(distantNutriments.getEnergie()));
                    nourritureScGrasValue.setText(nm.format(distantNourriture.getGras())
                            .concat(" ")
                            .concat(distantNutriments.getGras()));
                    nourritureScSucreValue.setText(nm.format(distantNourriture.getSucre())
                            .concat(" ")
                            .concat(distantNutriments.getSucre()));
                    nourritureScProteineValue.setText(nm.format(distantNourriture.getProteine())
                            .concat(" ")
                            .concat(distantNutriments.getProteine()));
                    nourritureScSelValue.setText(nm.format(distantNourriture.getSel())
                            .concat(" ")
                            .concat(distantNutriments.getSel()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void handleIngredients(List<String> userWarningCategories,
                                   List<String> userWarningIngredients,
                                   Context c) {

        nourritureIngredients = new ArrayList<>();
        nourritureWarningIngredients = new ArrayList<>();
        nourritureOkIngredients = new ArrayList<>();

        if (distantNourriture != null) {
            List<String> warningNames = distantNourriture.getWarnings();


            //for all nourriture's ingredients
            for (String in : warningNames) {
                //user don't want this ingredient
                if(userWarningIngredients.contains(in)){
                    nourritureWarningIngredients.add(in);
                    continue;
                }

                //check if this ingredient belongs to any unwanted category
                boolean unwanted = false;
                Ingredient ni = getIngredientFromName(in, ingredients);
                if(ni != null){
                    if(ni.getCategories() != null) {
                        for (String niCat : ni.getCategories()) {
                            if (userWarningCategories.contains(niCat)) {
                                unwanted = true;
                                if(!nourritureWarningIngredients.contains(in) //we don't want ok & not ok...
                                        && !nourritureOkIngredients.contains(in)) //nor doubles...
                                    nourritureWarningIngredients.add(in); //yes
                            }
                        }
                        if (!unwanted
                                && !nourritureWarningIngredients.contains(in)
                                && !nourritureOkIngredients.contains(in))
                            nourritureOkIngredients.add(in); //no
                    }
                }else{
                    if(!nourritureWarningIngredients.contains(in) //we don't want ok & not ok...
                            && !nourritureOkIngredients.contains(in)) //nor doubles...
                        nourritureOkIngredients.add(in);
                }
            }

            okIngredientsRVAdapter = new TextChipRecyclerViewAdapter(nourritureOkIngredients);
            warningIngredientsRVAdapter = new TextChipRecyclerViewAdapter(nourritureWarningIngredients);

            okNourritureScIngredientsList.setAdapter(okIngredientsRVAdapter);
            warningNourritureScIngredientsList.setAdapter(warningIngredientsRVAdapter);
        }
    }

    private void handleWarnings(final Context c) {
        //fetching user's warnings
        userWarningCategories = new ArrayList<>();

        DatabaseReference userRef = mDatabase.getReference(FirebaseUtils.getUserPath(userID));

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {
                };

                //get all user's categories from regimes
                List<String> categoriesTmp = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.child("regimes").getChildren()) {
                    categoriesTmp = child.child("forbids").getValue(gti);
                    for (String category : categoriesTmp) {
                        if (!userWarningCategories.contains(category))
                            userWarningCategories.add(category);
                    }
                }

                List<String> children;
                List<String> parent;


                //get all user's categories
                for (DataSnapshot catChild : dataSnapshot.child("categories").getChildren()) {
                    categoriesTmp = new ArrayList<>();

                    children = catChild.child("children").getValue(gti);
                    parent = catChild.child("parent").getValue(gti);
                    categoriesTmp.add(catChild.child("name").getValue(String.class));

                    if (children == null) children = new ArrayList<>();
                    if (parent != null) children.addAll(parent);

                    categoriesTmp.addAll(children);

                    for (String category : categoriesTmp) {
                        if (!userWarningCategories.contains(category))
                            userWarningCategories.add(category);
                    }
                }

                //get all user's ingredients
                String name;
                userWarningIngredients = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    userWarningIngredients.add(name);
                }

                handleIngredients(userWarningCategories, userWarningIngredients, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Ingredient getIngredientFromName(String name, List<Ingredient> ingredients) {
        for (Ingredient i : ingredients) {
            if (i.getName().equals(name)) return i;
        }
        return null;
    }
}
