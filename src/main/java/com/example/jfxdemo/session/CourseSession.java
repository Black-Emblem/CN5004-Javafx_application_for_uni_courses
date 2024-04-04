package com.example.jfxdemo.session;


public final class CourseSession {

    private static CourseSession instance;

    private int cid = -1;

    private CourseSession(int cid) {
        this.cid = cid;
    }

    public static CourseSession getInstance(int cid) {
        if(instance == null) {
            instance = new CourseSession(cid);
        }
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public static CourseSession getInstance() {
        return instance;
    }

    public static void setInstance(CourseSession instance) {
        CourseSession.instance = instance;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public static void cleanCourseSession()
    {
        instance.cid = -1;
    }

    @Override
    public String toString()
    {
        return "UserSession{" +
                "ID='" + cid +
                '}';
    }

    public boolean isSet() {
        if (instance.getCid() != -1){
            return true;
        }
        else {
            return false;
        }
    }
}
