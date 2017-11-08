package com.mnnyang.gzuclassschedule.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Version;
import com.mnnyang.gzuclassschedule.http.HttpCallback;
import com.mnnyang.gzuclassschedule.utils.ActivityUtil;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.VersionUpdate;

/**
 * Created by mnnyang on 17-11-8.
 */

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {

        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, null);
        initToolbar(v);
        initGithubTextView(v);
        initCheckUpdate(v);

        setHasOptionsMenu(true);
        return v;
    }

    private void initCheckUpdate(View v) {
        TextView tv = v.findViewById(R.id.tv_check_update);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();
            }
        });
    }

    private void initGithubTextView(View v) {
        TextView tv = v.findViewById(R.id.tv_github);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.github_gzuclassschedule));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void initToolbar(View v) {
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.about));
        toolbar.setNavigationIcon(R.drawable.ic_svg_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingActivity) getActivity()).removeAboutFragment();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void checkUpdate() {
        ToastUtils.show("正在检查更新...");
        final VersionUpdate versionUpdate = new VersionUpdate();
        versionUpdate.checkUpdate(new HttpCallback<Version>() {
            @Override
            public void onSuccess(Version version) {
                if (version == null) {
                    LogUtil.e(this, "version object is null");
                    return;
                }
                int localVersion = versionUpdate.getLocalVersion(app.mContext);

                LogUtil.d(this, String.valueOf(version.getCode()));
                if (version.getVersion() > localVersion) {
                    showUpdateDialog(version);
                } else {
                    ToastUtils.show("已是最新版本");
                }
            }

            @Override
            public void onFail(String errMsg) {
                LogUtil.e(this, errMsg);
                ToastUtils.show("抱歉,访问故障了");
            }
        });
    }

    private void showUpdateDialog(Version version) {
        final Activity activity = ActivityUtil.getTopActivity();
        if (activity == null) {
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.now_version))
                .setMessage(version.getMsg())
                .setPositiveButton("跳转到app商店更新",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VersionUpdate.goToMarket(activity.getBaseContext());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }
}
