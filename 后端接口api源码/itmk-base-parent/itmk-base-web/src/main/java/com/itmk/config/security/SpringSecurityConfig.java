package com.itmk.config.security;

import com.itmk.config.security.detailservice.CustomerUserDetailService;
import com.itmk.config.security.filter.CheckTokenFilter;
import com.itmk.config.security.handler.CustomAccessDeineHandler;
import com.itmk.config.security.handler.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置类
 * @Configuration:表明该类是一个配置类
 * @EnableWebSecurity:启用spring security
 * @EnableGlobalMethodSecurity(prePostEnabled = true);   启用spring security的注解
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomerUserDetailService customerUserDetailService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomAccessDeineHandler customAccessDeineHandler;
    @Autowired
    private CheckTokenFilter checkTokenFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //配置我们自己定义的customerUserDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决跨域
        http.cors().and().headers().frameOptions().disable();
        //配置token过滤器
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable().authorizeRequests()//除了登录和验证码请求，其他所有的请求都要进行验证
                .antMatchers("/api/sysUser/getImage", "/api/sysUser/login").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(loginFailureHandler)
                .accessDeniedHandler(customAccessDeineHandler);
    }

    //注入AuthenticationManager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
