package com.example.jfxdemo.session;


public final class AssignmentSession {

    private static AssignmentSession instance;

    private int aid = -1;

    private AssignmentSession(int aid) {
        this.aid = aid;
    }
    public static void deleteInstance() {
        instance = null;
    }

    public static AssignmentSession getInstance(int aid) {
        if(instance == null) {
            instance = new AssignmentSession(aid);
        }
        return instance;
    }

    public static AssignmentSession getInstance() {
        return instance;
    }

    public static void setInstance(AssignmentSession instance) {
        AssignmentSession.instance = instance;
    }

    public int getaid() {
        return aid;
    }

    public void setaid(int aid) {
        this.aid = aid;
    }

    public static void cleanAssignmentSession()
    {
        instance.aid = -1;
    }

    @Override
    public String toString()
    {
        return "UserSession{" +
                "ID='" + aid +
                '}';
    }
}