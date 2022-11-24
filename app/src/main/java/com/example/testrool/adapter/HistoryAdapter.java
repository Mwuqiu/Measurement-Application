package com.example.testrool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testrool.R;
import com.example.testrool.bean.HistoryItem;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private int resourceId;

    public HistoryAdapter(Context context, int resourceId, List<HistoryItem> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem historyItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) view.findViewById(R.id.tvv_name);
            viewHolder.result = (TextView) view.findViewById(R.id.tvv_result);
            viewHolder.date = (TextView) view.findViewById(R.id.tvv_date);
            view.setTag(viewHolder); //将viewHolder存储在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.itemName.setText(String.valueOf(historyItem.getItemName()));
        viewHolder.result.setText(historyItem.getResult());
        viewHolder.date.setText(historyItem.getDate());
        return view;
    }

    static class ViewHolder {
        TextView itemName;
        TextView result;
        TextView date;
    }
}
