package com.example.testrool;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.testrool.Http.HttpUtil;
import com.example.testrool.Http.URLs;
import com.example.testrool.bean.HistoryItem;
import com.example.testrool.bean.LoggedInUser;
import net.sf.json.JSONObject;
import java.io.FileNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowResultActivity extends Activity {
    ImageView imageView;

    TextView resultText;
    TextView getTime;
    TextView modelName;

    Button shareButton;

    Uri imageUri;
    String fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slideshow);
        Intent intent = getIntent();

        fromActivity = intent.getStringExtra("fromActivity");

        shareButton = findViewById(R.id.sharebtn);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ViewGroup viewGroup = (ViewGroup) view1.getParent();
                viewGroup.removeView(view1);
                View fullView = getWindow().getDecorView();
                fullView.destroyDrawingCache();
                fullView.setDrawingCacheEnabled(true);
                fullView.buildDrawingCache();
                Bitmap bitmap = fullView.getDrawingCache();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                Uri shareUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "IMG" + Calendar.getInstance().getTime(), null));
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, shareUri);
                startActivity(Intent.createChooser(intent, "title"));

                viewGroup.addView(view1);
                view1.setOnClickListener(this);
            }

        });

        //String fromActivity = intent.getStringExtra("fromActivity");

        if(fromActivity.equals("ShearPictureActivity")){
            //设置图片
            imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
            imageView = findViewById(R.id.image0);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
            //设置分析结果
            resultText = findViewById(R.id.analize);

            String str = "采用模型 : " + intent.getStringExtra("model_name") + "图片灰度值 : "+intent.getStringExtra("grey_result")
                    + "预测浓度值 : " + intent.getStringExtra("concen_result");

            resultText.setText(str);
            //设置采样时间
            getTime = findViewById(R.id.get_model_time);
            getTime.setText(intent.getStringExtra("get_time"));
            //设置使用模型
            modelName = findViewById(R.id.show_model_name);
            modelName.setText(intent.getStringExtra("model_name"));


            //将消息传递给historyPage
            HistoryItem historyItem = new HistoryItem();
            historyItem.setItemName(intent.getStringExtra("model_name"));
            historyItem.setDate(String.valueOf(getTime.getText()));
            historyItem.setResult(intent.getStringExtra("concen_result"));
            JSONObject jsonObject = historyItem.toJSONObject();
            jsonObject.put("uid", LoggedInUser.getLoggedInUser().getUserId());
            try {
                HttpUtil.postToServer(URLs.getHistoryUploadServlet(),jsonObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            getTime = findViewById(R.id.get_model_time);
            getTime.setText(intent.getStringExtra("get_time"));

            modelName = findViewById(R.id.show_model_name);
            modelName.setText(intent.getStringExtra("model_name"));

            resultText = findViewById(R.id.analize);
            resultText.setText("预测浓度值 : " + intent.getStringExtra("whole_result"));

        }
        ActivityCollectorUtil.addActivity(ShowResultActivity.this);
    }

    // to do 改变跳转的方式  这个会卡一下  避免使用回退键
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(ShowResultActivity.this);
        if(fromActivity.equals("ShearPictureActivity")){
            Intent intent = new Intent(ShowResultActivity.this,HomePageActivity.class);
            startActivity(intent);
        }
    }


}