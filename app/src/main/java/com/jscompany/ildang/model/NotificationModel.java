package com.jscompany.ildang.model;

public class NotificationModel extends  UserInfoModel{


    private long noti_seq;
    private String title;
    private String content;

    public long getNoti_seq() {
        return noti_seq;
    }

    public void setNoti_seq(long noti_seq) {
        this.noti_seq = noti_seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
