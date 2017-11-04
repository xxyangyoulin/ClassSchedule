package com.mnnyang.gzuclassschedule.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.mg.MgActivity;
import com.mnnyang.gzuclassschedule.impt.ImptActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

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
        System.out.println(preference.getTitle());
        String title = (String) preference.getTitle();

        if (title.equals(getString(R.string.user_add))) {
            return true;
        } else if (title.equals(getString(R.string.import_gzu))) {
            mPresenter.imptGzuCourse();
            return true;
        } else if (title.equals(getString(R.string.kb_manage))) {
            mPresenter.manageCourse();
            return true;
        } else if (title.equals(getString(R.string.del_all))) {
            mPresenter.deleteAllCourse();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void gotoImptActivity() {
        Intent intent = new Intent(getActivity(), ImptActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void gotoCourseMgActivity() {
        Intent intent = new Intent(getActivity(), MgActivity.class);
        startActivity(intent);
    }

    @Override
    public void showDeleting() {
        mDeleteDialog = new DialogHelper();
        mDeleteDialog.showProgressDialog(getActivity(), "正在删除", "请稍等...", false);
    }

    @Override
    public void hideDeleting() {
        if (mDeleteDialog != null) {
            mDeleteDialog.hideProgressDialog();
        }
        notifiUpdate();
    }

    /**
     * 通知更新
     */
    private void notifiUpdate() {
        Intent intent = new Intent();
        intent.setAction(Constant.INTENT_UPDATE);
        getActivity().sendBroadcast(intent);
    }
}