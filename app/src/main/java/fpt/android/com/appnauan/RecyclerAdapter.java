package fpt.android.com.appnauan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fpt.android.com.appnauan.Entities.Food;
import fpt.android.com.appnauan.Models.FirebaseModel;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = RecyclerAdapter.class.getSimpleName();

    private Context context;
    private RecyclerView recyclerView;
    private List<String> listTitles = new ArrayList<>();
    private List<String> listTitlesFull;
    private List<String> listImages;
    private List<String> listImagesFull;
    private List<String> filteredListImages;
    private String[] titles;
    private String[] images;
    private ArrayList<Food> foods;

    public RecyclerAdapter(Context context) {
        this.context = context;
        FirebaseModel firebaseModel = new FirebaseModel();
        firebaseModel.getFoods(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_each_dish, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //add to list
    public void reRenderView(ArrayList<Food> foods) {

        this.foods = foods;

        listImages = new ArrayList<>();
        listTitles = new ArrayList<>();

        titles = new String[foods.size()];
        images = new String[foods.size()];

        for (int i = 0; i < foods.size(); i++) {
            titles[i] = foods.get(i).getName();
            images[i] = foods.get(i).getImageUrl();
        }

        for (int i = 0; i < foods.size(); i++) {
            listTitles.add(titles[i]);
            listImages.add(images[i]);
        }

        listTitlesFull = new ArrayList<>(listTitles);
        listImagesFull = new ArrayList<>(listImages);

        notifyDataSetChanged();
    }

    //add information to card view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "title:" + listTitles.size());
        Log.i(TAG, "Image" + listImages.size());
        if (listTitles.size() > 0 && listImages.size() > 0) {
            holder.itemTitle.setText(listTitles.get(position));
            Glide.with(context).load(listImages.get(position)).into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return listTitles.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    //get list change when text change in search view
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredListTitles = new ArrayList<>();
            filteredListImages = new ArrayList<>();
            if (charSequence == null || charSequence.toString().length() == 0) {
                filteredListTitles.addAll(listTitlesFull);
                filteredListImages.addAll(listImagesFull);
                Log.i(TAG, Arrays.toString(listImagesFull.toArray()));
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (int i = 0; i < listTitlesFull.size(); i++) {
                    if (listTitlesFull.get(i) != null && listTitlesFull.get(i).toLowerCase().contains(filterPattern)) {
                        filteredListTitles.add(listTitlesFull.get(i));
                        filteredListImages.add(listImagesFull.get(i));
                    }
                }
                Log.i(TAG, Arrays.toString(listImagesFull.toArray()));

            }
            FilterResults results = new FilterResults();
            results.values = filteredListTitles;
            return results;
        }

        //set list before text change in search view
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listTitles = new ArrayList<>();
            listImages = new ArrayList<>();
            if (filterResults.values != null) {
                listTitles.addAll((List) filterResults.values);
            }

            if (filteredListImages != null) {
                listImages.addAll((List) filteredListImages);
            }
            notifyDataSetChanged();
        }
    };

    //set event of each card view
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DishItemDetailActivity.class);
                    if (foods != null) {
                        intent.putExtra("FoodItem", foods.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
