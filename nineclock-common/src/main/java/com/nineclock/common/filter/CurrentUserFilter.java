package com.nineclock.common.filter;

import cn.hutool.core.util.StrUtil;
import com.nineclock.common.entity.UserInfo;
import com.nineclock.common.utils.JsonUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "currentUserFilter", urlPatterns = "/*")
public class CurrentUserFilter extends GenericFilterBean {
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            //获取当前登录用户信息, 存储ThreadLocal
            String nameJsonStr = SecurityContextHolder.getContext().getAuthentication().getName();
            if(StrUtil.isNotEmpty(nameJsonStr) && !"anonymousUser".equals(nameJsonStr)){
                UserInfo userInfo = JsonUtils.toBean(nameJsonStr, UserInfo.class);
                CurrentUserHolder.set(userInfo);
            }
            //放行, 执行原始方法
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CurrentUserHolder.remove();//移除ThreadLocal中的用户信息
        }
    }
}