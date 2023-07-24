package com.entropy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
@Configuration
public class MyLocalResolver implements LocaleResolver {

    //自定义区域解析器
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        //从头信息中获取区域信息  再使用头信息数据
        String header = request.getHeader("Accept-Language");
        //用户手动切换传递的参数  优先使用用户传递的参数
        String l = request.getParameter("l");
        //创建Locale
        Locale locale = null;
        //解析信息:语言代码
        if (l != null && l != "") {
            String[] s = l.split("_"); //[zh,CN]
            locale = new Locale(s[0], s[1]);
        } else {
            String[] split = header.split(",")[0].split("-");//zh-CN
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocalResolver();
    }
}
