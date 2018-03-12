package com.mnnyang.gzuclassschedule.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.about.AboutActivity;
import com.mnnyang.gzuclassschedule.add.AddActivity;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.custom.settting.SettingItemNormal;
import com.mnnyang.gzuclassschedule.impt.ImptActivity;
import com.mnnyang.gzuclassschedule.mg.MgActivity;
import com.mnnyang.gzuclassschedule.utils.ActivityUtil;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

import static com.mnnyang.gzuclassschedule.app.Constant.themeColorArray;
import static com.mnnyang.gzuclassschedule.app.Constant.themeNameArray;

public class SettingActivity extends BaseActivity implements SettingContract.View,
        SettingItemNormal.SettingOnClickListener {
    private SettingItemNormal sinUserAdd;
    private SettingItemNormal sinImportGzu;
    private SettingItemNormal sinKbManage;
    private SettingItemNormal sinShowNoon;
    private SettingItemNormal sinHideFab;
    private SettingItemNormal sinMorePref;
    private SettingItemNormal sinFeedback;
    private SettingItemNormal sinAbout;
    private SettingPresenter mPresenter;
    private HorizontalScrollView hsvTheme;
    private LinearLayout layoutTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBackToolbar(getString(R.string.setting));

        initView();
        initDefaultValues();

        mPresenter = new SettingPresenter(this);
    }

    private void initView() {
        hsvTheme = findViewById(R.id.hsv_theme);
        layoutTheme = findViewById(R.id.layout_theme);

        sinUserAdd = findViewById(R.id.sin_user_add);
        sinImportGzu = findViewById(R.id.sin_import_gzu);
        sinKbManage = findViewById(R.id.sin_kb_manage);
        sinShowNoon = findViewById(R.id.sin_show_noon);
        sinHideFab = findViewById(R.id.sin_hide_fab);
        sinMorePref = findViewById(R.id.sin_more_pref);
        sinFeedback = findViewById(R.id.sin_feedback);
        sinAbout = findViewById(R.id.sin_about);


        sinUserAdd.setSettingOnClickListener(this);
        sinImportGzu.setSettingOnClickListener(this);
        sinKbManage.setSettingOnClickListener(this);
        sinShowNoon.setSettingOnClickListener(this);
        sinHideFab.setSettingOnClickListener(this);
        sinMorePref.setSettingOnClickListener(this);
        sinFeedback.setSettingOnClickListener(this);
        sinAbout.setSettingOnClickListener(this);

        layoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThemeDialog();
            }
        });
    }

    private void initDefaultValues() {
        sinShowNoon.setChecked(PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getBoolean(getString(R.string.app_preference_show_noon),
                        false));

        sinHideFab.setChecked(PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getBoolean(getString(R.string.app_preference_hide_fab),
                        true));
    }

    @Override
    public void onClick(View view, boolean checked) {
        System.out.println(view);
        switch (view.getId()) {
            case R.id.sin_user_add:
                gotoAddActivity();
                break;
            case R.id.sin_import_gzu:
                gotoImpActivity();
                break;

            case R.id.sin_kb_manage:
                gotoMgActivity();
                break;

            case R.id.sin_show_noon:
                showNoonPref(checked);
                break;

            case R.id.sin_hide_fab:
                hideFabPref(checked);
                break;

            case R.id.sin_more_pref:
                //TODO more opr
                ToastUtils.show("更多");
                break;
            case R.id.sin_feedback:
                mPresenter.feedback();
                break;
            case R.id.sin_about:
                gotoAboutActivity();
                break;

            case R.id.hsv_theme:
                showThemeDialog();
            default:
                break;
        }
    }

    int theme;
    private void showThemeDialog() {
        ScrollView scrollView = new ScrollView(this);
        RadioGroup radioGroup = new RadioGroup(this);
        scrollView.addView(radioGroup);
        int margin = ScreenUtils.dp2px(16);
        radioGroup.setPadding(margin / 2, margin, margin, margin);

        for (int i = 0; i < themeColorArray.length; i++) {
            AppCompatRadioButton arb = new AppCompatRadioButton(this);

            RadioGroup.LayoutParams params =
                    new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            arb.setLayoutParams(params);
            arb.setId(i);
            arb.setTextColor(getResources().getColor(themeColorArray[i]));
            arb.setText(themeNameArray[i]);
            arb.setTextSize(16);
            arb.setPadding(0, margin/2, 0, margin/2);
            radioGroup.addView(arb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                theme = checkedId;
            }
        });

        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showCustomDialog(this, scrollView,
                getString(R.string.theme_preference), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                dialog.dismiss();
                String key = getString(R.string.app_preference_theme);
                int oldTheme = Preferences.getInt(key, 0);

                if (theme != oldTheme) {
                    Preferences.putInt(key, theme);
                    ActivityUtil.finishAll();
                    startActivity(new Intent(app.mContext, CourseActivity.class));
                }
            }
        });
    }

    private void hideFabPref(boolean checked) {
        PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .edit()
                .putBoolean(getString(R.string.app_preference_hide_fab), checked)
                .apply();

        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_OTHER);
    }

    private void showNoonPref(boolean checked) {
        PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .edit()
                .putBoolean(getString(R.string.app_preference_show_noon), checked)
                .apply();

        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
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

    private void gotoAboutActivity() {
        gotoActivity(AboutActivity.class);
    }

    private void gotoMgActivity() {
        gotoActivity(MgActivity.class);
    }

    private void gotoImpActivity() {
        gotoActivity(ImptActivity.class);
    }

    public void gotoAddActivity() {
        gotoActivity(AddActivity.class);
    }


    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }
}
