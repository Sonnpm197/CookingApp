package fpt.android.com.appnauan.Utilities;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

public class AndroidSpeech {
    private Context context;
    private TextToSpeech tts;
    private String text;
    private int currentSentence = 0;
    private boolean isPaused = false;

    public AndroidSpeech(Context context) {
        this.context = context;
        this.clearData();
        this.onInit();
    }

    /**
     * Init TextToSpeech
     */
    private void onInit() {
        this.tts = new TextToSpeech(this.context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String s) {
                        try {
                            if (!isPaused) {
                                currentSentence = Integer.parseInt(s) + 1;
                                speak();
                            }
                        } catch (Exception e) {
                            currentSentence = 0;
                        }
                    }
                });
            }
        });
        this.setLanguage(Locale.US);
    }

    /**
     * Clear all data
     */
    public void clearData() {
        this.text = "";
        this.currentSentence = 0;
        isPaused = false;
    }

    /**
     * Pause speaker
     */
    public void onPause() {
        this.tts.stop();
        this.isPaused = true;

    }

    /**
     * Stop speaker
     */
    public void onStop() {
        this.onPause();
        this.clearData();
    }

    /**
     * Resume speaker
     */
    public void onResume() {
        this.speak();
        this.isPaused = false;
    }

    /**
     * Check is speeking or not
     *
     * @return status
     */
    public boolean isSpeaking() {
        return this.tts.isSpeaking();
    }

    /**
     * Check if can be spoken or not
     *
     * @return status
     */
    public boolean isSpeakable() {
        return this.tts.getEngines().size() > 0;
    }

    /**
     * set Lang
     *
     * @param lang Locale
     * @return status
     */
    public int setLanguage(Locale lang) {
        return this.tts.setLanguage(lang);
    }

    /**
     * Set text need to be spoken
     *
     * @param text String
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * do speak
     */
    public void speak() {
        String[] splitspeech = this.text.split("\\.");
        HashMap<String, String> params = new HashMap();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, this.currentSentence + "");
        this.tts.speak(splitspeech[this.currentSentence], TextToSpeech.QUEUE_ADD, params);
    }
}
