package com.mnnyang.gzuclassschedule.mvp.school;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.mvp.impt.ImptActivity;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;

/**
 * Created by xxyangyoulin on 2018/4/9.
 */

public class SchoolActivity extends BaseActivity implements SchoolContract.View, View.OnClickListener {

    private SchoolContract.Presenter mPresenter;
    private DialogHelper mDialogHelper;
    private DialogHelper mTestingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        initBackToolbar(getString(R.string.school));

        initView();

        new SchoolPresenter(this);
    }

    private void initView() {
        TextView tvGzu = findViewById(R.id.tv_gzu);
        TextView tvOtherSchool = findViewById(R.id.tv_other);
        tvOtherSchool.setOnClickListener(this);
        tvGzu.setOnClickListener(this);
    }

    @Override
    public void showNotice(String notice) {
        toast(notice);
    }

    @Override
    public void showInputDialog() {
        mDialogHelper = new DialogHelper();
//        dialogHelper.
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_input_course_table_name, null);

        final EditText editText = view.findViewById(R.id.et_course_table_name);
        String schoolUrl = Preferences.getString(getString(R.string.app_preference_url_school), "");
        editText.setText(schoolUrl);
        mDialogHelper.showCustomDialog(this, view,
                getString(R.string.please_input_school_url), new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);

                        String schoolUrl = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(schoolUrl)) {
                            toast(getString(R.string.can_not_be_empty));
                            return;
                        }

                        if (!schoolUrl.startsWith("http")) {
                            schoolUrl = "http://" + schoolUrl;
                        }
                        Preferences.putString(getString(R.string.app_preference_url_school), schoolUrl);
                        mPresenter.testUrl(schoolUrl);
                    }

                    @Override
                    public void onNegative(DialogInterface dialog, int which) {
                        super.onNegative(dialog, which);
                        mDialogHelper.hideCustomDialog();
                        mDialogHelper = null;
                    }
                });
    }

    @Override
    public void testingUrl(boolean bool) {
        if (mTestingDialog == null) {
            mTestingDialog = new DialogHelper();
        }

        if (bool) {
            mTestingDialog.showProgressDialog(this,
                    "正在测试网站是否可以访问", "请稍等...", false);
        } else {
            mTestingDialog.hideProgressDialog();
        }
    }

    @Override
    public void testUrlFailed(String url) {
        showNotice("目前网络无法访问:" + url + "\n");
    }

    @Override
    public void testUrlSucceed(String url) {
        if (mDialogHelper != null) {
            mDialogHelper.hideCustomDialog();
        }

        Intent intent = new Intent(this, ImptActivity.class);
        intent.putExtra(Constant.INTENT_SCHOOL_URL, url);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_gzu:
                selectedGzu();
                break;
            case R.id.tv_other:
                selectedOther();
                break;
            default:
                break;
        }
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

    private void selectedOther() {
        showInputDialog();
    }

    private void selectedGzu() {
        mPresenter.testUrl(Url.URL_GZU_HOST);
    }

    @Override
    public void setPresenter(SchoolContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
