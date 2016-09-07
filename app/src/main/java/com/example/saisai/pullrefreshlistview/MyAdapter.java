package com.example.saisai.pullrefreshlistview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by saisai on 2016/9/8.
 */
public class MyAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private final Activity activity;

    public MyAdapter(List<String> list,Context context){
        this.list=list;
        this.context=context;
        activity = (Activity)context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView tv=new TextView(context);
        tv.setText(list.get(i));
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(activity.getWindowManager().getDefaultDisplay().getWidth(),DimensUtils.dp2px(50));
        tv.setLayoutParams(params);
        return tv;
    }

}
