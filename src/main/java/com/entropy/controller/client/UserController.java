package com.entropy.controller.client;

import com.entropy.entity.ResponseData;
import com.entropy.entity.User;
import com.entropy.service.UserService;
import com.entropy.service.impl.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @Autowired
    private SmsServiceImpl smsService;
    @Autowired
    private UserService userService;
    @RequestMapping("/toRegister")
    public String toRegister() {
        //跳转到注册页面
        return "comm/register";
    }

    @RequestMapping("/sendSms")
    @ResponseBody
    public ResponseData sendSms(HttpServletRequest request, String tel) throws Exception {
        System.out.println("tel = " + tel);
        //把短信发送到指定的手机号  短信服务
        String code = smsService.sendSms(tel); //由于是测试服务,手机号只能用阿里云上绑定的测试手机号
        request.getSession().setAttribute("checkCode", code);
        System.out.println("验证码发送成功!");
        return ResponseData.OK();
    }

    //注册
    @RequestMapping("/register")
    public String register(HttpServletRequest request) {
        //获取注册数据
        String username = request.getParameter("username");  //手机号
        String password = request.getParameter("password");  //密码
        String checkCode = request.getParameter("checkCode");  //验证码
        //判断是否已注册
        boolean b = userService.isExist(username);
        if (b) {
            //用户已存在,不能注册
            request.setAttribute("errTel", "该手机号已注册,请重新输入");
            //回到注册页面
            return "comm/register";
        }
        //判断短信验证码是否正确
//        String code = (String) request.getSession().getAttribute("checkCode");
//        if (code == null || !code.equals(checkCode)) {
//            //验证码不正确
//            request.setAttribute("errCheckCode", "验证码错误");
//            //回到注册页面
//            return "comm/register";
//        }

        //数据正确,进行注册
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.registerUser(user);

        //销毁验证码
        request.getSession().removeAttribute("checkCode");
        return "comm/login"; //跳转到登录页面
    }
}
