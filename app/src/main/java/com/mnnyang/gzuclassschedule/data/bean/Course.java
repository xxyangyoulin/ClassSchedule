package com.mnnyang.gzuclassschedule.data.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mnnyang on 17-10-22.
 */

public class Course extends CourseAncestor implements Comparable<Course> {
    private int courseId;
    public static final int NODE_NOON = -1;

    /**
     * 课程名称
     */
    private String name;
    /**
     * 教室
     */
    private String classRoom;

    /**
     * 星期几 1-7
     */
    private int week;

    /**
     * 节数
     */
    private List<Integer> nodes = new ArrayList<>();

    /**
     * 起始周
     */
    private int startWeek;
    /**
     * 结束周
     */
    private int endWeek;

    /**
     * 单双周类型
     */
    private int weekType = SHOW_ALL;

    /**
     * 上课教师
     */
    private String teacher;

    /**
     * 原文本
     */
    private String source;

    /**
     * 是否显示
     */
    private boolean showOverlap = true;

    private String csName;

    /**
     * 课程表名 courseId
     */
    private int csNameId;

    public void init() {
        setRow(getWeek());
        setStartIndex(getStartWeek());
        setEndIndex(getEndWeek());
        setShowType(getWeekType());

        if (!getNodes().isEmpty()) {
            setCol(getNodes().get(0));
            setRowNum(getNodes().size());
        } else {
            LogUtil.e(this, "Node data is empty-->" + this.toString());
        }

        if (TextUtils.isEmpty(getClassRoom())) {
            setText(getName());
        } else {
            setText(getName() + "\n@" + getClassRoom());
        }
    }

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
        this.row = this.week;
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

    public Course setOverlapShow(boolean showOverlap) {
        this.showOverlap = showOverlap;
        return this;
    }

    public String getCsName() {
        return csName;
    }

    public Course setCsName(String csName) {
        this.csName = csName;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Course setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public int getCsNameId() {
        return csNameId;
    }

    public Course setCsNameId(int csNameId) {
        this.csNameId = csNameId;
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
            LogUtil.d(this, "setNodes(String[] nodes) Integer.decode(nodes[i]); err");
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
                    LogUtil.d(this, "setNodes(String[] nodes) { --> incontinuity" + lastNode + "-" + tempNode);
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
        return super.toString() + "DownCourse{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", week=" + week +
                ", nodes=" + nodes +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekType=" + weekType +
                ", teacher='" + teacher + '\'' +
                ", source='" + source + '\'' +
                ", showOverlap=" + showOverlap +
                ", csName='" + csName + '\'' +
                ", csNameId=" + csNameId +
                '}';
    }

    public String toSelfString() {
        return "DownCourse{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", week=" + week +
                ", nodes=" + nodes +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekType=" + weekType +
                ", teacher='" + teacher + '\'' +
                ", source='" + source + '\'' +
                ", showOverlap=" + showOverlap +
                ", csName='" + csName + '\'' +
                ", csNameId=" + csNameId +
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

        if (this.getEndWeek() < course.getStartWeek()
                || course.getEndWeek() < this.getStartWeek()) {

            return false;
        }

        return true;
    }

}
