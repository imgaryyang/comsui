package com.suidifu.matryoshka.customize;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Scanner;

import com.zufangbao.sun.utils.StringUtils;

/**
 *
 * Created by louguanyang on 2017/4/6.
 */
public class CustomizeServicesBuilder extends BaseServicesBuilder implements ICustomizeServicesBuilder {

//    @Override
//    public CustomizeServices build(String script) throws CompileException, IllegalAccessException, InstantiationException,
//            IOException {
//
//        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//        if (contextClassLoader == null) {
//            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
//        }
//
//        Scanner scanner = new Scanner(null, new StringReader(script));
//
//        return (CustomizeServices) createFastClassBodyEvaluator(scanner, CustomizeServices.class,
//                contextClassLoader);
//    }

	@Override
	public CustomizeServices build(String script, String sourceFilePath)
			throws CompileException, IllegalAccessException, InstantiationException, IOException {

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader == null) {
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
		}

		Scanner scanner = new Scanner(null, new StringReader(script));

		List<String> importList = new ArrayList<>();
		importList.addAll(BASE_IMPORT_PACKAGE);
		importList.addAll(CUSTOMIZE_IMPORT_PACKAGE);
		if(StringUtils.isNotEmpty(sourceFilePath)){
			String[] sourceFilePaths = sourceFilePath.split(SPLIT_REGEX);
			importList.addAll(Arrays.asList(sourceFilePaths));
		}
		String[] optionalDefaultImports = importList.toArray(new String[importList.size()]);

		return (CustomizeServices) createFastClassBodyEvaluator(scanner, CustomizeServices.class, contextClassLoader,
				optionalDefaultImports);
	}
}
