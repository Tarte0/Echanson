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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stev.echanson.R;
import stev.echanson.adapters.RecyclerViewAdapter;
import stev.echanson.classes.Categorie;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Model;
import stev.echanson.classes.Regime;

public class HealthConcernsFragment extends Fragment {
    private ViewPager viewPager;
    private View mainView;

    private FirebaseDatabase mDatabase;


    private RecyclerView regimesRecyclerView;
    private RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> regimesRVAdapter;
    private ArrayList<Regime> regimes;

    private RecyclerView categoriesRecyclerView;
    private RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> categoriesRVAdapter;
    private ArrayList<Categorie> categories;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_health_concerns, null);
        }

        mDatabase = FirebaseDatabase.getInstance();

        FirebaseUtils fbu = new FirebaseUtils(mDatabase);


        //   REGIMES      //////////////////////////////////////////////////////////////////////////
        regimes = new ArrayList<>();
        regimesRecyclerView = mainView.findViewById(R.id.recycler_view_regimes);

        DatabaseReference regimesRef = mDatabase.getReference(FirebaseUtils.REGIMES_PATH);
        regimesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String regimeName;
                List<String> regimeRelatedCat;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    regimeName = child.child("name").getValue(String.class);
                    regimeRelatedCat = child.child("relatedCategories").getValue(gti);
                    regimes.add(new Regime(regimeName, regimeRelatedCat));
                }

                ArrayList<String> regimeNames = new ArrayList<>();

                for(Regime r : regimes){
                    regimeNames.add(r.getName());
                }

                regimesRVAdapter = new RecyclerViewAdapter(getListData(regimeNames),
                        getResources().getColor(R.color.colorPrimaryDark));

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                regimesRecyclerView.setHasFixedSize(true);
                regimesRecyclerView.setLayoutManager(manager);
                regimesRecyclerView.setAdapter(regimesRVAdapter);
                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //   CATEGORIES   //////////////////////////////////////////////////////////////////////////
        categories = new ArrayList<>();
        categoriesRecyclerView = mainView.findViewById(R.id.recycler_view_categories);

        DatabaseReference categoriesRef = mDatabase.getReference(FirebaseUtils.CATEGORIES_PATH);
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                String catName;
                List<String> catRelatedCat;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    catName = child.child("name").getValue(String.class);
                    catRelatedCat = child.child("relatedCategories").getValue(gti);
                    categories.add(new Categorie(catName, catRelatedCat));
                }

                ArrayList<String> categoriesNames = new ArrayList<>();

                for(Categorie c : categories){
                    categoriesNames.add(c.getName());
                }

                categoriesRVAdapter = new RecyclerViewAdapter(getListData(categoriesNames),
                        getResources().getColor(R.color.colorPrimaryDark));

                LinearLayoutManager manager = new LinearLayoutManager(mainView.getContext());

                categoriesRecyclerView.setHasFixedSize(true);
                categoriesRecyclerView.setLayoutManager(manager);
                categoriesRecyclerView.setAdapter(categoriesRVAdapter);
                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
