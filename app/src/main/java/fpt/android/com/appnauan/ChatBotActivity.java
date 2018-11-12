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

    private ListView messagesOnUI;
    private EditText userMessage;
    private List<ChatModel> listChatModel;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        listChatModel = new ArrayList<>();

        messagesOnUI = (ListView) findViewById(R.id.listMessages);
        userMessage = (EditText) findViewById(R.id.userMessage);
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = userMessage.getText().toString();
                List<ChatModel> models;
                ChatModel model = new ChatModel(text, true); // user send message
                listChatModel.add(model);
                models = listChatModel;
                CustomAdapter adapter = new CustomAdapter(models, getApplicationContext());
                messagesOnUI.setAdapter(adapter);

                // TODO: implement AI here
                model = new ChatModel(text, false); // AI send message
                listChatModel.add(model);
                userMessage.setText("");
            }
        });
    }
}
