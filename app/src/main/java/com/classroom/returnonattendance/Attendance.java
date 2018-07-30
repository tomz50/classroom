package com.classroom.returnonattendance;

public class Attendance {
    private String id;
    private String userid;
    private String name;
    private String attendanceid;
    private String attendancedate;
    private String attendancetime;
    private String truserid;
    private String trtime;
    private String parentreply;
    private String reason;

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getAttendanceid() {
        return attendanceid;
    }

    public String getAttendancedate() {
        return attendancedate;
    }

    public String getAttendancetime() {
        return attendancetime;
    }

    public String getTruserid() {
        return truserid;
    }

    public String getTrtime() {
        return trtime;
    }

    public String getParentreply() {
        return parentreply;
    }

    public String getReason() {
        return reason;
    }

    public Attendance(String id, String userid, String name, String attendanceid, String attendancedate, String attendancetime, String truserid, String trtime, String parentreply, String reason) {
        this.id = id;
        this.userid = userid;
        this.name = name;
        this.attendanceid = attendanceid;
        this.attendancedate = attendancedate;
        this.attendancetime = attendancetime;
        this.truserid = truserid;
        this.trtime = trtime;
        this.parentreply = parentreply;
        this.reason = reason;

    }
}
