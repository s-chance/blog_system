package com.entropy.service.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.entropy.dao.ArticleMapper;
import com.entropy.dao.CommentMapper;
import com.entropy.dao.StatisticMapper;
import com.entropy.utils.EmailUtils;
import com.entropy.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Random;

@Service
public class SmsServiceImpl {

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

    public String sendSms(String tel) throws Exception {
        //验证码
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(10);
            code += num;
        }
        System.out.println("验证码:" + code);

        String to = tel;
        String subject = "这是一个验证码测试邮件";
        //采用模板页面  处理模板页面

        //创建Context对象
        Context context = new Context();

        context.setVariable("Sms", code);
        //处理模板页面
        String content = templateEngine.process("comm/sms", context);

        //测试发送邮件
        emailUtils.sendEmail(to, subject, content);

        //保存当前验证码,在登录时验证
        return code;
    }
}
