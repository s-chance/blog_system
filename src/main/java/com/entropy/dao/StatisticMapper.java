package com.entropy.dao;

import com.entropy.entity.Statistic;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface StatisticMapper {
    //查询点击量排名前十的数据
    @Select("select * from t_statistic order by hits desc limit 10")
    public List<Statistic> findHeatData();

    //更新统计Statistic对象
    public void updateStatistic(Statistic statistic);

//    //更新点击量  文章id对应的文章点击量+1
//    @Update("update t_statistic set hits = hits + 1 where article_id = #{articleId}")
//    public void updateHits(Integer articleId);
//
//    //更新评论量  文章id对应的文章评论量+1
//    @Update("update t_statistic set comments_num = comments_num + 1 where article_id = #{articleId}")
//    public void updateCommentNum(Integer articleId);
//
//    //根据文章id修改评论量 (删除评论)
//    @Update("update t_statistic set comments_num = comments_num - 1 where article_id = #{articleId}")
//    public void updateComment2(Integer articleId);

    //根据文章id查询statistic对象
    @Select("select * from t_statistic where article_id = #{articleId}")
    public Statistic findByArticleId(Integer articleId);

    //统计新文章  添加statistic对象
    @Insert("insert into t_statistic(article_id,hits,comments_num) "+
            "values(#{articleId},#{hits},#{commentsNum})")
    public void addStatistic(Statistic statistic);

    //根据文章id删除相关数据
    @Delete("delete from t_statistic where article_id = #{articleId}")
    public void deleteByArticleId(Integer articleId);

    //统计所有点击量
    @Select("select sum(hits) from t_statistic")
    public Integer getHitsNum();
}
