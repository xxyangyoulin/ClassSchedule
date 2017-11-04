package com.mnnyang.gzuclassschedule.data.bean;

/**
 * Created by mnnyang on 17-11-4.
 */

public class CsName {
    private int csNameId;
    private String name;

    public int getCsNameId() {
        return csNameId;
    }

    public CsName setCsNameId(int csNameId) {
        this.csNameId = csNameId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CsName setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass() == CsName.class
                && csNameId == ((CsName) obj).getCsNameId();
    }

    @Override
    public String toString() {
        return "CsName{" +
                "csNameId=" + csNameId +
                ", name='" + name + '\'' +
                '}';
    }
}
