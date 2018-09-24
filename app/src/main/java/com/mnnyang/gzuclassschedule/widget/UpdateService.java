package com.mnnyang.gzuclassschedule.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.custom.course2.CourseAncestor;
import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.utils.Preferences;

import java.util.ArrayList;
import java.util.List;


public class UpdateService extends RemoteViewsService {

    private List<CourseAncestor> mCourses;
    private int maxNodeSize = 16;
    private int currentWeek = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        initDemoData();
    }

    private void initDemoData() {
        int currentCsNameId = Preferences.getInt(
                getString(R.string.app_preference_current_cs_name_id), 0);
        CourseDbDao dao = CourseDbDao.newInstance();
        final ArrayList<Course> courses = dao.loadCourses(currentCsNameId);

        int i = 0;
        mCourses = new ArrayList<>();
        for (Course course : courses) {
            course.init(course.getWeek(),
                    course.getNodes().get(0),
                    course.getNodes().size(),
                    Utils.getRandomColor(i++));

            course.setStartIndex(course.getStartWeek());
            course.setEndIndex(course.getEndWeek());
            course.setText(course.getName() + "\n@" + course.getClassRoom());
            course.setShowIndex(course.getWeekType());

            mCourses.add(course);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
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
            final RemoteViews bigRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_demo_item);
            bigRemoteViews.removeAllViews(R.id.item_node_group);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_1);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_2);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_3);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_4);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_5);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_6);
            bigRemoteViews.removeAllViews(R.id.item_weekday_day_7);

            Intent intent = new Intent();
            //TODO
            //intent.setComponent(new ComponentName("包名", "类名"));
            //与CustomWidget中remoteViews.setPendingIntentTemplate配对使用
            bigRemoteViews.setOnClickFillInIntent(R.id.widget_list_item_layout, intent);

            for (int i = 1; i <= maxNodeSize; i++) {
                RemoteViews nodeRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_box_0);
                nodeRemoteViews.setTextViewText(R.id.widget_box_0, i + "");
                bigRemoteViews.addView(R.id.item_node_group, nodeRemoteViews);
            }


            for (int row = 0; row <= 7; row++) {
                for (int col = 1; col <= maxNodeSize; col++) {
                    CourseAncestor course = getCourseByRowCol(row, col);

                    RemoteViews dayRemoteViews = null;
                    if (course == null) {
                        dayRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_box_1);
                        dayRemoteViews.setTextViewText(R.id.widget_box_1, "");
                    } else {
                        col = col + course.getRowNum() - 1;
                        switch (course.getRowNum()) {
                            case 1:
                                dayRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_box_1);
                                dayRemoteViews.setTextViewText(R.id.widget_box_1, course.getText());
                                dayRemoteViews.setInt(R.id.widget_box_1, "setBackgroundColor", course.getColor());
                                break;
                            case 2:
                                dayRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_box_2);
                                dayRemoteViews.setTextViewText(R.id.widget_box_2, course.getText());
                                dayRemoteViews.setInt(R.id.widget_box_2, "setBackgroundColor", course.getColor());
                                break;
                            case 3:
                                dayRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_box_3);
                                dayRemoteViews.setTextViewText(R.id.widget_box_3, course.getText());
                                dayRemoteViews.setInt(R.id.widget_box_3, "setBackgroundColor", course.getColor());
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
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
