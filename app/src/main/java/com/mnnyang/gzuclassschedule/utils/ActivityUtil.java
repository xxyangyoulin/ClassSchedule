package com.mnnyang.gzuclassschedule.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

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

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     *
     */
    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager, "");
        checkNotNull(fragment, "");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }


    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager, "");
        checkNotNull(fragment, "");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
