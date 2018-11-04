package stev.echanson.fragments;

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
import stev.echanson.activities.HealthConcernsUpdateActivity;
import stev.echanson.adapters.ChipRecyclerViewAdapter;
import stev.echanson.adapters.RecyclerViewAdapter;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Model;
import stev.echanson.classes.Regime;

public class RegimeFragment extends Fragment {
    private HealthConcernsUpdateActivity baseActivity;
    private ViewPager viewPager;

    private View mainView;

    private FirebaseDatabase database;
    private String userID;

    List<Regime> regimes;
    List<Model> regimeNamesModels;
    private RecyclerView regimeList;
    private RecyclerViewAdapter regimesRVAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_uhc_regime, null);
        }

        baseActivity = (HealthConcernsUpdateActivity) getActivity();

        database = FirebaseDatabase.getInstance();

        regimeList = mainView.findViewById(R.id.regimesList);
        regimes = new ArrayList<>();

        FirebaseUtils fbu = new FirebaseUtils(database);

        DatabaseReference regimesRef = database.getReference(FirebaseUtils.REGIMES_PATH);
        regimesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                regimes = new ArrayList<>();
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {
                };
                String name;
                String icon;
                List<String> forbids;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    name = child.child("name").getValue(String.class);
                    icon = child.child("icon").getValue(String.class);
                    forbids = child.child("forbids").getValue(gti);
                    regimes.add(new Regime(name, icon, forbids));
                }

                regimeNamesModels = new ArrayList<>();

                for (Regime r : regimes) {
                    regimeNamesModels.add(new Model(r.getName()));
                }

                regimesRVAdapter = new RecyclerViewAdapter(regimeNamesModels,
                        getResources().getColor(R.color.colorPrimaryDark));

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                regimeList.setHasFixedSize(true);
                regimeList.setLayoutManager(manager);
                regimeList.setAdapter(regimesRVAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public List<Regime> getSelectedRegimes(){
        List<Regime> selectedRegimes = new ArrayList<>();
        for (Model model : regimeNamesModels) {
            if (model.isSelected()) {
                selectedRegimes.add(getRegimeFromName(model.getText(), regimes));
            }
        }
        return selectedRegimes;
    }

    private Regime getRegimeFromName(String name, List<Regime> regimes) {
        for(Regime r : regimes){
            if(r.getName().equals(name)) return r;
        }
        return null;
    }

    public void update(FirebaseDatabase database, String userID) {
        this.database = database;
        this.userID = userID;
    }
}