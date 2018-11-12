package fpt.android.com.appnauan.Models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import fpt.android.com.appnauan.Entities.Food;

public class FirebaseModel {

    /**
     *  TODO: Notify adapter here whenever data from firebase changed
     */

    private Context context;
    private ArrayList<Food> foods;
    private static final String TAG = FirebaseModel.class.getSimpleName();

    public FirebaseModel() {
    }

    public void addFood(Food food) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Food");

        HashMap<String, Object> params = new HashMap<>();
        params.put("name", food.getName());
        params.put("meat", food.getMeat());
        params.put("vegetable", food.getVegetable());
        params.put("recipe", food.getRecipe());
        params.put("type", food.getType());

        // add to DB
        databaseReference.push().setValue(params);
        Log.i(TAG, "Add: " + food.toString());
    }

    /**
     * Get food from firebase and update whenever data changes
     * @return
     */
    public void getFoods(/*TODO: , Food adapter*/) {
        foods = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Food");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);
                    Log.i(TAG, "Receive: " + food.toString());
                    foods.add(food);
                }

                // TODO: Nam update to adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeFood(final String foodName /*TODO: , Food adapter*/) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Food");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);
                    if (food.getName().equalsIgnoreCase(foodName)) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
