package com.example.testrool.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.example.testrool.Http.HttpUtil;
import com.example.testrool.R;
import com.example.testrool.Http.URLs;
import com.example.testrool.ShowResultActivity;
import com.example.testrool.adapter.HistoryAdapter;
import com.example.testrool.bean.HistoryItem;
import com.example.testrool.bean.LoggedInUser;
import com.example.testrool.databinding.FragmentGalleryBinding;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;


public class GalleryFragment extends ListFragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);*/

        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        ArrayList<HistoryItem> historyItems = new ArrayList<>();

        LoggedInUser user = LoggedInUser.getLoggedInUser();

        if(!"root".equals(user.getDisplayName())){
            try {
                String res = HttpUtil.postToServer(URLs.getHistoryitemServlet() + "?id=" + user.getUserId(), null);
                Log.e("RES", res);
                JSONArray jsonArray = JSONArray.fromObject(res);
                for (Object a : jsonArray){
                    JSONObject x = JSONObject.fromObject(a);
                    HistoryItem historyItem = HistoryItem.fromJSONObject(x);
                    historyItems.add(historyItem);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            HistoryItem historyItem = new HistoryItem();
            historyItem.setItemName("?????????");
            historyItem.setResult("??????");
            historyItem.setDate("2021-02-19 13:14:07");
            historyItems.add(historyItem);

            historyItem = new HistoryItem();
            historyItem.setItemName("?????????");
            historyItem.setResult("?????????");
            historyItem.setDate("2022-11-19 10:01:15");
            historyItems.add(historyItem);

            historyItem = new HistoryItem();
            historyItem.setItemName("2,4-D");
            historyItem.setResult("?????????");
            historyItem.setDate("2022-09-11 08:31:22");
            historyItems.add(historyItem);
        }

        HistoryAdapter.activity = getActivity();

        binding.list.setAdapter(new HistoryAdapter(getActivity(),R.layout.history_item,historyItems));

        return root;
    }

//    public class MyAdaptor extends BaseAdapter {//lv1?????????????
//
//        Context context;
//
//        public MyAdaptor(Context context) {
//            this.context = context;
//        }//???????????????????
//
//        @Override
//        public int getCount() {
//            return my_array.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }//????????????
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            //?????????itemview1??????????
//            view = LayoutInflater.from(context).inflate(R.layout.history_item, null);
//
//            TextView tvv1 = view.findViewById(R.id.tvv1);
//            TextView tvv2 = view.findViewById(R.id.tvv2);
//            TextView tvv3 = view.findViewById(R.id.tvv3);
//            tvv1.setText(my_array[i]);
//            tvv2.setText(my_array1[i]);
//
//            //???????????????????????????????????????????????????????
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy???MM???dd???? HH:mm:ss");
//            Date curDate = new Date(System.currentTimeMillis());//??????????????????
//            String str = formatter.format(curDate);
//            tvv3.setText(str);
//            return view;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}