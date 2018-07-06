package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/interface/interim")
@Log4j2

public class VirtualCallbackController extends BaseApiController {

    @PostMapping(value = "/virtualCallback")
    @ResponseBody
    public BaseResponse importAssetPackageAsync(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Map<String, String> result) throws Exception {
        log.info("VirtualCallback is beginning");
        for (Map.Entry<String, String> entry : result.entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue());
        }
        log.info("VirtualCallback finish");

//        String requestNo = result.get("requestNo");
//        String requestIp = result.get("requestIp");
//        String requestTime = result.get("requestTime");
//        String completeTime = result.get("completeTime");
//        String contractDetailSize = result.get("contractDetailSize");
//        String financialProductCode = result.get("financialProductCode");
//        String contractsTotalNumber = result.get("contractsTotalNumber");
//        String contractsTotalAmount = result.get("contractsTotalAmount");
//        String successCount = result.get("successCount");

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS, result);
    }
}
