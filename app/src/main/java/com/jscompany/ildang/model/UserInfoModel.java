package com.jscompany.ildang.model;

public class UserInfoModel extends  BaseModel{

    private long user_seq;
    private String cell_no;
    private String user_pwd;
    private String user_type;
    private String user_name;
    private String user_nick;
    private String com_name;
    private String address;
    private String user_bir_year;
    private String user_bir_month;
    private String user_bir_day;
    private long user_point;
    private String token;
    private String user_able_job;
    private String reg_date;
    private String push_yn;
    private String noti_sound;

    public String getNoti_sound() {
        return noti_sound;
    }

    public void setNoti_sound(String noti_sound) {
        this.noti_sound = noti_sound;
    }

    public String getPush_yn() {
        return push_yn;
    }

    public void setPush_yn(String push_yn) {
        this.push_yn = push_yn;
    }

    public String getUser_able_job() {
        return user_able_job;
    }

    public void setUser_able_job(String user_able_job) {
        this.user_able_job = user_able_job;
    }

    public long getUser_seq() {
        return user_seq;
    }

    public void setUser_seq(long user_seq) {
        this.user_seq = user_seq;
    }

    public String getCell_no() {
        return cell_no;
    }

    public void setCell_no(String cell_no) {
        this.cell_no = cell_no;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_bir_year() {
        return user_bir_year;
    }

    public void setUser_bir_year(String user_bir_year) {
        this.user_bir_year = user_bir_year;
    }

    public String getUser_bir_month() {
        return user_bir_month;
    }

    public void setUser_bir_month(String user_bir_month) {
        this.user_bir_month = user_bir_month;
    }

    public String getUser_bir_day() {
        return user_bir_day;
    }

    public void setUser_bir_day(String user_bir_day) {
        this.user_bir_day = user_bir_day;
    }

    public long getUser_point() {
        return user_point;
    }

    public void setUser_point(long user_point) {
        this.user_point = user_point;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
