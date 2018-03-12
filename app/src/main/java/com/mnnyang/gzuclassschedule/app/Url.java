package com.mnnyang.gzuclassschedule.app;

/**
 * Created by mnnyang on 17-10-23.
 */

public class Url {

    /**
     * 贵州大学正方教务管理系统
     */
    public static final String URL_HOST = "http://210.40.2.253:8888/";

    public static final String URL_CHECK_UPDATE_APP = "https://raw.githubusercontent.com/mnnyang/GzuClassSchedule/master/check.json";

    public static final String URL_CHECK_CODE = URL_HOST + "CheckCode.aspx";
    public static final String URL_LOAD_COURSE = URL_HOST + "xskbcx.aspx";
    public static final String URL_LOGIN_PAGE = URL_HOST + "default2.aspx";
    public static final String PARAM_XH = "xh";

    public static final String PARAM_XND = "xnd";
    public static final String PARAM_XQD = "xqd";

    public static final String __VIEWSTATE = "__VIEWSTATE";

    public static String VIEWSTATE_POST_CODE = "";

    public static String VIEWSTATE_LOGIN_CODE = "";
}
