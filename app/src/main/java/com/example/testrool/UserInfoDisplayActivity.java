package com.example.testrool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class UserInfoDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infodisplay);
        Intent intent = getIntent();
        TextView tvv = findViewById(R.id.infotvv);
        String str = intent.getStringExtra("mycontent");
        tvv.setText(str);

        // 尝试滑动
        tvv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}