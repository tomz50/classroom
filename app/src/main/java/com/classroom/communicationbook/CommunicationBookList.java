package com.classroom.communicationbook;

public class CommunicationBookList {
    private String id;
    private String stuserid;
    private String name;
    private String thtime;
    private String sttime;
    private String prtime;

    public CommunicationBookList(String id, String stuserid, String name, String thtime, String sttime, String prtime) {
        this.id = id;
        this.stuserid = stuserid;
        this.name = name;
        this.thtime = thtime;
        this.sttime = sttime;
        this.prtime = prtime;
    }

    public String getId() {
        return id;
    }

    public String getStuserid() {
        return stuserid;
    }

    public String getName() {
        return name;
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
}
