package fpt.android.com.appnauan;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fpt.android.com.appnauan.Adapter.CustomAdapter;
import fpt.android.com.appnauan.Entities.Food;
import fpt.android.com.appnauan.Models.ChatBotModel;
import fpt.android.com.appnauan.Models.ChatModel;

public class ChatBotActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH = 10;

    private ListView messagesOnUI;
    private EditText userMessage;
    private List<ChatModel> listChatModel;
    private Button btnSend;
    private ChatBotModel chatBotModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listChatModel = new ArrayList<>();
        chatBotModel = new ChatBotModel(this);

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

                chatBotModel.sendRequest(text); // also call setBotResponse
            }
        });
    }

    /**
     * This method will be called in ChatBotModel after finish receiving a message
     * isClickable: user can click on foodName if bot return a message with food name
     * @param response
     */
    public void setBotResponse(String response, boolean isClickable, Food food) {
        ChatModel model = new ChatModel(response, false); // AI send message
        model.setClickable(true); // set clickable to redirect to detailActivity
        model.setFood(food); // food to show on detail activity
        listChatModel.add(model);
        userMessage.setText("");
        CustomAdapter adapter = new CustomAdapter(listChatModel, getApplicationContext());
        messagesOnUI.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * After recording set text for user message and perform click
     */
    private void startVoiceRecording() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        } else {
            Toast.makeText(this, "This device does not support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    //get string after speech
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    userMessage.setText(result.get(0));
                    btnSend.performClick();
                    chatBotModel.setVoiceRecord(true);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chatbot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voiceRecord:
                startVoiceRecording();
            return true;
        }
        return false;
    }

    // Make chat bot stop speaking
    @Override
    protected void onPause() {
        super.onPause();
        chatBotModel.stopSpeaking();
    }
}
