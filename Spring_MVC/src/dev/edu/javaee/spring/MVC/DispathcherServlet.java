package dev.edu.javaee.spring.MVC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.edu.javaee.spring.RequestMapping.RequestMapping;
import dev.edu.javaee.spring.factory.BeanFactory;
import dev.edu.javaee.spring.factory.XMLBeanFactory;
import dev.edu.javaee.spring.resource.LocalFileResource;
import test.test;

public class DispathcherServlet extends HttpServlet{
	
    public DispathcherServlet() {
        super();  
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");  
		
		 String uri_path = request.getServletPath() ;  
		 System.out.println(uri_path);
		 Enumeration req = request.getParameterNames();
		 ModelAndView objMav = new ModelAndView();
		 while (req.hasMoreElements()) {
		     Object obj = (Object) req.nextElement();
		     
		     objMav.putRequest_Map(obj.toString(), request.getParameter(obj.toString()));
		 }
		 
		 LocalFileResource resource = new LocalFileResource(this.getClass().getResource("/").getPath() + "/bean.xml");
			BeanFactory beanFactory = new XMLBeanFactory(resource);
		test test = (test) beanFactory.getBean("test");
		 Method[] methods = test.getClass().getDeclaredMethods();
		
		for(Method method : methods){
			 RequestMapping requestMapping = null;
			 if((requestMapping=method.getAnnotation(RequestMapping.class))!=null){
				 String requestMappingValue = requestMapping.value();
				 if(requestMappingValue.equals(uri_path)){
					try {
						ModelAndView objModel=(ModelAndView) method.invoke(test, objMav);
						String nextUriPath = objModel.getViewName();
						Map<String,String> response_Map = objModel.getResponse_Map();
						List<String> nameList = new ArrayList<String>( response_Map.keySet());
						Collections.sort(nameList);
						for(String name : nameList){
					          request.setAttribute(name,response_Map.get(name));
					    }
						 request.getRequestDispatcher(nextUriPath+".jsp").forward(request,response);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 }
		}
	   
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
    }
	
	
}
