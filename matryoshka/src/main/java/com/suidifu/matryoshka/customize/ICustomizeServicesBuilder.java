package com.suidifu.matryoshka.customize;


import org.codehaus.commons.compiler.CompileException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by louguanyang on 2017/4/12.
 */
public interface ICustomizeServicesBuilder extends IBaseServicesBuilder{
	List<String> CUSTOMIZE_IMPORT_PACKAGE = Arrays.asList(new String[] { "org.apache.commons.logging.Log" });

//    CustomizeServices build(String script) throws CompileException, IllegalAccessException, InstantiationException, IOException;
    CustomizeServices build(String script,String sourceFilePath) throws CompileException, IllegalAccessException, InstantiationException, IOException;

}
