package cn.study.test;

public class SameName {
	public static void main(String[] args) {
		Fu fu = new Fu();
		Zi zi = new Zi();	
		System.out.println("1:" + fu.getName());
		System.out.println("2:" + zi.getName());
		
		fu.setName("aa");
		System.out.println("3:" + fu.getName());
		System.out.println("4:" + zi.getName());	
		
		//zi.name = "bb";
		//System.out.println("5:" + fu.getName());
		//System.out.println("6:" + zi.getName());		
		
		zi.setName("cc");//子类没有set方法，所以调用父类的set方法，改变了父类中的name值，而子类自己的name值不变
		System.out.println("7:" + fu.getName());//2个不同的对象，不可能会改变值
		System.out.println("8:" + zi.getName());
		
		
		System.out.println("9:" + zi.getSuperName()); //zi对象中父类的name的值被改变
	}
}


class Fu{
	String name = "Fu";

	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
}


class Zi extends Fu{
	String name = "Zi";
	

	public String getName() {
		return name;
	}
	
	public  String getSuperName(){
		return super.getName();
	}
	
	
}