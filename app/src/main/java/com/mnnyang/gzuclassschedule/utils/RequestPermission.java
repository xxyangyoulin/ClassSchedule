package com.mnnyang.gzuclassschedule.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 18-1-13.
 */

public class RequestPermission {
    private static final String TAG = "RequestPermission";
    private static int autoCode;
    private static SparseArray<Callback> callbacks = new SparseArray<>();

    private Object target;
    private String[] permissions;

    private Callback callback;

    public interface Callback {
        void onGranted();

        void onDenied();
    }

    private RequestPermission(@NonNull Activity activity) {
        this.target = activity;
    }

    private RequestPermission(@NonNull Fragment fragment) {
        this.target = fragment;
    }

    public static RequestPermission with(@NonNull Activity activity) {
        return new RequestPermission(activity);
    }

    public static RequestPermission with(@NonNull Fragment fragment) {
        return new RequestPermission(fragment);
    }

    public RequestPermission permissions(@NonNull String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void request(Callback callback) {
        this.callback = callback;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onGranted();
            }
            return;
        }

        Activity activity = getActivity();

        List<String> deniedList = getDeniedPermissions(activity);

        if (deniedList == null) {
            callback.onGranted();
            return;
        }

        int requestCode = autoCode++;
        request(deniedList, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void request(List<String> deniedList, int code) {
        String[] deniedPermissions = deniedList.toArray(new String[deniedList.size()]);
        if (target instanceof Activity) {
            ((Activity) target).requestPermissions(deniedPermissions, code);
            callbacks.put(code, callback);
        } else if (target instanceof Fragment) {
            ((Fragment) target).requestPermissions(deniedPermissions, code);
            callbacks.put(code, callback);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults) {
        Callback callback = callbacks.get(requestCode);

        if (callback == null) {
            Log.d(TAG, "callback is null");
            return;
        }

        callbacks.remove(requestCode);

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                callback.onDenied();
                return;
            }
        }

        callback.onGranted();
    }

    private Activity getActivity() {
        Activity activity;
        if (target instanceof Activity) {
            activity = (Activity) target;
        } else if (target instanceof Fragment) {
            activity = ((Fragment) target).getActivity();
        } else {
            throw new IllegalArgumentException("It has to be an activity or fragment.");
        }
        return activity;
    }

    private List<String> getDeniedPermissions(Activity activity) {
        List<String> deniedList = null;

        if (permissions == null) return null;

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {

                if (deniedList == null)
                    deniedList = new ArrayList<>();

                deniedList.add(permission);
            }
        }
        return deniedList;
    }
}
