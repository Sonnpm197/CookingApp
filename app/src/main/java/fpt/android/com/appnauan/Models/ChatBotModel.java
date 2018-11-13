package fpt.android.com.appnauan.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import fpt.android.com.appnauan.ChatBotActivity;
import fpt.android.com.appnauan.Entities.Food;
import fpt.android.com.appnauan.R;
import fpt.android.com.appnauan.Utilities.AndroidSpeech;

public class ChatBotModel {

    private String fillMeatQuestions =
            "Bạn có thịt gì ?," +
            "Bạn muốn nấu với thịt hay cá gì ?," +
            "Bạn có muốn nấu kèm với thịt không ?,";


    private String fillVegetableQuestions =
        "Bạn có nấu kèm rau củ gì không ?," +
        "Thế còn rau thì sao ?," +
        "Bạn muốn ăn kèm rau củ gì ?,";
    ;

    private AIDataService aiDataService;
    private ChatBotActivity chatBotActivity;
    private AndroidSpeech tts;
    private FirebaseModel firebaseModel;
    private boolean isVoiceRecord;

    private static final String TAG = ChatBotModel.class.getSimpleName();
    private Gson gson = GsonFactory.getGson();
    private static final String LANGUAGE_CODE = "en";
    private static final String CLIENT_ACCESS_TOKEN = "dd6d647a0dc244a384a046cfe55a9f23";


    public ChatBotModel(ChatBotActivity chatBotActivity) {
        this.chatBotActivity = chatBotActivity;
        firebaseModel = new FirebaseModel();
        tts = new AndroidSpeech(chatBotActivity);
        initService();
    }

    private void initService() {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(LANGUAGE_CODE);
        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN, lang, AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(chatBotActivity, config);
    }

    public void sendRequest(String queryString) {
        if (TextUtils.isEmpty(queryString)) {
            Toast.makeText(chatBotActivity, "Hãy đưa ra nguyên liệu để bot tư vấn bạn nhé", Toast.LENGTH_SHORT).show();
            return;
        }

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];

                if (!TextUtils.isEmpty(query)) {
                    request.setQuery(query);
                }

                try {
                    return aiDataService.request(request);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };

        task.execute(queryString);
    }



    private void onResult(final AIResponse response) {
        chatBotActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

                Log.i(TAG, gson.toJson(response));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);

                // If bot is asking some cases like "Bạn có nấu kèm rau củ gì ko thì không search" to fill your request
                boolean isFillingMaterial = false;
                if (fillMeatQuestions.contains(speech) || fillVegetableQuestions.contains(speech)) {
                    isFillingMaterial = true;
                }

                // If user used voice chat => bot uses voice to response
                if (isVoiceRecord) {
                    tts.setText(speech);
                    tts.speak();
                }

                chatBotActivity.setBotResponse(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    ArrayList listMeats = null;
                    ArrayList listVegetables = null;
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {

                        String key = entry.getKey();
                        JsonElement values = entry.getValue();
                        JsonArray array = null;

                        if (values.isJsonArray()) {
                            array = values.getAsJsonArray();
                        }

                        if (array != null) {
                            if (key.equals("RawProteinMaterial")) {
                                listMeats = gson.fromJson(array, ArrayList.class);
                            }

                            if (key.equals("Vegetable")) {
                                listVegetables = gson.fromJson(array, ArrayList.class);
                            }
                        }

                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }

                    if (!isFillingMaterial) {
                        firebaseModel.findFoodByIngredient(listMeats, listVegetables, ChatBotModel.this);
                    }
                }
            }

        });
    }

    /**
     * This method will be called in firebaseModel.findFoodByIngredient
     * @param food
     */
    public void responseWithReturnedFoodByIngredient(Food food) {
        if (food == null) {
            String response = "Xin lỗi mình chẳng tìm thấy món nào cả :(";
            chatBotActivity.setBotResponse(response);
            if (isVoiceRecord) {
                tts.setText(response);
                tts.speak();
                isVoiceRecord = false;
            }
            return;
        }

        String response = "Bạn có thể thử món " + food.getName();
        if (isVoiceRecord) {
            tts.setText(response);
            tts.speak();
            isVoiceRecord = false;
        }

        // Show bot response on UI
        chatBotActivity.setBotResponse(response);
    }

    private void onError(final AIError error) {
        Toast.makeText(chatBotActivity, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    public void setVoiceRecord(boolean voiceRecord) {
        isVoiceRecord = voiceRecord;
    }
}
