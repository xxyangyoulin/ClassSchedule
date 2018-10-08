package com.mnnyang.gzuclassschedule.app;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.data.greendao.DaoMaster;
import com.mnnyang.gzuclassschedule.data.greendao.DaoSession;
import com.mnnyang.gzuclassschedule.data.greendao.MyOpenHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.TimeUtils;
import com.mnnyang.gzuclassschedule.widget.UpdateJobService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

/**
 *
 */
public class AppUtils {

    /**
     * 生成UUID
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前周数
     */
    public static int getCurrentWeek(Context context) {
        int week = 1;

        //获取开始时间
        String beginMillis = Preferences.getString(context.getString(
                R.string.app_preference_start_week_begin_millis), "");

        //获取当前时间
        long currentMillis = Calendar.getInstance().getTimeInMillis();

        //存在开始时间
        if (!TextUtils.isEmpty(beginMillis)) {
            long intBeginMillis = Long.valueOf(beginMillis);

            //获取到的配置是时间大于当前时间 重置为第一周
            if (intBeginMillis > currentMillis) {
                LogUtil.e("getCurrentWeek", "intBeginMillis > currentMillis");
                PreferencesCurrentWeek(context, 1);

            } else {
                //计算出开始时间到现在时间的周数
                int weekGap = TimeUtils.getWeekGap(intBeginMillis, currentMillis);

                week += weekGap;
            }

        } else {
            //不存在开始时间 初始化为第一周
            PreferencesCurrentWeek(context, 1);
        }

        return week;
    }

    /**
     * 更改当前周
     */
    public static void PreferencesCurrentWeek(Context context, int currentWeekCount) {
        //得到一个当前周 周一的日期
        Calendar calendar = Calendar.getInstance();
        Date weekBegin = TimeUtils.getNowWeekBegin();
        calendar.setTime(weekBegin);

        if (currentWeekCount > 1) {
            calendar.add(Calendar.DATE, -7 * (currentWeekCount - 1));
        }

        LogUtil.e("PreferencesCurrentWeek", "preferences date" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        Preferences.putString(context.getString(R.string.app_preference_start_week_begin_millis),
                calendar.getTimeInMillis() + "");
    }

    /**
     * 根据邮箱获取gravator头像
     */
    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=128";
        return avatar;
    }

    /**
     * 更新widget组件
     */
    public static void updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.action.UPDATE_WIDGET");
        intent.setComponent(new ComponentName("com.mnnyang.gzuclassschedule", "com.mnnyang.gzuclassschedule.widget.MyWidget"));
        context.sendBroadcast(intent);
    }

    public static int UPDATE_WIDGET_JOB_ID = 1;

    /**
     * 启动桌面小部件更新服务
     */
    public static void startWidgetJobService(Context context) {
        if (!isJobPollServiceOn(context)) {
            LogUtil.i(AppUtils.class, "安排widget更新任务");
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(UPDATE_WIDGET_JOB_ID,
                    new ComponentName(context, UpdateJobService.class));
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
            builder.setRequiresCharging(true);
            builder.setPeriodic(60 * 1000); //一小时更新一次
            jobScheduler.schedule(builder.build());
        } else {
            LogUtil.i(AppUtils.class, "widget更新任务已经安排");
        }
    }


    private static boolean isJobPollServiceOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);

        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == UPDATE_WIDGET_JOB_ID) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取消widget更新任务
     */
    public static void cancelUpdateWidgetService(Context context) {
        LogUtil.e(AppUtils.class, "cancelUpdateWidgetService");
        JobScheduler scheduler = (JobScheduler) context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(UPDATE_WIDGET_JOB_ID);
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 粗略判断邮箱
     */
    public static boolean isEmail(String content) {
        String pattern = "[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z0-9.]+";

        return Pattern.matches(pattern, content);
    }

    /**
     * 复制旧数据的数据
     */
    public static void copyOldData(Context context) {
            migrateData(context);
    }

    /**
     * 迁移旧数据
     */
    private static void migrateData(Context context) {
        MyOpenHelper myOpenHelper = new MyOpenHelper(
                context, "coursev2.db", null);

        DaoMaster daoMaster = new DaoMaster(myOpenHelper.getWritableDatabase());

        DaoSession daoSession = daoMaster.newSession();

        CourseGroupDao courseGroupDao =
                daoSession.getCourseGroupDao();
        CourseV2Dao courseV2Dao = daoSession.getCourseV2Dao();

        ArrayList<CsItem> csItems = CourseDbDao.instance().loadCsNameList();

        for (CsItem csItem : csItems) {
            ArrayList<Course> courses = CourseDbDao.instance().loadCourses(csItem.getCsName().getCsNameId());
            CourseGroup group = new CourseGroup();
            group.setCgName(csItem.getCsName().getName());
            long insert1 = courseGroupDao.insert(group);

            for (Course course : courses) {

                if (course.getNodes() == null || course.getNodes().size() == 0 || course.getEndWeek() == 0) {
                    continue;
                }

                CourseV2 courseV2 = new CourseV2().setCouOnlyId(AppUtils.createUUID());

                courseV2.setCouName(course.getName());
                courseV2.setCouTeacher(course.getTeacher());
                courseV2.setCouLocation(course.getClassRoom());

                //node
                courseV2.setCouStartNode(course.getNodes().get(0));
                courseV2.setCouNodeCount(course.getNodes().size());

                //day
                courseV2.setCouWeek(course.getWeek());

                //week
                String couAllWeek = getAllWeek(course);
                if (couAllWeek.length() > 0) {
                    couAllWeek = couAllWeek.substring(0, couAllWeek.length() - 1);
                }

                courseV2.setCouAllWeek(couAllWeek);
                courseV2.setCouCgId(insert1);
                courseV2Dao.insert(courseV2);
            }
        }
    }

    @NonNull
    private static String getAllWeek(Course course) {
        int startWeek = course.getStartWeek();
        int endWeek = course.getEndWeek();
        int weekType = course.getWeekType();
        int step = 1;

        if (weekType != 0) {
            step = 2;
            if (weekType == Course.SHOW_DOUBLE && startWeek % 2 == 1) {
                startWeek += 1;
            } else if (weekType == Course.SHOW_SINGLE && startWeek % 2 == 0) {
                startWeek += 1;
            }
        }

        StringBuilder allWeek = new StringBuilder();
        for (int i = startWeek; i <= endWeek; i += step) {
            allWeek.append(i);
            allWeek.append(",");
        }

        return allWeek.toString();
    }
}
