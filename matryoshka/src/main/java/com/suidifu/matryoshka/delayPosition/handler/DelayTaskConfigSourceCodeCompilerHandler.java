package com.suidifu.matryoshka.delayPosition.handler;

import com.suidifu.matryoshka.delayPosition.DelayTaskServicesBuilder;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.pojo.SourceRepository;
import com.zufangbao.sun.utils.StringUtils;
import org.codehaus.commons.compiler.CompileException;

import java.io.IOException;

/**
 * Created by louguanyang on 2017/5/7.
 */
public class DelayTaskConfigSourceCodeCompilerHandler implements SourceCodeCompilerHandler {
    @Override
    public Object compile(SourceRepository sourceRepository) throws XcodeException {
        if (sourceRepository == null || StringUtils.isEmpty(sourceRepository.getSourceCode())) return null;
        DelayTaskServicesBuilder builder = new DelayTaskServicesBuilder();
        try {
            return builder.build(sourceRepository.getSourceCode(),sourceRepository.getCompileImport());
        } catch (CompileException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
