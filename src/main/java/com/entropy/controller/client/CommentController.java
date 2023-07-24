package com.entropy.controller.client;

import com.entropy.entity.Comment;
import com.entropy.entity.ResponseData;
import com.entropy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @RequestMapping("/comments/publish")
    @ResponseBody
    public ResponseData publish(HttpServletRequest request, @RequestParam("aid")Integer aId, @RequestParam("text")String content) {  //文章id和评论内容
        //发表评论:
        //创建评论对象
        Comment comment = new Comment();
        comment.setArticleId(aId); //文章id
        comment.setCreated(new Date()); //评论时间
        String ip = request.getRemoteAddr();
        comment.setIp(ip); //设置评论人ip
        comment.setContent(content); //评论内容
        comment.setStatus("approved"); //默认是approved
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setAuthor(user.getUsername()); //评论人用户名:当前登录用户
        commentService.publishComment(comment);
        return ResponseData.OK();
    }
}
