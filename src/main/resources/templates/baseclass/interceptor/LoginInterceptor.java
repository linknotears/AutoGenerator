package ${cfg.basePackage}.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if(!(handler instanceof HandlerMethod)){
    		return true;
    	}
        boolean flag = true;
        HttpSession session = request.getSession();
        Object user = session.getAttribute("existUser");
        String uri = request.getRequestURI();
        log.info("<<<<<<<<执行拦截>>>>>>>>" + ",uri=" + uri + ",user=" + user);
        if(user == null) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "/login");
            flag = false;
        }
        return flag;
    }
}
