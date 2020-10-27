package cn.study.elec.util;

import org.apache.struts2.ServletActionContext;

public class ValueUtils {
	/**
	 * 将对象压入栈顶
	 * @param commomMsg
	 */
	public static void putValueStack(Object o) {
		ServletActionContext.getContext().getValueStack().push(o);		
	}
	
}
