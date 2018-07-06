package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.ImportAssetPackageHandler;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/interface/interim")
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class TempAsyncImportAssetPackageController extends BaseApiController {
    @Resource
    private ImportAssetPackageHandler importAssetPackageHandler;

    @PostMapping(value = "/asyncImportAssetPackage")
    @ResponseBody
    public BaseResponse importAssetPackageAsync(HttpServletRequest request,
                                                HttpServletResponse response,
                                                String subRequestNo) throws Exception {
        log.info("TempAsyncImportAssetPackageController is beginning");
        String responseMessage = importAssetPackageHandler.asyncImportAssetPackage(subRequestNo);
        Map<String, String> data = new HashMap<>();
        data.put("processResponse", responseMessage);
        log.info("TempAsyncImportAssetPackageController finish");
        return wrapHttpServletResponse(response, ApiMessage.SUCCESS, data);
    }
}
