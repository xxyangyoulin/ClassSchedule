package com.mnnyang.gzuclassschedule.data.beanv2;

import android.text.TextUtils;

import com.mnnyang.gzuclassschedule.custom.course.CourseAncestor;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CourseV2 extends CourseAncestor {
    @Id(autoincrement = true)
    private Long couId;

    private String couName;
    private String couLocation;
    private String couTeacher;

    private Integer couWeek;

    private Integer couStartNode;
    private Integer couNodeCount;

    private String couAllWeek;

    private Integer couColor;

    private Long couCgId;

    @Generated(hash = 1410596089)
    public CourseV2(Long couId, String couName, String couLocation,
                    String couTeacher, Integer couWeek, Integer couStartNode,
                    Integer couNodeCount, String couAllWeek, Integer couColor,
                    Long couCgId) {
        this.couId = couId;
        this.couName = couName;
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

    public CourseV2 init() {
        setRow(getCouWeek());

        if (getCouColor() != null) {
            setColor(getCouColor());
        }

        getShowIndexes().clear();

        try {
            String[] split = couAllWeek.split(",");
            for (String s : split) {
                addIndex(Integer.parseInt(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getCouNodeCount() != 0) {
            setCol(getCouStartNode());
            setRowNum(getCouNodeCount());
        } else {
            LogUtil.e(this, "Node count is zero-->" + this.toString());
        }

        if (TextUtils.isEmpty(getCouLocation())) {
            setText(getCouName());
        } else {
            setText(getCouName() + "\n@" + getCouLocation());
        }

        return this;
    }

    /**
     * name teacher location 相等判为同一门课
     */
    public boolean isSameClass(CourseV2 other) {
        if (other == null) {
            return false;
        }

        if (other.getCouName() == null || this.getCouName() == null) {
            return false;
        }

        if (other.getCouName().equals(this.getCouName())) {
            if ((other.getCouTeacher() == null && this.getCouTeacher() == null)
                    || (other.getCouTeacher().equals(this.getCouTeacher()))) {
                return (other.getCouLocation() == null && this.getCouLocation() == null)
                        || (other.getCouLocation().equals(this.getCouLocation()));
            }
        }
        return false;
    }

    /**
     * name teacher 相等判为同一门课
     */
    public boolean isSameClassWithoutLocation(CourseV2 other) {
        if (other == null) {
            return false;
        }

        if (other.getCouName() == null || this.getCouName() == null) {
            return false;
        }

        if (other.getCouName().equals(this.getCouName())) {
            return (other.getCouTeacher() == null && this.getCouTeacher() == null)
                    || (other.getCouTeacher().equals(this.getCouTeacher()));
        }
        return false;
    }

    public Long getCouId() {
        return couId;
    }

    public CourseV2 setCouId(Long couId) {
        this.couId = couId;
        return this;
    }

    public String getCouName() {
        return couName;
    }

    public CourseV2 setCouName(String couName) {
        this.couName = couName;
        return this;
    }

    public String getCouLocation() {
        return couLocation;
    }

    public CourseV2 setCouLocation(String couLocation) {
        this.couLocation = couLocation;
        return this;
    }

    public String getCouTeacher() {
        return couTeacher;
    }

    public CourseV2 setCouTeacher(String couTeacher) {
        this.couTeacher = couTeacher;
        return this;
    }

    public Integer getCouWeek() {
        return couWeek;
    }

    public CourseV2 setCouWeek(Integer couWeek) {
        this.couWeek = couWeek;
        return this;
    }

    public Integer getCouStartNode() {
        return couStartNode;
    }

    public CourseV2 setCouStartNode(Integer couStartNode) {
        this.couStartNode = couStartNode;
        return this;
    }

    public Integer getCouNodeCount() {
        return couNodeCount;
    }

    public CourseV2 setCouNodeCount(Integer couNodeCount) {
        this.couNodeCount = couNodeCount;
        return this;
    }

    public String getCouAllWeek() {
        return couAllWeek;
    }

    public CourseV2 setCouAllWeek(String couAllWeek) {
        this.couAllWeek = couAllWeek;
        return this;
    }

    public Integer getCouColor() {
        return couColor;
    }

    public CourseV2 setCouColor(Integer couColor) {
        this.couColor = couColor;
        return this;
    }

    public Long getCouCgId() {
        return couCgId;
    }

    public CourseV2 setCouCgId(Long couCgId) {
        this.couCgId = couCgId;
        return this;
    }

    @Override
    public String toString() {
        return "CourseV2{" +
                "couId=" + couId +
                ", couName='" + couName + '\'' +
                ", couLocation='" + couLocation + '\'' +
                ", couTeacher='" + couTeacher + '\'' +
                ", couWeek=" + couWeek +
                ", couStartNode=" + couStartNode +
                ", couNodeCount=" + couNodeCount +
                ", couAllWeek='" + couAllWeek + '\'' +
                ", couColor=" + couColor +
                ", couCgId=" + couCgId +
                '}';
    }

    public String toSuperString() {
        return super.toString();
    }
}
