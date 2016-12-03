package dev.edu.javaee.spring.component;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.edu.javaee.spring.bean.BeanDefinition;
import dev.edu.javaee.spring.bean.PropertyValues;

public class ComponentCreateBean {
	private List<String> beannamelist = null;

	public List<BeanDefinition> AutoCreateBean() {

		String root = System.getProperty("user.dir") + "/src/test/edu/javaee/spring";
		List<BeanDefinition> beandeflist = new ArrayList<BeanDefinition>();
		beannamelist = new ArrayList<String>();

		File file = new File(root);
		if (file.isDirectory()) {

			String files[] = file.list();
			for (String filename : files) {
				String beanName = null;
				BeanDefinition beandef = new BeanDefinition();
				Component annotationTmp = null;
				String relativePath = "test.edu.javaee.spring" + "." + filename.substring(0, filename.length() - 5);
				beandef.setBeanClassName(relativePath);

				Class<?> beanClass = null;
				try {
					beanClass = Class.forName(relativePath);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beandef.setBeanClass(beanClass);
				if ((annotationTmp = (Component) beanClass.getAnnotation(Component.class)) != null) {
					System.out.println("Component自动导入的bean : "+annotationTmp.value());
					this.beannamelist.add(annotationTmp.value());
					Object bean = null;
					try {
						bean = beanClass.newInstance();
						beandef.setBean(bean);

					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					beandeflist.add(beandef);
				} else {

				}
			}
		}
		return beandeflist;

	}

	public List<String> getAutoCreateBeanName() {
		return this.beannamelist;
	}
}
