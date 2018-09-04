package com.jscompany.ildang.model;

public class IldangModel extends UserInfoModel{

    private String search_type;
    private long job_seq;
    private String job_type;
    private String job_type_str;
    private String loc_gu;
    private String loc_gu_str;
    private String loc_dong;
    private String loc_dong_str;
    private String finish_yn;
    private String work_date;
    private String order_cell_no;
    private String remain_time;
    private int work_pay;


    public String getRemain_time() {
        return remain_time;
    }

    public void setRemain_time(String remain_time) {
        this.remain_time = remain_time;
    }

    public int getWork_pay() {
        return work_pay;
    }

    public void setWork_pay(int work_pay) {
        this.work_pay = work_pay;
    }

    public String getJob_type_str() {
        return job_type_str;
    }

    public void setJob_type_str(String job_type_str) {
        this.job_type_str = job_type_str;
    }

    private int ildang_count;

    public String getOrder_cell_no() {
        return order_cell_no;
    }

    public void setOrder_cell_no(String order_cell_no) {
        this.order_cell_no = order_cell_no;
    }

    public String getLoc_gu_str() {
        return loc_gu_str;
    }

    public void setLoc_gu_str(String loc_gu_str) {
        this.loc_gu_str = loc_gu_str;
    }

    public String getLoc_dong_str() {
        return loc_dong_str;
    }

    public void setLoc_dong_str(String loc_dong_str) {
        this.loc_dong_str = loc_dong_str;
    }

    public String getSearch_type() {
        return search_type;
    }
    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }
    public int getIldang_count() {
        return ildang_count;
    }
    public void setIldang_count(int ildang_count) {
        this.ildang_count = ildang_count;
    }
    public String getWork_date() {
        return work_date;
    }
    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }
    public long getJob_seq() {
        return job_seq;
    }
    public void setJob_seq(long job_seq) {
        this.job_seq = job_seq;
    }
    public String getJob_type() {
        return job_type;
    }
    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }
    public String getLoc_gu() {
        return loc_gu;
    }
    public void setLoc_gu(String loc_gu) {
        this.loc_gu = loc_gu;
    }
    public String getLoc_dong() {
        return loc_dong;
    }
    public void setLoc_dong(String loc_dong) {
        this.loc_dong = loc_dong;
    }
    public String getFinish_yn() {
        return finish_yn;
    }
    public void setFinish_yn(String finish_yn) {
        this.finish_yn = finish_yn;
    }


}
