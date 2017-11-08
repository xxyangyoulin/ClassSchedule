package com.mnnyang.gzuclassschedule.setting;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

public class SettingActivity extends BaseActivity {

    AboutFragment mAboutFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBackToolbar(getString(R.string.setting));

        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingFragment()).commit();
    }

    public void addAboutFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in,R.anim.fragment_out);
        if (mAboutFragment == null) {
            mAboutFragment = AboutFragment.newInstance();
            ft.replace(android.R.id.content, mAboutFragment);
        } else {
            ft.show(mAboutFragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void removeAboutFragment() {
        if (mAboutFragment == null) return;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in,R.anim.fragment_out);
        ft.hide(mAboutFragment);
        ft.commitAllowingStateLoss();
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
    public void onBackPressed() {
        if (mAboutFragment != null && !mAboutFragment.isHidden()) {
            removeAboutFragment();
            return;
        }

        super.onBackPressed();
    }
}
