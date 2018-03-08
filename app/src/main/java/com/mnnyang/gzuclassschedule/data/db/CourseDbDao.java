package com.mnnyang.gzuclassschedule.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.bean.CsName;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-11-1.
 */

public class CourseDbDao {

    private CourseDbDao() {
    }

    private static final class Holder {
        private static final CourseDbDao DAO = new CourseDbDao();
    }

    public static CourseDbDao newInstance() {
        return Holder.DAO;
    }

    /**
     * 添加课程<br>
     * 应检查课程信息的准确性后再调用该方法 <br>
     *
     * @return success return null or return conflict object
     */
    public Course addCourse(Course course) {
        Course conflictCourse = hasConflictCourse(course);
        if (null != conflictCourse) {
            return conflictCourse;
        }

        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        ContentValues values = new ContentValues();

        putAllNotId(course, values);

        db.beginTransaction();
        try {
            long courseId = db.insert(CoursesPsc.CourseEntry.TABLE_NAME, null, values);

            for (Integer integer : course.getNodes()) {
                values.clear();
                values.put(CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID, courseId);
                values.put(CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM, integer);
                db.insert(CoursesPsc.NodeEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        db.close();
        return null;
    }

    /**
     * 更新
     */
    public Course updateCourse(Course course) {

        Course conflictCourse = hasConflictCourse(course);
        if (null != conflictCourse) {
            return conflictCourse;
        }

        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        db.beginTransaction();

        try {

            course.setCsNameId(getCsNameId(course.getCsName(), db));

            ContentValues values = new ContentValues();
            putAllNotId(course, values);
            db.update(CoursesPsc.CourseEntry.TABLE_NAME,
                    values,
                    CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + "=?",
                    new String[]{course.getCourseId() + ""});

            deleteNodeByCourseId(course.getCourseId(), db);
            for (Integer integer : course.getNodes()) {
                values.clear();
                values.put(CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID, course.getCourseId());
                values.put(CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM, integer);
                db.insert(CoursesPsc.NodeEntry.TABLE_NAME, null, values);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return null;
    }

    public boolean removeByCsNameId(int id) {

        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(CoursesPsc.CourseEntry.TABLE_NAME,
                    CoursesPsc.CourseEntry.COLUMN_NAME_CS_NAME_ID + "=?"
                    , new String[]{String.valueOf(id)});

            db.delete(CoursesPsc.CsNameEntry.TABLE_NAME,
                    CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID + "=?",
                    new String[]{String.valueOf(id)});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public void removeCourse(int courseId) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        removeCourse(courseId, db);
        db.close();
    }

    private void removeCourse(int courseId, SQLiteDatabase db) {
        db.delete(CoursesPsc.CourseEntry.TABLE_NAME,
                CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + "=?",
                new String[]{courseId + ""});
        deleteNodeByCourseId(courseId, db);
    }

    public boolean removeAllData() {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(CoursesPsc.NodeEntry.TABLE_NAME, null, null);
            db.delete(CoursesPsc.CsNameEntry.TABLE_NAME, null, null);
            db.delete(CoursesPsc.CourseEntry.TABLE_NAME, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void deleteNodeByCourseId(int courseId, SQLiteDatabase db) {
        db.delete(CoursesPsc.NodeEntry.TABLE_NAME,
                CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID + "=?",
                new String[]{courseId + ""});
    }

    /**
     * 课程冲突判断
     */
    private Course hasConflictCourse(Course course) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();

        int csNameId = getCsNameId(course.getCsName(), db);
        course.setCsNameId(csNameId);

        String sql = "select * from " + CoursesPsc.CourseEntry.TABLE_NAME
                + " where " + CoursesPsc.CourseEntry.COLUMN_NAME_WEEK + "='" + course.getWeek() + "'"
                + " AND " + CoursesPsc.CourseEntry.COLUMN_NAME_CS_NAME_ID + "='" + csNameId + "'"
                + " AND " + CoursesPsc.CourseEntry.COLUMN_NAME_WEEK_TYPE + "='" + course.getWeekType() + "'"
                + " AND " + CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + "!='" + course.getCourseId() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Course conflictCourse = parse(cursor);
            sql = "select * from " + CoursesPsc.NodeEntry.TABLE_NAME
                    + " where " + CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID + "=" + conflictCourse.getCourseId();
            Cursor nodeCursor = db.rawQuery(sql, null);

            while (nodeCursor.moveToNext()) {
                conflictCourse.addNode(nodeCursor.getInt(nodeCursor.getColumnIndex(CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM)));
            }

            nodeCursor.close();

            if (course.equals(conflictCourse)) {
                LogUtil.e(this, course.getName() + " 和 " + conflictCourse.toString() + "冲突!!");
                cursor.close();
                db.close();
                return conflictCourse;
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    /**
     * 课程表名称冲突
     *
     * @param csName
     * @return
     */
    public boolean hasConflictCourseTableName(String csName) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        String sql = "select * from " + CoursesPsc.CsNameEntry.TABLE_NAME
                + " where `" + CoursesPsc.CsNameEntry.COLUMN_NAME_NAME + "`='" + csName + "'";
        System.out.println(sql);
        Cursor cursor = db.rawQuery(sql, null);

        return cursor.moveToNext();
    }

    /**
     * 根据课程表名获取课程表名id 不存在则插入
     */
    public int getCsNameId(String csName) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        int id = getCsNameId(csName, db);

        db.close();
        return id;
    }

    public int getCsNameId(String csName, SQLiteDatabase db) {
        System.out.println("被调用");

        String sql = "select * from " + CoursesPsc.CsNameEntry.TABLE_NAME
                + " where `" + CoursesPsc.CsNameEntry.COLUMN_NAME_NAME + "`='" + csName + "'";
        System.out.println(sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID));
            cursor.close();
            return id;
        } else {
            ContentValues values = new ContentValues();
            values.put(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME, csName);
            return (int) db.insert(CoursesPsc.CsNameEntry.TABLE_NAME, null, values);
        }
    }

    /**
     *
     * @param csNameId
     * @param newCsName
     * @return conflict return 0
     */
    public int updateCsName(int csNameId, String newCsName) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();

        String sql = "select * from "+CoursesPsc.CsNameEntry.TABLE_NAME
                +" where `"+ CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID+"`!="+csNameId
                +" and `"+ CoursesPsc.CsNameEntry.COLUMN_NAME_NAME+"`='"+newCsName+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return 0;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME, newCsName);
        int update = db.update(CoursesPsc.CsNameEntry.TABLE_NAME, values,
                CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID + "=?",
                new String[]{String.valueOf(csNameId)});

        db.close();
        return update;
    }

    public String getCsName(int csNameId) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        String sql = "select * from " + CoursesPsc.CsNameEntry.TABLE_NAME
                + " where `" + CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID + "`='" + csNameId + "'";
        System.out.println(sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME));

            cursor.close();
            db.close();
            return name;
        }
        return "";
    }



    /**
     * 加载课程数据
     */
    public ArrayList<Course> loadCourses(String csName) {

        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();

        int csNameId = getCsNameId(csName, db);

        String sql = "select * from " + CoursesPsc.CourseEntry.TABLE_NAME + " where "
                + CoursesPsc.CourseEntry.COLUMN_NAME_CS_NAME_ID + "='" + csNameId + "'";

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Course> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = parse(cursor);
            course.setCsName(csName);
            courses.add(course);

            sql = "select * from " + CoursesPsc.NodeEntry.TABLE_NAME
                    + " where " + CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID + "=" + course.getCourseId();
            Cursor nodeCursor = db.rawQuery(sql, null);

            while (nodeCursor.moveToNext()) {
                course.addNode(nodeCursor.getInt(nodeCursor.getColumnIndex(CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM)));
            }

            nodeCursor.close();
        }
        cursor.close();

        db.close();
        return courses;
    }

    public ArrayList<CsItem> loadCsNameList() {
        ArrayList<CsItem> csItems = new ArrayList<>();

        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        Cursor cursor = db.query(CoursesPsc.CsNameEntry.TABLE_NAME, null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            int nameId = cursor.getInt(cursor.getColumnIndex(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME_ID));
            String name = cursor.getString(cursor.getColumnIndex(CoursesPsc.CsNameEntry.COLUMN_NAME_NAME));

            //TODO 额外数据 例如数据的条数

            CsItem csItem = new CsItem();
            csItem.setCsName(new CsName().setName(name).setCsNameId(nameId));
            csItems.add(csItem);
        }
        cursor.close();
        db.close();

        return csItems;
    }


    private void putAllNotId(Course course, ContentValues values) {
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_NAME, course.getName());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_CLASS_ROOM, course.getClassRoom());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_CS_NAME_ID, course.getCsNameId());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_WEEK, course.getWeek());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_START_WEEK, course.getStartWeek());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_END_WEEK, course.getEndWeek());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_TEACHER, course.getTeacher());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_SOURCE, course.getSource());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_WEEK_TYPE, course.getWeekType());
    }


    private Course parse(Cursor cursor) {
        Course course = new Course();
        course.setName(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_NAME)))
                .setClassRoom(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_CLASS_ROOM)))
                .setTeacher(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_TEACHER)))
                .setWeek(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_WEEK)))
                .setStartWeek(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_START_WEEK)))
                .setEndWeek(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_END_WEEK)))
                .setSource(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_SOURCE)))
                .setCsName(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_CS_NAME_ID)))
                .setWeekType(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_WEEK_TYPE)))
                .setCourseId(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID)));
        return course;
    }
}
