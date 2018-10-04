package com.mnnyang.gzuclassschedule.widget;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.mnnyang.gzuclassschedule.utils.LogUtil;

public class UpdateJobService extends JobService {

    @Override
    public void onCreate() {
        LogUtil.e(this, "onCreate");
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @Override
    public void onDestroy() {
        LogUtil.e(this, "onDestroy");

        super.onDestroy();
    }
}
