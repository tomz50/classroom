package com.classroom.attendancerecord;

public class AttendanceInsertFeeback {
    String name;
    String attendancetime;

    public AttendanceInsertFeeback(String name, String attendancetime) {
        this.name = name;
        this.attendancetime = attendancetime;
    }

    public String getName() {
        return name;
    }

    public String getAttendancetime() {
        return attendancetime;
    }
}
