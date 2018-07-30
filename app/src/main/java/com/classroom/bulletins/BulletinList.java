package com.classroom.bulletins;

//import android.graphics.Bitmap;


public class BulletinList {
//    private Bitmap pic;
    private String id;
    private String subject;
    private String implementationdate;
    private String effectivedate;

    public BulletinList(String id, String subject, String implementationdate, String effectivedate) {
        //this.pic = pic;
        this.id = id;
        this.subject = subject;
        this.implementationdate = implementationdate;
        this.effectivedate = effectivedate;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getImplementationdate() {
        return implementationdate;
    }

    public String getEffectivedate() {
        return effectivedate;
    }
}

