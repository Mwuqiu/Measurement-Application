package com.example.testrool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testrool.Calculater.CalculateModel;
import com.example.testrool.Http.HttpUtil;
import com.example.testrool.Http.URLs;
import com.example.testrool.bean.LoggedInUser;
import com.example.testrool.bean.Model;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ModeBuildActivity extends AppCompatActivity {
    static public ArrayList<String> imageId =  new ArrayList<>();
    static public ArrayList<String> imageConcen =  new ArrayList<>();
    static public ArrayList<String> imageGrayLevel =  new ArrayList<>();
    final int ChoosePhoto = 1;
    ListView listView;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_build);
        Button btn = findViewById(R.id.add_btn);
        Button buildBtn = findViewById(R.id.build_btn);
        buildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageConcen.size() != imageGrayLevel.size() || imageConcen.size() <= 1 ){
                    Toast.makeText(getApplicationContext(), "请先完成图片浓度值的输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                Double[] x = new Double[imageGrayLevel.size()];
                Double[] y = new Double[imageConcen.size()];
                for(int i = 0 ; i  < imageConcen.size() ;i++){
                    x[i] = Double.parseDouble(imageConcen.get(i));
                    y[i] = Double.parseDouble(imageGrayLevel.get(i));
                }
                Model model = CalculateModel.getModel(x,y,imageConcen.size());
                Log.e("AB",model.getA() + " " + model.getB());
                //生成输入model名字的输入框

                final EditText inputServer = new EditText(ModeBuildActivity.this);
                //inputServer.setHint("请在此处输入Model ：" + model +"的名称");

                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder = new AlertDialog.Builder(ModeBuildActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);//设置取消键
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sign = inputServer.getText().toString();//点击确认后获取输入框的内容
                        if (sign != null && !sign.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                            // TODO 上传 MODLE  String -> Json  sign 为名字
                            model.setName(sign);
                            model.setDate(String.valueOf(System.currentTimeMillis()));
                            JSONObject jsonObject = model.toJSONObject();
                            jsonObject.put("uid", LoggedInUser.getLoggedInUser().getUserId());
                            try {
                                HttpUtil.postToServer(URLs.UPLOAD_MODEL_SERVLET, jsonObject);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ModeBuildActivity.this, "请为您建立的Model设置名称", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();//启动
            }
        });

        listView = findViewById(R.id.image_list);

        listView.setAdapter(new MyAdaptor(ModeBuildActivity.this));

        Intent intent = getIntent();

        if (intent.getStringExtra("picture_uri") != null) {
            imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
            imageId.add(String.valueOf(imageId.size() + 1));
            imageGrayLevel.add(intent.getStringExtra("cal_result"));
            imageConcen.add("not added");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbumMethod();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final EditText inputServer = new EditText(ModeBuildActivity.this);
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder = new AlertDialog.Builder(ModeBuildActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);//设置取消键
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sign = inputServer.getText().toString();//点击确认后获取输入框的内容
                        if (sign != null && !sign.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                            imageConcen.set(i,sign);
                            TextView tv_imageConcen = view.findViewById(R.id.image_concen);
                            tv_imageConcen.setText(imageConcen.get(i));
                        } else {
                            Toast.makeText(ModeBuildActivity.this, "浓度值为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();//启动
            }
        });
    }
    //两者皆有
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChoosePhoto:
                assert data != null;
                handleImageOnKitKat(data);
            default:
                break;
        }
    }
    //相册选取相关
    //取得图片的路径
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    //打开相册
    private void openAlbumMethod() {
        if (ContextCompat.checkSelfPermission(ModeBuildActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ModeBuildActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, ChoosePhoto);
    }

    //相册访问权限
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //通过Uri和selection来获取真实的图片路径
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //显示相册中取出的图片 在这里直接进行跳转
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            Intent intent = new Intent(ModeBuildActivity.this, ShearPictureActivity.class);
            intent.putExtra("picture_uri", uri.toString());
            intent.putExtra("fromActivity","ModeBuild");
            startActivity(intent);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyAdaptor extends BaseAdapter {//lv1的适配器

        Context context;

        public MyAdaptor(Context context) {
            this.context = context;
        }//带参构造函数

        @Override
        public int getCount() {
            return imageId.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }//条目位置

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(context).inflate(R.layout.buildmode_item, null);

            TextView tv_imageId = view.findViewById(R.id.image_id);
            TextView tv_imageGrayLevel = view.findViewById(R.id.image_grayLevel);
            TextView tv_imageConcen = view.findViewById(R.id.image_concen);

            tv_imageId.setText(imageId.get(i));
            tv_imageGrayLevel.setText(imageGrayLevel.get(i));
            tv_imageConcen.setText(imageConcen.get(i));


            ImageView imageView = view.findViewById(R.id.item_image);
            try {
                //设置图片
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                if(bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return view;
        }
    }
}