package com.mnnyang.gzuclassschedule.data.beanv2;

import java.util.List;

public class DownCourseWrapper extends BaseBean {


    private List<DownCourse> data;

    public List<DownCourse> getData() {
        return data;
    }

    public void setData(List<DownCourse> data) {
        this.data = data;
    }

    public static class DownCourse {
        /**
         * week : 2
         * all_week : 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
         * name : tf FB b
         * color : -1
         * group_name : 默认
         * start_node : 4
         * location :
         * node_count : 1
         * teacher :
         */

        private int week;
        private String all_week;
        private String name;
        private int color;
        private String group_name;
        private int start_node;
        private String location;
        private int node_count;
        private String teacher;

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public String getAll_week() {
            return all_week;
        }

        public void setAll_week(String all_week) {
            this.all_week = all_week;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public int getStart_node() {
            return start_node;
        }

        public void setStart_node(int start_node) {
            this.start_node = start_node;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getNode_count() {
            return node_count;
        }

        public void setNode_count(int node_count) {
            this.node_count = node_count;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }
    }
}
