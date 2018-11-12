package fpt.android.com.appnauan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fpt.android.com.appnauan.Adapter.CustomAdapter;
import fpt.android.com.appnauan.Models.ChatModel;

public class ChatBotActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    Button btn_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        listView = (ListView) findViewById(R.id.list_of_message);
        editText = (EditText) findViewById(R.id.user_message);
        btn_send_message = (Button) findViewById(R.id.btnSend);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                List<ChatModel> models;
                ChatModel model = new ChatModel(text, true); // user send message
                list_chat.add(model);
                models = list_chat;
                CustomAdapter adapter = new CustomAdapter(models, getApplicationContext());
                listView.setAdapter(adapter);
                model = new ChatModel(text, false); // AI send message
                list_chat.add(model);
                editText.setText("");
            }
        });
    }
}
