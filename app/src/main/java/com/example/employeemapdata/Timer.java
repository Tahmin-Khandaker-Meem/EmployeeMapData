package com.example.employeemapdata;


import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Timer extends AppCompatActivity {
    public int counter;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        textView = (TextView) findViewById(R.id.timerDisplay);
        editText = (EditText) findViewById(R.id.editText);
    }

    public void countDown(View view) {
        long time = Long.parseLong(editText.getText().toString());
        time = time*1000;
        new CountDownTimer( time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(String.valueOf(counter));
                counter++;
            }

            @Override
            public void onFinish() {
                textView.setText("00:00");
                Toast.makeText(Timer.this,"Times up!",Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}
