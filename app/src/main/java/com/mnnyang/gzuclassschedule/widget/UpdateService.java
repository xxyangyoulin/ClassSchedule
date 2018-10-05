package com.mnnyang.gzuclassschedule.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;
import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.mvp.course.CourseActivity;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

//import com.mnnyang.gzuclassschedule.data.bean.Course;
//import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;


public class UpdateService extends RemoteViewsService {

    private List<CourseAncestor> mCourses;
    private int maxNodeSize = 16;
    private int mCurrentWeek = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initDemoData() {
        //TODO
        long group_id = Preferences.getLong(
                getString(R.string.app_preference_current_cs_name_id), 0);
        //CourseDbDao dao = CourseDbDao.instance();
        //final ArrayList<Course> courses = dao.loadCourses(group_id);
        mCurrentWeek = AppUtils.getCurrentWeek(getBaseContext());

        List<CourseV2> courseV2s = Cache.instance().getCourseV2Dao()
                .queryBuilder()
                .where(CourseV2Dao.Properties.CouCgId.eq(group_id))
                .list();

        mCourses = new ArrayList<>();
        for (CourseV2 course : courseV2s) {
            course.init();
            if (course.getColor() == -1) {
                course.setColor(Utils.getRandomColor());
            }
            course.setActiveStatus(course.shouldShow(mCurrentWeek));
            mCourses.add(course);
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsFactory {

        private final Context mContext;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            initDemoData();

            LogUtil.e(this, "getViewAt");
            final RemoteViews bigRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_demo_item);
            bigRemoteViews.removeAllViews(R.id.item_node_group);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_1);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_2);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_3);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_4);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_5);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_6);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_7);

            Intent intent = new Intent(mContext, CourseActivity.class);
            //TODO
            //intent.setComponent(new ComponentName("包名", "类名"));
            //与CustomWidget中remoteViews.setPendingIntentTemplate配对使用

            bigRemoteViews.setOnClickFillInIntent(R.id.item_weekday_layout, intent);

            for (int i = 1; i <= maxNodeSize; i++) {
                RemoteViews nodeRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_node);
                nodeRemoteViews.setTextViewText(R.id.widget_box_0, i + "");
                bigRemoteViews.addView(R.id.item_node_group, nodeRemoteViews);
            }


            for (int row = 0; row <= 7; row++) {
                for (int col = 1; col <= maxNodeSize; col++) {
                    CourseAncestor course = getCourseByRowCol(row, col);

                    RemoteViews dayRemoteViews = null;
                    if (course == null) {
                        dayRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_cell_1);
                        dayRemoteViews.setTextViewText(R.id.widget_cell_1, "");
                    } else {
                        col = col + course.getRowNum() - 1;

                        int layout = -1;
                        int id = -1;

                        switch (course.getRowNum()) {
                            case 1:
                                layout = R.layout.widget_cell_1;
                                id = R.id.widget_cell_1;
                                break;
                            case 2:
                                layout = R.layout.widget_cell_2;
                                id = R.id.widget_cell_2;
                                break;
                            case 3:
                                layout = R.layout.widget_cell_3;
                                id = R.id.widget_cell_3;
                                break;
                            case 4:
                                layout = R.layout.widget_cell_4;
                                id = R.id.widget_cell_4;
                                break;
                            case 5:
                                layout = R.layout.widget_cell_5;
                                id = R.id.widget_cell_5;
                                break;
                            case 6:
                                layout = R.layout.widget_cell_6;
                                id = R.id.widget_cell_6;
                                break;
                            case 7:
                                layout = R.layout.widget_cell_7;
                                id = R.id.widget_cell_7;
                                break;
                            case 8:
                                layout = R.layout.widget_cell_8;
                                id = R.id.widget_cell_8;
                                break;
                            case 9:
                                layout = R.layout.widget_cell_9;
                                id = R.id.widget_cell_9;
                                break;
                            case 10:
                                layout = R.layout.widget_cell_10;
                                id = R.id.widget_cell_10;
                                break;
                            case 11:
                                layout = R.layout.widget_cell_11;
                                id = R.id.widget_cell_11;
                                break;
                            case 12:
                                layout = R.layout.widget_cell_12;
                                id = R.id.widget_cell_12;
                                break;
                            case 13:
                                layout = R.layout.widget_cell_13;
                                id = R.id.widget_cell_13;
                                break;
                            case 14:
                                layout = R.layout.widget_cell_14;
                                id = R.id.widget_cell_14;
                                break;
                            case 15:
                                layout = R.layout.widget_cell_15;
                                id = R.id.widget_cell_15;
                                break;
                            case 16:
                                layout = R.layout.widget_cell_16;
                                id = R.id.widget_cell_16;
                                break;
                            default:
                                break;
                        }

                        if (id != -1) {
                            dayRemoteViews = new RemoteViews(getPackageName(), layout);
                            dayRemoteViews.setTextViewText(id, course.getText());

                            if(course.getActiveStatus()){
                                dayRemoteViews.setInt(id, "setBackgroundColor", course.getColor());
                                dayRemoteViews.setInt(id, "setTextColor", 0xFFFFFFFF);
                            }else{
                                dayRemoteViews.setInt(id, "setBackgroundColor", 0xFFE3EEF5);
                                dayRemoteViews.setInt(id, "setTextColor", 0xFFbadac9);
                            }
                        }
                    }

                    switch (row) {
                        case 1:
                            bigRemoteViews.addView(R.id.item_weekday_day_1, dayRemoteViews);
                            break;
                        case 2:
                            bigRemoteViews.addView(R.id.item_weekday_day_2, dayRemoteViews);
                            break;
                        case 3:
                            bigRemoteViews.addView(R.id.item_weekday_day_3, dayRemoteViews);
                            break;
                        case 4:
                            bigRemoteViews.addView(R.id.item_weekday_day_4, dayRemoteViews);
                            break;
                        case 5:
                            bigRemoteViews.addView(R.id.item_weekday_day_5, dayRemoteViews);
                            break;
                        case 6:
                            bigRemoteViews.addView(R.id.item_weekday_day_6, dayRemoteViews);
                            break;
                        case 7:
                            bigRemoteViews.addView(R.id.item_weekday_day_7, dayRemoteViews);
                            break;
                    }

                }
            }
            return bigRemoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private CourseAncestor getCourseByRowCol(int row, int col) {
        CourseAncestor result = null;
        for (CourseAncestor course : mCourses) {
            if (course.getRow() == row && course.getCol() == col) {
                if (course.getActiveStatus()) {
                    return course;
                } else {
                    result = course;
                }
            }
        }
        return result;
    }
}
