package com.zihao.slidelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slide.library.OnSlideListener;
import com.slide.library.SlideView;

import java.util.List;

/**
 * ClassName：SlideAdapter
 * Description：TODO<SlideListView数据适配器>
 * Author：zihao
 * Date：2017/9/30 10:39
 * Version：v1.0
 */
public class SlideAdapter extends BaseAdapter {

    private Context context;
    private List<String> dataArray;
    private OnSlideListener listener;

    public SlideAdapter(Context context, List<String> dataArray, OnSlideListener listener) {
        this.context = context;
        this.dataArray = dataArray;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dataArray != null ? dataArray.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // 这里的SlideView实际上等同于我们ListView中的convertView，它包含显示内容item以及滑动删除的内容
        SlideView slideView;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            View itemView = LayoutInflater.from(context).inflate(
                    R.layout.slide_lv_item, parent, false);// 这里的item用来显示正常可视内容

            slideView = new SlideView(context, position);
            slideView.setContentView(itemView);// 添加默认状态下可视的itemView

            viewHolder.tvTitle = itemView.findViewById(R.id.slide_tv_title);

            slideView.setOnSlideListener(listener);
            slideView.setTag(viewHolder);
        } else {
            slideView = (SlideView) convertView;
            viewHolder = (ViewHolder) slideView.getTag();
        }

        viewHolder.tvTitle.setText(dataArray.get(position));
        return slideView;
    }

    private class ViewHolder {
        TextView tvTitle;
    }

}
