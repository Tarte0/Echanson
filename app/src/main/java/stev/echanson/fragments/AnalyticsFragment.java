package stev.echanson.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import stev.echanson.R;
import stev.echanson.classes.ChartHelper;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Food;
import stev.echanson.classes.Nourriture;

public class AnalyticsFragment extends Fragment {
    ViewPager viewPager;

    View mainView;

    private static FirebaseDatabase mDatabase;
    private static FirebaseUser currentFirebaseUser;
    private static String userID;
    private FirebaseUtils fbu;

    private HorizontalBarChart barChart;

    List<Food> userEatenFoods;

    HashMap<String, Nourriture> nourritureHashMap;
    HashMap<String, Integer> eatenFoodsQuantity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_analytics, null);
        }

        barChart = mainView.findViewById(R.id.bar_chart);

        ChartHelper.getBarChart2(getContext(),barChart,0,0,0,0);

        mDatabase = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        fbu = new FirebaseUtils(mDatabase);

        nourritureHashMap = new HashMap<>();
        eatenFoodsQuantity = new HashMap<>();

        createNourritureMap();

        return mainView;
    }

    private void createNourritureMap(){
        //Fetching specific "nourriture" on FireBase
        DatabaseReference nourritureRef = mDatabase.getReference(FirebaseUtils.NOURRITURE_PATH);

        nourritureHashMap = new HashMap<>();
        eatenFoodsQuantity = new HashMap<>();

        nourritureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Nourriture n;
                for (DataSnapshot nourriture : dataSnapshot.getChildren()) {
                    n = nourriture.getValue(Nourriture.class);
                    nourritureHashMap.put(nourriture.getKey(), n);
                }

                getAllFoodsNutrients();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllFoodsNutrients(){
        DatabaseReference userEatenFoodsRef = mDatabase.getReference(FirebaseUtils.getUserEatenFoodsPath(userID));

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date currentDate = new Date();
        final String currentDateString = dateFormat.format(currentDate); //2016/11/16 12:08:43

        Food eatenFood;
        userEatenFoods = new ArrayList<>();

        userEatenFoodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userEatenFoods = new ArrayList<>();
                    String efDate;
                    String efNourriture;
                    int quantity;

                    for (DataSnapshot eatenFoodData : dataSnapshot.getChildren()) {
                        efDate = eatenFoodData.child("date").getValue(String.class);
                        efNourriture = eatenFoodData.child("nourriture").getValue(String.class);

                        if(parseDate(efDate).equals(currentDateString)){
                            quantity = eatenFoodsQuantity.containsKey(efNourriture) ?
                                    eatenFoodsQuantity.get(efNourriture) : 0;
                            eatenFoodsQuantity.put(efNourriture, quantity+1);
                        }
                    }

                    updateUserValues();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUserValues(){
        double sucre = 0;
        double sel = 0;
        double gras = 0;
        double proteine = 0;
        Nourriture n;
        Iterator it = eatenFoodsQuantity.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            n = nourritureHashMap.get(pair.getKey());
            if(n!=null) {
                sucre += n.getSucre() * (int) pair.getValue();
                sel += n.getSel() * (int) pair.getValue();
                gras += n.getGras() * (int) pair.getValue();
                proteine += n.getProteine() * (int) pair.getValue();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        ChartHelper.getBarChart2(getContext(),barChart,sucre,sel,gras,proteine);
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private String parseDate(String timestamp){
        SimpleDateFormat dayTime = new SimpleDateFormat("dd/MM/yy");
        return dayTime.format(new Date(Long.parseLong(timestamp)));
    }

}
