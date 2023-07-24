package com.entropy.service.impl;

import com.entropy.dao.ArticleMapper;
import com.entropy.dao.CommentMapper;
import com.entropy.dao.StatisticMapper;
import com.entropy.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailUtils emailUtils;
    //定时发送邮件到QQ邮箱
    @Scheduled(cron = "0 0 12 * * *") //定时器 cron表达式 "秒 分 时 日 月 年"
    //"0 0 12 * * *"  每年每月每日12时整触发定时任务
    //"0 0 0/1 * * *"  每隔1小时触发  ','枚举  '-'范围  '/'每隔  '*'任意
    public void sendTemplateEmail() throws MessagingException {
        String to = "";
        String subject = "这是一个测试邮件";
        //采用模板页面  处理模板页面
        //文章总数量:[[${articlesNum}]]
        //总评论量:[[${commentsNum}]]
        //总点击量:[[${hitsNum}]]
        Integer articlesNum = articleMapper.getArticlesNum();
        Integer commentsNum = commentMapper.getCommentsNum();
        Integer hitsNum = statisticMapper.getHitsNum();
        //创建Context对象
        Context context = new Context();
        context.setVariable("articlesNum", articlesNum);
        context.setVariable("commentsNum", commentsNum);
        context.setVariable("hitsNum", hitsNum);
        //处理模板页面
        String content = templateEngine.process("comm/email", context);

        //测试发送邮件
        emailUtils.sendEmail(to, subject, content);
    }
}
