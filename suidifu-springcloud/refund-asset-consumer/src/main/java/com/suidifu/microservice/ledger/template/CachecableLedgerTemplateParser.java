/**
 * 
 */
package com.suidifu.microservice.ledger.template;

import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAccountTemplateKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastAccountTemplate;
import com.suidifu.microservice.exception.TemplatenDontExistException;
import com.suidifu.xcode.handler.XcodeServerHandler;
import com.zufangbao.sun.ledgerbookv2.dictionary.LedgerConstant;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.handler.impl.DefaultLedgerTemplateParser;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser;
import com.zufangbao.sun.ledgerbookv2.service.BusinessScenarioDefinitionService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author wukai
 *
 */
public class CachecableLedgerTemplateParser extends DefaultLedgerTemplateParser implements  LedgerTemplateParser{

	private static Log LOG = LogFactory.getLog(CachecableLedgerTemplateParser.class);
	
	@Autowired
	private XcodeServerHandler xcodeServerHandler;
	
	@Autowired
	private FastHandler fastHandler;
	
	@Autowired
	private BusinessScenarioDefinitionService businessScenarioDefinitionService;

	public static String BUSINESS_TYPE;

	/* (non-Javadoc)
	 * @see com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser#parseSection(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> parseSection(Map<String, Object> contextFrame,String ledgerBookNo,EventType eventType) {
		BUSINESS_TYPE = eventType.toString();

		xcodeServerHandler.register(BUSINESS_TYPE, new LedgerTemplateSourceCompilerHandler());

		Map<FastKey, String> map = new HashMap<>();
		map.put(FastAccountTemplateKeyEnum.LEDGER_BOOK_NO, ledgerBookNo);
		map.put(FastAccountTemplateKeyEnum.EVENT_TYPE, eventType.ordinal()+"");

		contextFrame.put(LedgerConstant.Event_Type.BATCH_SERIAL_UUID, UUID
				.randomUUID().toString());
		contextFrame.put(LedgerConstant.Event_Type.LEDGER_BOOK_NO, ledgerBookNo);
		contextFrame.put(LedgerConstant.Event_Type.EVENT_TYPE, eventType);

		try {
			long start = System.currentTimeMillis();
			FastAccountTemplate fastAccountTemplate = fastHandler.getByKey(map, FastAccountTemplate.class, true);
			LOG.info("fastHandler getByKey used :"+(System.currentTimeMillis()-start));
			if(null == fastAccountTemplate){
				LOG.info(TemplatenDontExistException.TEMPLATE_DONT_EXIST+"场景类型:"+eventType.toString());
				return Collections.EMPTY_LIST;
			}

			String signature = fastAccountTemplate.getTemplateSignature();
			if (StringUtils.isBlank(signature)) {
				LOG.info("parse event type [" + eventType + "] section : template signature is empty.");
				return Collections.EMPTY_LIST;
			}
			start = System.currentTimeMillis();
			Object parsedValue = xcodeServerHandler.getNewest(BUSINESS_TYPE, signature);
			LOG.info("getNewest used :"+(System.currentTimeMillis()-start));
			if(null != parsedValue){
				
				return (List<Map<String, Object>>) parsedValue;
			}
			return super.parseSection("", signature);
			
		} catch (Exception e) {
			
			LOG.error("#CacableLedgerTemplateParser# parseSection with cache occur exception with stack trace ["+ExceptionUtils.getFullStackTrace(e)+"],decide to choose json parse");
		}
		
		return super.parseSection("", null);
	}
	@Override
	public void parseSectionV2(Map<String, Object> contextFrame,String ledgerBookNo,EventType eventType) {
		contextFrame.put(LedgerConstant.Event_Type.BATCH_SERIAL_UUID, UUID
				.randomUUID().toString());
		contextFrame.put(LedgerConstant.Event_Type.LEDGER_BOOK_NO, ledgerBookNo);
		contextFrame.put(LedgerConstant.Event_Type.EVENT_TYPE, eventType);
	}

	@Override
	public List<Map<String , Object>> fixCum(String ledgerBookNo,EventType eventType){
		BUSINESS_TYPE = eventType.toString();
		xcodeServerHandler.register(BUSINESS_TYPE, new LedgerTemplateSourceCompilerHandler());
		Map<FastKey, String> map = new HashMap<>();
		map.put(FastAccountTemplateKeyEnum.LEDGER_BOOK_NO, ledgerBookNo);
		map.put(FastAccountTemplateKeyEnum.EVENT_TYPE, eventType.ordinal()+"");
		try {
			long start = System.currentTimeMillis();
			FastAccountTemplate fastAccountTemplate = fastHandler.getByKey(map, FastAccountTemplate.class, true);
//			LOG.info("fastHandler getByKey used :"+(System.currentTimeMillis()-start));
			if(null == fastAccountTemplate){
				LOG.info(TemplatenDontExistException.TEMPLATE_DONT_EXIST+"场景类型:"+eventType.toString());
				return Collections.EMPTY_LIST;
			}

			String signature = fastAccountTemplate.getTemplateSignature();
			if (StringUtils.isBlank(signature)) {
//				LOG.info("parse event type [" + eventType + "] section : template signature is empty.");
				return Collections.EMPTY_LIST;
			}
			start = System.currentTimeMillis();
			Object parsedValue = xcodeServerHandler.getNewest(BUSINESS_TYPE, signature);
//			LOG.info("getNewest used :"+(System.currentTimeMillis()-start));
			if(null != parsedValue){

				return (List<Map<String, Object>>) parsedValue;
			}
			return super.parseSection("", signature);

		} catch (Exception e) {

			LOG.error("#CacableLedgerTemplateParser# parseSection with cache occur exception with stack trace ["+ExceptionUtils.getFullStackTrace(e)+"],decide to choose json parse");
		}

		return super.parseSection("", null);
	}
	
}
