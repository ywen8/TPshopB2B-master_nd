package com.tpshop.mall.widget.swipetoloadlayout;

interface SwipeTrigger {

    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();

}
