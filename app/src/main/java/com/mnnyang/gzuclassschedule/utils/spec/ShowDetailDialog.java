package com.mnnyang.gzuclassschedule.utils.spec;

import android.app.Activity;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

/**
 * Created by mnnyang on 17-11-7.
 */

public class ShowDetailDailog {
    public void show(Activity activity, Course course){
        if (null ==course){
            LogUtils.e(this, "show()--> course is null");
            return;
        }


    }
}
