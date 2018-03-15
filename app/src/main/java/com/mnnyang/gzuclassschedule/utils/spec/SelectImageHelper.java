package com.mnnyang.gzuclassschedule.utils.spec;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

import java.io.File;

/**
 * Created by xxyangyoulin on 2018/3/15.
 */

public class SelectImageHelper {
    private Activity mActivity;

    public SelectImageHelper(Activity activity) {
        mActivity = activity;
    }

    public void start(int requestCode){
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(albumIntent, requestCode);
    }

    public void crop(File toSaveFile, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");//TODO crop view not find
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(toSaveFile));

        mActivity.startActivityForResult(intent, requestCode);
    }

}
