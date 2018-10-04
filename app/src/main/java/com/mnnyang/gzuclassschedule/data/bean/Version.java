package com.mnnyang.gzuclassschedule.data.bean;

/**
 * Created by mnnyang on 17-11-7.
 */

public class Version {

    /**
     * version : 1
     * code : 1.0
     * msg : update info
     */

    private int version;
    private String code;
    private String msg;

    public int getVersion() {
        return version;
    }

    public Version setVersion(int version) {
        this.version = version;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Version setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Version setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
