package com.djk.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by dujinkai on 2018/6/14.
 * 操作日志切面
 */
@Component
@Aspect
@Slf4j
public class SysLogAspect {

    /**
     * 切点
     */
    @Pointcut("@annotation(com.djk.es.util.SysLog)")
    public void pointCut() {

    }

    /**
     * 方法执行前切入
     */
    @Before("pointCut()")
    public void printLog(JoinPoint joinPoint) {


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog syslog = method.getAnnotation(SysLog.class);

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        //请求的参数
        Object[] args = joinPoint.getArgs();

        log.info("begin to execute className:{} \r\n methodName:{} \r\n logDesc:{} \r\n params:{}", className, methodName, syslog.value(),args);
    }
}
