package com.example.testrool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.testrool.ui.gallery.GalleryFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testrool.databinding.ActivityMineInfoBinding;

import java.io.File;
import java.io.IOException;

public class HomePageActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMineInfoBinding binding;

    public static final int TakePhoto = 1;

    public static final int ChoosePhoto = 2;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMineInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMineInfo.toolbar);

        //浮动按钮
        binding.appBarMineInfo.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoseWayMenu(binding.appBarMineInfo.fab);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_mine_info);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        ActivityCollectorUtil.addActivity(HomePageActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(HomePageActivity.this);
    }

    private void showChoseWayMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.choseway, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_photo:
                        startCamera();
                        break;
                    case R.id.access_dir:
                        openAlbumMethod();
                        break;
                    case R.id.build_mode:
                        Intent intent = new Intent(HomePageActivity.this, ModeBuildActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_info, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shareButton:
                //to do tiaozhuan share page
                int a;
                a= 1;
                break;
        }
        return true;
    }*/



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_mine_info);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //相机拍摄相关
    private void startCamera() {
        File outputImage = new File(getExternalCacheDir(), "outPut_img.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(HomePageActivity.this, "com.example.testrool.fileprvider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TakePhoto);
    }

    //两者皆有
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TakePhoto:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(HomePageActivity.this, ShearPictureActivity.class);
                    intent.putExtra("fromActivity", "HomePage");
                    intent.putExtra("picture_uri", imageUri.toString());
                    startActivity(intent);
                }
                break;
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
        if (ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            Intent intent = new Intent(HomePageActivity.this, ShearPictureActivity.class);
            intent.putExtra("picture_uri", uri.toString());
            intent.putExtra("fromActivity", "HomePage");
            startActivity(intent);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}