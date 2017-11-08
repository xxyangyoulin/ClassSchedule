package com.mnnyang.gzuclassschedule.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 17-11-8.
 */

public class ActivityUtil {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static Activity getTopActivity() {
        return activities.get(activities.size() - 1);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
