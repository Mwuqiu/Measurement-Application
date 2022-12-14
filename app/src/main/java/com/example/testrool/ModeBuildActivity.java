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
import net.sf.json.JSONObject;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ModeBuildActivity extends AppCompatActivity {

    static public ArrayList<String> imageId =  new ArrayList<>();
    static public ArrayList<String> imageConcen =  new ArrayList<>();
    static public ArrayList<String> imageGrayLevel =  new ArrayList<>();
    static public ArrayList<String> imageUriList = new ArrayList<>();

    final int ChoosePhoto = 1;

    ListView listView;

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
                    Toast.makeText(getApplicationContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                //????????????model??????????????????

                final EditText inputServer = new EditText(ModeBuildActivity.this);
                //inputServer.setHint("??????????????????Model ???" + model +"?????????");

                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder = new AlertDialog.Builder(ModeBuildActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("??????", null);//???????????????
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sign = inputServer.getText().toString();//???????????????????????????????????????
                        if (sign != null && !sign.isEmpty()) {//????????????????????????????????????????????????????????????
                            // TODO ?????? MODLE  String -> Json  sign ?????????
                            model.setName(sign);
                            model.setDate(String.valueOf(System.currentTimeMillis()));
                            JSONObject jsonObject = model.toJSONObject();
                            jsonObject.put("uid", LoggedInUser.getLoggedInUser().getUserId());
                            try {
                                HttpUtil.postToServer(URLs.getModelUploadServlet(),jsonObject);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(ModeBuildActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ModeBuildActivity.this,HomePageActivity.class);
                            startActivity(intent);
                            clearList();
                        } else {
                            Toast.makeText(ModeBuildActivity.this, "??????????????????Model????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();//??????
            }
        });

        listView = findViewById(R.id.image_list);

        listView.setAdapter(new MyAdaptor(ModeBuildActivity.this));

        Intent intent = getIntent();

        if (intent.getStringExtra("picture_uri") != null) {
            imageUriList.add(intent.getStringExtra("picture_uri"));
//            imageUri = Uri.parse(intent.getStringExtra("picture_uri"));
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
                        .setNegativeButton("??????", null);//???????????????
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sign = inputServer.getText().toString();//???????????????????????????????????????
                        if (sign != null && !sign.isEmpty()) {//????????????????????????????????????????????????????????????
                            imageConcen.set(i,sign);
                            TextView tv_imageConcen = view.findViewById(R.id.image_concen);
                            tv_imageConcen.setText(imageConcen.get(i));
                        } else {
                            Toast.makeText(ModeBuildActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();//??????
            }
        });
        ActivityCollectorUtil.addActivity(ModeBuildActivity.this);
    }


    private void clearList(){
        imageId.clear();
        imageConcen.clear();
        imageGrayLevel.clear();
        imageUriList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(ModeBuildActivity.this);
    }

    //????????????
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChoosePhoto:
                handleImageOnKitKat(data);
            default:
                break;
        }
    }
    //??????????????????
    //?????????????????????
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // ?????????document?????????Uri????????????document id??????
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // ????????????????????????id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // ?????????content?????????Uri??????????????????????????????
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // ?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // ??????????????????????????????
    }

    //????????????
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

    //??????????????????
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

    //??????Uri???selection??????????????????????????????
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // ??????Uri???selection??????????????????????????????
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //?????????????????????????????? ???????????????????????????
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

    public class MyAdaptor extends BaseAdapter {//lv1????????????

        Context context;

        public MyAdaptor(Context context) {
            this.context = context;
        }//??????????????????

        @Override
        public int getCount() {
            return imageId.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }//????????????

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Uri imageUri = Uri.parse(imageUriList.get(i));

            view = LayoutInflater.from(context).inflate(R.layout.buildmode_item, null);

            TextView tv_imageId = view.findViewById(R.id.image_id);
            TextView tv_imageGrayLevel = view.findViewById(R.id.image_grayLevel);
            TextView tv_imageConcen = view.findViewById(R.id.image_concen);

            tv_imageId.setText(imageId.get(i));
            tv_imageGrayLevel.setText(imageGrayLevel.get(i));
            tv_imageConcen.setText(imageConcen.get(i));


            ImageView imageView = view.findViewById(R.id.item_image);
            try {
                //????????????
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