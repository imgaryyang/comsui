package com.zufangbao.earth.web.controller.bankCard;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.model.BankCardModel;
import com.zufangbao.sun.yunxin.handler.BankCardHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/bankCard")
public class BankCardController {

    @Autowired
    private BankCardHandler bankCardHandler;

    /**
     * @param outerIdentifier 外部关联编号
     * @param identityOrdinal 关联实体类型
     * @param bankCardModel
     * @return
     */
    @RequestMapping("/add")
    public @ResponseBody
    String addBankCard(@RequestParam("outerIdentifier") String outerIdentifier,
                       @RequestParam("identityOrdinal") int identityOrdinal,
                       @ModelAttribute BankCardModel bankCardModel,
                       @Secure Principal principal, HttpServletRequest request) {
        Result result = new Result();
        try {
            String msg = bankCardHandler.add(outerIdentifier, identityOrdinal, bankCardModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return JsonUtils.toJsonString(result.fail().message(msg));
            }
            return JsonUtils.toJsonString(result.success());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJsonString(result.fail());
        }

    }

    /**
     * @param uuid            银行卡uuid
     * @param identityOrdinal 关联实体类型
     * @param bankCardModel
     * @return
     */
    @RequestMapping("/modify")
    public @ResponseBody
    String modifyBankCard(@RequestParam("uuid") String uuid,
                          @RequestParam("identityOrdinal") int identityOrdinal,
                          @ModelAttribute BankCardModel bankCardModel,
                          @Secure Principal principal, HttpServletRequest request) {

        Result result = new Result();
        try {
            String msg = bankCardHandler.modify(uuid, identityOrdinal, bankCardModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return JsonUtils.toJsonString(result.fail().message(msg));
            }
            return JsonUtils.toJsonString(result.success());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJsonString(result.fail());
        }

    }

    /**
     * @param uuid 银行卡uuid
     * @return
     */
    @RequestMapping("/delete")
    public @ResponseBody
    String deleteBankCard(@RequestParam("uuid") String uuid,
                          @Secure Principal principal, HttpServletRequest request) {

        Result result = new Result();
        try {
            String msg = bankCardHandler.delete(uuid, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return JsonUtils.toJsonString(result.fail().message(msg));
            }
            return JsonUtils.toJsonString(result.success());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJsonString(result.fail());
        }

    }
}
