package com.classroom.returnonattendance;

//import android.graphics.Bitmap;

public class AttendanceList {
    private String id;
    private String userid;
    private String name;
    private String attendancetime_i;
    private String attendancetime_o;
    private String trtime;
    private String attendancedate;
    private String reason;

    public AttendanceList(String id, String userid, String name, String attendancetime_i, String attendancetime_o, String trtime, String attendancedate, String reason) {
        this.id = id;
        this.userid = userid;
        this.name = name;
        this.attendancetime_i = attendancetime_i;
        this.attendancetime_o = attendancetime_o;
        this.trtime = trtime;
        this.attendancedate = attendancedate;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getAttendancetime_i() {
        return attendancetime_i;
    }

    public String getAttendancetime_o() {
        return attendancetime_o;
    }

    public String getTrtime() {
        return trtime;
    }

    public String getAttendancedate() {
        return attendancedate;
    }

    public String getReason() {
        return reason;
    }
}
