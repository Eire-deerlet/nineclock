package com.nineclock.auth.intergration.aop;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;
import com.nineclock.auth.intergration.entity.NcOauthTokenDTO;
import com.nineclock.auth.intergration.threadLocal.IntergrationAuthenticationHolder;
import com.nineclock.auth.intergration.threadLocal.UserHolder;
import com.nineclock.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * aop切面类
 */
@Aspect
@Component
@Slf4j
public class AuthTokenAspect {


    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object postHandleAuthTokenResult(ProceedingJoinPoint joinPoint){
        log.info("AOP认证请求拦截");

        Result<NcOauthTokenDTO> result = null;

        Object returnObj=null;

        try {
            // 1.获取到request对象
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();

            // 2.获取参数，组装IntergrationAuthenticationEntity
            IntergrationAuthenticationEntity entity = new IntergrationAuthenticationEntity();
            entity.setAuthType(request.getParameter("auth_type"));
            entity.setAuthParameters(request.getParameterMap());
            //3.往threadLocal存数据
            IntergrationAuthenticationHolder.set(entity);
            //4.调用目标方法执行
            returnObj=joinPoint.proceed();

            if (returnObj != null) {

                // 获取原始方法返回结果
                ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) returnObj;

                // 如果原始方法执行成功
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    // 获取原始方法结果中的响应体
                    OAuth2AccessToken oAuth2AccessToken = responseEntity.getBody();

                    // 封装返回结果
                    NcOauthTokenDTO ncOauthTokenDTO = new NcOauthTokenDTO();
                    ncOauthTokenDTO.setOauth2AccessToken(oAuth2AccessToken);
                    ncOauthTokenDTO.setUser(UserHolder.get());

                    result = Result.success(ncOauthTokenDTO);


                } else {
                    // 如果获取令牌失败，直接封装错误信息，返回结果
                    result = Result.errorCodeMessage(responseEntity.getStatusCodeValue(), "取令牌失败");
                    return ResponseEntity.status(responseEntity.getStatusCode()).body(result);
                }

            }

        } catch (Throwable throwable) {

            log.error("拦截认证请求出错：", throwable);
            result = Result.errorCodeMessage(401, throwable.getMessage());

            return ResponseEntity.status(result.getCode()).body(result);

        } finally {
            // 用完threadLocal后进行移除，防止内存溢出
            IntergrationAuthenticationHolder.remove();
            UserHolder.remove();
        }

        return ResponseEntity.ok(result);
    }
}
