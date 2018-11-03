package stev.echanson.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import stev.echanson.R;
import stev.echanson.classes.ImageUtils;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        ImageView imageView = findViewById(R.id.fullImageView);

        Intent i = getIntent();

        /*
        int position =  i.getExtras().getInt("id");
        String[] pictures  = i.getExtras().getStringArray("pictures");

        Bitmap[] picturesBitmap = new Bitmap[pictures.length];

        for(int j =0; j < pictures.length; j++){
            picturesBitmap[j] = ImageUtils.convertB64ToBitmap(pictures[j]);
        }

        ImageAdapter adapter = new ImageAdapter(this,picturesBitmap);

        imageView.setImageBitmap(adapter.pictures[position]);
        */

        // Récupération des informations de l'image
        String date = i.getExtras().getString("date");
        String nourriture = i.getExtras().getString("nourriture");

        Bitmap picture = ImageUtils.convertB64ToBitmap(i.getExtras().getString("picture"));
        imageView.setImageBitmap(picture);

        SimpleDateFormat dayTime = new SimpleDateFormat("dd/MM/yy");
        String strDate = dayTime.format(new Date(Long.parseLong(date)));


        TextView textView = findViewById(R.id.textView2);
        textView.setText(getString(R.string.full_image_text, strDate,nourriture ));

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
