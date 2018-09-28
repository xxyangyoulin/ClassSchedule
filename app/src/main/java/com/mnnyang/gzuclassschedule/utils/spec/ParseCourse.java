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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
                if (courseSource.length() <= 6) {
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

    public static void main(String[] args) {
        //String s = readToString("/home/xxyangyoulin/Android/project/ClassSchedule/app/src/main/java/com/mnnyang/gzuclassschedule/impt/demo.html");
        String s = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\" rowspan=\"1\" width=\"2%\">时间</td>\n" +
                "        <td align=\"Center\" width=\"14%\">星期一</td>\n" +
                "        <td align=\"Center\" width=\"14%\">星期二</td>\n" +
                "        <td align=\"Center\" width=\"14%\">星期三</td>\n" +
                "        <td align=\"Center\" width=\"14%\">星期四</td>\n" +
                "        <td align=\"Center\" width=\"14%\">星期五</td>\n" +
                "        <td class=\"noprint\" align=\"Center\" width=\"14%\">星期六</td>\n" +
                "        <td class=\"noprint\" align=\"Center\" width=\"14%\">星期日</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\">早晨</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"5\" width=\"1%\">上午</td>\n" +
                "        <td width=\"1%\">第1节</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">船舶货运专题A<br>任选<br>周一第1,2节{第6-8周}<br>王庸凯<br>獐210<br><br>船舶货运专题A<br>任选<br>周一第1,2节{第2-4周}<br>王庸凯<br>獐210\n" +
                "        </td>\n" +
                "        <td align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
                "        <td align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
                "        <td align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">远洋运输业务与海商法<br>必修<br>周五第1,2节{第6-17周}<br>姚智慧<br>獐332<br><br>远洋运输业务与海商法<br>必修<br>周五第1,2节{第1-4周}<br>姚智慧<br>獐332\n" +
                "        </td>\n" +
                "        <td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第2节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第3节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\">远洋运输业务与海商法<br>必修<br>周三第3,4节{第6-17周}<br>姚智慧<br>獐131<br><br>远洋运输业务与海商法<br>必修<br>周三第3,4节{第1-4周}<br>姚智慧<br>獐131\n" +
                "        </td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第4节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第5节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"5\">下午</td>\n" +
                "        <td>第6节</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\">航海气象与海洋学专题<br>任选<br>周一第6,7节{第7-18周}<br>张飞成<br>獐422</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\">船舶货运专题A<br>任选<br>周五第6,7节{第2-4周}<br>王庸凯<br>獐410<br><br>船舶货运专题A<br>任选<br>周五第6,7节{第6-8周}<br>王庸凯<br>獐410\n" +
                "        </td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第7节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第8节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\" rowspan=\"2\">大学生职业发展与就业指导<br>课外<br>周二第8,9节{第12-12周|双周}<br>王超鹏<br>獐103</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第9节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第10节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"4\" width=\"1%\">晚上</td>\n" +
                "        <td>第11节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第12节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第13节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>第14节</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>";

        final ArrayList<Course> courses = ParseCourse.parse(s);

        for (Course course : courses) {
            System.out.println("TEST"+course.toSelfString());
        }
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Course> parseTextInfo(String source, int node) {

        System.out.println("---->"+source+"]]]");

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

            //Log.e("ParseCourse", "parseTextInfo omit:" + source);
        }

        return courses;
    }

    /**
     * 周一第1,2节{第2-16周|双周}
     * {第1-15周|2节/单周}
     * <p>
     * TODO 周一第1,2节{第1-15周|2节/周} //次格式未解决 数据不足
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
            course.setWeekType(Course.SHOW_SINGLE);
        } else if (time.contains("|双周")) {
            course.setWeekType(Course.SHOW_DOUBLE);
        }

        //节数
        Matcher matcher = pattern1.matcher(time);

        if (matcher.find()) {
            String nodeInfo = matcher.group(0);
            String[] nodes = nodeInfo.substring(1, nodeInfo.length() - 1).split(",");
          /*  for (String node : nodes) {
                System.out.print(node);
            }*/

            course.setNodes(nodes);
        } else if (htmlNode != 0) {
            course.addNode(htmlNode);
        } else {
            //周一第1,2节{第1-15周|2节/周}
            //TODO 上传无法解析的数据
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
