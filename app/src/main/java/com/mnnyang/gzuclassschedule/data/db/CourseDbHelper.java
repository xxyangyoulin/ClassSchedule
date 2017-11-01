package com.mnnyang.gzuclassschedule.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mnnyang on 17-10-23.
 */

public class CourseDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "course.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_COURSES =
            "CREATE TABLE " + CoursesPsc.CourseEntry.TABLE_NAME + " (" +
                    CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_COURSE_TIME + TEXT_TYPE + COMMA_SEP +

                    CoursesPsc.CourseEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_CLASS_ROOM + TEXT_TYPE + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_TEACHER + TEXT_TYPE + COMMA_SEP +

                    CoursesPsc.CourseEntry.COLUMN_NAME_WEEK + INTEGER_TYPE + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_START_WEEK + INTEGER_TYPE + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_END_WEEK + INTEGER_TYPE + COMMA_SEP +
                    CoursesPsc.CourseEntry.COLUMN_NAME_WEEK_TYPE + INTEGER_TYPE + COMMA_SEP +

                    CoursesPsc.CourseEntry.COLUMN_NAME_SOURCE + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_NODE =
            "CREATE TABLE " + CoursesPsc.NodeEntry.TABLE_NAME + " (" +
                    CoursesPsc.NodeEntry.COLUMN_NAME_COURSE_ID + INTEGER_TYPE + COMMA_SEP +
                    CoursesPsc.NodeEntry.COLUMN_NAME_NODE_NUM + INTEGER_TYPE +
                    " )";

    public CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NODE);
        db.execSQL(SQL_CREATE_COURSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
