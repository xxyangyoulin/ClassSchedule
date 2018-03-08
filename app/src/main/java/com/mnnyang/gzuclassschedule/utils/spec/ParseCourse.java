package com.mnnyang.gzuclassschedule.utils.spec;

import android.util.Log;


import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.data.db.CoursesPsc;
import com.mnnyang.gzuclassschedule.utils.LogUtil;

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

public class ParseCourse {
    private static final String pattern = "第.*节";
    private static final String[] other = {"时间", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
            "星期日", "早晨", "上午", "下午", "晚上"};
    private static final Pattern pattern1 = Pattern.compile("第\\d{1,2}.*节");
    private static final Pattern pattern2 = Pattern.compile("\\{第\\d{1,2}[-]*\\d*周");

    public static String parseViewStateCode(String html) {
        String code = "";
        Document doc = org.jsoup.Jsoup.parse(html);
        Elements inputs = doc.getElementsByAttributeValue("name", Url.__VIEWSTATE);
        if (inputs.size() > 0) {
            code = inputs.get(0).attr("value");
            LogUtil.d(CoursesPsc.class, "finded __VIEWSTATE code=" + code);
        } else {
            LogUtil.d(CoursesPsc.class, "Not find __VIEWSTATE code");
        }

        return code;
    }

    /**
     * @param html
     * @return 解析失敗返回空
     */
    public static CourseTime parseTime(String html) {
        String SELECTED = "selected";
        String OPTION = "option";

        Document doc = org.jsoup.Jsoup.parse(html);
        CourseTime courseTime = new CourseTime();

        Elements selects = doc.getElementsByTag("select");
        if (selects == null || selects.size() < 2) {
            LogUtil.e(ParseCourse.class, "select < 2 ");
            return null;
        }

        Elements options = selects.get(0).getElementsByTag(OPTION);

        for (Element o : options) {
            String year = o.text().trim();
            courseTime.years.add(year);
            if (o.attr(SELECTED).equals(SELECTED)) {
                courseTime.selectYear = year;
            }
        }

        options = selects.get(1).getElementsByTag(OPTION);
        for (Element o : options) {
            String term = o.text().trim();
            courseTime.terms.add(term);
            if (o.attr(SELECTED).equals(SELECTED)) {
                courseTime.selectTerm = term;
            }
        }

        return courseTime;
    }

    /**
     * @param html
     * @return 解析失败返回空
     */
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
                courses.addAll(ParseCourse.parseTextInfo(courseSource, node));
            }
        }

        return courses;
    }

    private static ArrayList<Course> parseTextInfo(String source, int node) {

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

            Log.e("ParseCourse", "parseTextInfo omit:" + source);
        }

        return courses;
    }

    /**
     * 周一第1,2节{第2-16周|双周}
     *  {第1-15周|2节/单周}
     *
     * 周一第1,2节{第1-15周|2节/周}
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
            course.setWeekType(Course.WEEK_SINGLE);
        } else if (time.contains("|双周")) {
            course.setWeekType(Course.WEEK_DOUBLE);
        }

        //节数
        Matcher matcher = pattern1.matcher(time);

        if (matcher.find()) {
            String nodeInfo = matcher.group(0);
            String[] nodes = nodeInfo.substring(1, nodeInfo.length() - 1).split(",");
          /*  for (String node : nodes) {
                System.out.print(node);
            }*/
            LogUtil.e(ParseCourse.class, "匹配上");

            LogUtil.e(ParseCourse.class, "time"+time+"---解析"+nodes);
            course.setNodes(nodes);
        } else if (htmlNode != 0) {
            course.addNode(htmlNode);
        }else{
            //周一第1,2节{第1-15周|2节/周}
            LogUtil.e(ParseCourse.class, "没有匹配上"+time);

        }

        //周数
        matcher = pattern2.matcher(time);
        if (matcher.find()) {
            String weekInfo = matcher.group(0);//第2-16周
            if (weekInfo.length() < 2) {
                return;
            }
            String[] weeks = weekInfo.substring(2, weekInfo.length() - 1).split("-");

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
        for (int i = 0; i < Constant.WEEK.length; i++) {
            if (Constant.WEEK[i].equals(chinaWeek)) {
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
