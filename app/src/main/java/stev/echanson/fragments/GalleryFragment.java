package stev.echanson.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stev.echanson.R;
import stev.echanson.activities.FullImageActivity;
import stev.echanson.classes.Food;
import stev.echanson.classes.ImageAdapter;
import stev.echanson.classes.ImageUtils;

public class GalleryFragment extends Fragment {
    ViewPager viewPager;

    View mainView;

    private static FirebaseDatabase mDatabase;
    private static FirebaseUser currentFirebaseUser;
    private static String userID;

    private ArrayList<Food> food;
    private Bitmap[] pictures;

    private boolean shouldRefreshOnResume = false;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_gallery, null);
            updateGallery(getPicturesFromFirebase());
        }
        return mainView;
    }

    public void updateGallery(Bitmap[] pictures){
        GridView gridView = mainView.findViewById(R.id.gallery_grid_view);
        gridView.setAdapter(new ImageAdapter(this.getContext(), pictures));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent  = new Intent(getContext(), FullImageActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
    }

    public Bitmap[] getPicturesFromFirebase() {
        food = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //userID = currentFirebaseUser.getUid();

        DatabaseReference userImageRef = mDatabase.getReference("users/PfY3DNTX0ZZmpcUjLhRRwcUMf0C3/pictures");

        userImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food = new ArrayList<>();
                String date;
                String nourriture;
                String imgB64;
                int i = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    date = child.child("date").getValue(String.class);
                    nourriture = child.child("nourriture").getValue(String.class);
                    imgB64 = child.child("picture").getValue(String.class);
                    food.add(new Food(date, nourriture, imgB64));
                    i++;
                }
                pictures = new Bitmap[i];
                for(int j = 0; j<i; j++){
                    pictures[j] = ImageUtils.convertB64ToBitmap(food.get(j).getPicture());
                }

                updateGallery(pictures);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return pictures;
    }

    @Override
    public void onResume(){
        super.onResume();

        // Refresh de la gallery quand on revient de CameraActivity
        if(shouldRefreshOnResume){
            updateGallery(getPicturesFromFirebase());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

}
