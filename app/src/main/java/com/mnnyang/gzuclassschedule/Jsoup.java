package com.mnnyang.gzuclassschedule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.custom.util.Utils;
import com.mnnyang.gzuclassschedule.data.bean.Course;
import com.mnnyang.gzuclassschedule.custom.CourseTableView;
import com.mnnyang.gzuclassschedule.custom.CourseView;
import com.mnnyang.gzuclassschedule.utils.CourseParse;
import com.mnnyang.gzuclassschedule.utils.LogUtils;

import java.util.ArrayList;


public class Jsoup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        final CourseView couseView = (CourseView) findViewById(R.id.course_view);
        final CourseTableView view = couseView.getCourseTableView();
//        view.setCourseTextColor(Color.BLACK);

        ArrayList<Course> parse = CourseParse.parse(html2);
        couseView.setCourseData(parse)
                .setDividerSize(0)
                .setWeekText("一", "二", "三", "四", "五", "六", "日")
                .setWeekTextSize(13)
                .setWeekTextColor(0xa0000000)
                .setMonthTextSize(0);

        couseView.getCourseTableView().setCourseItemRadius(5)
//                .setShowVerticalDivider(true)
        .setVerticalDividerMargin(5).setHorizontalDividerMargin(5);
        for (Course c : parse) {
            System.out.println(c.toString());
        }

        couseView.setOnItemClickListener(new CourseTableView.OnItemClickListener() {
            @Override
            public void onClick(Course course, LinearLayout itemLayout) {
                System.out.println("fff");
            }
        });

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(this, "click");
                Course course = new Course().setName("666").setClassRoom("777")
                        .setStartWeek(1).setEndWeek(18).setWeek(3).addNode(Course.NODE_NOON);
                couseView.addCourse(course);
//                view.setCourseTextColor(Color.GREEN);
                view.setCurrentWeekCount(view.getCurrentWeekCount() == 2 ? 1 : 2);
//                view.updateView();
                couseView.setMonth(10).updateView();
            }
        });
    }

    String html4 = "\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<HTML lang=\"gb2312\">\n" +
            "\t<HEAD>\n" +
            "\t\t<title>现代教学管理信息系统</title><meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\">\n" +
            "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
            "\t\t<meta http-equiv=\"Content-Language\" content=\"gb2312\">\n" +
            "\t\t<meta content=\"all\" name=\"robots\">\n" +
            "\t\t<meta content=\"作者信息\" name=\"author\">\n" +
            "\t\t<meta content=\"版权信息\" name=\"Copyright\">\n" +
            "\t\t<meta content=\"站点介绍\" name=\"description\">\n" +
            "\t\t<meta content=\"站点关键词\" name=\"keywords\">\n" +
            "\t\t<LINK href=\"style/base/favicon.ico\" type=\"image/x-icon\" rel=\"icon\">\n" +
            "\t\t\t<LINK media=\"all\" href=\"style/base/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "\t\t\t\t<LINK media=\"all\" href=\"style/standard/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "\t\t\t\t\t<!--<script defer>   \n" +
            "\t\tfunction  PutSettings()   \n" +
            "\t\t\t{     \n" +
            "\t\t\tfactory.printing.header=\"\";   \n" +
            "\t\t\tfactory.printing.footer=\"\";   \n" +
            "\t\t    factory.printing.leftMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.topMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.rightMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.bottomMargin=\"5\";\n" +
            "\t\t }\n" +
            "\n" +
            "\t\t\t\t\t</script>-->\n" +
            "\t\t\t\t\t<style> @media Print { .bgnoprint { }\n" +
            "\t.noprint { DISPLAY: none }}\n" +
            "\t</style>\n" +
            "\t</HEAD>\n" +
            "\t<BODY>\n" +
            "\t\t<!--<OBJECT id=\"factory\" style=\"DISPLAY: none\" codeBase=\"ScriptX.cab#Version=5,60,0,360\" classid=\"clsid:1663ed61-23eb-11d2-b92f-008048fdd814\"\n" +
            "\t\t\tVIEWASTEXT>\n" +
            "\t\t</OBJECT>-->\n" +
            "\t\t<form name=\"xskb_form\" method=\"post\" action=\"xskbcx.aspx?xh=1500170110&amp;xm=%D1%EE%D3%D1%C1%D6&amp;gnmkdm=N121603\" id=\"xskb_form\">\n" +
            "<input type=\"hidden\" name=\"__EVENTTARGET\" value=\"\" />\n" +
            "<input type=\"hidden\" name=\"__EVENTARGUMENT\" value=\"\" />\n" +
            "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"dDwzOTI4ODU2MjU7dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47aTwyPjtpPDQ+O2k8Nz47aTw5PjtpPDExPjtpPDEzPjtpPDE1PjtpPDI0PjtpPDI2PjtpPDI4PjtpPDMwPjtpPDMyPjtpPDM0Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDwxMjAwNS0yMDAwNjI7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8eG47eG47Pj47Pjt0PGk8Mz47QDwyMDE3LTIwMTg7MjAxNi0yMDE3OzIwMTUtMjAxNjs+O0A8MjAxNy0yMDE4OzIwMTYtMjAxNzsyMDE1LTIwMTY7Pj47bDxpPDE+Oz4+Ozs+O3Q8dDw7O2w8aTwyPjs+Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjE1MDAxNzAxMTA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnk+WQje+8muadqOWPi+aelzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85a2m6Zmi77ya6K6h566X5py656eR5a2m5LiO5oqA5pyv5a2m6ZmiOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrvvJrorqHnrpfmnLrnp5HlrabkuI7mioDmnK87Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv+ePre+8muiuoeenkTE1Mjs+Pjs+Ozs+O3Q8O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8Mj47aTwyPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+O2w8aTwwPjs+O2w8dDw7bDxpPDE+O2k8Mj47PjtsPHQ8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+O2k8NT47aTw2Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDzkvIHkuJrlrp7ot7XvvIjnp7vliqjlubPlj7DlvIDlj5HlupTnlKjvvIk7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOW8oOa1t+WugTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Mi4wOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwwMy0wNDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Jm5ic3BcOzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Jm5ic3BcOzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Jm5ic3BcOzs+Pjs+Ozs+Oz4+O3Q8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+O2k8NT47aTw2Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDzorqHnrpfmnLrnoazku7bns7vnu5/nu7zlkIjorr7orqE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOW8oOWOmuatpi/lp5rlh6/lraY7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDIuMDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MDEtMDQ7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjs+Pjs+Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8Mj47aTwyPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+O2w8aTwwPjs+O2w8dDw7bDxpPDE+O2k8Mj47PjtsPHQ8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPDIwMTYtMjAxNzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Mzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85LyB5Lia5a6e6Le177yI56e75Yqo5bmz5Y+w5byA5Y+R5bqU55So77yJOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlvKDmtbflroE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDIuMDs+Pjs+Ozs+Oz4+O3Q8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPDIwMTYtMjAxNzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Mzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w86K6h566X5py656Gs5Lu257O757uf57u85ZCI6K6+6K6hOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlvKDljprmraYv5aea5Yev5a2mOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwyLjA7Pj47Pjs7Pjs+Pjs+Pjs+Pjs+Pjs+Pjs+joq+Tl/Xt9rvsF1QeiJOQ4JKIkw=\" />\n" +
            "\n" +
            "<script language=\"javascript\" type=\"text/javascript\">\n" +
            "<!--\n" +
            "\tfunction __doPostBack(eventTarget, eventArgument) {\n" +
            "\t\tvar theform;\n" +
            "\t\tif (window.navigator.appName.toLowerCase().indexOf(\"microsoft\") > -1) {\n" +
            "\t\t\ttheform = document.xskb_form;\n" +
            "\t\t}\n" +
            "\t\telse {\n" +
            "\t\t\ttheform = document.forms[\"xskb_form\"];\n" +
            "\t\t}\n" +
            "\t\ttheform.__EVENTTARGET.value = eventTarget.split(\"$\").join(\":\");\n" +
            "\t\ttheform.__EVENTARGUMENT.value = eventArgument;\n" +
            "\t\ttheform.submit();\n" +
            "\t}\n" +
            "// -->\n" +
            "</script>\n" +
            "\n" +
            "\t\t\t<!-- 多功能操作区 -->\n" +
            "\t\t\t<!-- 内容显示区开始 -->\n" +
            "\t\t\t<div class=\"main_box \">\n" +
            "\t\t\t\t<div class=\"mid_box\">\n" +
            "\t\t\t\t\t<div class=\"title noprint\">\n" +
            "\t\t\t\t\t\t<p>\n" +
            "\t\t\t\t\t\t\t<!-- 查询得到的数据量显示区域 --></p>\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<!-- From内容 --><span class=\"formbox\">\n" +
            "\t\t\t\t\t\t<TABLE class=\"formlist noprint\" id=\"Table2\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"center\"><select name=\"xnd\" onchange=\"__doPostBack('xnd','')\" language=\"javascript\" id=\"xnd\">\n" +
            "\t<option value=\"2017-2018\">2017-2018</option>\n" +
            "\t<option selected=\"selected\" value=\"2016-2017\">2016-2017</option>\n" +
            "\t<option value=\"2015-2016\">2015-2016</option>\n" +
            "\n" +
            "</select><span id=\"Label2\"><font size=\"4\">学年第</font></span><select name=\"xqd\" onchange=\"__doPostBack('xqd','')\" language=\"javascript\" id=\"xqd\">\n" +
            "\t<option value=\"1\">1</option>\n" +
            "\t<option value=\"2\">2</option>\n" +
            "\t<option selected=\"selected\" value=\"3\">3</option>\n" +
            "\n" +
            "</select><span id=\"Label1\"><font size=\"4\">学期学生个人课程表</font></span></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR class=\"trbg1\">\n" +
            "\t\t\t\t\t\t\t\t<TD><span id=\"Label5\">学号：1500170110</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label6\">姓名：杨友林</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label7\">学院：计算机科学与技术学院</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label8\">专业：计算机科学与技术</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label9\">行政班：计科152</span>\n" +
            "\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;<span id=\"labTS\"><font color=\"Red\"></font></span><span id=\"labTip\"><font color=\"Red\"></font></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name=\"btnPrint\" id=\"btnPrint\" type=\"button\" class=\"button\" style=\"DISPLAY:none\" onclick=\"window.print();\" value=\"打印课表\" />\n" +
            "\t\t\t\t\t\t\t\t</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t</TABLE>\n" +
            "\t\t\t\t\t\t<br>\n" +
            "\t\t\t\t\t\t<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">\n" +
            "\t<tr>\n" +
            "\t\t<td colspan=\"2\" rowspan=\"1\" width=\"2%\">时间</td><td align=\"Center\" width=\"14%\">星期一</td><td align=\"Center\" width=\"14%\">星期二</td><td align=\"Center\" width=\"14%\">星期三</td><td align=\"Center\" width=\"14%\">星期四</td><td align=\"Center\" width=\"14%\">星期五</td><td class=\"noprint\" align=\"Center\" width=\"14%\">星期六</td><td class=\"noprint\" align=\"Center\" width=\"14%\">星期日</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td colspan=\"2\">早晨</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">上午</td><td width=\"1%\">第1节</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">形势与政策<br>周一第1,2节{第1-2周}<br>文厚泓<br>逸夫楼401D1</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">数据结构课程设计<br>周二第1,2节{第1-2周}<br>程欣宇<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">数据结构课程设计<br>周三第1,2节{第1-2周}<br>程欣宇<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">形势与政策<br>周四第1,2节{第1-2周}<br>文厚泓<br>逸夫楼401D1</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">数据结构课程设计<br>周五第1,2节{第1-2周}<br>程欣宇<br>博学楼实验室804</td><td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td><td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第2节</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第3节</td><td align=\"Center\" rowspan=\"2\">数据结构课程设计<br>周一第3,4节{第1-2周}<br>程欣宇<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\">移动平台开发课程设计<br>周二第3,4节{第1-2周}<br>涂孝颖<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\">移动平台开发课程设计<br>周三第3,4节{第1-2周}<br>涂孝颖<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\">移动平台开发课程设计<br>周四第3,4节{第1-2周}<br>涂孝颖<br>博学楼实验室804</td><td align=\"Center\" rowspan=\"2\">移动平台开发课程设计<br>周五第3,4节{第1-2周}<br>涂孝颖<br>博学楼实验室804</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第4节</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">下午</td><td>第5节</td><td align=\"Center\" rowspan=\"2\">毛泽东思想和中国特色社会主义理论体系概论（2）<br>周一第5,6节{第1-2周}<br>姚荣<br>外语楼112DT</td><td align=\"Center\">&nbsp;</td><td align=\"Center\" rowspan=\"2\">毛泽东思想和中国特色社会主义理论体系概论（2）<br>周三第5,6节{第1-2周}<br>姚荣<br>外语楼112DT</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第6节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第7节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第8节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"3\" width=\"1%\">晚上</td><td>第9节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第10节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第11节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr>\n" +
            "</table>\n" +
            "\t\t\t\t\t\t<br>\n" +
            "\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<div class=\"noprint\" align=\"left\">调、停（补）课信息：</div>\n" +
            "\t\t\t\t\t\t<table class=\"datelist noprint\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DBGrid\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>编号</td><td>课程名称</td><td>原上课时间地点教师</td><td>现上课时间地点教师</td><td>申请时间</td>\n" +
            "\t</tr>\n" +
            "</table>\n" +
            "\t\t\t\t\t\t<TABLE class=\"noprint\" id=\"Table3\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">实践课(或无上课时间)信息：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD valign=\"top\"><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DataGrid1\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>课程名称</td><td>教师</td><td>学分</td><td>起止周</td><td>上课时间</td><td>上课地点</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>企业实践（移动平台开发应用）</td><td>张海宁</td><td>2.0</td><td>03-04</td><td>&nbsp;</td><td>&nbsp;</td>\n" +
            "\t</tr><tr class=\"alt\">\n" +
            "\t\t<td>计算机硬件系统综合设计</td><td>张厚武/姚凯学</td><td>2.0</td><td>01-04</td><td>&nbsp;</td><td>&nbsp;</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">实习课信息：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DBGridYxkc\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>学年</td><td>学期</td><td>课程名称</td><td>实习时间</td><td>模块代号</td><td>先修模块</td><td>实习编号</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">未安排上课时间的课程：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"Datagrid2\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>学年</td><td>学期</td><td>课程名称</td><td>教师姓名</td><td>学分</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>2016-2017</td><td>3</td><td>企业实践（移动平台开发应用）</td><td>张海宁</td><td>2.0</td>\n" +
            "\t</tr><tr class=\"alt\">\n" +
            "\t\t<td>2016-2017</td><td>3</td><td>计算机硬件系统综合设计</td><td>张厚武/姚凯学</td><td>2.0</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t</TABLE>\n" +
            "\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t<div class=\"footbox noprint\"><em class=\"footbox_con\"><span class=\"pagination\"></span>\n" +
            "\t\t\t\t\t\t\t<span class=\"footbutton\"></span>\n" +
            "\t\t\t\t\t\t\t<!-- 底部按钮位置 --></em></div>\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</form>\n" +
            "\t</BODY>\n" +
            "</HTML>\n";

    String html3 = "\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<HTML lang=\"gb2312\">\n" +
            "\t<HEAD>\n" +
            "\t\t<title>现代教学管理信息系统</title><meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\">\n" +
            "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
            "\t\t<meta http-equiv=\"Content-Language\" content=\"gb2312\">\n" +
            "\t\t<meta content=\"all\" name=\"robots\">\n" +
            "\t\t<meta content=\"作者信息\" name=\"author\">\n" +
            "\t\t<meta content=\"版权信息\" name=\"Copyright\">\n" +
            "\t\t<meta content=\"站点介绍\" name=\"description\">\n" +
            "\t\t<meta content=\"站点关键词\" name=\"keywords\">\n" +
            "\t\t<LINK href=\"style/base/favicon.ico\" type=\"image/x-icon\" rel=\"icon\">\n" +
            "\t\t\t<LINK media=\"all\" href=\"style/base/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "\t\t\t\t<LINK media=\"all\" href=\"style/standard/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "\t\t\t\t\t<!--<script defer>   \n" +
            "\t\tfunction  PutSettings()   \n" +
            "\t\t\t{     \n" +
            "\t\t\tfactory.printing.header=\"\";   \n" +
            "\t\t\tfactory.printing.footer=\"\";   \n" +
            "\t\t    factory.printing.leftMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.topMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.rightMargin=\"5\";   \n" +
            "\t\t\tfactory.printing.bottomMargin=\"5\";\n" +
            "\t\t }\n" +
            "\n" +
            "\t\t\t\t\t</script>-->\n" +
            "\t\t\t\t\t<style> @media Print { .bgnoprint { }\n" +
            "\t.noprint { DISPLAY: none }}\n" +
            "\t</style>\n" +
            "\t</HEAD>\n" +
            "\t<BODY>\n" +
            "\t\t<!--<OBJECT id=\"factory\" style=\"DISPLAY: none\" codeBase=\"ScriptX.cab#Version=5,60,0,360\" classid=\"clsid:1663ed61-23eb-11d2-b92f-008048fdd814\"\n" +
            "\t\t\tVIEWASTEXT>\n" +
            "\t\t</OBJECT>-->\n" +
            "\t\t<form name=\"xskb_form\" method=\"post\" action=\"xskbcx.aspx?xh=1500170110&amp;xm=%D1%EE%D3%D1%C1%D6&amp;gnmkdm=N121603\" id=\"xskb_form\">\n" +
            "<input type=\"hidden\" name=\"__EVENTTARGET\" value=\"\" />\n" +
            "<input type=\"hidden\" name=\"__EVENTARGUMENT\" value=\"\" />\n" +
            "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"dDwzOTI4ODU2MjU7dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47aTwyPjtpPDQ+O2k8Nz47aTw5PjtpPDExPjtpPDEzPjtpPDE1PjtpPDI0PjtpPDI2PjtpPDI4PjtpPDMwPjtpPDMyPjtpPDM0Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDwxMjAwNS0yMDAwNjI7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8eG47eG47Pj47Pjt0PGk8Mz47QDwyMDE3LTIwMTg7MjAxNi0yMDE3OzIwMTUtMjAxNjs+O0A8MjAxNy0yMDE4OzIwMTYtMjAxNzsyMDE1LTIwMTY7Pj47bDxpPDA+Oz4+Ozs+O3Q8dDw7O2w8aTwwPjs+Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjE1MDAxNzAxMTA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnk+WQje+8muadqOWPi+aelzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85a2m6Zmi77ya6K6h566X5py656eR5a2m5LiO5oqA5pyv5a2m6ZmiOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrvvJrorqHnrpfmnLrnp5HlrabkuI7mioDmnK87Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv+ePre+8muiuoeenkTE1Mjs+Pjs+Ozs+O3Q8O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwxPjtpPDE+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47bDxpPDA+Oz47bDx0PDtsPGk8MT47PjtsPHQ8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+O2k8NT47aTw2PjtpPDc+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPDIwMDY5MDQ5Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwyMDA2OTA0OTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85o2iMDAwODs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8KDIwMTctMjAxOC0xKS0wODA2MDQxMzFhLTIwMDY5MDQ5LTE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOaVsOaNruW6k+WOn+eQhjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85ZGoNOesrDHoioLov57nu60y6IqCe+esrDEtMTblkah9L+WNmuWtpualvDEyM0Qv5aec5a2j5pilOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlkag056ysMeiKgui/nue7rTLoioJ756ysMS0xNuWRqH0v5Y2a5a2m5qW8MTMwRC/lp5zlraPmmKU7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDIwMTctMDktMTQtMTAtMTg7Pj47Pjs7Pjs+Pjs+Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwzPjtpPDM+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47bDxpPDA+Oz47bDx0PDtsPGk8MT47aTwyPjtpPDM+Oz47bDx0PDtsPGk8MD47aTwxPjtpPDI+O2k8Mz47aTw0PjtpPDU+O2k8Nj47PjtsPHQ8cDxwPGw8VGV4dDs+O2w85pON5L2c57O757uf6K++56iL6K6+6K6hOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzpmYjmmZPmmI47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDEuMDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MDQtMTE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjs+Pjt0PDtsPGk8MD47aTwxPjtpPDI+O2k8Mz47aTw0PjtpPDU+O2k8Nj47PjtsPHQ8cDxwPGw8VGV4dDs+O2w86K6h566X5py6572R57uc6K++56iL6K6+6K6hOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlrZTlub/pu5Q7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDEuMDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MDQtMTE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjs+Pjt0PDtsPGk8MD47aTwxPjtpPDI+O2k8Mz47aTw0PjtpPDU+O2k8Nj47PjtsPHQ8cDxwPGw8VGV4dDs+O2w85pWw5o2u5bqT5Y6f55CG6K++56iL6K6+6K6hOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlp5zlraPmmKU7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDEuMDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MDQtMTE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjs+Pjs+Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8Mz47aTwzPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+O2w8aTwwPjs+O2w8dDw7bDxpPDE+O2k8Mj47aTwzPjs+O2w8dDw7bDxpPDA+O2k8MT47aTwyPjtpPDM+O2k8ND47PjtsPHQ8cDxwPGw8VGV4dDs+O2w8MjAxNy0yMDE4Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwxOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzorqHnrpfmnLrnvZHnu5zor77nqIvorr7orqE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtlOW5v+m7lDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MS4wOz4+Oz47Oz47Pj47dDw7bDxpPDA+O2k8MT47aTwyPjtpPDM+O2k8ND47PjtsPHQ8cDxwPGw8VGV4dDs+O2w8MjAxNy0yMDE4Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwxOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzmlbDmja7lupPljp/nkIbor77nqIvorr7orqE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnnOWto+aYpTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MS4wOz4+Oz47Oz47Pj47dDw7bDxpPDA+O2k8MT47aTwyPjtpPDM+O2k8ND47PjtsPHQ8cDxwPGw8VGV4dDs+O2w8MjAxNy0yMDE4Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwxOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzmk43kvZzns7vnu5/or77nqIvorr7orqE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOmZiOaZk+aYjjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MS4wOz4+Oz47Oz47Pj47Pj47Pj47Pj47Pj47Ppb028FTPb1uFwULCKrfDk05+Xx/\" />\n" +
            "\n" +
            "<script language=\"javascript\" type=\"text/javascript\">\n" +
            "<!--\n" +
            "\tfunction __doPostBack(eventTarget, eventArgument) {\n" +
            "\t\tvar theform;\n" +
            "\t\tif (window.navigator.appName.toLowerCase().indexOf(\"microsoft\") > -1) {\n" +
            "\t\t\ttheform = document.xskb_form;\n" +
            "\t\t}\n" +
            "\t\telse {\n" +
            "\t\t\ttheform = document.forms[\"xskb_form\"];\n" +
            "\t\t}\n" +
            "\t\ttheform.__EVENTTARGET.value = eventTarget.split(\"$\").join(\":\");\n" +
            "\t\ttheform.__EVENTARGUMENT.value = eventArgument;\n" +
            "\t\ttheform.submit();\n" +
            "\t}\n" +
            "// -->\n" +
            "</script>\n" +
            "\n" +
            "\t\t\t<!-- 多功能操作区 -->\n" +
            "\t\t\t<!-- 内容显示区开始 -->\n" +
            "\t\t\t<div class=\"main_box \">\n" +
            "\t\t\t\t<div class=\"mid_box\">\n" +
            "\t\t\t\t\t<div class=\"title noprint\">\n" +
            "\t\t\t\t\t\t<p>\n" +
            "\t\t\t\t\t\t\t<!-- 查询得到的数据量显示区域 --></p>\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<!-- From内容 --><span class=\"formbox\">\n" +
            "\t\t\t\t\t\t<TABLE class=\"formlist noprint\" id=\"Table2\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"center\"><select name=\"xnd\" onchange=\"__doPostBack('xnd','')\" language=\"javascript\" id=\"xnd\">\n" +
            "\t<option selected=\"selected\" value=\"2017-2018\">2017-2018</option>\n" +
            "\t<option value=\"2016-2017\">2016-2017</option>\n" +
            "\t<option value=\"2015-2016\">2015-2016</option>\n" +
            "\n" +
            "</select><span id=\"Label2\"><font size=\"4\">学年第</font></span><select name=\"xqd\" onchange=\"__doPostBack('xqd','')\" language=\"javascript\" id=\"xqd\">\n" +
            "\t<option selected=\"selected\" value=\"1\">1</option>\n" +
            "\t<option value=\"2\">2</option>\n" +
            "\t<option value=\"3\">3</option>\n" +
            "\n" +
            "</select><span id=\"Label1\"><font size=\"4\">学期学生个人课程表</font></span></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR class=\"trbg1\">\n" +
            "\t\t\t\t\t\t\t\t<TD><span id=\"Label5\">学号：1500170110</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label6\">姓名：杨友林</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label7\">学院：计算机科学与技术学院</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label8\">专业：计算机科学与技术</span>|\n" +
            "\t\t\t\t\t\t\t\t\t<span id=\"Label9\">行政班：计科152</span>\n" +
            "\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;<span id=\"labTS\"><font color=\"Red\"></font></span><span id=\"labTip\"><font color=\"Red\"></font></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name=\"btnPrint\" id=\"btnPrint\" type=\"button\" class=\"button\" style=\"DISPLAY:none\" onclick=\"window.print();\" value=\"打印课表\" />\n" +
            "\t\t\t\t\t\t\t\t</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t</TABLE>\n" +
            "\t\t\t\t\t\t<br>\n" +
            "\t\t\t\t\t\t<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">\n" +
            "\t<tr>\n" +
            "\t\t<td colspan=\"2\" rowspan=\"1\" width=\"2%\">时间</td><td align=\"Center\" width=\"14%\">星期一</td><td align=\"Center\" width=\"14%\">星期二</td><td align=\"Center\" width=\"14%\">星期三</td><td align=\"Center\" width=\"14%\">星期四</td><td align=\"Center\" width=\"14%\">星期五</td><td class=\"noprint\" align=\"Center\" width=\"14%\">星期六</td><td class=\"noprint\" align=\"Center\" width=\"14%\">星期日</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td colspan=\"2\">早晨</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">上午</td><td width=\"1%\">第1节</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">操作系统<br>周一第1,2节{第2-16周|双周}<br>陈晓明<br>博学楼123D</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">unix系统级软件开发<br>周二第1,2节{第1-16周}<br>王以松<br>博学楼123D</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">操作系统<br>周三第1,2节{第1-16周}<br>陈晓明<br>博学楼217D</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">数据库原理<br>周四第1,2节{第1-16周}<br>姜季春<br>博学楼130D<br><br><font color='red'>(换0008)</font></td><td align=\"Center\" rowspan=\"2\" width=\"7%\">算法设计与分析（双语）<br>周五第1,2节{第1-16周}<br>秦进<br>逸夫楼408D</td><td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td><td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第2节</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第3节</td><td align=\"Center\" rowspan=\"2\">数据库原理<br>周一第3,4节{第2-16周|双周}<br>姜季春<br>博学楼117D</td><td align=\"Center\" rowspan=\"2\">专业英语<br>周二第3,4节{第1-16周}<br>周婵<br>博学楼123D</td><td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实<br>周三第3,4节{第1-15周|单周}<br>黄初华<br>博学楼217D<br><br>计算机网络<br>周三第3,4节{第2-16周|双周}<br>孔广黔<br>博学楼217D</td><td align=\"Center\" rowspan=\"2\">unix系统级软件开发<br>周四第3,4节{第1-15周|单周}<br>王以松<br>博学楼123D</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第4节</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">下午</td><td>第5节</td><td align=\"Center\" rowspan=\"2\">计算机网络<br>周一第5,6节{第1-16周}<br>孔广黔<br>博学楼220D</td><td align=\"Center\">&nbsp;</td><td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实实验<br>周三第5,6节{第1-16周}<br>黄初华<br>外语楼225D</td><td align=\"Center\">&nbsp;</td><td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实<br>周五第5,6节{第1-16周}<br>黄初华<br>博学楼218D</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第6节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第7节</td><td align=\"Center\" rowspan=\"2\">服务端开发技术<br>周一第7,8节{第1-16周}<br>薛现斌<br>博学楼619</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\" rowspan=\"2\">服务端开发技术实验<br>周五第7,8节{第4-11周}<br>薛现斌<br>博学楼实验室709</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第8节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"3\" width=\"1%\">晚上</td><td>第9节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第10节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第11节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr>\n" +
            "</table>\n" +
            "\t\t\t\t\t\t<br>\n" +
            "\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<div class=\"noprint\" align=\"left\">调、停（补）课信息：</div>\n" +
            "\t\t\t\t\t\t<table class=\"datelist noprint\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DBGrid\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>编号</td><td>课程名称</td><td>原上课时间地点教师</td><td>现上课时间地点教师</td><td>申请时间</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>换0008</td><td>数据库原理</td><td>周4第1节连续2节{第1-16周}/博学楼123D/姜季春</td><td>周4第1节连续2节{第1-16周}/博学楼130D/姜季春</td><td>2017-09-14-10-18</td>\n" +
            "\t</tr>\n" +
            "</table>\n" +
            "\t\t\t\t\t\t<TABLE class=\"noprint\" id=\"Table3\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">实践课(或无上课时间)信息：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD valign=\"top\"><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DataGrid1\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>课程名称</td><td>教师</td><td>学分</td><td>起止周</td><td>上课时间</td><td>上课地点</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>操作系统课程设计</td><td>陈晓明</td><td>1.0</td><td>04-11</td><td>&nbsp;</td><td>&nbsp;</td>\n" +
            "\t</tr><tr class=\"alt\">\n" +
            "\t\t<td>计算机网络课程设计</td><td>孔广黔</td><td>1.0</td><td>04-11</td><td>&nbsp;</td><td>&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>数据库原理课程设计</td><td>姜季春</td><td>1.0</td><td>04-11</td><td>&nbsp;</td><td>&nbsp;</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">实习课信息：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DBGridYxkc\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>学年</td><td>学期</td><td>课程名称</td><td>实习时间</td><td>模块代号</td><td>先修模块</td><td>实习编号</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD align=\"left\">未安排上课时间的课程：</TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t\t<TR>\n" +
            "\t\t\t\t\t\t\t\t<TD><table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"Datagrid2\" width=\"100%\">\n" +
            "\t<tr class=\"datelisthead\">\n" +
            "\t\t<td>学年</td><td>学期</td><td>课程名称</td><td>教师姓名</td><td>学分</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>2017-2018</td><td>1</td><td>计算机网络课程设计</td><td>孔广黔</td><td>1.0</td>\n" +
            "\t</tr><tr class=\"alt\">\n" +
            "\t\t<td>2017-2018</td><td>1</td><td>数据库原理课程设计</td><td>姜季春</td><td>1.0</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>2017-2018</td><td>1</td><td>操作系统课程设计</td><td>陈晓明</td><td>1.0</td>\n" +
            "\t</tr>\n" +
            "</table></TD>\n" +
            "\t\t\t\t\t\t\t</TR>\n" +
            "\t\t\t\t\t\t</TABLE>\n" +
            "\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t<div class=\"footbox noprint\"><em class=\"footbox_con\"><span class=\"pagination\"></span>\n" +
            "\t\t\t\t\t\t\t<span class=\"footbutton\"></span>\n" +
            "\t\t\t\t\t\t\t<!-- 底部按钮位置 --></em></div>\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</form>\n" +
            "\t</BODY>\n" +
            "</HTML>\n";

    String html2 = "\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
            "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<HTML lang=\"gb2312\">\n" +
            "<HEAD>\n" +
            "    <title>现代教学管理信息系统</title>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\">\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"gb2312\">\n" +
            "    <meta content=\"all\" name=\"robots\">\n" +
            "    <meta content=\"作者信息\" name=\"author\">\n" +
            "    <meta content=\"版权信息\" name=\"Copyright\">\n" +
            "    <meta content=\"站点介绍\" name=\"description\">\n" +
            "    <meta content=\"站点关键词\" name=\"keywords\">\n" +
            "    <LINK href=\"style/base/favicon.ico\" type=\"image/x-icon\" rel=\"icon\">\n" +
            "    <LINK media=\"all\" href=\"style/base/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "    <LINK media=\"all\" href=\"style/standard/jw.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
            "\n" +
            "    <style>\n" +
            "        tr td {\n" +
            "            border: 1px solid brown;\n" +
            "        }\n" +
            "    </style>\n" +
            "</HEAD>\n" +
            "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">\n" +
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
            "        <td rowspan=\"4\" width=\"1%\">上午</td>\n" +
            "        <td width=\"1%\">第1节</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">操作系统<br>周一第1,2节{第2-16周|双周}<br>陈晓明<br>博学楼123D</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">unix系统级软件开发<br>周二第1,2节{第1-16周}<br>王以松<br>博学楼123D</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">操作系统<br>周三第1,2节{第1-16周}<br>陈晓明<br>博学楼217D</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">数据库原理<br>周四第1,2节{第1-16周}<br>姜季春<br>博学楼130D<br><br><font color='red'>(换0008)</font>\n" +
            "        </td>\n" +
            "        <td align=\"Center\" rowspan=\"2\" width=\"7%\">算法设计与分析（双语）<br>周五第1,2,3节{第1-16周}<br>秦进<br>逸夫楼408D</td>\n" +
            "        <td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第2节</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第3节</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">数据库原理<br>周一第3,4节{第2-16周|双周}<br>姜季春<br>博学楼117D</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">专业英语<br>周二第3,4节{第1-16周}<br>周婵<br>博学楼123D</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实<br>周三第3,4节{第1-15周|单周}<br>黄初华<br>博学楼217D<br><br>计算机网络<br>周三第3,4节{第2-16周|双周}<br>孔广黔<br>博学楼217D\n" +
            "        </td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">unix系统级软件开发<br>周四第3,4节{第1-15周|单周}<br>王以松<br>博学楼123D</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第4节</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td rowspan=\"4\" width=\"1%\">下午</td>\n" +
            "        <td>第5节</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">计算机网络<br>周一第5,6节{第1-16周}<br>孔广黔<br>博学楼220D</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实实验<br>周三第5,6节{第1-16周}<br>黄初华<br>外语楼225D</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">计算机图形学与虚拟现实<br>周五第5,6节{第1-16周}<br>黄初华<br>博学楼218D</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第6节</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第7节</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">服务端开发技术<br>周一第7,8节{第1-16周}<br>薛现斌<br>博学楼619</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\" rowspan=\"2\">服务端开发技术实验<br>周五第7,8节{第4-11周}<br>薛现斌<br>博学楼实验室709</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">移动平台开发课程设计<br>周日第3,4节{第1-2周}<br>涂孝颖<br>博学楼实验室804</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>第8节</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td rowspan=\"3\" width=\"1%\">晚上</td>\n" +
            "        <td>第9节</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
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
            "        <td>第11节</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "        <td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "</table>\n" +
            "<br>\n" +
            "\n" +
            "\n" +
            "<div class=\"noprint\" align=\"left\">调、停（补）课信息：</div>\n" +
            "<table class=\"datelist noprint\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DBGrid\" width=\"100%\">\n" +
            "    <tr class=\"datelisthead\">\n" +
            "        <td>编号</td>\n" +
            "        <td>课程名称</td>\n" +
            "        <td>原上课时间地点教师</td>\n" +
            "        <td>现上课时间地点教师</td>\n" +
            "        <td>申请时间</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td>换0008</td>\n" +
            "        <td>数据库原理</td>\n" +
            "        <td>周4第1节连续2节{第1-16周}/博学楼123D/姜季春</td>\n" +
            "        <td>周4第1节连续2节{第1-16周}/博学楼130D/姜季春</td>\n" +
            "        <td>2017-09-14-10-18</td>\n" +
            "    </tr>\n" +
            "</table>\n" +
            "<TABLE class=\"noprint\" id=\"Table3\" width=\"100%\">\n" +
            "    <TR>\n" +
            "        <TD align=\"left\">实践课(或无上课时间)信息：</TD>\n" +
            "    </TR>\n" +
            "    <TR>\n" +
            "        <TD valign=\"top\">\n" +
            "            <table class=\"datelist\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" id=\"DataGrid1\" width=\"100%\">\n" +
            "                <tr class=\"datelisthead\">\n" +
            "                    <td>课程名称</td>\n" +
            "                    <td>教师</td>\n" +
            "                    <td>学分</td>\n" +
            "                    <td>起止周</td>\n" +
            "                    <td>上课时间</td>\n" +
            "                    <td>上课地点</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>操作系统课程设计</td>\n" +
            "                    <td>陈晓明</td>\n" +
            "                    <td>1.0</td>\n" +
            "                    <td>04-11</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                </tr>\n" +
            "                <tr class=\"alt\">\n" +
            "                    <td>计算机网络课程设计</td>\n" +
            "                    <td>孔广黔</td>\n" +
            "                    <td>1.0</td>\n" +
            "                    <td>04-11</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>数据库原理课程设计</td>\n" +
            "                    <td>姜季春</td>\n" +
            "                    <td>1.0</td>\n" +
            "                    <td>04-11</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                    <td>&nbsp;</td>\n" +
            "                </tr>\n" +
            "            </table>";

    String html = "<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">\n" +
            "\t<tr>\n" +
            "\t\t<td colspan=\"2\" rowspan=\"1\" width=\"2%\">时间</td><td align=\"Center\" width=\"14%\">星期一</td><td align=\"Center\"\n" +
            "                                                                                                 width=\"14%\">星期二</td><td\n" +
            "            align=\"Center\" width=\"14%\">星期三</td><td align=\"Center\" width=\"14%\">星期四</td><td align=\"Center\"\n" +
            "                                                                                          width=\"14%\">星期五</td><td\n" +
            "            class=\"noprint\" align=\"Center\" width=\"14%\">星期六</td><td class=\"noprint\" align=\"Center\" width=\"14%\">星期日</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td colspan=\"2\">早晨</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">上午</td><td width=\"1%\">第1节</td><td align=\"Center\" rowspan=\"2\" width=\"7%\">高等数学1-1<br>周一第1,2节{第4-16周}<br>刘桂珍<br>新区北楼138D<br>2015年12月30日(14:30-16:30)<br>新区东楼101DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\" width=\"7%\">大学英语（一）<br>周二第1,2节{第5-16周}<br>孟江波<br>新区北楼310D<br>2015年12月28日(14:30-16:30)<br>新区东楼110DT</td><td\n" +
            "                                align=\"Center\" width=\"7%\">&nbsp;</td><td align=\"Center\" rowspan=\"2\"\n" +
            "                                                                         width=\"7%\">大学英语（一）<br>周四第1,2节{第5-16周}<br>孟江波<br>新区北楼310D<br>2015年12月28日(14:30-16:30)<br>新区东楼110DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\" width=\"7%\">高等数学1-1<br>周五第1,2节{第4-16周}<br>刘桂珍<br>新区北楼138D<br>2015年12月30日(14:30-16:30)<br>新区东楼101DT</td><td\n" +
            "                                class=\"noprint\" align=\"Center\" width=\"7%\">&nbsp;</td><td class=\"noprint\" align=\"Center\"\n" +
            "                                                                                         width=\"7%\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第2节</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\"\n" +
            "                                                                                                    align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第3节</td><td align=\"Center\" rowspan=\"2\">信息技术导论<br>周一第3,4节{第5-15周|单周}<br>陈梅/许华容<br>新区北楼138D</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">体育1<br>周二第3,4节{第4-11周}<br>张筑全<br></td><td align=\"Center\"\n" +
            "                                                                                                     rowspan=\"2\">大学生心理健康<br>周三第3,4节{第7-14周}<br>陈芳媛<br>新区北楼负102D</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">思想道德修养与法律基础<br>周四第3,4节{第4-16周|双周}<br>秦小娟<br>新区北楼115D<br>2015年12月29日(14:30-16:30)<br>新区东楼110DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">高级语言程序设计<br>周五第3,4节{第4-16周}<br>龙慧云<br>新区北楼112D<br>2016年1月5日(09:00-11:00)<br>新区西楼302DT</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第4节</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"4\" width=\"1%\">下午</td><td>第5节</td><td align=\"Center\" rowspan=\"2\">诗歌创作入门<br>{第6-13周|2节/周}<br>赵永刚<br>新区北楼138D</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">高级语言程序设计<br>周二第5,6节{第4-16周}<br>龙慧云<br>新区北楼105D<br>2016年1月5日(09:00-11:00)<br>新区西楼302DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">高等数学1-1<br>周三第5,6节{第4-16周|双周}<br>刘桂珍<br>新区北楼335D<br>2015年12月30日(14:30-16:30)<br>新区东楼101DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">高级语言程序设计实验<br>周四第5,6节{第7-14周}<br>龙慧云<br></td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第6节</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\"\n" +
            "                                                                                                    align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第7节</td><td align=\"Center\" rowspan=\"2\">思想道德修养与法律基础<br>周一第7,8节{第4-16周}<br>秦小娟<br>新区北楼115D<br>2015年12月29日(14:30-16:30)<br>新区东楼110DT</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">诗歌创作入门<br>{第6-13周|2节/周}<br>赵永刚<br>新区北楼214D</td><td\n" +
            "                                align=\"Center\" rowspan=\"2\">信息技术导论<br>周三第7,8节{第4-16周}<br>陈梅/许华容<br>新区北楼138D</td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\"\n" +
            "                                                                                            align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第8节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td rowspan=\"3\" width=\"1%\">晚上</td><td>第9节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td><td class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第10节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\"\n" +
            "                                                                                            align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr><tr>\n" +
            "\t\t<td>第11节</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td\n" +
            "                                align=\"Center\">&nbsp;</td><td align=\"Center\">&nbsp;</td><td class=\"noprint\"\n" +
            "                                                                                            align=\"Center\">&nbsp;</td><td\n" +
            "                                class=\"noprint\" align=\"Center\">&nbsp;</td>\n" +
            "\t</tr>\n" +
            "</table>";
}
