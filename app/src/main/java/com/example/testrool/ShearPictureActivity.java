package com.example.testrool;

import com.example.testrool.Calculater.CalculateGray;
import com.example.testrool.Http.HttpUtil;
import com.example.testrool.Http.URLs;
import com.example.testrool.bean.HistoryItem;
import com.example.testrool.bean.LoggedInUser;
import com.example.testrool.bean.Model;
import com.example.testrool.ui.gallery.GalleryFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ShearPictureActivity extends AppCompatActivity {
    final static int PICTURE_CROPPING_CODE = 1;
    Bitmap bitmap = null;
    private Uri imageUri;
    private Uri croppedUri;
    private ImageView picture;
    private Button finishBtn;
    private Button reChoseBtn;
    Double greyRel;
    Double concenRel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shear_picture);
        Intent intent = getIntent();
        String fromActivity = intent.getStringExtra("fromActivity");
        imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
        picture = findViewById(R.id.picture);
        pictureCropping(imageUri);
        reChoseBtn = findViewById(R.id.reshear_button);
        reChoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShearPictureActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });
        finishBtn = findViewById(R.id.confirm_button);
        reChoseBtn = findViewById(R.id.reshear_button);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greyRel = CalculateGray.getGray(bitmap, 2);
                if (fromActivity.equals("ModeBuild")) {
                    Intent intent = new Intent(ShearPictureActivity.this, ModeBuildActivity.class);
                    intent.putExtra("picture_uri", imageUri.toString());
                    intent.putExtra("cal_result", String.valueOf(CalculateGray.getGray(bitmap, 2)));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShearPictureActivity.this, ShowResultActivity.class);
                    ArrayList<Model> modelList = new ArrayList<>();
                    ArrayList<String> itemNames = new ArrayList<>();
                    try {
                        String res = HttpUtil.postToServer(URLs.getModelServlet() + "?uid=" + LoggedInUser.getLoggedInUser().getUserId(), null);
                        JSONArray jsonArray = JSONArray.fromObject(res);
                        for (Object jsonObject : jsonArray) {
                            JSONObject x = JSONObject.fromObject(jsonObject);
                            Model model = Model.fromJSONObject(x);
                            modelList.add(model);
                            itemNames.add(model.getName());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    String[] itemNames = new String[modelList.size()];
                    //String[] itemNames = {"model1","model2"};

//                    Model model1 = new Model();
//                    model1.setA(1.11);
//                    model1.setB(12.2);
//
//                    Model model2 = new Model();
//                    model2.setA(-1.11);
//                    model2.setB(12.2);
//
//                    modelList.add(model1);
//                    modelList.add(model2);


                    //TODO 模型选择 选项框
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShearPictureActivity.this);
                    builder.setNegativeButton("取消", null);
                    builder.setTitle("请选择本次预测使用模型");
                    String[] itemNamesArr = itemNames.toArray(new String[itemNames.size()]);
                    builder.setItems(itemNamesArr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Model chosenModel = modelList.get(i);
                            Double a = chosenModel.getA();
                            Double b = chosenModel.getB();
                            concenRel = (greyRel - b)/a;
                            intent.putExtra("picture_uri", imageUri.toString());
                            intent.putExtra("grey_result", String.valueOf(greyRel));
                            intent.putExtra("concen_result", String.valueOf(concenRel));
                            intent.putExtra("model_name", chosenModel.getName());
                            intent.putExtra("fromActivity", "ShearPictureActivity");
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String strTime = formatter.format(curDate);

                            intent.putExtra("get_time", strTime);
                            startActivity(intent);
                        }
                    }).show();
                }
            }
        });
        ActivityCollectorUtil.addActivity(ShearPictureActivity.this);
    }

    private void pictureCropping(Uri uri) {
        // 调用系统图片�?�?
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true�?设置在开�?的Intent�?设置显示的VIEW�?裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY �?宽高的比�?
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX` outputY �?裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        croppedUri = uri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedUri);
        startActivityForResult(intent, PICTURE_CROPPING_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICTURE_CROPPING_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    picture.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(ShearPictureActivity.this);
    }
}