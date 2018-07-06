package com.suidifu.microservice.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.microservice.entity.JournalVoucher;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.AppArriveRecord;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.PaymentOrderModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;

@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements JournalVoucherService {

	
}