package com.example.speechtest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100; // 確認現在是否開始為錄音的狀態

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = findViewById(R.id.txtSpeechInput);
        btnSpeak = findViewById(R.id.btnSpeak);


        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSpeechInput();
            }
        });
    }
    private void recordSpeechInput(){
        // 用 Intent 來傳遞語音識別的模式，並且開啟語音模式
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //  開啟語音活動：透過 Android 內建語音辨識
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, //  設定語音辨識的模型
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); //  自由形式的語音識別
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); //  設定語音辨識的語言：Locale.getDefault() 指定為手機預設的語系
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"請說話"); //  設定語音提示文字：提示使用者可以開始語音
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT); // 使用 startActivityForResult 啟動 Intent 語音識別
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported), //  如果沒有安裝具有語音辨識 Activity 的時候，顯示錯誤訊息
                    Toast.LENGTH_SHORT).show();
        }
    }

    //  覆寫 onActivityResult 方法，以取得語音辨識的結果，並顯示出來
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {

                //  如果是語音辨識的回傳，且辨識成功，才會處理
                if (resultCode == RESULT_OK && data != null) {

                    // 取出多個辨識結果，並儲存在 String 的 ArrayList 中
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }
        }
    }
}
