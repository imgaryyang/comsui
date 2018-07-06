package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.zufangbao.sun.entity.contract.ContractSourceDocumentDetailMapper;
import java.util.List;

/**
 * @author louguanyang
 */
public interface SourceDocumentDetailService extends GenericService<SourceDocumentDetail> {

  SourceDocumentDetail getSourceDocumentDetail(String sourceDocumentDetailUuid);

  List<SourceDocumentDetail> getValidDeductSourceDocumentDetailsBySourceDocumentUuid(String sourceDocumentUuid);

  List<ContractSourceDocumentDetailMapper> getContractUuidSourceDocumentDetailUuidMapper(
      List<String> sourceDocumentDetailUuidList);

}