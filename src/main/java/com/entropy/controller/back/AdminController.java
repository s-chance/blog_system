package com.entropy.controller.back;

import com.entropy.entity.Article;
import com.entropy.entity.Comment;
import com.entropy.entity.ResponseData;
import com.entropy.service.ArticleService;
import com.entropy.service.CommentService;
import com.entropy.utils.Commons;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    //跳转到后台首页
    @RequestMapping(value = {"", "/index"})
    public String admin(HttpServletRequest request) {
        //数据准备
        //文章总数
        Integer articlesNum = articleService.getArticlesNum();
        //评论总数
        Integer commentsNum = commentService.getCommentsNum();
        //最新文章
        List<Article> recentArticles = articleService.getRecentArticles(5);
        //最新评论
        List<Comment> recentComments = commentService.getRecentComments(5);
        request.setAttribute("articlesNum", articlesNum);
        request.setAttribute("commentsNum", commentsNum);
        request.setAttribute("articles", recentArticles);
        request.setAttribute("comments", recentComments);
//        request.setAttribute("commons", new Commons()); //待优化 使用拦截器

        return "back/index"; //跳转到后台
    }

    //跳转到发布文章页面
    @RequestMapping("/article/toEditPage")
    public String toEditPage() {

        return "back/article_edit";
    }

    //发布文章
    @RequestMapping("/article/publish")
    @ResponseBody
    public ResponseData publish(Article article) {
        articleService.publishArticle(article);
        return ResponseData.OK();
    }

    //文章管理 跳转到文章管理页面
    @RequestMapping("/article")
    public String article(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, @RequestParam(value = "count", defaultValue = "10")Integer count) {
        //判断
//        pageNum = pageNum == null?1:pageNum;
        //分页查询文章数据
        PageInfo<Article> articles = articleService.findArticleWithPage(pageNum, count);
        request.setAttribute("articles", articles);
        return "back/article_list";
    }

    //跳转到编辑页面
    @RequestMapping("/article/{articleId}")
    public String toEditPage(HttpServletRequest request, @PathVariable("articleId")Integer articleId) {
        //获取文章数据
        Article article = articleService.findById(articleId);
        request.setAttribute("contents", article);
        return "back/article_edit";
    }

    //修改文章
    @RequestMapping("/article/modify")
    @ResponseBody
    public ResponseData modify(Article article) { //这里的article数据并不完整

        articleService.updateArticle(article);

        return ResponseData.OK();
    }

    //删除文章
    @RequestMapping("/article/delete")
    @ResponseBody
    public ResponseData delete(Integer articleId) {
        articleService.deleteArticle(articleId);
        return ResponseData.OK();
    }

    //跳转到评论管理  处理分页请求
    @RequestMapping("/comments")
    public String comments(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, @RequestParam(value = "count", defaultValue = "10")Integer count) {
        //获取评论数据 分页
        PageInfo<Comment> pageInfo = commentService.findWithPage(pageNum, count);
        request.setAttribute("comments", pageInfo);
        return "back/comment_list";
    }

    //删除评论请求
    @RequestMapping("/comment/delete")
    @ResponseBody
    public ResponseData commentDelete(Integer id) {
        //删除评论
        commentService.deleteById(id);
        return ResponseData.OK();
    }

}
