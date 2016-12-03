package dev.edu.javaee.spring.factory;

import java.util.List;

import dev.edu.javaee.spring.bean.BeanDefinition;

public interface BeanFactory {
	Object getBean(String beanName);
	Object getMap();
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
	public abstract List<BeanDefinition> getlist();
	public abstract List<String> getBeanName();
}
