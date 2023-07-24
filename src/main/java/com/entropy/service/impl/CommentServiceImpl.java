package com.entropy.service.impl;

import com.entropy.dao.CommentMapper;
import com.entropy.dao.StatisticMapper;
import com.entropy.entity.Article;
import com.entropy.entity.Comment;
import com.entropy.entity.Statistic;
import com.entropy.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    //根据文章id查询评论数据:分页
    @Override
    public PageInfo<Comment> findCommentsByArticleId(Integer articleId, Integer commentPageNum, Integer count) {
        commentPageNum = commentPageNum == null?1:commentPageNum;

        PageHelper.startPage(commentPageNum, count);
        List<Comment> comments = commentMapper.findCommentsByArticleId(articleId);
        PageInfo<Comment> commentPageInfo = new PageInfo<>(comments);
        return commentPageInfo;
    }

    //发布评论
    @Override
    public void publishComment(Comment comment) {
        //添加评论
        commentMapper.addComment(comment);
        //根据文章id更新文章评论量
        Integer articleId = comment.getArticleId();
        Statistic statistic = statisticMapper.findByArticleId(articleId);
        statistic.setCommentsNum(statistic.getCommentsNum()+1); //评论量+1
        statisticMapper.updateStatistic(statistic);

//        statisticMapper.updateCommentNum(articleId);
    }

    //统计评论总数
    @Override
    public Integer getCommentsNum() {
        return commentMapper.getCommentsNum();
    }

    //获取最新留言
    @Override
    public List<Comment> getRecentComments(Integer num) {
        return commentMapper.getRecentComments(num);
    }

    //查询所有评论  分页
    @Override
    public PageInfo<Comment> findWithPage(Integer pageNum, Integer count) {
        PageHelper.startPage(pageNum, count);
        List<Comment> all = commentMapper.findAll();
        PageInfo<Comment> commentPageInfo = new PageInfo<>(all);
        return commentPageInfo;
    }

    //根据评论id删除评论
    @Override
    public void deleteById(Integer id) {

        //修改t_statistic评论量数据
        //根据评论id获取文章id
        Comment comment = commentMapper.findById(id);
        Integer articleId = comment.getArticleId();
        //根据文章id修改评论量
        Statistic statistic = statisticMapper.findByArticleId(articleId);
        statistic.setCommentsNum(statistic.getCommentsNum()-1);
        statisticMapper.updateStatistic(statistic);

//        statisticMapper.updateComment2(articleId);

        //删除t_comment表格数据
        commentMapper.deleteById(id);

    }
}
