package com.leyou.gateway.filter;

import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.properties.JwtProperties;
import com.leyou.gateway.properties.WhiteListProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties(value = WhiteListProperties.class)
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private WhiteListProperties whiteListProperties;

    @Override
    public String filterType() {
        //路由前拦截
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    public boolean shouldFilter() {
        //获取白名单
        List<String> allowPaths = whiteListProperties.getAllowPaths();
        //获取请求路径
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURL().toString();
        for (String allowPath : allowPaths) {
            //如果包含在白名单中，则不需要检验
            if(StringUtils.contains(url, allowPath)) {
                return false;
            }
        }
        //执行过滤
        return true;
    }

    public Object run() throws ZuulException {
        //初始化 zuul网关的运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取参数
        HttpServletRequest request = context.getRequest();
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //如果token不存在 则不转发zuul请求
        //如果token为空，则解析也会失败，但这里增加判断可以少一步解析过程
        if(StringUtils.isBlank(token)) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        try {
            //尝试解析token，如果解析失败，则不转发zuul请求
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}