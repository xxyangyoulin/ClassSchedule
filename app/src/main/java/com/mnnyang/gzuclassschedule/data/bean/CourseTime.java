package com.mnnyang.gzuclassschedule.data.bean;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-11-4.
 */

public class CourseTime {
    public ArrayList<String> years = new ArrayList<>();
    public ArrayList<String> terms = new ArrayList<>();
    public String selectYear;
    public String selectTerm;

    @Override
    public String toString() {
        return "CourseTime{" +
                "years=" + years +
                ", terms=" + terms +
                ", selectYear='" + selectYear + '\'' +
                ", selectTerm='" + selectTerm + '\'' +
                '}';
    }
}
