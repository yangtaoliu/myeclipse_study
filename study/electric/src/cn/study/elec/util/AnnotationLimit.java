package cn.study.elec.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* 
@Inherited   注解类的子类有无这个注解,允许子类继承父类的注解,即在某个类中，如果父类中包含某个注解，子类中也会包含标注了@Inherited的注解，没有标注的，子类中不含有这个注解

如：
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InheritedTest {
    String value() default "this is default value for custom annotation InheritedTest that can use Annotation @Inherited.";
}

@Retention(RetentionPolicy.RUNTIME)		这里没有Inherited
public @interface UnInheritedTest {
    String value() default "this is default value for custom annotation UnInheritedTest that can not use Annotation @Inherited.";
}


@InheritedTest("使用@Inherited的注解Parent类")
@UnInheritedTest("未使用@Inherited的注解Parent类")
public class Parent {

}

public class Child extends Parent{

}

Class<Child> clazz = Child.class;
clazz.isAnnotationPresent(InheritedTest.class)  	ture
clazz.isAnnotationPresent(UnInheritedTest.class)	false

https://www.jianshu.com/p/4a3ffb79c10e


@Documented  生成的javadoc文档是否含有注解，不写就没有
@Retention(RetentionPolicy.RUNTIME) 改变注解的存活范围
@Target(ElementType.METHOD) 标识注解应用的位置
*/
/**
 * 自定义注解
 */
//被这个注解修饰的注解，利用反射，将其他的注解读取出来
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationLimit {
	String mid();//权限的code
	String pid();//父级权限的code	
}
