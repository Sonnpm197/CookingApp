package fpt.android.com.appnauan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fpt.android.com.appnauan.Entities.Food;
import fpt.android.com.appnauan.Utilities.AndroidSpeech;

public class DishItemDetailActivity extends AppCompatActivity {
    public ProgressBar loading_spinner;
    //Tag
    private static final String TAG = DishItemDetailActivity.class.getSimpleName();
    //object target
    private Bitmap mainPic;
    //View
    private TextView dishTitle;
    private ImageView dishPic;
    private TextView ingredientContent;
    private TextView recipeContent;
    private FloatingActionButton textToSpeechBtn;
    private AndroidSpeech androidSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve view objects from UI
        initView();

        // Get parcelable Food from recyclerAdapter
        loadSelectedItem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.androidSpeech != null) {
            this.androidSpeech.onPause();
        }
    }

    private void initView() {
        // This class supports text to speech
        this.androidSpeech = new AndroidSpeech(this);

        // Get view objects from UI
        this.dishTitle = findViewById(R.id.dishTitle);
        this.dishPic = findViewById(R.id.dishPic);
        this.ingredientContent = findViewById(R.id.ingredientContent);
        this.recipeContent = findViewById(R.id.recipeContent);
        this.textToSpeechBtn = findViewById(R.id.textToSpeechBtn);
        this.textToSpeechBtn.setEnabled(this.androidSpeech.isSpeakable());
        this.textToSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (androidSpeech.isSpeaking()) {
                    androidSpeech.onPause();
                } else {
                    androidSpeech.onResume();
                }
            }
        });
    }

    /**
     * Get data from intent
     */
    private void loadSelectedItem() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Food food = bundle.getParcelable("FoodItem");
            getSupportActionBar().setTitle(food.getName());
            dishTitle.setText(food.getName());
            Glide.with(this).load(food.getImageUrl()).into(dishPic);
            ingredientContent.setText("Rau: " + food.getVegetable() + "\n" + "Thịt: " + food.getMeat());
            recipeContent.setText(food.getRecipe());
            androidSpeech.setText(food.getRecipe());
        }
    }

    /**
     * On back button click
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
