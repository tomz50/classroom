package com.classroom.home;

public class Users {
    private String groupid;
    private String userid;
    private String name;

    public Users(String groupid, String userid, String name) {
        this.groupid = groupid;
        this.userid = userid;
        this.name = name;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

}
