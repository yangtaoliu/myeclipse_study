package cn.study.test.jar;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.mybatis.spring.mapper.MapperScannerConfigurer;

public class TestJar {
	public static void main(String[] args) {
		System.out.println(TestJar.getJarPath(MapperScannerConfigurer.class));
	}
	
	public static String getJarPath(Class clazz){
		ProtectionDomain domain = clazz.getProtectionDomain();
		CodeSource codeSource = domain.getCodeSource();
		return codeSource.getLocation().toString();	
	}
}
