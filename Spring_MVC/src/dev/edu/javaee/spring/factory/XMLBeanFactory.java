package dev.edu.javaee.spring.factory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import dev.edu.javaee.spring.autowird.AutowireCreateBean;
import dev.edu.javaee.spring.autowird.Autowired;
import dev.edu.javaee.spring.bean.BeanDefinition;
import dev.edu.javaee.spring.bean.BeanUtil;
import dev.edu.javaee.spring.bean.PropertyValue;
import dev.edu.javaee.spring.bean.PropertyValues;
import dev.edu.javaee.spring.component.ComponentCreateBean;
import dev.edu.javaee.spring.resource.Resource;

public class XMLBeanFactory extends AbstractBeanFactory {

	private String xmlPath;
	private List<BeanDefinition> list = new ArrayList<BeanDefinition>();
	private List<String> beannamelist = new ArrayList<String>();
	private Map<String, String> name_ref_mapping = new HashMap<String, String>();
	private Map<String, Map<String, String>> ref_relation_bean = new HashMap<String, Map<String, String>>();

	public XMLBeanFactory(Resource resource) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(resource.getInputStream());
			NodeList beanList = document.getElementsByTagName("bean");

			for (int i = 0; i < beanList.getLength(); i++) {

				Node bean = beanList.item(i);
				BeanDefinition beandef = new BeanDefinition();
				String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
				String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();

				beandef.setBeanClassName(beanClassName);

				try {
					Class<?> beanClass = Class.forName(beanClassName);
					beandef.setBeanClass(beanClass);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PropertyValues propertyValues = new PropertyValues();

				NodeList propertyList = bean.getChildNodes();
				for (int j = 0; j < propertyList.getLength(); j++) {
					Node property = propertyList.item(j);
					if (property instanceof Element) {
						Element ele = (Element) property;

						String name = ele.getAttribute("name");
						Class<?> type;
						try {

							type = beandef.getBeanClass().getDeclaredField(name).getType();
							Object value = ele.getAttribute("value");
							if (value != null && value.toString().length() > 0) {
								if (type == Integer.class) {
									value = Integer.parseInt((String) value);
								}

								propertyValues.AddPropertyValue(new PropertyValue(name, value));
							} else {
								String ref = ele.getAttribute("ref");
								if (ref == null || ref.length() == 0) {
									throw new IllegalArgumentException(
											"Configuration problem: <property> element for property '" + name
													+ "' must specify a ref or value");
								}
								if (getBean(ref) != null) {
									Object beanReference = getBean(ref);
									propertyValues.AddPropertyValue(new PropertyValue(name, beanReference));
								} else {
									name_ref_mapping.put(name, ref);
									ref_relation_bean.put(beanName, name_ref_mapping);
								}

							}

						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				beandef.setPropertyValues(propertyValues);

				this.registerBeanDefinition(beanName, beandef);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) this.getMap();
		ComponentCreateBean componentCreateBean = new ComponentCreateBean();
		List<BeanDefinition> autoCreateBeanList = componentCreateBean.AutoCreateBean();

		List<String> autoCreateBeanNameList = componentCreateBean.getAutoCreateBeanName();
		for (int i = 0; i < autoCreateBeanList.size(); i++) {
			beanDefinitionMap.put(autoCreateBeanNameList.get(i), autoCreateBeanList.get(i));
		}

		Iterator it = ref_relation_bean.keySet().iterator();
		while (it.hasNext()) {

			String beanname = it.next().toString();
			BeanDefinition bean = getBeanDefinition(beanname);
			Map<String, String> map = ref_relation_bean.get(beanname);
			Iterator it1 = map.keySet().iterator();

			while (it1.hasNext()) {
				String propertyname = it1.next().toString();
				bean.getPropertyValues().AddPropertyValue(new PropertyValue(propertyname, getBean(propertyname)));
			}

			this.registerBeanDefinition(beanname, bean);
		}

		this.CreateleftBean();

	}

	@Override
	protected BeanDefinition GetCreatedBean(String beanName, BeanDefinition beanDefinition) {

		try {
			// set BeanClass for BeanDefinition

			Class<?> beanClass = beanDefinition.getBeanClass();
			// set Bean Instance for BeanDefinition
			Object bean = null;
			Field[] fields = beanClass.getDeclaredFields();
			Method[] methods = beanClass.getMethods();
			boolean flag = false;
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].isAnnotationPresent(Autowired.class)) {
					flag = true;
				}
			}
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].isAnnotationPresent(Autowired.class)) {
					flag = true;
				}
			}

			if (beanClass.getDeclaredConstructors()[0].getParameterTypes().length == 0 && flag == false) {
				bean = beanClass.newInstance();

				List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
				for (PropertyValue propertyValue : fieldDefinitionList) {
					BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
				}

				beanDefinition.setBean(bean);

				return beanDefinition;

			} else {
				list.add(beanDefinition);
				beannamelist.add(beanName);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public List<BeanDefinition> getlist() {
		// TODO Auto-generated method stub
		return list;
	}

	public List<String> getBeanName() {
		return beannamelist;
	}

	public void CreateleftBean() {

		try {
			List<BeanDefinition> FinishedBeanList = new ArrayList<BeanDefinition>();
			Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) this.getMap();
			Iterator it = beanDefinitionMap.values().iterator();

			while (it.hasNext()) {
				FinishedBeanList.add((BeanDefinition) it.next());
			}

			List<BeanDefinition> FinishedLeftDefinition = AutowireCreateBean.CreateRemainedBean(list, FinishedBeanList);

			for (int i = 0; i < FinishedLeftDefinition.size(); i++) {
				beanDefinitionMap.put(beannamelist.get(i), FinishedLeftDefinition.get(i));

			}

		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | NoSuchMethodException
				| SecurityException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
