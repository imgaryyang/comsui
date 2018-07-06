package com.suidifu.dowjones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/30 <br>
 * @time: 上午12:14 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class StatisticsReport implements Serializable {
    private String denominator;//分子
    private String numerator;//分母
    private String quotient;//商数(以%表示)
    private String statisticsDate;//只包含年月日
    private int computeType;//计算类型：0剩余本金 1剩余本息
    private int formulaId;//公式id：0动态池逾期率 1月度静态池逾期率
    private String denominatorDesc; //分子中文描述
    private String numeratorDesc;//分母中文描述
    private String quotientDesc;//商数描述(以%表示)
    private int statisticsNumber; //统计笔数

    public static StatisticsReport parseRating(String str) {
        String[] fields = str.split(",");
        if (fields.length != 10) {
            log.info("The elements are ::");
            Stream.of(fields).forEach(log::info);
            throw new IllegalArgumentException("Each line must contain 10 fields while the current line has ::" + fields.length);
        }

        String denominator = fields[0].trim();
        String numerator = fields[1].trim();
        String quotient = fields[2].trim();
        String statisticsDate = fields[3].trim();
        int computeType = Integer.parseInt(fields[4]);
        int formulaId = Integer.parseInt(fields[5]);
        String denominatorDesc = fields[6].trim();
        String numeratorDesc = fields[7].trim();
        String quotientDesc = fields[8].trim();
        int statisticsNumber = Integer.parseInt(fields[9]);

        return new StatisticsReport(denominator, numerator, quotient, statisticsDate, computeType, formulaId, denominatorDesc, numeratorDesc,
                quotientDesc, statisticsNumber);
    }

}