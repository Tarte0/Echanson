package stev.echanson.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    public Bitmap[] pictures;


    public ImageAdapter(Context c, Bitmap[] pictures){
        this.context = c;
        this.pictures = pictures!=null ? pictures : new Bitmap[0];
    }

    public ImageAdapter(Context c){
        this.context = c;
    }

    @Override
    public int getCount() {
        return pictures.length;
    }

    @Override
    public Object getItem(int i) {
        return pictures[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // screen width
        int width= context.getResources().getDisplayMetrics().widthPixels;
        ImageView imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(pictures[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(width/3,width/3 ));
        return imageView;
    }


    public Bitmap[] getPicturesFromStorage() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "echanson";

        Bitmap[] picturesStored;

        File directory = new File(path);
        File[] files = directory.listFiles();

        picturesStored = new Bitmap[files.length];

        for (int i = 0; i < files.length; i++)
        {
            try {
                FileInputStream streamIn = new FileInputStream(files[i]);

                Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image

                picturesStored[i] = bitmap;

                streamIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picturesStored;
    }


}