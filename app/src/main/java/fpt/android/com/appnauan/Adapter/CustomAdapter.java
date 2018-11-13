package fpt.android.com.appnauan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

import fpt.android.com.appnauan.DishItemDetailActivity;
import fpt.android.com.appnauan.Models.ChatModel;
import fpt.android.com.appnauan.R;

public class CustomAdapter extends BaseAdapter {

    private List<ChatModel> list_chat_models;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAdapter(List<ChatModel> list_chat_models, Context context) {
        this.list_chat_models = list_chat_models;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_chat_models.size();
    }

    @Override
    public Object getItem(int position) {
        return list_chat_models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            final ChatModel chatModel = list_chat_models.get(position);
            if (chatModel.isUserSend()) {
                view = layoutInflater.inflate(R.layout.list_item_message_send, null);
            }
            else {
                view = layoutInflater.inflate(R.layout.list_item_message_recv, null);
            }

            BubbleTextView text_message = (BubbleTextView) view.findViewById(R.id.text_message);
            text_message.setText(list_chat_models.get(position).getMessage());

            if (chatModel.isClickable() && chatModel.getFood() != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DishItemDetailActivity.class);
                        intent.putExtra("FoodItem", chatModel.getFood());
                        context.startActivity(intent);
                    }
                });
            }
        }
        return view;
    }
}
