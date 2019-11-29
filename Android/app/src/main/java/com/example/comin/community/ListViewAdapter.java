package com.example.comin.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comin.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String,Object>> dialogList;
    private LayoutInflater inflater;
    private ViewHolder viewholder;
    private Context context;

    String TAG_IMAGE;
    String TAG_TEXT;
    String TAG_IDX;
    public ListViewAdapter(Context context, List<Map<String,Object>> tempList){
        this.dialogList = tempList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        TAG_IMAGE = "insuranceimage";
        TAG_TEXT = "insurancetext";
        TAG_IDX = "insuranceidx";
    }
    @Override
    public int getCount() {
        return dialogList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> tag = dialogList.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.tagdialogrow,null);

            viewholder = new ViewHolder();
            viewholder.insuranceitem = (TextView) convertView.findViewById(R.id.insuranceitem);
            viewholder.insuranceimage = (ImageView) convertView.findViewById(R.id.insuranceimage);

            convertView.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.insuranceimage.setImageResource((Integer)tag.get(TAG_IMAGE));
        viewholder.insuranceitem.setText((String)tag.get(TAG_TEXT));

        return convertView;
    }
    class ViewHolder{
        public ImageView insuranceimage;
        public TextView insuranceitem;
    }
}
