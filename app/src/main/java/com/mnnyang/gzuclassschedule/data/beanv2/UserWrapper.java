package com.mnnyang.gzuclassschedule.data.beanv2;

import java.io.Serializable;

public class UserWrapper extends BaseBean {

    /**
     * msg : 成功
     * code : 1
     * data : {"username":"fff","email":"sdf"}
     */
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        /**
         * email : sdf
         */

        private String email;

        public String getEmail() {
            return email;
        }

        public User setEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public String toString() {
            return "User{" +
                    "email='" + email + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return super.toString() + "UserWrapper{" +
                "data=" + data +
                '}';
    }
}
