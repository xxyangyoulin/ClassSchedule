package com.mnnyang.gzuclassschedule.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.add.AddActivity;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.mg.MgActivity;
import com.mnnyang.gzuclassschedule.impt.ImptActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

import java.util.List;

public class SettingFragment extends PreferenceFragment implements SettingContract.View {

    private SettingPresenter mPresenter;
    private DialogHelper mDeleteDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.page_setting);

        mPresenter = new SettingPresenter(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String title = (String) preference.getTitle();

        if (title.equals(getString(R.string.user_add))) {
            gotoAddActivity();
            return true;
        } else if (title.equals(getString(R.string.import_gzu))) {
            gotoImptActivity();
            return true;
        } else if (title.equals(getString(R.string.kb_manage))) {
            gotoMgActivity();
            return true;
        } else if (title.equals(getString(R.string.del_all))) {
            showDeleteConfirmDialog();
            return true;
        } else if (title.equals(getString(R.string.feedback))) {
            feedbackByQQ();
            return true;
        } else if (title.equals(getString(R.string.hide_fab))) {
            ((BaseActivity) getActivity()).notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_OTHER);
            return true;
        } else if (title.equals(getString(R.string.show_noon_course))) {
            ((BaseActivity) getActivity()).notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
            return true;
        } else if (title.equals(getString(R.string.version))) {
            mPresenter.checkUpdate();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }



    private void feedbackByQQ() {
        if (!QQIsAvailable(getActivity())) {
            ToastUtils.show(getString(R.string.qq_not_installed));
            return;
        }
        String url1 = "mqqwpa://im/chat?chat_type=wpa&uin=" + getString(R.string.qq_number);
        Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));

        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i1.setAction(Intent.ACTION_VIEW);

        startActivity(i1);
    }

    public static boolean QQIsAvailable(Context context) {
        final PackageManager mPackageManager = context.getPackageManager();
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

    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void showDeleteConfirmDialog() {
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showNormalDialog(getActivity(), getString(R.string.please_note), getString(R.string.dialog_confirm_delete),
                new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        mPresenter.deleteAllCourse();
                    }
                });
    }

    public void gotoImptActivity() {
        ((BaseActivity) getActivity()).gotoActivity(ImptActivity.class);
        getActivity().finish();
    }

    public void gotoMgActivity() {
        ((BaseActivity) getActivity()).gotoActivity(MgActivity.class);
    }

    public void gotoAddActivity() {
        ((BaseActivity) getActivity()).gotoActivity(AddActivity.class);
    }

    @Override
    public void showDeleting() {
        mDeleteDialog = new DialogHelper();
        mDeleteDialog.showProgressDialog(getActivity(), getString(R.string.deleting), getString(R.string.please_wait_a_moment), false);
    }

    @Override
    public void hideDeleting() {
        if (mDeleteDialog != null) {
            mDeleteDialog.hideProgressDialog();
        }
        ((BaseActivity) getActivity()).notifiUpdateMainPage(
                Constant.INTENT_UPDATE_TYPE_COURSE);
    }

}