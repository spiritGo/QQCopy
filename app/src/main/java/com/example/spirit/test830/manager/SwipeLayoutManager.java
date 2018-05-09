package com.example.spirit.test830.manager;

import com.example.spirit.test830.views.SwipeLayout;

public class SwipeLayoutManager {
    private SwipeLayoutManager() {
    }

    private static SwipeLayoutManager swipeLayoutManager = new SwipeLayoutManager();


    public static SwipeLayoutManager getSwipeLayoutManager() {
        return swipeLayoutManager;
    }

    private SwipeLayout currentLayout;

    public void setSwipeLayout(SwipeLayout layout) {
        this.currentLayout = layout;
    }

    public boolean isShouldSwipe(SwipeLayout layout) {
        if (currentLayout == null) {
            //有打开的layout
            return true;
        } else {
            return currentLayout == layout;
        }
    }

    public void closeCurrentLayout() {
        if (currentLayout != null) {
            currentLayout.close();
        }
    }

    //清空当前记录的已经打开的layout
    public void clearCurrentLayout() {
        currentLayout = null;
    }
}
