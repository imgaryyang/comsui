package com.suidifu.dowjones.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author veda
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzJob {

    /**
     * 信托合同uuid
     */
    private String financialContractUuid;

    /**
     * 统计的日期 "2018-02-03"
     */
    private Date constraintDate;

    /**
     * Job状态
     * 0 默认 已创建 用不到 已创建默认就处理中
     * 1 处理中
     * 2 完成
     * 3 异常
     */
    private Integer processStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * job 开始时间
     */
    private Date startTime;

    /**
     * job 结束时间
     */
    private Date endTime;

    /**
     * job 类型
     * 0 每日回购数据统计
     * 1 每日担保数据统计
     * 2 每日应还款数据统计
     * 3 每日实际还款数据统计
     */
    private Integer jobType;
}
