package com.zufangbao.earth.yunxin.api.controller;


import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FILE_UNSUPPORTED;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.INVALID_PARAMS;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_REQUEST_NO;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REQUEST_NO_IS_EMPTY;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.CONTRACT_NOT_MATCH_FINANCIAL_CONTRACT;

import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.constant.AppendixConsts;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.yunxin.exception.FileUnsupportedException;
import com.zufangbao.sun.yunxin.handler.AppendixHandler;
import com.zufangbao.sun.yunxin.handler.AppendixLogHandler;
import com.zufangbao.sun.yunxin.service.files.AppendixLogService;
import com.zufangbao.sun.yunxin.service.files.AppendixService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequestMapping(ApiConstant.ApiUrlConstant.URL_API_V3)
@Controller
@Api(value = "五维金融贷后接口V3.0", description = "五维金融贷后接口V3.0")
public class AppendixApiController extends BaseApiController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private AppendixService appendixService;

    @Autowired
    private AppendixHandler appendixHandler;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private AppendixLogService appendixLogService;

    @Autowired
    private AppendixLogHandler appendixLogHandler;

    private static String uploadFilePath;

    @Value("#{config['uploadPath']}")
    private void setSavePath(String uploadPath){
        if(StringUtils.isEmpty(uploadPath)){
            uploadFilePath = getClass().getResource(".").getFile() + SAVE_DIR + File.separator;
        }else if(uploadPath.endsWith(File.separator)){
            uploadFilePath = uploadPath + SAVE_DIR + File.separator;
        }else{
            uploadFilePath = uploadPath + File.separator + SAVE_DIR + File.separator;
        }
    }

    private int NOT_SET_VALUE = -1;

    private String SAVE_DIR = "appendix";

    private String[] fileTypes = {"pdf","PDF"};

    private static final Log LOGGER = LogFactory.getLog(AppendixApiController.class);

    @RequestMapping(value = ApiConstant.ApiUrlConstant.UPLOAD_APPENDIX, method = RequestMethod.POST)
    @ApiOperation(value = "文件(潍闪通协议)上传接口", notes = "文件上传")
    @ResponseBody
    public String uploadAppendicesOfContract(HttpServletRequest request, HttpServletResponse response){
        try {
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;

            if (fileRequest.getContentLengthLong() > AppendixConsts.MAX_MESSAGE_LENGTH){
                return signErrorResult(response, new ApiException(INVALID_PARAMS, "文件超过限制(2M)"));
            }

            HashMap<String, String> allParameters = getAllParameters(request);

            String requestNo = allParameters.get("requestNo");

            if(StringUtils.isEmpty(requestNo)){
                return signErrorResult(response, new ApiException(REQUEST_NO_IS_EMPTY));
            }

            if(appendixLogService.existRequestNo(requestNo)){
                return signErrorResult(response, new ApiException(REPEAT_REQUEST_NO));
            }

            String productCode = allParameters.get("productCode");
            if (StringUtils.isEmpty(productCode)){
                return signErrorResult(response, new ApiException(INVALID_PARAMS,"productCode 未填写"));
            }

            FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productCode);
            if (financialContract == null){
                return signErrorResult(response, new ApiException(FINANCIAL_CONTRACT_NOT_EXIST));
            }

            String contractUniqueId = allParameters.get("contractUniqueId");
            if(StringUtils.isEmpty(contractUniqueId)){
                return signErrorResult(response, new ApiException(INVALID_PARAMS, "contractUniqueId 未填写"));
            }
            Contract contract = contractService.getContractByUniqueId(contractUniqueId);
            if (contract == null){
                return signErrorResult(response, new ApiException(CONTRACT_NOT_EXIST));
            }

            if (!Objects.equals(contract.getFinancialContractUuid(),financialContract.getFinancialContractUuid())){
                return signErrorResult(response, new ApiException(CONTRACT_NOT_MATCH_FINANCIAL_CONTRACT));
            }

            int fileSize = NOT_SET_VALUE;
            String fileSizeStr = allParameters.get("size");
            if(StringUtils.isNotEmpty(fileSizeStr)){
                try {
                    fileSize = Integer.parseInt(fileSizeStr);
                }catch (Exception e){
                    return signErrorResult(response, new ApiException(INVALID_PARAMS, "size 未填写 或 格式不正确"));
                }
            }

            Iterator<String> fileNames = fileRequest.getFileNames();

            if(fileSize!= NOT_SET_VALUE && !fileNames.hasNext() && fileSize != 0){
                return signErrorResult(response, new ApiException(INVALID_PARAMS, "文件数量不符"));
            }

            int count = 0;
            while (fileSize != NOT_SET_VALUE && fileNames.hasNext()) {
                String fileName = fileNames.next();
                List<MultipartFile> files = fileRequest.getFiles(fileName);
                count += files.size();
                if (count > fileSize || (!fileNames.hasNext()) && !Objects.equals(count, fileSize)) {
                    return signErrorResult(response, new ApiException(INVALID_PARAMS, "文件数量不符"));
                }
            }

            fileNames = fileRequest.getFileNames();
            List<String> filePathList = new ArrayList<>();
            while (fileNames.hasNext()) {
                String fileName = fileNames.next();
                List<MultipartFile> files = fileRequest.getFiles(fileName);
                for (MultipartFile multipartFile : files) {
                    String originalFilename = multipartFile.getOriginalFilename();
//                    if (!FilenameUtils.isExtension(originalFilename,fileTypes)) {
//                        LOGGER.error("FileUnsupportedException, ");
//                        throw new FileUnsupportedException();
//                    }
                    File temp = appendixHandler.saveAppendixToDisk(contractUniqueId, uploadFilePath, multipartFile);
                    if (temp == null) {
                        LOGGER.warn("saveUploadFile fail, temp file is null");
                        continue;
                    }
                    filePathList.add(temp.getAbsolutePath());
                    LOGGER.info("save file to service success, file path:" + temp.getAbsolutePath());
                }
            }

            appendixLogHandler.saveAppendices(filePathList, contractUniqueId,requestNo, AppendixConsts.GENERATE_WAY_INTERFACE);

            return signSucResult(response);
        }catch (FileUnsupportedException e) {
            e.printStackTrace();
            LOGGER.error("upload fail, 文件类型错误" + e.getMessage());
            return signErrorResult(response, new ApiException(FILE_UNSUPPORTED));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("upload fail, message:" + e.getMessage());
            return signErrorResult(response, e);
        }
    }

}
