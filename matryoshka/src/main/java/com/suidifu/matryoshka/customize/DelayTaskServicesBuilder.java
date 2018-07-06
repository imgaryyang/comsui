package com.suidifu.matryoshka.customize;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Scanner;

import com.zufangbao.sun.utils.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 后置任务脚本编译器
 * Created by louguanyang on 2017/5/7.
 */
public class DelayTaskServicesBuilder extends BaseServicesBuilder implements IDelayTaskServicesBuilder {
//    @Override
//    public DelayTaskServices build(String script) throws CompileException, IllegalAccessException, InstantiationException, IOException {
//        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//        if (contextClassLoader == null) {
//            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
//        }
//
//        Scanner scanner = new Scanner(null, new StringReader(script));
//
//        return (DelayTaskServices) createFastClassBodyEvaluator(scanner, DelayTaskServices.class,
//                contextClassLoader);
//    }

	@Override
	public DelayTaskServices build(String script,String sourceFilePath)
			throws CompileException, IllegalAccessException, InstantiationException, IOException {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        }

        Scanner scanner = new Scanner(null, new StringReader(script));
        
        List<String> importList = new ArrayList<>();
		importList.addAll(BASE_IMPORT_PACKAGE);
		importList.addAll(DELAY_TASK_IMPORT_PACKAGE);
		if(StringUtils.isNotEmpty(sourceFilePath)){
			String[] sourceFilePaths = sourceFilePath.split(SPLIT_REGEX);
			importList.addAll(Arrays.asList(sourceFilePaths));
		}
		String[] optionalDefaultImports = importList.toArray(new String[importList.size()]);
		
        return (DelayTaskServices) createFastClassBodyEvaluator(scanner, DelayTaskServices.class,
                contextClassLoader,optionalDefaultImports);
	}
}
