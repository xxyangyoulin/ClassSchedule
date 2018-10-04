package com.mnnyang.gzuclassschedule.data.beanv2;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import com.mnnyang.gzuclassschedule.data.greendao.DaoSession;
import com.mnnyang.gzuclassschedule.data.greendao.CourseV2Dao;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;

@Entity
public class CourseGroup {
    @Id(autoincrement = true)
    private Long cgId;

    private String cgName;

    private String cgSchool;

    @ToMany(referencedJoinProperty = "couCgId")
    List<CourseV2> cgItems;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 652781247)
    private transient CourseGroupDao myDao;

    @Generated(hash = 1163735872)
    public CourseGroup(Long cgId, String cgName, String cgSchool) {
        this.cgId = cgId;
        this.cgName = cgName;
        this.cgSchool = cgSchool;
    }

    @Generated(hash = 1555955741)
    public CourseGroup() {
    }

    public Long getCgId() {
        return this.cgId;
    }

    public void setCgId(Long cgId) {
        this.cgId = cgId;
    }

    public String getCgName() {
        return this.cgName;
    }

    public void setCgName(String cgName) {
        this.cgName = cgName;
    }

    public String getCgSchool() {
        return this.cgSchool;
    }

    public void setCgSchool(String cgSchool) {
        this.cgSchool = cgSchool;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 136005675)
    public List<CourseV2> getCgItems() {
        if (cgItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseV2Dao targetDao = daoSession.getCourseV2Dao();
            List<CourseV2> cgItemsNew = targetDao._queryCourseGroup_CgItems(cgId);
            synchronized (this) {
                if (cgItems == null) {
                    cgItems = cgItemsNew;
                }
            }
        }
        return cgItems;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 864002162)
    public synchronized void resetCgItems() {
        cgItems = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 182780495)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCourseGroupDao() : null;
    }
}
