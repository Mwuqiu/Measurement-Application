package com.example.testrool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);
        Intent intent = getIntent();
        TextView tvv = findViewById(R.id.messtext);
        tvv.setText(intent.getStringExtra("content"));

        // 尝试滑动
        tvv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}