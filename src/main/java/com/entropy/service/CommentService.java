package com.entropy.service;

import com.entropy.entity.Comment;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentService {
    //根据文章id查询评论数据
    public PageInfo<Comment> findCommentsByArticleId(Integer articleId, Integer commentPageNum, Integer count);

    //添加评论
    public void publishComment(Comment comment);

    //统计评论总数
    public Integer getCommentsNum();

    //获取最新留言
    public List<Comment> getRecentComments(Integer num);

    //查询所有评论  分页
    public PageInfo<Comment> findWithPage(Integer pageNum, Integer count);

    //根据评论id删除评论
    public void deleteById(Integer id);
}
