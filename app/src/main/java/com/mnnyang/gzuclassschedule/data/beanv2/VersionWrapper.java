package com.mnnyang.gzuclassschedule.data.beanv2;

/**
 * Created by mnnyang on 17-11-7.
 */

public class VersionWrapper {

    /**
     * msg : 成功
     * code : 1
     * data : {"code":3,"describe":"无说明","name":"2.2"}
     */

    private String msg;
    private int code;
    private Version data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Version getData() {
        return data;
    }

    public void setData(Version data) {
        this.data = data;
    }

    public static class Version {
        /**
         * code : 3
         * describe : 无说明
         * name : 2.2
         */

        private int code;
        private String describe;
        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
