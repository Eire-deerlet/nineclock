package com.nineclock.approve.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;


@Component
public class NcFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        if(servletRequestAttributes != null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if(request != null){
                String authorization = request.getHeader("Authorization");
                requestTemplate.header("Authorization", authorization);
            }
        }
    }
}
