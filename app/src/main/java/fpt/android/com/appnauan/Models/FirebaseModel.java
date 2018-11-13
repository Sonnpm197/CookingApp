package fpt.android.com.appnauan.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fpt.android.com.appnauan.Entities.Food;
import fpt.android.com.appnauan.RecyclerAdapter;

public class FirebaseModel {

    private Context context;
    private ArrayList<Food> foods;
    private static final String TAG = FirebaseModel.class.getSimpleName();

    public FirebaseModel() {
    }

    public void addFood(Food food) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("food");

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
    public void getFoods(final RecyclerAdapter recyclerAdapter) {
        foods = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("food");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "Id: " + snapshot.getKey());
                    Food food = snapshot.getValue(Food.class);
                    Log.i(TAG, "Receive: " + food.toString());
                    foods.add(food);
                }

                // Rerender UI after get data from Firebase
                recyclerAdapter.reRenderView(foods);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void findFoodByIngredient(final ArrayList meatParams, final ArrayList vegetableParams, final ChatBotModel chatBotModel) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("food");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean foundAvailableFood = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);

                    boolean containMeat = false;
                    boolean containVegetable = false;

                    if (meatParams.size() != 0) {
                        if (food != null && food.getMeat() != null) {
                            String multiMeats = food.getMeat().toLowerCase();
                            for (Object type : meatParams) {
                                if (multiMeats.contains(type.toString().toLowerCase())) {
                                    containMeat = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Start matching vegetable
                    if (vegetableParams.size() != 0) {
                        if (food != null && food.getVegetable() != null) {
                            String multiVegetable = food.getVegetable().toLowerCase();
                            for (Object type : vegetableParams) {
                                if (multiVegetable.contains(type.toString().toLowerCase())) {
                                    containVegetable = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (containMeat && containVegetable) {
                        foundAvailableFood = true;
                        chatBotModel.responseWithReturnedFoodByIngredient(food);
                        break;
                    }
                }

                // After the loop if cannot find any then return response to user
                if (!foundAvailableFood) {
                    chatBotModel.responseWithReturnedFoodByIngredient(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeFood(final String foodName) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("food");
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
