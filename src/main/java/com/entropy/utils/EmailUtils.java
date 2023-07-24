package com.entropy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Component
public class EmailUtils {
    //邮件api
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String form;
    //发送邮件 收件人  邮件主题  邮件内容
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //使用工具类初始化邮件对象
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(form); //设置邮件发送者(固定)
        helper.setTo(to); //收件人
        helper.setSubject(subject); //邮件主题
        helper.setText(content); //邮件内容
        javaMailSender.send(mimeMessage);
    }
}
