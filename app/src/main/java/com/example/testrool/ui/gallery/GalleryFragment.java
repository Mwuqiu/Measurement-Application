package com.example.testrool.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testrool.R;
import com.example.testrool.ShowResultActivity;
import com.example.testrool.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GalleryFragment extends ListFragment {

    private FragmentGalleryBinding binding;

    static public  ArrayList<String> my_array =  new ArrayList<>();//数据待补充

    static public ArrayList<String> my_array1 = new ArrayList<>();

    static private boolean initItem = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);*/

        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        if(initItem == false){
            my_array.add("点此可查看详细信息");
            my_array1.add("合格");
            initItem = true;
        }

        binding.list.setAdapter(new MyAdaptor(getActivity()));

        /*lv1 = binding.list;

        lv1.setAdapter(new MyAdaptor(HistoryPageActivity.this));//上下文传activity*/


        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击条目转到resultactivity,可显示详细信息，待补充
                Intent intent = new Intent(getActivity(), ShowResultActivity.class);
                //TO DO 向 result Page 传递信息
                startActivity(intent);
            }
        });

/*        final TextView textView = binding.textGallery;

        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button button = binding.testButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "a ba a ba", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        return root;
    }

    public class MyAdaptor extends BaseAdapter {//lv1的适配器

        Context context;

        public MyAdaptor(Context context) {
            this.context = context;
        }//带参构造函数

        @Override
        public int getCount() {
            return my_array.size();
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
            //布局从itemview1中获取
            view = LayoutInflater.from(context).inflate(R.layout.history_item, null);

            TextView tvv1 = view.findViewById(R.id.tvv1);
            TextView tvv2 = view.findViewById(R.id.tvv2);
            TextView tvv3 = view.findViewById(R.id.tvv3);
            tvv1.setText(my_array.get(i));
            tvv2.setText(my_array1.get(i));

            //此处取当前时间，实际应改为取样时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            tvv3.setText(str);
            return view;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}