package com.entropy.service;

import com.entropy.entity.Article;
import com.entropy.entity.Statistic;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleService {
    //分页查询方法
    public PageInfo<Article> findArticleWithPage(Integer page, Integer count);

    //根据文章id查询数据
    public Article findById(Integer id);

    //获取排行榜数据
    public List<Article> getHeatData();

    //统计文章总数
    public Integer getArticlesNum();

    //查询最新文章
    public List<Article> getRecentArticles(Integer num);

    //发布新文章
    public void publishArticle(Article article);

    //修改文章  参数中有的数据修改,没有的数据不变
    public void updateArticle(Article article);

    //删除文章
    public void deleteArticle(Integer articleId);
}
