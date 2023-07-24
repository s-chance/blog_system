package com.entropy.service.impl;

import com.entropy.dao.StatisticMapper;
import com.entropy.entity.Statistic;
import com.entropy.service.StatisticService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticMapper statisticMapper;
    //更新点击量  文章id对应的文章点击量+1
    @Override
    public void updateHits(Integer articleId) {

        Statistic statistic = statisticMapper.findByArticleId(articleId);
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateStatistic(statistic);

//        statisticMapper.updateHits(articleId);
    }
    //更新评论量  文章id对应的文章评论量+1
//    @Override
//    public void updateCommentNum(Integer articleId) {
//        statisticMapper.updateCommentNum(articleId);
//    }


}
