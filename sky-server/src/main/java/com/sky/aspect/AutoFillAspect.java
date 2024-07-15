package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

    /**
     * 前置通知
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段的自动填充...");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return;
        }
        Object arg = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        switch (operationType) {
            case INSERT:
                try {
                    //Set直接用于属性添加值，所以需要先获取属性，然后再设置值
//                    arg.getClass().getDeclaredField("createTime").set(arg, now);
//                    arg.getClass().getDeclaredField("createUser").set(arg, currentId);
//                    arg.getClass().getDeclaredField("updateTime").set(arg, now);
//                    arg.getClass().getDeclaredField("updateUser").set(arg, currentId);
                    //invoke 方法调用类中的方法，参数是方法的参数，所以需要把参数传进去
                    arg.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(arg, now);
                    arg.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(arg, currentId);
                    arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(arg, now);
                    arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(arg, currentId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case UPDATE:
                try {
                    arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(arg, now);
                    arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(arg, currentId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
