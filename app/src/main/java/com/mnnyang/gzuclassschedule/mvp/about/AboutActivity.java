package com.mnnyang.gzuclassschedule.mvp.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.beanv2.VersionWrapper;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.spec.VersionUpdate;

/**
 * Created by xxyangyoulin on 2018/3/13.
 */

public class AboutActivity extends BaseActivity implements AboutContract.View {

    private AboutContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initBackToolbar(getString(R.string.about));
        initLinkTextView();
        initVersionName();
        initCheckUpdate();

        new AboutPresenter(this);
    }

    private void initVersionName() {
        TextView tvVersionName = findViewById(R.id.tv_version);

        VersionUpdate vu = new VersionUpdate();
        String versionName = vu.getLocalVersionName(app.mContext);
        tvVersionName.setText(versionName);
    }

    private void initCheckUpdate() {
        TextView tv = findViewById(R.id.tv_check_update);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkUpdate();
            }
        });
    }

    private void initLinkTextView() {
        findViewById(R.id.tv_github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.github_gzuclassschedule));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_blog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.blog_xxyangyoulin_url));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    @Override
    public void showMassage(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void showUpdateVersionInfo(VersionWrapper.Version version) {
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showNormalDialog(this, getString(R.string.now_version), version.getDescribe(), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                VersionUpdate.goToMarket(getBaseContext());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
