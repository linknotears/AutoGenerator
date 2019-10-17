package com.huihaha.onlineorder.aspect;

import com.huihaha.onlineorder.entity.Club;
import com.huihaha.onlineorder.entity.User;
import com.huihaha.onlineorder.interceptor.LoginInterceptor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;

@Aspect
@Component
public class AutoInjectAspect {
    private static final Logger log = LoggerFactory.getLogger(AutoInjectAspect.class);
    @Pointcut("execution( * com.huihaha.onlineorder.controller.*.findPage(..)) || execution( * com.huihaha.onlineorder.controller.*.saveOrUpdate(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void pointCut1() {}
    @Pointcut("execution( * com.huihaha.onlineorder.controller.*.findList(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void pointCut2() {}

    private void injectParam(int index,String propertyName,String sessionKey,JoinPoint joinPoint) throws Exception {
        HttpSession session = LoginInterceptor.threadSession.get();
        String logStr = "";
        Object[] args = joinPoint.getArgs();
        Object obj = args[index];
        Field field = null;
        if(obj == null) {
            Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            obj = parameterTypes[index].getDeclaredConstructor().newInstance();
        }
        try {
            field = obj.getClass().getDeclaredField(propertyName);
            logStr += obj.getClass().getName() + "." + propertyName + " 进行了反射,";
        }catch (NoSuchFieldException e){
            logStr += "没有找到字段,";
        }
        if(field!=null){
            Object prop = session.getAttribute(sessionKey);
            field.setAccessible(true);
            field.set(obj, prop);
            log.info( logStr + propertyName + ">>>>>" + prop );
        }
    }
    @Before("pointCut1()")
    public void injectParameter1(JoinPoint joinPoint) throws Exception {
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        int index = 1;
        if (!Club.class.equals(parameterTypes[index]))
            injectParam(index,"clubId","clubId",joinPoint);
        if (!User.class.equals(parameterTypes[index]))
            injectParam(index,"userId","userId",joinPoint);
        else
            injectParam(index,"master","userId",joinPoint);
    }

    @Before("pointCut2()")
    public void injectParameter2(JoinPoint joinPoint) throws Exception {
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        int index = 0;
        if (!Club.class.equals(parameterTypes[index]))
            injectParam(index,"clubId","clubId",joinPoint);
        if (!User.class.equals(parameterTypes[index]))
            injectParam(index,"userId","userId",joinPoint);
        else
            injectParam(index,"master","userId",joinPoint);
    }

}
