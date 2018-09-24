package com.mnnyang.gzuclassschedule.custom.course2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class StopEventView extends FrameLayout {
    public StopEventView(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
