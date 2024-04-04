package com.example.jfxdemo.grade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Grade {

    private int grade;
    private int cID;
    private int aID;
    private String date;
    static ArrayList<Grade> CgradeArray = new ArrayList<Grade>();
    static ArrayList<Grade> AgradeArray = new ArrayList<Grade>();

    public Grade(int grade, int cID, int aID, String date) {
        this.grade = grade;
        this.cID = cID;
        this.aID = aID;
        this.date = date;
    }

    public static void addCGrade(int grade, int cID, int aID, String date)
    {
        CgradeArray.add(new Grade(grade,cID,aID,date));
    }

    public static ArrayList<Grade> getCGradeArray()
    {
        return CgradeArray;
    }

    public static void clearCGrades()
    {
        CgradeArray.clear();
    }

    public static int getCLength()
    {
        return CgradeArray.size();
    }

    public static int getSpecificCGrade(int pos)
    {
        return CgradeArray.get(pos).grade;
    }

    public static String getSpecificCDate(int pos)
    {
        return (CgradeArray.get(pos).date + " ID: " + CgradeArray.get(pos).cID);
    }

    public static Comparator<Grade> sortByDate = new Comparator<Grade>() {
        @Override
        public int compare(Grade o1, Grade o2) {
            return o1.date.compareTo(o2.date);
        }
    };
    public static void sortCGrade() {
        Collections.sort(CgradeArray, Grade.sortByDate);
    }

    public static double findAvaregeCGrade(){
        double ava = 0;
        for (int i = 0; i < getCLength(); i++){
            ava += getSpecificCGrade(i);
        }
        return ava/getCLength();
    }

    public static void addAGrade(int grade, int cID, int aID, String date)
    {
        AgradeArray.add(new Grade(grade,cID,aID,date));
    }

    public static ArrayList<Grade> getAGradeArray()
    {
        return AgradeArray;
    }

    public static void clearAGrades()
    {
        AgradeArray.clear();
    }

    public static int getALength()
    {
        return AgradeArray.size();
    }

    public static int getSpecificAGrade(int pos)
    {
        return AgradeArray.get(pos).grade;
    }

    public static String getSpecificADate(int pos)
    {
        return (AgradeArray.get(pos).date + " ID: " + AgradeArray.get(pos).aID);
    }

    public static void sortAGrade() {
        Collections.sort(AgradeArray, Grade.sortByDate);
    }
}
