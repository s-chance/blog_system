package com.entropy.entity;

import java.util.Date;

public class Comment {
    //评论id
    private Integer id;
    //文章id
    private Integer articleId;
    //评论时间
    private Date created;
    //评论人的ip
    private String ip;
    //评论内容
    private String content;
    //评论状态
    private String status;
    //评论人
    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", created=" + created +
                ", ip='" + ip + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
