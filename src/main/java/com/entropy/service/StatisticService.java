package com.entropy.service;


import org.apache.ibatis.annotations.Insert;

public interface StatisticService {
    //更新点击量  文章id对应的文章点击量+1
    public void updateHits(Integer articleId);

    //更新评论量  文章id对应的文章评论量+1
//    public void updateCommentNum(Integer articleId);

}
