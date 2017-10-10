package com.slide.library;

/**
 * ClassName：OnSlideListener
 * Description：TODO<SlideView滑块监听>
 * Author：zihao
 * Date：2017/9/30 09:52
 * Version：v1.0
 */
public interface OnSlideListener {

    /**
     * 视图滚动回调
     */
    void onSlide(SlideView view, int status, int
            position);

    /**
     * item删除回调
     */
    void onDelete(int index);

}
