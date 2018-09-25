package com.mnnyang.gzuclassschedule.utils.spec;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.custom.WheelView;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 显示详细信息
 * Created by mnnyang on 17-11-7.
 */

public class PopupWindowDialog {
    private int mWeek;
    private int mNodeStart;
    private int mNodeEnd;

    private int mEndWeek;
    private int mStartWeek;
    private int mWeekType;

    public interface WeekRangeCallback {
        void onSelected(int start, int end, int type);
    }

    public interface SelectTimeCallback {
        void onSelected(int week, int nodeStart, int endStart);
    }

    public void showSelectTimeDialog(Activity activity, final int week, int nodeStart,
                                     int nodeEnd, final SelectTimeCallback callback) {
        mWeek = week;
        mNodeStart = nodeStart;
        mNodeEnd = nodeEnd;

        DialogHelper helper = new DialogHelper();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_week_and_node, null);
        final ArrayList<String> weeks = new ArrayList<>();
        final ArrayList<String> nodes = new ArrayList<>();

        WheelView wvWeek = (WheelView) view.findViewById(R.id.wv_week);
        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_start_node);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_end_node);

        final int noonNode = Preferences.getInt(activity.getString(R.string.app_preference_noon_node),
                Integer.parseInt(activity.getString(R.string.default_noon_node)));

        for (int i = 1; i <= 7; i++) {
            weeks.add(Constant.WEEK[i]);
        }

        int maxNode = Preferences.getInt(
                activity.getString(R.string.app_preference_max_node), Constant.DEFAULT_MAX_NODE_COUNT);
        for (int i = 1; i <= maxNode; i++) {
            nodes.add("第" + i + "节");
        }

        wvWeek.setItems(weeks);
        wvStart.setItems(nodes);
        wvEnd.setItems(nodes);

        wvWeek.setSeletion(mWeek - 1);
        wvStart.setSeletion(mNodeStart - 1);
        wvEnd.setSeletion(mNodeEnd - 1);
        wvWeek.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mStartWeek = selectedIndex;
            }
        });

        wvWeek.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mWeek = selectedIndex;
            }
        });

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeStart = selectedIndex;

                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                    return;
                }

                if (mNodeEnd > noonNode && mNodeStart <= noonNode) {
                    wvEnd.setSeletion(noonNode - 1);
                }
            }
        });

        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeEnd = selectedIndex;

               /* if (mNodeEnd > noonNode && mNodeStart <= noonNode) {
                    ToastUtils.show("早上课程的最大为 " + noonNode + " 节");
                    wvEnd.setSeletion(noonNode - 1);
                    return;
                }*/

                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                }
            }
        });

        helper.showCustomDialog(activity, view,
                activity.getString(R.string.select_course_time), new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        dialog.dismiss();
                        callback.onSelected(mWeek, mNodeStart, mNodeEnd);
                    }
                });
    }

    public void showWeekRangeDialog(
            Activity activity, int defStart, final int defEnd,
            int defType, final WeekRangeCallback callback) {

        mStartWeek = defStart;
        mEndWeek = defEnd;
        mWeekType = defType;

        DialogHelper helper = new DialogHelper();
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_select_week_range, null);

        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_week_start);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_week_end);
        RadioGroup rgWeekType = (RadioGroup) view.findViewById(R.id.rg_week_type);

        ArrayList<String> weeks = new ArrayList<String>();
        for (int i = 1; i <= 25; i++) {
            weeks.add("第" + i + "周");
        }

        wvStart.setItems(weeks);
        wvEnd.setItems(weeks);
        wvStart.setSeletion(defStart - 1);
        wvEnd.setSeletion(defEnd - 1);

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                LogUtil.e(this, selectedIndex + "");
                mStartWeek = selectedIndex;
                if (mStartWeek > mEndWeek) {
                    wvEnd.setSeletion(mStartWeek - 1);
                }
            }
        });
        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mEndWeek = selectedIndex;
                if (mStartWeek > mEndWeek) {
                    wvEnd.setSeletion(mStartWeek - 1);
                }
            }
        });

        rgWeekType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.arb_all:
                        mWeekType = Course.SHOW_ALL;
                        break;

                    case R.id.arb_single:
                        mWeekType = Course.SHOW_SINGLE;
                        break;

                    case R.id.arb_double:
                        mWeekType = Course.SHOW_DOUBLE;
                        break;
                }
            }
        });

        helper.showCustomDialog(activity, view, app.mContext.
                getString(R.string.select_week_count), new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                dialog.dismiss();
                callback.onSelected(mStartWeek, mEndWeek, mWeekType);
            }
        });
    }
}
