package com.banelco.empresas.util.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.util.annotation.Protect;
import com.banelco.empresas.util.annotation.Wipe;
import com.banelco.empresas.util.security.SecurityUtil;

@Component
@Aspect
public class LoggerManger {
    private static final Logger log = LoggerFactory.getLogger(LoggerManger.class);

	@SuppressWarnings("rawtypes")
	@Around("execution(* com.banelco.empresas.manager..*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        Exception ex = null;
        Object retVal = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            retVal = joinPoint.proceed();
        } catch (Exception e) {
            ex = e;
        }

        stopWatch.stop();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(joinPoint.getTarget().getClass().getSimpleName());
        logMessage.append("|");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append("(");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = methodSignature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
        	if(args[i] instanceof List && ((List)args[i]).size() > 5) {
				logMessage.append("Lista no imprimible").append(",");
			} else if(parameterAnnotations[i].length > 0 && (parameterAnnotations[i][0] instanceof Wipe)) {
            	logMessage.append(SecurityUtil.wipe());
            } else if(parameterAnnotations[i].length > 0 && (parameterAnnotations[i][0] instanceof Protect)) {
            	logMessage.append(SecurityUtil.protect(ObjectUtils.toString(args[i])));
            } else {
            	logMessage.append(ObjectUtils.toString(args[i])).append(",");
            }
        }

        if (args.length > 0) {
            logMessage.deleteCharAt(logMessage.length() - 1);
        }

        logMessage.append(")|");
        logMessage.append(stopWatch.getTotalTimeMillis());
        logMessage.append("ms");
        if (ex != null) {
            logMessage.append("|");
            if (ex instanceof EmpresasApiException) {
                logMessage.append(((EmpresasApiException) ex).getCodigoError());
            } else {
                logMessage.append(ex.getClass().getSimpleName());
                logMessage.append("|");
                logMessage.append(ex.getMessage());
            }
            if(log.isErrorEnabled())
            	log.error(logMessage.toString());
            throw ex;
        } else {
        	if(log.isErrorEnabled())
        		log.info(logMessage.toString());
        }
        return retVal;
    }
}
