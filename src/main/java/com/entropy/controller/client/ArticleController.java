package com.entropy.controller.client;

import com.entropy.entity.Article;
import com.entropy.entity.Comment;
import com.entropy.entity.Statistic;
import com.entropy.service.ArticleService;
import com.entropy.service.CommentService;
import com.entropy.service.StatisticService;
import com.entropy.utils.Commons;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private CommentService commentService;

    //分页查询通用方法
    @RequestMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") Integer pageNum, @RequestParam(value = "count", defaultValue = "5") Integer count) {
        //分页查询代码
        //首页文章分页数据查询
        PageInfo<Article> articles = articleService.findArticleWithPage(pageNum, 5);
        //保存分页数据
        request.setAttribute("articles", articles);

        //排行榜数据
        List<Article> articleList = articleService.getHeatData();
        //把排行榜数据保存到request域中
        request.setAttribute("articleList", articleList);

        //传递Commons对象
//        request.setAttribute("commons", new Commons());

        //跳转到首页
        return "client/index";
    }

    //进入首页
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        //调用通用分页方法
        return page(request, 1, 5);
    }

    //跳转到详情页
    @RequestMapping("/articleDetail/{articleId}")
    public String articleDetail(HttpServletRequest request, @PathVariable("articleId") Integer articleId, @RequestParam(value = "commentPageNum", defaultValue = "1") Integer commentPageNum, @RequestParam(value = "count", defaultValue = "5") Integer count) {
        //准备详情页的数据
        //文章信息
        Article article = articleService.findById(articleId);


        //分页评论信息
        PageInfo<Comment> comments = commentService.findCommentsByArticleId(articleId, commentPageNum, count);
        //更新文章点击量
        statisticService.updateHits(articleId);
        //保存评论信息
        request.setAttribute("comments", comments);
        //保存文章信息
        request.setAttribute("article", article);
        //传递Commons对象
//        request.setAttribute("commons", new Commons());

        return "client/articleDetails";
    }

    //登录跳转请求:跳转到登录页面
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map map) {
        //保存原始访问路径

        //获取原始访问路径
        String referer = request.getHeader("Referer");
        //获取原始访问路径
        String url = request.getParameter("url");
        System.out.println("referer="+referer);
        System.out.println("url="+url);
        //先判断保存的url
        if (url != null && url != "") {
            //使用url作为原始访问路径
            map.put("url", url);
        } else if (referer != null && referer.contains("login")) {
            //如果头信息中存在login字段(排除登录-->登录的情况)
            map.put("url", "");
        } else {
            //使用头信息中的原始访问路径
            map.put("url", referer);
        }

        //跳转到登录页面
        return "comm/login";
    }

    //处理错误页面请求
    @RequestMapping("/errorPage/{comm}/{errNum}")
    public String errorPage(@PathVariable("comm")String comm, @PathVariable("errNum")String errNum) {
        return comm+"/"+errNum;
    }

}
