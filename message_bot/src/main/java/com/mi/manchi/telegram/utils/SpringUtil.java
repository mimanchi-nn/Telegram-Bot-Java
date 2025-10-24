package com.mi.manchi.telegram.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return (T) context.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return (T) context.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	public static String[] getActiveProfiles() {
		return context.getEnvironment().getActiveProfiles();
	}

	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

}