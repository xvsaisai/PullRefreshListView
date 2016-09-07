package com.example.saisai.pullrefreshlistview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by saisai on 2016/8/29.
 */
public class PullRefreshListView extends RelativeLayout {


    private ImageView headView;
    private LayoutParams headPramas;
    private ImageView footView;
    private LayoutParams footParams;
    public ListView lv;
    private LayoutParams lvParams;
    private int headHeight;
    private int footHeight;
    private float y;


    public PullRefreshListView(Context context) {
        this(context,null);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {


        headView =new ImageView(getContext());
        headView.setImageResource(R.drawable.icon_loaing_frame_1);
        headPramas = new LayoutParams(LayoutParams.MATCH_PARENT, DimensUtils.dp2px(20));
        headPramas.addRule(CENTER_HORIZONTAL);
        headView.measure(0,0);
        headHeight = headView.getMeasuredHeight();
        headPramas.topMargin=-headHeight;
        headView.setLayoutParams(headPramas);
        headView.setId(R.id.pull_load_head);

        footView = new ImageView(getContext());
        footView.setImageResource(R.drawable.icon_loaing_frame_1);
        footParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        footParams.addRule(ALIGN_PARENT_BOTTOM);
        footView.measure(0,0);
        footHeight = footView.getMeasuredHeight();
        footParams.bottomMargin=-footHeight;
        footView.setLayoutParams(footParams);
        footView.setId(R.id.pull_load_foot);


        lv = new ListView(getContext());
        lvParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lvParams.addRule(BELOW,R.id.pull_load_head);
        lvParams.addRule(ABOVE,R.id.pull_load_foot);
        lv.setLayoutParams(lvParams);

        addView(headView);
        addView(footView);
        addView(lv);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        lv.setOnItemClickListener(listener);
    }

    public void setAdapter(BaseAdapter adapter){
        lv.setAdapter(adapter);
    }

    public void addHeadView(View head){
        lv.addHeaderView(head);
    }
    public void addFootView(View foot){
        lv.addFooterView(foot);
    }

    //设置item点击时候的状态
    public void setSelector(Drawable drawable) {
        if (drawable == null)
            throw new RuntimeException(getClass().getSimpleName() + "====设置的背景选择器不能为null");
        lv.setSelector(drawable);
    }

    /**
     *     得到listView第一个显示的条目
     */
    public int getFirstVisibilePosition(){
        return lv.getFirstVisiblePosition();
    }

    /**
     * 设置选中的item
     * @param position
     */
    public void setSelection(int position){
        lv.setSelection(position);
    }

    /**
     *
     *     滚动监听
     */
    public interface OnScrollListener {
        void onScrollStateChanged(AbsListView view, int scrollState);
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    boolean isDownUsable = true;

    /**
     * 设置下拉刷新是否启用
     * @param downUsable
     */
    public void setPullDownUsable(boolean downUsable) {
        this.isDownUsable = downUsable;
    }

    boolean isUpUsable = true;

    /**
     * 设置上拉加载是否启用
     * @param upUsable
     */
    public void setPullUpUsable(boolean upUsable) {
        this.isUpUsable = upUsable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                y=ev.getY();
                if(isPull||isLoad){
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:


                float moveY = ev.getY() - y;
                if(moveY>0&&lv.getFirstVisiblePosition()==0){
                    View childAt = lv.getChildAt(0);
                    if(childAt.getTop()>=0){
                        if (!isDownUsable) {
                            break;
                        }
                        return true;
                    }
                }
//                Log.e("tag",lv.getLastVisiblePosition()+"===="+(lv.getAdapter().getCount()-1));
                if(moveY<0&&lv.getLastVisiblePosition()==lv.getAdapter().getCount()-1){
                    int i = lv.getCount() - lv.getFirstVisiblePosition();
                    View childAt = lv.getChildAt(i - 1);
                    if(childAt.getBottom()<=lv.getMeasuredHeight()){
//                        Log.e("tag","true");
                        if (!isUpUsable) {
                            break;
                        }
                        return true;
                    }
                }

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_MOVE:

                if(isPull||isLoad){
                    break;
                }
                float moveY = event.getY() - y;
                if(moveY>0&&lv.getFirstVisiblePosition()==0){
                    if (!isDownUsable) {
                        break;
                    }
                    headPramas.topMargin= (int) (-headHeight+moveY);
                    headView.setLayoutParams(headPramas);
                }
                if(moveY<0&&lv.getLastVisiblePosition()==lv.getAdapter().getCount()-1){
                    if (!isUpUsable) {
                        break;
                    }
                    footParams.bottomMargin= (int) (-footHeight-moveY);
                    footView.setLayoutParams(footParams);
                    lvParams.topMargin= (int) moveY;
                    lv.setLayoutParams(lvParams);
                }

                break;

            case MotionEvent.ACTION_UP:

                if(headPramas.topMargin>=0){
                    isPull=true;
                    headPramas.topMargin=0;
                    headView.setLayoutParams(headPramas);
                    headView.setImageResource(R.drawable.load);
                    if(onPullOrLoadListener!=null){
                        onPullOrLoadListener.pull();
                    }
                }else {
                    headPramas.topMargin=-headHeight;
                    headView.setLayoutParams(headPramas);
                }

                if(footParams.bottomMargin>=0){
                    isLoad=true;
                    footParams.bottomMargin=0;
                    footView.setLayoutParams(footParams);
                    footView.setImageResource(R.drawable.load);
                    lvParams.topMargin = -footHeight;
                    lv.setLayoutParams(lvParams);
                    if(onPullOrLoadListener!=null){
                        onPullOrLoadListener.load();
                    }
                }else {
                    footParams.bottomMargin=-footHeight;
                    footView.setLayoutParams(footParams);
                    lvParams.topMargin = 0;
                    lv.setLayoutParams(lvParams);
                }
                break;
        }

        return true;
    }


    boolean isPull;
    boolean isLoad;

    /**
     * 下拉刷新完成执行
     */
    public void pullSuccess(){
        isPull=false;
        headView.setImageResource(R.drawable.icon_loaing_frame_1);
        headPramas.topMargin=-headHeight;
        headView.setLayoutParams(headPramas);
    }
    /**
     * 上拉加载完成执行
     */
    public void loadSuccess(){
        isLoad=false;
        footView.setImageResource(R.drawable.icon_loaing_frame_1);
        footParams.bottomMargin=-footHeight;
        footView.setLayoutParams(footParams);
        lvParams.topMargin=0;
        lv.setLayoutParams(lvParams);
    }

    public interface OnPullOrLoadListener{
        void pull();
        void load();
    }

    private OnPullOrLoadListener onPullOrLoadListener;
    public void setOnPullOrLoadListener(OnPullOrLoadListener onPullOrLoadListener){
        this.onPullOrLoadListener=onPullOrLoadListener;
    }

}
