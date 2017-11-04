package com.mnnyang.gzuclassschedule.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

public class AddActivity extends BaseActivity implements AddContract.View {

    private AddContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initBackToolbar("添加");
        mPresenter = new AddPresenter(this);
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

    /**
     * 通知更新
     */
    private void notifiUpdate() {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.update");
        sendBroadcast(intent);
    }
}
