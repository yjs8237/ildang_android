package com.jscompany.ildang.model;

public class QnaModel extends  UserInfoModel{

    private long qna_seq;
    private String title;
    private String content;
    private String reply;
    private String reply_date;
    private String finish_yn;

    public long getQna_seq() {
        return qna_seq;
    }

    public void setQna_seq(long qna_seq) {
        this.qna_seq = qna_seq;
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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }

    public String getFinish_yn() {
        return finish_yn;
    }

    public void setFinish_yn(String finish_yn) {
        this.finish_yn = finish_yn;
    }
}
