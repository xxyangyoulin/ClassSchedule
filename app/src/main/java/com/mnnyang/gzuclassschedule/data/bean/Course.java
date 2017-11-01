package com.mnnyang.gzuclassschedule.data.bean;

import android.support.annotation.NonNull;


import com.mnnyang.gzuclassschedule.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mnnyang on 17-10-22.
 */

public class Course implements Comparable<Course> {
    private int id;
    //所有周
    public static final int ALL_WEEK = 0;
    //双周
    public static final int DOUBLE_WEEK = 1;
    //单周
    public static final int SINGLE_WEEK = 2;

    public static final int NODE_NOON = -1;

    /**
     * 课程名称
     */
    protected String name;
    /**
     * 教室
     */
    protected String classRoom;

    /**
     * 星期几 1-7
     */
    protected int week;

    /**
     * 节数 -1为中午(-_-!!中午还有上课的...)
     */
    protected List<Integer> nodes = new ArrayList<>();

    /**
     * 起始周
     */
    protected int startWeek;
    /**
     * 结束周
     */
    protected int endWeek;

    /**
     * 单双周类型
     */
    protected int weekType = ALL_WEEK;

    /**
     * 上课教师
     */
    protected String teacher;

    /**
     * 原文本
     */
    protected String source;

    /**
     * 是否显示
     */
    protected boolean showOverlap = true;

    protected String courseTime;

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public Course setClassRoom(String classRoom) {
        this.classRoom = classRoom;
        return this;
    }

    public int getWeek() {
        return week;
    }

    public Course setWeek(int week) {
        this.week = week;
        return this;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public Course setStartWeek(int startWeek) {
        this.startWeek = startWeek;
        return this;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public Course setEndWeek(int endWeek) {
        this.endWeek = endWeek;
        return this;
    }

    public int getWeekType() {
        return weekType;
    }

    public Course setWeekType(int weekType) {
        this.weekType = weekType;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    public Course setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Course setSource(String source) {
        this.source = source;
        return this;
    }

    public boolean isShowOverlap() {
        return showOverlap;
    }

    public Course setShowOverlap(boolean showOverlap) {
        this.showOverlap = showOverlap;
        return this;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public Course setCourseTime(String courseTime) {
        this.courseTime = courseTime;
        return this;
    }

    public int getId() {
        return id;
    }

    public Course setId(int id) {
        this.id = id;
        return this;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    /**
     * 设置课程节数
     * *必须升序排列 例如:3 4 节课
     * *错位部分将会被抛弃 例如:2 4节课程将会抛弃4
     */
    public void setNodes(String[] nodes) {
        int intNodes[] = new int[nodes.length];

        try {
            for (int i = 0; i < nodes.length; i++) {
                intNodes[i] = Integer.decode(nodes[i]);
            }
            setNodes(intNodes);
        } catch (Exception e) {
            LogUtils.d(this, "setNodes(String[] nodes) Integer.decode(nodes[i]); err");
            e.printStackTrace();
        }
    }

    /**
     * 设置课程节数
     * 必须生序排列
     */
    public void setNodes(int[] nodes) {
        if (nodes.length == 0) {
            return;
        }

        try {
            int lastNode = nodes[0];
            int tempNode = 0;

            addNode(lastNode);
            for (int i = 1; i < nodes.length; i++) {
                tempNode = nodes[i];
                if (tempNode - lastNode != 1) {
                    LogUtils.d(this, "setNodes(String[] nodes) { --> incontinuity" + lastNode + "-" + tempNode);
                    return;
                }
                addNode(tempNode);
                lastNode = tempNode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加课程节数
     */
    public Course addNode(int node) {
        if (!this.nodes.contains(node)) {
            this.nodes.add(node);
            Collections.sort(this.nodes, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
        }
        return this;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", week=" + week +
                ", nodes=" + nodes +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekType=" + weekType +
                ", teacher='" + teacher + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Course o) {
        int weekStatus = week - o.getWeek();
        if (weekStatus != 0) {
            return weekStatus;
        }

        if (getNodes().size() == 0) {
            return 1;
        }

        if (o.getNodes().size() == 0) {
            return -1;
        }

        return nodes.get(0) - o.getNodes().get(0);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Course course = (Course) obj;
        if (getWeek() != course.getWeek()) {
            return false;
        }
        if (getNodes().size() == 0 || course.getNodes().size() == 0) {
            return false;
        }

        if (!getNodes().get(0).equals(course.getNodes().get(0))) {
            return false;
        }

        if (getWeekType() != course.getWeekType()) {
            return false;
        }

        return true;
    }

}
