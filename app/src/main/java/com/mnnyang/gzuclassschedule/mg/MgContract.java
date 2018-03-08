package com.mnnyang.gzuclassschedule.mg;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-11-4.
 */

public interface MgContract {
    interface Presenter extends BasePresenter {
        void deleteCsName(int csNameId, DialogHelper dh);
        void switchCsName(int csNameId);
        void reloadCsNameList();
        void addCsName(String csName);
    }

    interface View extends BaseView<Presenter> {
        void showList(ArrayList<CsItem> csNames);
        void showNotice(String notice);
        void gotoCourseActivity();
        void deleteFinish();
        void addCsNameSucceed();
    }

    interface Model{
        ArrayList<CsItem> getCsItemData();
    }
}
