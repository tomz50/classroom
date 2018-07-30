package com.classroom.communicationbook;

public class CommunicationBook {
    private String id;
    private String classid;
    private String attendancedate;
    private String stuserid;
    private String name;
    private String homework;
    private String thcontact;
    private String prcontact;
    private String thtime;
    private String sttime;
    private String prtime;
    private String thuser;
    private String pruser;

    public CommunicationBook(String id, String classid, String attendancedate, String stuserid, String name, String homework, String thcontact, String prcontact, String thtime, String sttime, String prtime, String thuser, String pruser) {
        this.id = id;
        this.classid = classid;
        this.attendancedate = attendancedate;
        this.stuserid = stuserid;
        this.name = name;
        this.homework = homework;
        this.thcontact = thcontact;
        this.prcontact = prcontact;
        this.thtime = thtime;
        this.sttime = sttime;
        this.prtime = prtime;
        this.thuser = thuser;
        this.pruser = pruser;
    }

    public String getId() {
        return id;
    }

    public String getClassid() {
        return classid;
    }

    public String getAttendancedate() {
        return attendancedate;
    }

    public String getStuserid() {
        return stuserid;
    }

    public String getName() {
        return name;
    }

    public String getHomework() {
        return homework;
    }

    public String getThcontact() {
        return thcontact;
    }

    public String getPrcontact() {
        return prcontact;
    }

    public String getThtime() {
        return thtime;
    }

    public String getSttime() {
        return sttime;
    }

    public String getPrtime() {
        return prtime;
    }

    public String getThuser() {
        return thuser;
    }

    public String getPruser() {
        return pruser;
    }
}
