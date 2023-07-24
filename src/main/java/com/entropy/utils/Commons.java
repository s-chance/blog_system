package com.entropy.utils;

import com.entropy.entity.Article;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Commons {
    //文章缩略图
    public static String show_thumb(Article article) {
        //获取缩略图
        String thumbnail = article.getThumbnail();
        if (thumbnail != null && thumbnail != "") {
            return thumbnail;
        } else {
            //随机显示一张图片
            return "/user/img/rand/"+article.getId()+".png";
        }
    }

    //把Date格式化为 yyyy-MM-dd
    public static String dateFormat(Date created) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(created);
        return format;
    }
    //文章详情页跳转路径
    public static String permalink(Integer articleId) {
        return "/articleDetail/"+articleId;
    }
    //文章摘要
    public static String intro(Article article, Integer length) {
        //获取文章内容
        String content = article.getContent();
        //判断文章长度是否足够
        if (content.length() > 75) {
            //截取指定的长度
            String substring = content.substring(0, length);
            return substring+"......";
        }
        //文章长度不够直接返回
        return content;
    }
    //显示文章内容
    public static String article(String content) {
        return content;
    }

    //回到前台首页
    public static String site_url() {
        return "/";
    }

    //跳转到指定详情页
    public static String site_url(String url) {
        return url;
    }
}
