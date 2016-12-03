package dev.edu.javaee.spring.factory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.edu.javaee.spring.bean.BeanDefinition;

public abstract class AbstractBeanFactory implements BeanFactory {
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	public Object getBean(String beanName) {
		if (this.beanDefinitionMap.get(beanName) != null)
			return this.beanDefinitionMap.get(beanName).getBean();
		else
			return null;
	}

	public BeanDefinition getBeanDefinition(String beanname) {
		return this.beanDefinitionMap.get(beanname);
	}

	public Object getMap() {
		return this.beanDefinitionMap;
	}

	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinition = GetCreatedBean(beanName, beanDefinition);
		if (beanDefinition != null)
			this.beanDefinitionMap.put(beanName, beanDefinition);
	}

	protected abstract BeanDefinition GetCreatedBean(String beanName, BeanDefinition beanDefinition);

	public abstract List<BeanDefinition> getlist();

	public abstract List<String> getBeanName();
}
