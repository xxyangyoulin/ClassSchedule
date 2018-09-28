package com.mnnyang.gzuclassschedule.app;

public class Cache {
    private String email;

    private Cache() {
    }

    private static final class Holder {
        private static final Cache instance = new Cache();
    }


    public static Cache instance() {
        return Holder.instance;
    }


    public String getEmail() {
        return email;
    }

    public Cache setEmail(String email) {
        this.email = email;
        return this;
    }
}
