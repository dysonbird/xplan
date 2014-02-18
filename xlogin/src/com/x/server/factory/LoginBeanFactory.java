package com.x.server.factory;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoginBeanFactory {
	private static final Logger logger = LoggerFactory.getLogger(LoginBeanFactory.class);
	private static ApplicationContext factory = null;
	static {
		logger.info("初始化spring容器");
		ClassPathXmlApplicationContext reg = new ClassPathXmlApplicationContext("bean.xml");
	    factory =  reg;

	}
	public static Object getBean(String beanName){
		return factory.getBean(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Map<String,T> getBeans(Class<T> clazz) {
		return factory.getBeansOfType(clazz);
	}
}
