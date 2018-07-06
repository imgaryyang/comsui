package com.suidifu.morganstanley.controller.api.pre;

import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustProperties;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.model.request.pre.UploadFile;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.yunxin.entity.files.FileType;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_API;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_URL_UPLOAD_FILE;

/**
 * 前置接口API
 * @author louguanyang on 2017/4/24.
 */
@RestController
@RequestMapping(PRE_API)
@Api(tags = {"五维金融前置接口"}, description = " ")
@Log4j2
public class FileUploadController extends BaseApiController {
    @Resource
    @Qualifier("productCategoryCacheHandler")
    private ProductCategoryCacheHandler productCategoryCacheHandler;
    @Resource
    private FileRepositoryService fileRepositoryService;
    @Resource
    private YntrustProperties yntrustProperties;

    @ResponseBody
    @PostMapping(value = PRE_URL_UPLOAD_FILE)
    @ApiOperation(value = "文件上传前置接口", httpMethod = "POST", notes = "文件上传", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    public BaseResponse upload(HttpServletRequest request,
                               HttpServletResponse response,
                               @ApiParam(value = "商户代码", required = true, defaultValue = "HA0100")
                               @PathVariable String channelCode,
                               @ApiParam(value = "服务代码", required = true, defaultValue = "10001", allowableValues = "10001,10002")
                               @PathVariable String serviceCode,
                               @NotNull(message = "请上传文件")
                               @ApiParam(value = "上传文件", required = true)
                               @RequestParam("file") MultipartFile[] files,
                               @ApiParam(value = "uploadFile", required = true)
                               @Validated @ModelAttribute UploadFile uploadFile,
                               BindingResult bindingResult) {
        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        String tradeTime = uploadFile.getTradeTime();
        log.info("\n开始调用文件上传前置接口，请求参数：\n[channelCode: {}, \nserviceCode: {}, " +
                        "\nrequestNo: {},\ntradeTime: {}, \nipAddress:{}]\n",
                channelCode, serviceCode, uploadFile.getRequestNo(), tradeTime, IpUtils.getIpAddress(request));

        if (isNullTradeTime(tradeTime)) {
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "请输入正确的交易时间");
        }
        String requestUrl = request.getRequestURL().toString();
        String[] split = requestUrl.split(PRE_API);
        String url = split[1];
        ProductCategory productCategory = productCategoryCacheHandler.get(url, true);

        //信托产品代码
        String fileTypeCode = productCategory.getProductLv3Code();
        String productCode = productCategory.getProductLv1Code();

        if (isEmptyProductCodeOrFileTypeCode(fileTypeCode, productCode)) {
            throw new ApiException(ApiMessage.API_NOT_FOUND);
        }

        if (isNullFileType(fileTypeCode, serviceCode)) {
            throw new ApiException(ApiMessage.UNSUPPORTED_FILE_TYPE);
        }
        FileType fileType = FileType.fromTypeCode(fileTypeCode);

        List<String> filePathList;
        try {
            filePathList = uploadFiles(files, fileType);
        } catch (IOException e) {
            log.error("upload fail, message: {}", ExceptionUtils.getStackTrace(e));
            throw new ApiException(ApiMessage.UNSUPPORTED_FILE_TYPE);
        }

        fileRepositoryService.saveFileRepository(filePathList, productCode, fileTypeCode, getTradeTime(tradeTime));

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
    }

    private Date getTradeTime(String tradeTime) {
        return DateUtils.parseDate(tradeTime != null ? tradeTime : DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT), DateUtils.LONG_DATE_FORMAT);
    }

    private boolean isNullTradeTime(String tradeTime) {
        if (null == getTradeTime(tradeTime)) {
            log.warn("save File Repository fail, tradeTime is null");
            return true;
        }
        return false;
    }

    private boolean isNullFileType(String fileTypeCode, String serviceCode) {
        if (StringUtils.isEmpty(fileTypeCode)) {
            return true;
        }
        if (!StringUtils.equalsIngoreNull(fileTypeCode, serviceCode)) {
            log.warn("ProductCategory 表 ProductLv3Code 配置错误！");
            return true;
        }
        return false;
    }

    private boolean isEmptyProductCodeOrFileTypeCode(String fileTypeCode, String productCode) {
        if (StringUtils.isEmpty(fileTypeCode) || StringUtils.isEmpty(productCode)) {
            log.warn("save File Repository fail, productCode or fileTypeCode is empty");
            return true;
        }
        return false;
    }

    private List<String> uploadFiles(MultipartFile[] files, FileType fileType) throws IOException {
        List<String> filePathList = new ArrayList<>();
        log.info("\nFile length: {}\n", files.length);
        for (MultipartFile multipartFile : files) {
            String originalFileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();

            log.info("\noriginalFileName: {}\ncontentType: {}\n", originalFileName, contentType);

            if (!FilenameUtils.isExtension(originalFileName, fileType.getSupportExtensions())) {
                log.error("FileUnsupportedException");
                throw new ApiException(ApiMessage.FILE_UNSUPPORTED);
            }

            String uploadPath = yntrustProperties.getUploadPath();
            File temp = FileUtils.saveUploadFile(uploadPath, multipartFile);
            if (temp == null) {
                log.warn("saveUploadFile fail, temp file is null");
                continue;
            }

            //csv file check first line
            if (FilenameUtils.isExtension(originalFileName, FilenameUtils.CSV_EXTENSIONS) &&
                    !CsvUtils.checkFirstLine(temp, fileType)) {
                log.warn("csv 文件内容格式错误");
                throw new ApiException(ApiMessage.FILE_FORMAT_ERROR);
            }

            filePathList.add(temp.getPath());
            log.info("save file to service success, file path:{}", temp.getPath());
        }
        return filePathList;
    }
}