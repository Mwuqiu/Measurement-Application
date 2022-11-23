package com.example.testrool;

import com.example.testrool.Calculater.CalculateGray;
import com.example.testrool.ui.gallery.GalleryFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



import java.io.FileNotFoundException;

public class ShearPictureActivity extends AppCompatActivity {

    final static int PICTURE_CROPPING_CODE = 1;

    Bitmap bitmap = null;

    private Uri imageUri;

    private Uri croppedUri;

    private ImageView picture;

    private Button finishBtn;

    private Button reChoseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shear_picture);

        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
        picture = findViewById(R.id.picture);
        pictureCropping(imageUri);

        finishBtn = findViewById(R.id.confirm_button);

        reChoseBtn = findViewById(R.id.reshear_button);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击确定跳转至 result 界面， 需要传送 图片 + 浓度值 + 日期
                //mdoe == 1 为平均值
                //Log.e("tempResult", String.valueOf(new CalculateGray().getGray(bitmap,1)));
                //reChoseBtn.setText(String.valueOf(new CalculateGray().getGray(bitmap,2)));
                Intent intent = new Intent(ShearPictureActivity.this,ShowResultActivity.class);
                intent.putExtra("picture_uri", imageUri.toString());
                intent.putExtra("cal_result",String.valueOf(new CalculateGray().getGray(bitmap,2)));
                //TO DO 改变 item 的值
                GalleryFragment.my_array.add("testItem");
                GalleryFragment.my_array1.add("testItem1");
                startActivity(intent);
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private void pictureCropping(Uri uri) {
        // 调用系统图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX` outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        croppedUri = uri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedUri);
        startActivityForResult(intent, PICTURE_CROPPING_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICTURE_CROPPING_CODE:
                if(resultCode == RESULT_OK){
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

}