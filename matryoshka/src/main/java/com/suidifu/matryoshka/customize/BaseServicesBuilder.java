package com.suidifu.matryoshka.customize;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.nullanalysis.Nullable;
import org.codehaus.janino.ClassBodyEvaluator;
import org.codehaus.janino.JaninoRuntimeException;
import org.codehaus.janino.Scanner;

import java.io.IOException;

/**
 * Created by louguanyang on 2017/5/7.
 */
public class BaseServicesBuilder {
//    public static Object createFastClassBodyEvaluator(Scanner scanner, @Nullable Class<?> optionalBaseType,
//                                                       @Nullable ClassLoader optionalParentClassLoader) throws CompileException, IOException {
//        return createFastClassBodyEvaluator(scanner, "SC", optionalBaseType != null && !optionalBaseType.isInterface
//                ()?optionalBaseType:null, optionalBaseType != null && optionalBaseType.isInterface()?new Class[]{optionalBaseType}:new Class[0], optionalParentClassLoader);
//    }

	public static Object createFastClassBodyEvaluator(Scanner scanner, @Nullable Class<?> optionalBaseType,
			@Nullable ClassLoader optionalParentClassLoader, String[] optionalDefaultImports)
			throws CompileException, IOException {
		return createFastClassBodyEvaluator(scanner, "SC",
				optionalBaseType != null && !optionalBaseType.isInterface() ? optionalBaseType : null,
				optionalBaseType != null && optionalBaseType.isInterface() ? new Class[] { optionalBaseType }
						: new Class[0],
				optionalParentClassLoader,optionalDefaultImports);
	}

//	public static Object createFastClassBodyEvaluator(Scanner scanner, String className, @Nullable Class<?>
//    optionalExtendedClass, Class<?>[] implementedInterfaces, @Nullable ClassLoader optionalParentClassLoader) throws CompileException, IOException {
//		String[] optionalDefaultImports=new String[]{"java.math.*",
//                "com.zufangbao.sun.*",
//                "com.zufangbao.gluon.*",
//                "com.zufangbao.wellsfargo.*",
//                "com.suidifu.matryoshka.*",
//                "com.suidifu.matryoshka.cache.*",
//                "com.suidifu.matryoshka.customize.*",
//                "com.suidifu.matryoshka.handler.*",
//                "com.suidifu.matryoshka.delayPosition.*",
//                "com.suidifu.matryoshka.delayPosition.handler.*",
//                "com.suidifu.matryoshka.delayTask.*",
//                "com.suidifu.matryoshka.delayTask.enums.*",
//                "com.suidifu.matryoshka.prePosition.handler.*",
//                "com.suidifu.matryoshka.productCategory.*",
//                "com.suidifu.matryoshka.service.*",
//                "com.suidifu.matryoshka.service.delayTask.*",
//                "com.suidifu.matryoshka.snapshot.*",
//                "java.util.*",
//	            "com.alibaba.fastjson.*",    
//	            "com.demo2do.core.entity.*",
//                "com.zufangbao.sun.utils.*",
//                "com.zufangbao.sun.yunxin.entity.model.api.modify.*",
//                "com.zufangbao.sun.yunxin.entity.*",
//                "com.zufangbao.sun.yunxin.entity.api.mutableFee.*",
//                "org.apache.commons.collections.*",
//                "org.apache.commons.logging.*"
//        };
//		return createFastClassBodyEvaluator(scanner, className, optionalExtendedClass, implementedInterfaces, optionalParentClassLoader, optionalDefaultImports);
//	}
	
    public static Object createFastClassBodyEvaluator(Scanner scanner, String className, @Nullable Class<?>
            optionalExtendedClass, Class<?>[] implementedInterfaces, @Nullable ClassLoader optionalParentClassLoader,String[] optionalDefaultImports) throws CompileException, IOException {
        ClassBodyEvaluator cbe = new ClassBodyEvaluator();
        cbe.setClassName(className);
        cbe.setExtendedClass(optionalExtendedClass);
        cbe.setImplementedInterfaces(implementedInterfaces);
        cbe.setParentClassLoader(optionalParentClassLoader);

        cbe.setDefaultImports(optionalDefaultImports);

        cbe.cook(scanner);
        Class c = cbe.getClazz();

        try {
            return c.newInstance();
        } catch (InstantiationException var8) {
            throw new CompileException(var8.getMessage(), null);
        } catch (IllegalAccessException var9) {
            throw new JaninoRuntimeException(var9.toString(), var9);
        }
    }
}
