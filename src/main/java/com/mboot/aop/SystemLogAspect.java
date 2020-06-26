package com.mboot.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import com.mboot.annotation.SystemLog;
import com.mboot.entity.Log;
import com.mboot.entity.User;
import com.mboot.service.LogService;
import com.mboot.service.UserService;
import com.mboot.utils.IpInfoUtil;
import com.mboot.utils.ObjectUtil;
import com.mboot.utils.ThreadPoolUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring AOP
 *
 * @author Mboot
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

	private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");

	@Autowired
	private LogService logService;

	@Autowired
	private UserService userService;

	@Autowired
	private IpInfoUtil ipInfoUtil;

	@Autowired(required = false)
	private HttpServletRequest request;

	/**
	 * Controller
	 */
	// @Pointcut("execution(* *..controller..*Controller*.*(..))")
	@Pointcut("@annotation(com.mboot.annotation.SystemLog)")
	public void controllerAspect() {

	}

	/**
	 *
	 * @param joinPoint
	 * @throws InterruptedException
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException {

		Date beginTime = new Date();
		beginTimeThreadLocal.set(beginTime);
	}

	/**
	 *
	 * @param joinPoint
	 */
	@AfterReturning("controllerAspect()")
	public void after(JoinPoint joinPoint) {
		try {
			String username = "";
			String description = getControllerMethodInfo(joinPoint).get("description").toString();
			Map<String, String[]> requestParams = request.getParameterMap();

			Log log = new Log();

			if (Objects.equals(getControllerMethodInfo(joinPoint).get("type"), 0)) {
				User user = userService.getLoginUser(request);
				if (user != null) {
					username = user.getUsername();
				}
				log.setUsername(username);
			}
			log.setName(description);
			log.setLogType((int) getControllerMethodInfo(joinPoint).get("type"));
			log.setRequestUrl(request.getRequestURI());
			log.setRequestType(request.getMethod());
			log.setRequestParam(ObjectUtil.mapToString(requestParams));

			log.setIp(ipInfoUtil.getIpAddr(request));
			log.setCreateBy("system");
			log.setUpdateBy("system");
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());
			log.setDelFlag(0);

			long beginTime = beginTimeThreadLocal.get().getTime();
			long endTime = System.currentTimeMillis();
			Long logElapsedTime = endTime - beginTime;
			log.setCostTime(logElapsedTime.intValue());

			// logService.insert(log);
			ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log, logService));

		} catch (Exception e) {
			log.error("AOP Post notification exception", e);
		}
	}

	/**
	 * Save log to database
	 */
	private static class SaveSystemLogThread implements Runnable {

		private Log log;
		private LogService logService;

		public SaveSystemLogThread(Log esLog, LogService logService) {
			this.log = esLog;
			this.logService = logService;
		}

		@Override
		public void run() {
			logService.insert(log);
		}
	}

	/**
	 * Get the description information of the method in the annotation Used for
	 * Controller layer annotation
	 *
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>(16);
		// Get the target class name
		String targetName = joinPoint.getTarget().getClass().getName();
		// Get the method name
		String methodName = joinPoint.getSignature().getName();
		// Get related parameters
		Object[] arguments = joinPoint.getArgs();
		// Generate class objects
		Class targetClass = Class.forName(targetName);
		// Get the methods in this class
		Method[] methods = targetClass.getMethods();

		String description = "";
		Integer type = null;

		for (Method method : methods) {
			if (!method.getName().equals(methodName)) {
				continue;
			}
			Class[] clazzs = method.getParameterTypes();
			if (clazzs.length != arguments.length) {
				// Whether the number of parameters in the comparison method is the same as the
				// number of parameters obtained from the cut surface, because the method can be
				// overloaded
				continue;
			}
			description = method.getAnnotation(SystemLog.class).description();
			type = method.getAnnotation(SystemLog.class).type().ordinal();
			map.put("description", description);
			map.put("type", type);
		}
		return map;
	}

}
