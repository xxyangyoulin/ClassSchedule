package com.mnnyang.gzuclassschedule.impt;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.http.HttpUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

/**
 * Created by mnnyang on 17-11-1.
 */

public class ImptModel implements ImptContract.Model {
    @Override
    public void getCaptcha(final ImageView iv) {

        HttpUtils.newInstance().captcha(app.mContext.getCacheDir(),
                new HttpCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        ToastUtils.show(errMsg);
                    }
                });
    }
}
