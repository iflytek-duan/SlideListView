package com.slide.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * ClassName：SlideListView
 * Description：TODO<自定义滑动删除ListView>
 * Author：zihao
 * Date：2017/9/30 09:57
 * Version：v1.0
 *
 * @link (Android处理滑动与点击事件的冲突)[http://www.cnblogs.com/liujingg/archive/2015/12/03/5017127.html]
 */
public class SlideListView extends ListView {

    private static final int MIN_SCROLL_DISTANCE = 50;// 最小滑动距离

    private SlideView mFocusedItemView;// 获取到焦点的itemView

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 收缩关闭指定视图
     *
     * @param position position
     */
    public void shrinkItemView(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int startX = (int) event.getX();
                int startY = (int) event.getY();
                int position = pointToPosition(startX, startY);

                if (position != INVALID_POSITION) {
                    try {
                        mFocusedItemView = (SlideView) getChildAt(position);
                    } catch (ClassCastException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }

        // 为Item设置滑动监听
        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

}
