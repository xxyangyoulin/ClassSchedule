package com.mnnyang.gzuclassschedule.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

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

    public void addCourse(Course course) {
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
    }

    public void updateCourse(Course course) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        putAllNotId(course, values);
        db.update(
                CoursesPsc.CourseEntry.TABLE_NAME,
                values,
                CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + "=?",
                new String[]{course.getId() + ""});

        db.close();
    }

    public void removeCourse(int courseId) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        db.delete(
                CoursesPsc.CourseEntry.TABLE_NAME,
                CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + "=?",
                new String[]{courseId + ""});

        db.close();
    }

    public ArrayList<Course> getCourses(String courseTime) {
        SQLiteDatabase db = new CourseDbHelper(app.mContext).getWritableDatabase();
        String sql = "select * from " + CoursesPsc.CourseEntry.TABLE_NAME;/*+" where "
                + CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_TIME + "='"+courseTime+"'";*/

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Course> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = parse(cursor);
            courses.add(course);

            sql = "select * from " + CoursesPsc.NodeEntry.TABLE_NAME
                    + " where " + CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID + "=" + course.getId();
            Cursor nodeCursor = db.rawQuery(sql, null);

            while (nodeCursor.moveToNext()) {
                course.addNode(nodeCursor.getInt(nodeCursor.getColumnIndex(CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM)));
                System.out.println("找到ndoe"+course.getNodes());
            }

            nodeCursor.close();
        }
        cursor.close();


        db.close();
        return courses;
    }

    private void putAllNotId(Course course, ContentValues values) {
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_NAME, course.getName());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_CLASS_ROOM, course.getClassRoom());
        values.put(CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_TIME, course.getCourseTime());
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
                .setCourseTime(cursor.getString(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_TIME)))
                .setWeekType(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_WEEK_TYPE)))
                .setId(cursor.getInt(cursor.getColumnIndex(CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID)));
        return course;
    }
}
