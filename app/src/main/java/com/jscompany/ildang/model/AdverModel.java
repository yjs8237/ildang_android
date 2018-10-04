package com.jscompany.ildang.model;

public class AdverModel {


    private long ad_seq;
    private String cell_no;
    private String type;
    private String type_str;
    private String com_name;
    private String contact_num;
    private String location;
    private String title;
    private String content;
    private String address;
    private String start_date;
    private String reg_date;
    private String end_date;
    private String ilgam_cell_no;
    private int adv_days;
    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType_str() {
        return type_str;
    }

    public void setType_str(String type_str) {
        this.type_str = type_str;
    }

    public int getAdv_days() {
        return adv_days;
    }

    public void setAdv_days(int adv_days) {
        this.adv_days = adv_days;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getCell_no() {
        return cell_no;
    }

    public void setCell_no(String cell_no) {
        this.cell_no = cell_no;
    }

    public long getAd_seq() {
        return ad_seq;
    }
    public void setAd_seq(long ad_seq) {
        this.ad_seq = ad_seq;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getContact_num() {
        return contact_num;
    }
    public void setContact_num(String contact_num) {
        this.contact_num = contact_num;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getCom_name() {
        return com_name;
    }
    public void setCom_name(String com_name) {
        this.com_name = com_name;
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getIlgam_cell_no() {
        return ilgam_cell_no;
    }
    public void setIlgam_cell_no(String ilgam_cell_no) {
        this.ilgam_cell_no = ilgam_cell_no;
    }



}
