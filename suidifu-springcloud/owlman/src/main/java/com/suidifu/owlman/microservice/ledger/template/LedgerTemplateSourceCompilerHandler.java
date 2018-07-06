/**
 * 
 */
package com.suidifu.owlman.microservice.ledger.template;

import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.pojo.SourceRepository;
import com.zufangbao.sun.ledgerbookv2.exception.MakeTemplateGenerateShlefException;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParserUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author wukai
 *
 */
public class LedgerTemplateSourceCompilerHandler implements
		SourceCodeCompilerHandler {

	/* (non-Javadoc)
	 * @see com.suidifu.xcode.handler.SourceCodeCompilerHandler#compile(com.suidifu.xcode.pojo.SourceRepository)
	 */

	private static Log log = LogFactory.getLog
			(LedgerTemplateSourceCompilerHandler.class);

	@Override
	public Object compile(SourceRepository sourceRepository)
			throws XcodeException {
		
		String sourceCode =  sourceRepository.getSourceCode();

		try {
			return LedgerTemplateParserUtils.parseSection(sourceCode);
		} catch (MakeTemplateGenerateShlefException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return null;
		}
	}

}
