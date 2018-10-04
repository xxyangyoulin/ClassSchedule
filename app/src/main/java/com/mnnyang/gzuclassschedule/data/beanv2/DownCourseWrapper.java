package com.mnnyang.gzuclassschedule.data.beanv2;

import java.io.Serializable;
import java.util.List;

public class DownCourseWrapper extends BaseBean {

    private List<DownCourse> data;

    public List<DownCourse> getData() {
        return data;
    }

    public void setData(List<DownCourse> data) {
        this.data = data;
    }

    public static class DownCourse implements Serializable {
        /**
         * group : 默认课表
         * name : hhh
         * color : -1
         * start_node : 1
         * location : 45
         * time_all_week : 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
         * time_which_day : 4
         * node_count : 4
         * teacher : hgjhg
         */

        private String group;
        private String name;
        private int color;
        private int start_node;
        private String location;
        private String time_all_week;
        private int time_which_day;
        private int node_count;
        private String teacher;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
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

        public String getTime_all_week() {
            return time_all_week;
        }

        public void setTime_all_week(String time_all_week) {
            this.time_all_week = time_all_week;
        }

        public int getTime_which_day() {
            return time_which_day;
        }

        public void setTime_which_day(int time_which_day) {
            this.time_which_day = time_which_day;
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
