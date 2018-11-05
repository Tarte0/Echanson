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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import stev.echanson.R;
import stev.echanson.activities.FullImageActivity;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.Food;
import stev.echanson.classes.ImageAdapter;
import stev.echanson.classes.ImageUtils;

public class GalleryFragment extends Fragment {
    ViewPager viewPager;

    View mainView;

    private static FirebaseDatabase mDatabase;
    private static FirebaseUser currentFirebaseUser;
    private static String userID;
    private FirebaseUtils fbu;

    private ArrayList<Food> foods;
    private Bitmap[] pictures;

    private boolean shouldRefreshOnResume = false;

    private GridView gridView;
    private ImageAdapter imageAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_gallery, null);
        }

        gridView = mainView.findViewById(R.id.gallery_grid_view);
        imageAdapter = new ImageAdapter(this.getContext(), pictures);

        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent  = new Intent(getContext(), FullImageActivity.class);

                intent.putExtra("picture",foods.get(position).getPicture());
                intent.putExtra("nourriture",foods.get(position).getNourriture());
                intent.putExtra("date",foods.get(position).getDate());

                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();
        fbu = new FirebaseUtils(mDatabase);

        updateGallery(getPicturesFromFirebase());

        return mainView;
    }

    public void updateGallery(final Bitmap[] pictures){
        imageAdapter = new ImageAdapter(getContext(), pictures);
        gridView.setAdapter(imageAdapter);
    }


    public Bitmap[] getPicturesFromFirebase() {
        foods = new ArrayList<>();

        DatabaseReference userImageRef = mDatabase.getReference(FirebaseUtils.getUserEatenFoodsPath(userID));

        userImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods = new ArrayList<>();
                String date;
                String nourriture;
                String imgB64;
                int i = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()){

                    date = child.child("date").getValue(String.class);
                    nourriture = child.child("nourriture").getValue(String.class);
                    imgB64 = child.child("picture").getValue(String.class);

                    foods.add(new Food(date, nourriture, imgB64));
                    i++;
                }
                if (i > 0) {
                    pictures = new Bitmap[i];
                    for (int j = 0; j < i; j++) {
                        pictures[j] = ImageUtils.convertB64ToBitmap(foods.get(j).getPicture());
                    }

                    updateGallery(pictures);
                }
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

        // Gallery refresh on CameraActivity's comeback
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
