package dev.edu.javaee.spring.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {
	public static Method findMethod(Class<?> cls, String name, Class<?>... parameterTypes)
	{
		Class<?> currentClass = cls;
		while(currentClass != null)
		{
			Method[] allMethods = currentClass.getDeclaredMethods();
			for(Method method : allMethods)
			{
				if(name.equals(method.getName()) &&
						(parameterTypes == null || 
						Arrays.equals(parameterTypes, method.getParameterTypes())))
						return method;
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	

}
