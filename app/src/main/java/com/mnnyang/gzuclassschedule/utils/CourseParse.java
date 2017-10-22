package com.mnnyang.gzuclassschedule.utils;

import android.util.Log;


import com.mnnyang.gzuclassschedule.custom.Course;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GZU课程解析
 * Created by mnnyang on 17-10-19.
 */

public class CourseParse {

    private static final String pattern = "第.*节";
    private static final String[] other = {"时间", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日", "早晨", "上午", "下午", "晚上"};
    private static final String[] week = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周七"};
    private static final Pattern pattern1 = Pattern.compile("第\\d{1,2}.*节");
    private static final Pattern pattern2 = Pattern.compile("第\\d{1,2},\\d{1,2}周");

    public static ArrayList<Course> parse(String html) {

        Document doc = org.jsoup.Jsoup.parse(html);
        Element table1 = doc.getElementById("Table1");
        Elements trs = table1.getElementsByTag("tr");

        ArrayList<Course> courses = new ArrayList<>();

        int node = 0;
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            for (Element td : tds) {
                String courseSource = td.text().trim();
                if (courseSource.length() <= 1) {
                    //null data
                    continue;
                }

                if (Pattern.matches(pattern, courseSource)) {
                    //node number
                    try {
                        node = Integer.decode(courseSource.substring(1, courseSource.length() - 1));
                    } catch (Exception e) {
                        node = 0;
                        e.printStackTrace();
                    }
                    continue;
                }

                if (inArray(other, courseSource)) {
                    //other data
                    continue;
                }
                courses.addAll(CourseParse.parseText(courseSource, node));
            }
        }

        return courses;
    }

    private static ArrayList<Course> parseText(String source, int node) {

        ArrayList<Course> courses = new ArrayList<>();
        String[] split = source.split(" ");
        if (split.length / 4 > 0 && split.length % 4 == 0) {
            for (int i = 0; i < split.length / 4; i++) {
                Course course = new Course();
                courses.add(course);

                course.setSource(source);
                course.setName(split[i * 4]);
                String time = split[1 + i * 4];
                parseTime(course, time, node);
                course.setTeacher(split[2 + i * 4]);
                course.setClassRoom(split[3 + i * 4]);
            }

        } else if (split.length > 2) {
            Course course = new Course();
            courses.add(course);

            course.setSource(source);
            course.setName(split[0]);
            String time = split[1];
            parseTime(course, time, node);
            course.setTeacher(split[2]);
            if (split.length > 3) {
                course.setClassRoom(split[3]);
            }
        } else {
            //TODO other type
            Course course = new Course();
            courses.add(course);
            course.setSource(source);
            course.setName(source);

            Log.e("CourseParse", "parseText omit:" + source);
        }

        return courses;
    }

    /**
     * 周一第1,2节{第2-16周|双周}
     * 周一第1,2节{第2-16周|双周}
     */
    private static void parseTime(Course course, String time, int htmlNode) {
        //week
        if (time.charAt(0) == '周') {
            String weekStr = time.substring(0, 2);
            int week = getIntWeek(weekStr);
            course.setWeek(week);
        }

        //单双周
        if (time.contains("|单周")) {
            course.setWeekType(Course.SINGLE_WEEK);
        } else if (time.contains("|双周")) {
            course.setWeekType(Course.DOUBLE_WEEK);
        }

        //节数
        Matcher matcher = pattern1.matcher(time);
        System.out.println(time);

        if (matcher.find()) {
            String nodeInfo = matcher.group(0);
            String[] nodes = nodeInfo.substring(1, nodeInfo.length() - 1).split(",");
            System.out.println("-----node");
            for (String node : nodes) {
                System.out.print(node);
            }
            System.out.println("-----nodee");
            course.setNodes(nodes);
        } else if (htmlNode != 0) {
            course.addNode(htmlNode);
        }

        //周数
        matcher = pattern2.matcher(time);
        if (matcher.find()) {
            String weekInfo = matcher.group(0);//第2-16周
            String[] weeks = weekInfo.substring(1, weekInfo.length() - 1).split("-");
            System.out.println("转换");
            for (String s : weeks) {
                System.out.println(s);
            }
            if (weeks.length > 0) {
                int startWeek = Integer.decode(weeks[0]);
                course.setStartWeek(startWeek);
            }
            if (weeks.length > 1) {
                int endWeek = Integer.decode(weeks[1]);
                course.setEndWeek(endWeek);
            }
        }
    }

    /**
     * 汉字转换int
     */
    private static int getIntWeek(String chinaWeek) {
        for (int i = 0; i < week.length; i++) {
            if (week[i].equals(chinaWeek)) {
                return i;
            }
        }
        return 0;
    }

    private static boolean inArray(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue))
                return true;
        }
        return false;

    }
}
