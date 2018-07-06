package com.suidifu.morganstanley.handler.repayment;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 提前全额还款接口 Handler
 *
 * @author louguanyang at 2017/10/17 16:50
 * @mail louguanyang@hzsuidifu.com
 */
public interface PrepaymentApiHandler {
    /**
     * 检查是否存在重复请求编号
     *
     * @param requestNo 请求编号
     * @param ip        IP地址
     */
    void checkRequestNo(@NotBlank String requestNo, @NotBlank String ip);

}
