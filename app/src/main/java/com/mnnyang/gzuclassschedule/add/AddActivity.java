package com.mnnyang.gzuclassschedule.add;

import android.content.Intent;
import android.os.Bundle;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

public class AddActivity extends BaseActivity implements AddContract.View {

    private AddContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mPresenter = new AddPresenter(this);
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
