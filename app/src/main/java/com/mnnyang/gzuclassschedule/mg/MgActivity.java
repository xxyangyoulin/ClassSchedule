package com.mnnyang.gzuclassschedule.mg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.course.CourseActivity;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.mg.adapter.MgAdapter;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.spec.RecyclerBaseAdapter;

import java.util.ArrayList;

public class MgActivity extends BaseActivity implements MgContract.View, View.OnClickListener {

    private MgPresenter mPresenter;
    private MgAdapter mAdapter;
    ArrayList<CsItem> csItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initBackToolbar(getString(R.string.kb_manage));
        initRecyclerView();
        initFab();
        mPresenter = new MgPresenter(this, csItems);
        mPresenter.start();
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mAdapter = new MgAdapter(R.layout.layout_item_cs, csItems);

        mAdapter.setFooter(LayoutInflater.from(this).inflate(
                R.layout.layout_no_more, recyclerView, false));

        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                getBaseContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setItemClickListener(new RecyclerBaseAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                switchDialog((Integer) view.getTag());
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                deleteDialog((Integer) view.getTag());
            }
        });
    }

    private void deleteDialog(final int id) {
        DialogHelper dh = new DialogHelper();
        dh.showNormalDialog(this, getString(R.string.warning),
                "确认要删除该课表吗?",
                new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        deletingDialog(id);
                    }
                });
    }

    private void deletingDialog(int id) {
        DialogHelper dh = new DialogHelper();
        dh.showProgressDialog(this, getString(R.string.deleting),
                getString(R.string.please_wait_a_moment), false);

        mPresenter.deleteCsName(id, dh);
    }

    private void switchDialog(final int tag) {
        DialogHelper dh = new DialogHelper();
        dh.showNormalDialog(this, getString(R.string.warning),
                "确认要切换到该课表吗?",
                new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        mPresenter.switchCsName(tag);
                        notifiUpdateMainPage(Constant.INTENT_UPDATE_TYPE_COURSE);
                    }
                });
    }

    @Override
    public void showList(ArrayList<CsItem> csNames) {
        String curName = Preferences.getString(getString(R.string.app_preference_current_sd_name), getString(R.string.default_course_name));
        mAdapter.setCurrentName(curName);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void gotoCourseActivity() {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteFinish() {
        mPresenter.loadCsNameList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                add();
                break;
            default:
                break;
        }
    }

    private void add() {
        DialogHelper helper = new DialogHelper();
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_input_course_table_name, null);
        final EditText editText = view.findViewById(R.id.et_course_table_name);

        helper.showCustomDialog(this, view,
                getString(R.string.please_input_course_table_name), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                String courseTableName = editText.getText().toString().trim();
                if (TextUtils.isEmpty(courseTableName)){
                    toast(getString(R.string.course_name_can_not_be_empty));
                }else{
                    //TODO 检查

                }
            }
        });
    }
}
