package com.mnnyang.gzuclassschedule.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.mg.CourseMgActivity;
import com.mnnyang.gzuclassschedule.impt.ImptActivity;

public class SettingFragment extends PreferenceFragment implements SettingContract.View {

    private SettingPresenter mPresenter;

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
    public void gotoImptActivity() {
        Intent intent = new Intent(getActivity(), ImptActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void gotoCourseMgActivity() {
        Intent intent = new Intent(getActivity(), CourseMgActivity.class);
        startActivity(intent);
    }
}