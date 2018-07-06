package com.suidifu.morganstanley.controller.api.dailyfile;

import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.model.request.dailyfile.RebuildDailyFile;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.DateUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.wellsfargo.yunxin.data.sync.entity.DailyFileType;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.RebuildDailyFileHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_IMPORT_ASSET_PACKAGE;
import static com.zufangbao.wellsfargo.yunxin.data.sync.entity.DailyFileType.DAILY_RECONCILIATION_FILE_TYPE;

/**
 * 导出每日对账文件导出
 *
 * @author fanxiaofan
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"重新生成日常对账文件"}, description = " ")
public class DailyFileController extends BaseApiController {

  @Resource
  private RebuildDailyFileHandler rebuildDailyFileHandler;
  @Resource
  private YntrustFileTask yntrustFileTask;

  @ApiOperation(value = "重新生成日常对账文件", httpMethod = "POST", notes = "重新生成日常对账文件", response = BaseResponse.class)
  @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
  @PostMapping(value = "/rebuildDailyReconciliationFile")
  public BaseResponse rebuildDailyReconciliationFile(
      HttpServletRequest request, HttpServletResponse response,
      @Validated @ModelAttribute RebuildDailyFile rebuildDailyFile, BindingResult bindingResult) {
    BaseResponse baseResponse = getValidatedResult(bindingResult);
    if (baseResponse != null) {
      return baseResponse;
    }
    DailyFileType fileType = rebuildDailyFile.getFileTypeByCode();
    if (null == fileType) {
      throw new ApiException(ApiMessage.INVALID_PARAMS);
    }
    if (!DAILY_RECONCILIATION_FILE_TYPE.contains(fileType)) {
      throw new ApiException(ApiMessage.INVALID_PARAMS);
    }
    Date queryDate = rebuildDailyFile.acquireQueryDate();
    Date endDate = null;
    if (StringUtils.isNotEmpty(rebuildDailyFile.getEndDate())) {
      endDate = rebuildDailyFile.acquireEndDate();
    }
    String path = yntrustFileTask.getRebuildPath();
    List<String> dataUniqueIdList = rebuildDailyFileHandler
        .acquireCandidateKeyList(fileType, rebuildDailyFile.getFinancialContractNo(), queryDate,
            endDate);
    rebuildDailyFileHandler.rebuildDailyReconciliationFile(fileType, dataUniqueIdList, path, true);
    return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
  }
}