package com.github.nguyen_viet_thanh_trung.speakingwell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by vinguyen on 19/06/15.
 */
public class SpeechRecognitionActivity extends Activity implements RecognitionListener {
    private SpeechRecognizer myRecognizer = null;
    private Intent intent;
    private  final  String TAG = "VoiceRecognition";
    private ImageButton btnStop; // press this button to finish your speak
    public static final String SPEECH_RECOGNITION_EXTRA_RESULT = "com.github.nguyen_viet_thanh_trung.speakingwell.result";
    public static final String SPEECH_RECOGNITION_EXTRA_ERROR = "com.github.nguyen_viet_thanh_trung.speakingwell.error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        myRecognizer =SpeechRecognizer.createSpeechRecognizer(this);
        myRecognizer.setRecognitionListener(this);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //Considers input in free form English-US
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        myRecognizer.startListening(intent);

        btnStop = (ImageButton) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRecognizer.stopListening();
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

        if (null != myRecognizer) {
            myRecognizer.destroy();
        }
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String message;
		switch (errorCode) {
		case SpeechRecognizer.ERROR_AUDIO:
			message = "Audio recording error";
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			message = "Client side error";
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			message = "Insufficient permissions";
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			message = "Network error";
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			message = "Network timeout";
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			message = "No match";
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			message = "RecognitionService busy";
			break;
		case SpeechRecognizer.ERROR_SERVER:
			message = "Error from server";
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = "No speech input";
			break;
		default:
			message = "Didn't understand, please try again.";
			break;
		}
        Log.d(TAG, "onError:" + message);
        Intent res = new Intent();
        res.putExtra(SpeechRecognitionActivity.SPEECH_RECOGNITION_EXTRA_ERROR, message);
        setResult(RESULT_CANCELED, res);
        finish();
    }

    /*
     * Receiving results from SpeechRecognizer, parse the result which has the highest confidence value
     * (i.e the first element in ArrayList) to MainActivity
     */
    @Override
    public void onResults(Bundle bundle) {
        Log.d(TAG, "onResults:");
        ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        for (int i = 0; i < results.size(); i++) {
            Log.d(TAG, results.get(0) + " (score: " + scores[i] + ")");
        }
        Intent res = new Intent();
        res.putExtra(SpeechRecognitionActivity.SPEECH_RECOGNITION_EXTRA_RESULT, results.get(0));
        setResult(RESULT_OK, res);
        finish();
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.d(TAG, "onEvent");
    }
}
