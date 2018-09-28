package com.mnnyang.gzuclassschedule.mvp.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;

import java.util.List;

/**
 * Created by mnnyang on 17-10-19.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View view) {
        this.mView = view;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        //nothing to do
    }

    @Override
    public void feedback() {
        if (!QQIsAvailable()) {
            mView.showNotice(app.mContext.getString(R.string.qq_not_installed));
            return;
        }

        String url1 = "mqqwpa://im/chat?chat_type=wpa&uin=" +
                app.mContext.getString(R.string.qq_number);
        Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));

        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i1.setAction(Intent.ACTION_VIEW);

        app.mContext.startActivity(i1);
    }

    private boolean QQIsAvailable() {
        final PackageManager mPackageManager = app.mContext.getPackageManager();
        List<PackageInfo> pinfo = mPackageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
