package com.mnnyang.gzuclassschedule.data.bean;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 课程
 * Created by mnnyang on 17-10-19.
 */

public class Course implements Comparable<Course> {
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
     * 节数 -1为中午(-_-!!中午还有上课的...)
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
     * 单双周  all single double
     */
    private char ads = 'a';

    /**
     * 上课教师
     */
    private String teacher;

    /**
     * 原文本
     */
    private String source;

    private int rowSpan = 1;

    public int getRowSpan() {
        return rowSpan;
    }

    public Course setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
        return this;
    }

    public Course() {
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
        return this;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public Course addNode(int node) {
        if (!this.nodes.contains(node)) {
            this.nodes.add(node);
            //TODO
            Collections.sort(this.nodes, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
        }
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

    public char getAds() {
        return ads;
    }

    public Course setAds(char ads) {
        this.ads = ads;
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

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", week='" + week + '\'' +
                ", nodes=" + nodes +
                ", startWeek='" + startWeek + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", ads=" + ads +
                ", teacher='" + teacher + '\'' +
                ", source='" + source + '\'' +
                '}';
    }


    public void setNodes(String[] nodes) {
        try {
            for (String node : nodes) {
                addNode(Integer.decode(node));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(@NonNull Course o) {
        if (week - o.getWeek() != 0) {
            return week - o.getWeek();
        }
        return nodes.get(0) - o.getNodes().get(0);
    }
}
