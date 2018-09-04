package com.jscompany.ildang.model;

public class CodeModel extends  UserInfoModel{

    private String job_type;
    private String search_type;
    private String cd_class;
    private String cd_type;
    private String cd_value;
    private int count;


    public String getJob_type() {
        return job_type;
    }
    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }
    public String getSearch_type() {
        return search_type;
    }
    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getCd_class() {
        return cd_class;
    }
    public void setCd_class(String cd_class) {
        this.cd_class = cd_class;
    }
    public String getCd_type() {
        return cd_type;
    }
    public void setCd_type(String cd_type) {
        this.cd_type = cd_type;
    }
    public String getCd_value() {
        return cd_value;
    }
    public void setCd_value(String cd_value) {
        this.cd_value = cd_value;
    }

}
