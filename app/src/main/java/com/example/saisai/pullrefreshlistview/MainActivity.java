package com.example.saisai.pullrefreshlistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullRefreshListView.OnPullOrLoadListener {

    private PullRefreshListView pulllv;
    private List<String> list;
    private MyAdapter adapter;

    android.os.Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.pulllv = (PullRefreshListView) findViewById(R.id.pulllv);

        initData();
        initAdapter();
        initListener();


    }

    /**
     * 设置监听
     */
    private void initListener() {

        pulllv.setOnPullOrLoadListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        list = new ArrayList<>();
        for (int i=0;i<50;i++){
            list.add("item==="+i);
        }
    }
    /**
     * 初始化适配器
     */
    private void initAdapter() {

        adapter = new MyAdapter(list,this);
        pulllv.setAdapter(adapter);
    }


    int refreshCount=0;
    /**
     * 下拉刷新时候回调
     */
    @Override
    public void pull() {

        //模拟加载数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add(0,"刷新的数据"+refreshCount++);
                adapter.notifyDataSetChanged();
                //恢复默认状态
                pulllv.pullSuccess();
            }
        },3000);
    }

    int loadCount=0;
    /**
     * 上拉加载时候回调
     */
    @Override
    public void load() {
        //模拟加载数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add("加载的数据"+loadCount++);
                adapter.notifyDataSetChanged();
                //恢复默认状态
                pulllv.loadSuccess();
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移出任务
        handler.removeCallbacksAndMessages(null);
    }
}
