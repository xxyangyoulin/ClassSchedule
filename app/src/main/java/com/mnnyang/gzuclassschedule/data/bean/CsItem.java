package com.mnnyang.gzuclassschedule.data.bean;

/**
 * Created by mnnyang on 17-11-4.
 */

public class CsItem {
    private CsName mCsName;
    private int count;

    public CsName getCsName() {
        return mCsName;
    }

    public CsItem setCsName(CsName csName) {
        mCsName = csName;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CsItem setCount(int count) {
        this.count = count;
        return this;
    }
}
