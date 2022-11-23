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

        String fromActivity = intent.getStringExtra("fromActivity");

        imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
        picture = findViewById(R.id.picture);
        pictureCropping(imageUri);

        finishBtn = findViewById(R.id.confirm_button);

        reChoseBtn = findViewById(R.id.reshear_button);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromActivity.equals("ModeBuild")){
                    //TODO 跳转至创建模型界�? 传递浓度�?
                    Intent intent = new Intent(ShearPictureActivity.this,ModeBuildActivity.class);
                    intent.putExtra("picture_uri", imageUri.toString());
                    intent.putExtra("cal_result",String.valueOf(new CalculateGray().getGray(bitmap,2)));
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(ShearPictureActivity.this,ShowResultActivity.class);
                    intent.putExtra("picture_uri", imageUri.toString());
                    intent.putExtra("cal_result",String.valueOf(new CalculateGray().getGray(bitmap,2)));
                    startActivity(intent);
                    //onDestroy();
                }
                //Intent intent = new Intent(ShearPictureActivity.this,ShowResultActivity.class);
                //intent.putExtra("picture_uri", imageUri.toString());
                //intent.putExtra("cal_result",String.valueOf(new CalculateGray().getGray(bitmap,2)));
                //TO DO 改变 item 的�?
//                GalleryFragment.my_array.add("testItem");
//                GalleryFragment.my_array1.add("testItem1");
                // startActivity(intent);
            }
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
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