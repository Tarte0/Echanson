package stev.echanson.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import stev.echanson.R;
import stev.echanson.classes.FirebaseUtils;
import stev.echanson.classes.ImageUtils;
import stev.echanson.classes.Nourriture;

public class FullImageActivity extends AppCompatActivity {

    private static FirebaseDatabase mDatabase;
    private static FirebaseUser currentFirebaseUser;
    private static String userID;

    private FirebaseUtils fbu;

    private Nourriture distantNourriture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        ImageView imageView = findViewById(R.id.fullImageView);

        mDatabase = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        fbu = new FirebaseUtils(mDatabase);

        Intent i = getIntent();

        // Get image infos
        String date = i.getExtras().getString("date");
        String nourritureName = i.getExtras().getString("nourriture");

        //Fetching specific "nourriture" on FireBase
        DatabaseReference nourritureRef = mDatabase.getReference(fbu.getTargetNourriturePathFromFirebase(nourritureName));
        DatabaseReference targetNourriture = nourritureRef.child(nourritureName);
        targetNourriture.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distantNourriture = dataSnapshot.getValue(Nourriture.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Bitmap picture = ImageUtils.convertB64ToBitmap(i.getExtras().getString("picture"));
        imageView.setImageBitmap(picture);

        SimpleDateFormat dayTime = new SimpleDateFormat("dd/MM/yy");
        String strDate = dayTime.format(new Date(Long.parseLong(date)));


        TextView textView = findViewById(R.id.textView2);
        textView.setText(getString(R.string.full_image_text, strDate,nourritureName));

        Toolbar toolbar = findViewById(R.id.previousToolbar);
        ImageButton toolbarButton = toolbar.findViewById(R.id.toolbarPreviousB);

        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
