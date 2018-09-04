package com.jscompany.ildang.listview;

public class NotiListItem {

    private String title;
    private String content;
    private String reg_date;
    private String view_count;
    private String noti_seq;


    public String getNoti_seq() {
        return noti_seq;
    }

    public void setNoti_seq(String noti_seq) {
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

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }
}
