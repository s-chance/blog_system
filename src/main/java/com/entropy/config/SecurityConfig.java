package com.entropy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collection;

@EnableWebSecurity //开启MVC Security安全支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    @Value("${COOKIE.VALIDITY}")
    private Integer validate;
    //设置访问控制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义用户访问控制
        http.authorizeRequests()
                .antMatchers("/", "/articleDetail/**", "/page/**", "/login", "/toRegister", "/sendSms", "/register").permitAll()
                .antMatchers( "/article_img/**", "/assets/**", "/back/**", "/node_modules/**", "/user/**").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().authenticated();

        //登录相关控制
        http.formLogin()
                .loginPage("/login") //登录页面的跳转路径
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        //获取拦截页面
                        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
                        //获取原始访问页面
                        String url = httpServletRequest.getParameter("url");
                        if (savedRequest != null) {
                            //跳转到拦截页面
                            httpServletResponse.sendRedirect(savedRequest.getRedirectUrl());
                        } else if (url != null && url != "") {
                            //跳转到原始访问页面
                            httpServletResponse.sendRedirect(url);
                        } else {
                            //跳转到主页
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                            boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));
                            if (isAdmin) {
                                //管理员跳转到后台首页  /admin
                                httpServletResponse.sendRedirect("/admin");
                            } else {
                                //非管理员跳转到前台首页 /
                                httpServletResponse.sendRedirect("/");
                            }
                        }

                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        //返回登录页面,重新登录
                        //获取原始访问路径
                        String url = httpServletRequest.getParameter("url");
                        httpServletResponse.sendRedirect("/login?error&url="+url);
                    }
                });

        //退出控制
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
        //是否开启记住我功能,设置cookie有效时间
        http.rememberMe().alwaysRemember(true).tokenValiditySeconds(validate);
        //自定义403页面
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                //跳转到自定义403页面
                //发送跳转到403页面的请求
                httpServletRequest.getRequestDispatcher("/errorPage/comm/error_403").forward(httpServletRequest, httpServletResponse);
            }
        });
    }

    //设置认证控制
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //JDBC认证方式
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //JDBC身份认证SQL
        //查询用户
        String userSQL = "select username,password,valid from t_user where username = ?";
        //查询权限
        String authoritySQL = "select a.username,b.authority " +
                "from t_user a,t_authority b,t_user_authority c " +
                "where a.id = c.user_id and b.id = c.authority_id and username = ?";
        auth.jdbcAuthentication().passwordEncoder(encoder)
                .dataSource(dataSource)
                .usersByUsernameQuery(userSQL)
                .authoritiesByUsernameQuery(authoritySQL);
    }
}
