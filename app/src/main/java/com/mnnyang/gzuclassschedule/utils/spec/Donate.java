package com.mnnyang.gzuclassschedule.utils.spec;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.utils.RequestPermission;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

import java.io.File;
import java.io.InputStream;

public class Donate {
    /**
     * 微信
     */
    public void donateWeixinRemind(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("微信捐赠操作步骤")
                .setMessage("点击确定按钮后会跳转微信扫描二维码界面：\n\n" + "1. 点击右上角的菜单按钮\n\n" + "2. 点击'从相册选取二维码'\n\n" + "3. 选择第一张二维码图片即可\n\n")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        donateWeixin(activity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 支付宝支付
     */
    public void donateAlipay(Activity activity) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(activity);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(activity, activity.getString(R.string.alipay_qr_code));
        }
    }

    private void donateWeixin(final Activity activity) {
        RequestPermission.with(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestPermission.Callback() {
                    @Override
                    public void onGranted() {
                        toDonateWeixin(activity);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show("微信捐赠需要在内存中存入二维码图片");
                    }
                });
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void toDonateWeixin(Activity activity) {
        InputStream weixinQrIs = activity.getResources().openRawResource(R.raw.weixin_donate);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "MDKeBiao" + File.separator +
                "didikee_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(activity, qrPath);
    }
}
