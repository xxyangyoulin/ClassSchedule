package com.mnnyang.gzuclassschedule.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseV2;
import com.mnnyang.gzuclassschedule.data.db.CourseDbDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.data.greendao.DaoMaster;
import com.mnnyang.gzuclassschedule.data.greendao.DaoSession;
import com.mnnyang.gzuclassschedule.utils.Preferences;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 */
public class AppUtils {
    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=64";
        System.out.println(avatar);
        return avatar;
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.action.UPDATE_WIDGET");
        intent.setComponent(new ComponentName("com.mnnyang.gzuclassschedule", "com.mnnyang.gzuclassschedule.widget.MyWidget"));
        context.sendBroadcast(intent);
    }

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
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
        boolean first_enter_app = Preferences.getBoolean("first_enter_app", true);
        if (first_enter_app) {
            startCopy(context);
            Preferences.putBoolean("first_enter_app", false);
        }

    }

    private static void startCopy(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                context, "coursev2.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());

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

                CourseV2 courseV2 = new CourseV2();

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

                System.out.println("----------------" + couAllWeek);
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
