package com.jscompany.ildang.model;

public class GuideModel {

    private long guide_seq;
    private String type;
    private String title;
    private String content;

    public long getGuide_seq() {
        return guide_seq;
    }

    public void setGuide_seq(long guide_seq) {
        this.guide_seq = guide_seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
