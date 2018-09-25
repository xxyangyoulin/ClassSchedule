package com.mnnyang.gzuclassschedule.mg;

import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-11-4.
 */

public class MgModel implements MgContract.Model {
    @Override
    public ArrayList<CsItem> getCsItemData() {
        return CourseDbDao.newInstance().loadCsNameList();
    }
}
