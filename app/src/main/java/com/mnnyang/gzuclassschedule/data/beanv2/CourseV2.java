package com.mnnyang.gzuclassschedule.data.beanv2;

import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CourseV2 extends CourseAncestor {
    @Id(autoincrement = true)
    private Long couId;

    private String cgName;
    private String couLocation;
    private String couTeacher;

    private Integer couWeek;

    private Integer couStartNode;
    private Integer couNodeCount;

    private String couAllWeek;

    private Integer couColor;

    private Long couCgId;

    @Generated(hash = 1598859975)
    public CourseV2(Long couId, String cgName, String couLocation,
            String couTeacher, Integer couWeek, Integer couStartNode,
            Integer couNodeCount, String couAllWeek, Integer couColor,
            Long couCgId) {
        this.couId = couId;
        this.cgName = cgName;
        this.couLocation = couLocation;
        this.couTeacher = couTeacher;
        this.couWeek = couWeek;
        this.couStartNode = couStartNode;
        this.couNodeCount = couNodeCount;
        this.couAllWeek = couAllWeek;
        this.couColor = couColor;
        this.couCgId = couCgId;
    }

    @Generated(hash = 552593624)
    public CourseV2() {
    }

    public Long getCouId() {
        return this.couId;
    }

    public void setCouId(Long couId) {
        this.couId = couId;
    }

    public String getCgName() {
        return this.cgName;
    }

    public void setCgName(String cgName) {
        this.cgName = cgName;
    }

    public String getCouLocation() {
        return this.couLocation;
    }

    public void setCouLocation(String couLocation) {
        this.couLocation = couLocation;
    }

    public String getCouTeacher() {
        return this.couTeacher;
    }

    public void setCouTeacher(String couTeacher) {
        this.couTeacher = couTeacher;
    }

    public Integer getCouWeek() {
        return this.couWeek;
    }

    public void setCouWeek(Integer couWeek) {
        this.couWeek = couWeek;
    }

    public Integer getCouStartNode() {
        return this.couStartNode;
    }

    public void setCouStartNode(Integer couStartNode) {
        this.couStartNode = couStartNode;
    }

    public Integer getCouNodeCount() {
        return this.couNodeCount;
    }

    public void setCouNodeCount(Integer couNodeCount) {
        this.couNodeCount = couNodeCount;
    }

    public String getCouAllWeek() {
        return this.couAllWeek;
    }

    public void setCouAllWeek(String couAllWeek) {
        this.couAllWeek = couAllWeek;
    }

    public Integer getCouColor() {
        return this.couColor;
    }

    public void setCouColor(Integer couColor) {
        this.couColor = couColor;
    }

    public Long getCouCgId() {
        return this.couCgId;
    }

    public void setCouCgId(Long couCgId) {
        this.couCgId = couCgId;
    }
}
