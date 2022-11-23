package com.example.testrool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class ShowResultActivity extends Activity {

    ImageView imageView;

    TextView resultText;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
        imageView = findViewById(R.id.image0);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        resultText = findViewById(R.id.tv2);
        resultText.setText(intent.getStringExtra("cal_result"));
    }

    // to do 改变跳转的方式  这个会卡一下  避免使用回退键
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(ShowResultActivity.this,HomePageActivity.class);
        //intent.putExtra("fragmentChose","2");
        startActivity(intent);
    }
}