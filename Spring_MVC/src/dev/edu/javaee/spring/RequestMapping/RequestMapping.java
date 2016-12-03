package dev.edu.javaee.spring.RequestMapping;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface RequestMapping {

	
	String value() default "";

	String[] params() default {};

	
	String[] headers() default {}; 

	
	String[] consumes() default {};

	
	 
	String[] produces() default {};

}
