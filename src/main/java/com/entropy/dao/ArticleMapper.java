package com.entropy.dao;

import com.entropy.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    //查询所有文章
    @Select("select a.*,b.hits,b.comments_num " +
            "from t_article a,t_statistic b " +
            "where a.id = b.article_id " +
            "order by id desc")
    public List<Article> findAll();

    //根据文章id查询数据
    @Select("select * from t_article where id = #{id}")
    public Article findById(Integer id);

    //统计文章总数
    @Select("select count(*) from t_article")
    public Integer getArticlesNum();

    //查询最新文章
    @Select("select a.*,b.hits,b.comments_num " +
            "from t_article a,t_statistic b " +
            "where a.id = b.article_id " +
            "order by id desc limit #{num}")
    public List<Article> getRecentArticles(Integer num);

    //添加文章
    @Insert("insert into t_article (title,content,created,modified,categories,tags,allow_comment,thumbnail) "+
            "values(#{title},#{content},#{created},#{modified},#{categories},#{tags},#{allowComment},#{thumbnail})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") //绑定数据库自动生成的主键值
    void addArticle(Article article);

    //获取排行榜数据
    @Select("select a.*,b.hits,b.comments_num " +
            "from t_article a,t_statistic b " +
            "where a.id = b.article_id " +
            "order by hits desc limit 10")
    public List<Article> getHeatData();

    //修改文章  参数中有的数据修改,没有的数据不变
    public void updateArticle(Article article);

    //删除文章
    @Delete("delete from t_article where id = #{articleId}")
    public void deleteArticle(Integer articleId);
}
