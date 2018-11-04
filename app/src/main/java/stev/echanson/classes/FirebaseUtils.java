package stev.echanson.classes;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static stev.echanson.classes.ImageUtils.convertBitmapToB64;

public class FirebaseUtils {
    private static FirebaseDatabase database;

    public static final String METADATA_PATH = "metadata";
    public static final String CATEGORIES_PATH = METADATA_PATH + "/categories";
    public static final String INGREDIENTS_PATH = METADATA_PATH + "/ingredients";
    public static final String NUTRIMENTS_PATH = METADATA_PATH + "/nutriments";
    public static final String REGIMES_PATH = METADATA_PATH + "/regimes";
    public static final String NOURRITURE_PATH = "nourriture";
    public static final String USER_PATH = "users";

    public FirebaseUtils(FirebaseDatabase mDatabase) {
        database = mDatabase;
    }

    public static String getUserPath(String user){
        return USER_PATH.concat("/").concat(user);
    }

    public static String getUserRegimesPath(String user){
        return getUserPath(user).concat("/regimes");
    }

    public static String getUserCategoriesPath(String user){
        return getUserPath(user).concat("/categories");
    }

    public static String getUserIngredientsPath(String user){
        return getUserPath(user).concat("/ingredients");
    }

    public static String getUserNewIngredientsPath(String userID, String ingredientName) {
        return getUserIngredientsPath(userID).concat("/").concat(ingredientName);
    }

    public static String getUserNewRegimesPath(String userID, String regimeName) {
        return getUserRegimesPath(userID).concat("/").concat(regimeName);
    }

    public static String getUserNewCategoriesPath(String userID, String categorieName) {
        return getUserCategoriesPath(userID).concat("/").concat(categorieName);
    }

    public String getTargetNourriturePathFromFirebase(String nourriture) {
        return NOURRITURE_PATH.concat("/").concat(nourriture);
    }

    public static String getUserPicturesPath(String userID) {
        return getUserPath(userID).concat("/pictures");
    }

    public static void saveUserImage(Bitmap bitmapPicture, String date, String nourriture, String userID) {

        DatabaseReference userImageRef = database.getReference(getUserPicturesPath(userID));

        String pictureB64 = convertBitmapToB64(bitmapPicture);

        Food food = new Food(date, nourriture, pictureB64);

        userImageRef.push().setValue(food);

    }
}