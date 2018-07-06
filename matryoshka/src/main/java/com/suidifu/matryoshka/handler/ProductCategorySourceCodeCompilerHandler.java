package com.suidifu.matryoshka.handler;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.commons.compiler.CompileException;

import com.suidifu.matryoshka.customize.CustomizeServicesBuilder;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.pojo.SourceRepository;
import com.zufangbao.sun.utils.StringUtils;

/**
 * Created by louguanyang on 2017/5/4.
 */
public class ProductCategorySourceCodeCompilerHandler implements SourceCodeCompilerHandler {
	Log log=LogFactory.getLog(ProductCategorySourceCodeCompilerHandler.class);
    @Override
    public Object compile(SourceRepository sourceRepository) throws XcodeException {
        try {
            if (sourceRepository == null || StringUtils.isEmpty(sourceRepository.getSourceCode())) return null;
            CustomizeServicesBuilder builder = new CustomizeServicesBuilder();
            log.info("ProductCategorySourceCodeCompilerHandler CompileImport:"+sourceRepository.getCompileImport());
            return builder.build(sourceRepository.getSourceCode(),sourceRepository.getCompileImport());
        } catch (CompileException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
