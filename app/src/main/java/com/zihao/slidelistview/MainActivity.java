package com.zihao.slidelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.slide.library.OnSlideListener;
import com.slide.library.SlideListView;
import com.slide.library.SlideView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSlideListener {

    /**
     * 默认打开的item，值为-1
     */
    private static final int DEFAULT_OPEN_POSITION = -1;

    private SlideListView slideListView;
    private SlideAdapter slideAdapter;
    private List<String> dataList;

    private int lastOpenPos = DEFAULT_OPEN_POSITION;// 记录上一次滑动打开的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        slideListView = (SlideListView) findViewById(R.id.slide_lv);
        dataList = getData();
        slideAdapter = new SlideAdapter(this, dataList, this);

        slideListView.setAdapter(slideAdapter);
        slideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Log.d(MainActivity.class.getSimpleName(), "onItemClick index:" + index);
                shrinkItemView(lastOpenPos != DEFAULT_OPEN_POSITION ? lastOpenPos : index);
            }
        });
    }

    private List<String> getData() {
        List<String> stringList = new ArrayList<>();

        stringList.add("这是一个测试的item1");
        stringList.add("这是一个测试的item2");
        stringList.add("这是一个测试的item3");
        stringList.add("这是一个测试的item4");
        stringList.add("这是一个测试的item5");
        stringList.add("这是一个测试的item6");
        stringList.add("这是一个测试的item7");
        stringList.add("这是一个测试的item8");
        stringList.add("这是一个测试的item9");

        return stringList;
    }

    @Override
    public void onSlide(SlideView slideView, int status, int position) {
        switch (status) {
            case SlideView.SLIDE_STATUS_ON:// 打开状态
                Log.e(MainActivity.class.getSimpleName(), "lastP:" + lastOpenPos + ",pos:" + position);
                // 在滑动操作其它item的同时，关闭上个打开的视图
                if (lastOpenPos != DEFAULT_OPEN_POSITION && lastOpenPos != position) {
                    // 这里需要判断正在滑动操作的item是否为与上个打开的item，如果一致，不处理
                    shrinkItemView(lastOpenPos);
                }

                lastOpenPos = position;
                break;
            case SlideView.SLIDE_STATUS_OFF:// 关闭状态
                break;
        }
    }

    @Override
    public void onDelete(int index) {
        Log.e(MainActivity.class.getSimpleName(), "del-index:" + index);
        shrinkItemView(index);
        dataList.remove(index);
        slideAdapter.notifyDataSetChanged();
    }

    /**
     * 关闭指定itemView的展开视图
     *
     * @param index 已展开视图的下标
     */
    private void shrinkItemView(int index) {
        slideListView.shrinkItemView(index);
        lastOpenPos = DEFAULT_OPEN_POSITION;
    }
}
