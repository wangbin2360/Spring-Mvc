package test;

import dev.edu.javaee.spring.Controller.Controller;
import dev.edu.javaee.spring.MVC.ModelAndView;
import dev.edu.javaee.spring.RequestMapping.RequestMapping;

@Controller
public class test {
  
	@RequestMapping("/hello")
	public ModelAndView  hello(ModelAndView mdv) {
		ModelAndView mav=mdv;
		// TODO Auto-generated constructor stub
		mav.setViewName("test");
		mav.addObject("name", mav.getRequest_Map("name"));
		mav.addObject("pas", mav.getRequest_Map("pas"));
		return mav;
	}
	@RequestMapping("/hello2")
	public ModelAndView  hello2(ModelAndView mdv) {
		ModelAndView mav =mdv;
		// TODO Auto-generated constructor stub
		mav.setViewName("test");
		return mav;
	}
	
}
