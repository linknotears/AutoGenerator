package com.huihaha.onlineorder.interceptor;

import com.huihaha.onlineorder.entity.User;
import com.huihaha.onlineorder.entity.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
        HttpSession session = request.getSession();
        ResultData result = new ResultData();
        User user = (User) session.getAttribute("existUser");
        String uri = request.getRequestURI();
        System.out.println( "uri=" + uri + ",user=" + user );
        log.info("<<<<<<<<开始拦截>>>>>>>>");
        if(user!=null) {
            Object userId = user.getUsername();
            Object root = user.getRoot();
            Object useClubId = user.getUseClubId();
            if("1".equals(root+"")){
                request.setAttribute("userId",userId);
            }else{
                Object master = session.getAttribute("master");
                request.setAttribute("userId",master);
            }
            request.setAttribute("useClubId",useClubId);
        }else {
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "/login");
            flag = false;
        }
        return flag;
    }
}
