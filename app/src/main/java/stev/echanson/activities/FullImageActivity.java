package stev.echanson.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import stev.echanson.R;
import stev.echanson.classes.ImageAdapter;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        int position =  i.getExtras().getInt("id");
        ImageAdapter adapter = new ImageAdapter(this);

        ImageView imageView = findViewById(R.id.fullImageView);
        imageView.setImageBitmap(adapter.pictures[position]);


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
