package com.entropy.service.impl;

import com.entropy.dao.ArticleMapper;
import com.entropy.dao.CommentMapper;
import com.entropy.dao.StatisticMapper;
import com.entropy.entity.Article;
import com.entropy.entity.Statistic;
import com.entropy.service.ArticleService;
import com.entropy.service.StatisticService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    //分页查询
    @Override
    public PageInfo<Article> findArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articles = articleMapper.findAll();
        //封装点击量
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        return pageInfo;
    }

    //根据文章id查询数据
    @Override
    public Article findById(Integer id) {
        //先从redis缓存中查询
        Article article = (Article) redisTemplate.opsForValue().get("article_" + id);
        //判断是否有数据
        if (article == null) {
            //如果没有数据,再从数据库查询
            article  = articleMapper.findById(id);
            //缓存查询结果
            redisTemplate.opsForValue().set("article_"+id, article);
        }

        return article;
    }

    //获取排行榜数据
    @Override
    public List<Article> getHeatData() {

        List<Article> heatData = articleMapper.getHeatData();
        return heatData;
//        //文章id 文章点击量 文章评论量
//        List<Statistic> statistics = statisticMapper.findHeatData();
//        //创建集合,保存排行榜数据
//        ArrayList<Article> articleList = new ArrayList();
//        //遍历
//        for (Statistic statistic : statistics) {
//            //获取文章id
//            Integer articleId = statistic.getArticleId();
//            //通过文章id获取文章
//            Article article = articleMapper.findById(articleId);
//            //获取文章点击量
//            Integer hits = statistic.getHits();
//            //获取文章评论量
//            Integer commentsNum = statistic.getCommentsNum();
//
//            //封装评论量和点击量到Article中
//            article.setHits(hits);
//            article.setCommentsNum(commentsNum);
//
//            //保存到articleList集合中
//            articleList.add(article);
//        }
//        return articleList;
    }

    //统计文章总数
    @Override
    public Integer getArticlesNum() {
        return articleMapper.getArticlesNum();
    }

    //获取后台首页最新文章
    @Override
    public List<Article> getRecentArticles(Integer num) {
        //遍历文章集合,查询文章的点击量,封装到Article
        List<Article> recentArticles = articleMapper.getRecentArticles(num);
//        for (Article article : recentArticles) {
//            //查询文章点击量
//            Statistic statistic = statisticMapper.findByArticleId(article.getId());
//            Integer hits = statistic.getHits();
//            Integer commentsNum = statistic.getCommentsNum();
//            //封装到Article
//            article.setHits(hits);
//            article.setCommentsNum(commentsNum);
//        }
        return recentArticles;
    }

    //发布新文章
    @Override
    public void publishArticle(Article article) {
        //添加新文章到t_article表格
        //数据初始化
        article.setCreated(new Date());
        article.setCategories("默认分类");
        article.setTags("默认标签");
        article.setHits(0);
        article.setCommentsNum(0);
        articleMapper.addArticle(article);
        //添加到t_statistic表格
        Statistic statistic = new Statistic();
        statistic.setArticleId(article.getId());
        statistic.setHits(0);
        statistic.setCommentsNum(0);
        statisticMapper.addStatistic(statistic);
    }
    //修改文章  参数中有的数据修改,没有的数据不变
    @Override
    public void updateArticle(Article article) {
        //修改modified字段
        article.setModified(new Date());
        //修改t_article表格数据
        articleMapper.updateArticle(article);
        //直接删除redis缓存中对应的文章数据
        redisTemplate.delete("article_"+article.getId());
    }
    //删除文章
    @Override
    public void deleteArticle(Integer articleId) {
        //删除t_article表格数据
        articleMapper.deleteArticle(articleId);
        //删除文章相关的评论 t_comment表格数据
        commentMapper.deleteByArticleId(articleId);
        //删除文章统计数据 t_statistic表格数据
        statisticMapper.deleteByArticleId(articleId);
        //删除文章对应的redis缓存数据
        redisTemplate.delete("article_"+articleId);
    }
}
