package com.mnnyang.gzuclassschedule.data.db;

import android.provider.BaseColumns;

/**
 * Created by mnnyang on 17-10-23.
 */

public class CoursesPsc {

    private CoursesPsc() {
    }

    public static abstract class NodeEntry implements BaseColumns {
        public static final String TABLE_NAME = "node";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_NODE_NUM = "node_num";
    }

    public static abstract class CsNameEntry implements BaseColumns {
        public static final String TABLE_NAME = "cs_name";
        public static final String COLUMN_NAME_NAME_ID = "name_id";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_CS_NAME_ID = "cs_name_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CLASS_ROOM = "class_room";

        public static final String COLUMN_NAME_WEEK = "week";
        public static final String COLUMN_NAME_START_WEEK = "start_week";
        public static final String COLUMN_NAME_END_WEEK = "end_week";

        public static final String COLUMN_NAME_WEEK_TYPE = "week_type";
        public static final String COLUMN_NAME_TEACHER = "teacher";
        public static final String COLUMN_NAME_SOURCE = "source";

    }
}
