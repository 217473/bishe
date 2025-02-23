package com.itmk.config.security.filter;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itmk.config.security.detailservice.CustomerUserDetailService;
import com.itmk.config.security.handler.LoginFailureHandler;
import com.itmk.jwt.JwtUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Component("CheckTokenFilter")
public class CheckTokenFilter extends OncePerRequestFilter {
    @Value("#{'${ignore.url}'.split(',')}")
    private List<String> ignoreUrl = Collections.emptyList();
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomerUserDetailService customerUserDetailService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //获取请求的url
            String url = request.getRequestURI();
            //请求不在配置的白名单，验证token
            if(!ignoreUrl.contains(url)){
                //token验证
                validateToken(request);
            }
        }catch (AuthenticationException e){
            loginFailureHandler.commence(request,response,e);
            return;
        }
        filterChain.doFilter(request,response);
    }
    protected void  validateToken(HttpServletRequest request){
        //请求头部获取token
        String token = request.getHeader("token");
        //如果请求头部token为空，则从参数中获取token
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        if(StringUtils.isEmpty(token)){
            throw new RuntimeException("请传递token！");
        }
        //token验证
        if(!jwtUtils.verify(token)){
            throw new RuntimeException("非法的token！");
        }
        //解析token
        DecodedJWT decodedJWT = jwtUtils.jwtDecode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        //登录账号
        String username = claims.get("username").asString();
        //用户认证
        UserDetails details = customerUserDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details,
                null,details.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //设置到springsecurity上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
