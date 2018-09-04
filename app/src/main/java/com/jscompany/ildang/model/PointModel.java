package com.jscompany.ildang.model;

public class PointModel extends  UserInfoModel{

    private long point_seq;
    private String type;
    private String acc_name;
    private int money;
    private String acc_no;
    private String finish_yn;
    private String bank_name;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public long getPoint_seq() {
        return point_seq;
    }

    public void setPoint_seq(long point_seq) {
        this.point_seq = point_seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getFinish_yn() {
        return finish_yn;
    }

    public void setFinish_yn(String finish_yn) {
        this.finish_yn = finish_yn;
    }
}
