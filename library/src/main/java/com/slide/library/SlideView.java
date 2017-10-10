package com.slide.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * ClassName：SlideView
 * Description：TODO<侧滑删除视图>
 * Author：zihao
 * Date：2017/9/30 09:50
 * Version：v1.0
 */
public class SlideView extends LinearLayout {
    private static final String TAG = SlideView.class.getSimpleName();
    /**
     * 视图滑动状态-关闭
     */
    public static final int SLIDE_STATUS_OFF = 0;
    /**
     * 视图滑动状态-打开
     */
    public static final int SLIDE_STATUS_ON = 1;

    private LinearLayout viewContent;// 中心视图内容
    private int position;// 视图的下标
    private Scroller scroller;
    private int holderWidth = 50;// 删除视图宽度

    private OnSlideListener onSlideListener;
    private int lastX = 0;// 最终X轴位置
    private int lastY = 0;// 最终Y轴位置
    private static final int TAN = 2;// 偏差系数

    /**
     * SlideView构造方法
     *
     * @param context  上下文对象
     * @param position 视图在列表中的下标
     */
    public SlideView(Context context, int position) {
        this(context, null, position);
    }

    /**
     * SlideView构造方法
     *
     * @param context  上下文对象
     * @param attrs    attrs
     * @param position 视图在列表中的下标
     */
    public SlideView(Context context, AttributeSet attrs, int position) {
        super(context, attrs);
        this.position = position;
        initView();
    }

    private void initView() {
        scroller = new Scroller(getContext());

        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(getContext(), R.layout.slide_view_merge, this);
        viewContent = findViewById(R.id.view_content);
        RelativeLayout deleteLayout = findViewById(R.id.delete_layout);// 滑动出现的删除视图
        deleteLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (onSlideListener != null) {
                    onSlideListener.onDelete(position);
                }
            }
        });
        holderWidth = deleteLayout.getLayoutParams().width;
    }

    /**
     * 设置中心内容--Item视图
     */
    public void setContentView(View view) {
        viewContent.addView(view);
    }

    /**
     * 收缩--关闭
     */
    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
            // 反馈当前行为
            if (onSlideListener != null) {
                onSlideListener.onSlide(this, SLIDE_STATUS_OFF, position);
            }
        }
    }

    /**
     * 触摸命令事件监听
     *
     * @param event MotionEvent
     */
    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        Log.d(TAG, "x=" + x + "  y=" + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
                // 判断是否滚动结束
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:// 移动
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }

                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > holderWidth) {
                        newScrollX = holderWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:// 抬起
                newScrollX = 0;

                // 获取滑动值
                if (scrollX - holderWidth * 0.75 > 0) {
                    newScrollX = holderWidth;
                }

                // 滚动到指定位置
                this.smoothScrollTo(newScrollX, 0);
                // 反馈当前行为
                if (onSlideListener != null) {
                    onSlideListener.onSlide(this, newScrollX == 0 ? SLIDE_STATUS_OFF
                            : SLIDE_STATUS_ON, position);
                }
                break;
        }

        // 记录最终停靠下标
        lastX = x;
        lastY = y;
    }

    /**
     * 滚动到指定位置
     *
     * @param destX 目标X坐标
     * @param destY 目标Y坐标
     */
    private void smoothScrollTo(int destX, int destY) {
        // 缓慢滚动到指定位置
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        scroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {// 判断是否滚动完成
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 为滑块View设置监听
     */
    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }
}
