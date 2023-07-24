package com.entropy.dao;

import com.entropy.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    //根据文章id查询评论
    @Select("select * from t_comment where article_id = #{articleId} order by id desc")
    public List<Comment> findCommentsByArticleId(Integer articleId);

    //添加评论
    @Insert("insert into t_comment(article_id,created,ip,content,status,author) " +
            "values(#{articleId},#{created},#{ip},#{content},#{status},#{author})")
    public void addComment(Comment comment);

    //统计评论总数
    @Select("select count(*) from t_comment")
    public Integer getCommentsNum();

    //获取最新留言
    @Select("select * from t_comment order by id desc limit #{num}")
    public List<Comment> getRecentComments(Integer num);

    //根据文章id删除相关评论
    @Delete("delete from t_comment where article_id = #{articleId}")
    public void deleteByArticleId(Integer articleId);

    //查询所有评论
    @Select("select * from t_comment order by created desc")
    public List<Comment> findAll();

    //根据评论id删除评论
    @Delete("delete from t_comment where id=#{id}")
    public void deleteById(Integer id);

    //根据评论id查询
    @Select("select * from t_comment where id=#{id}")
    public Comment findById(Integer id);
}
