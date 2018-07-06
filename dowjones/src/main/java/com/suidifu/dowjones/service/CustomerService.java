package com.suidifu.dowjones.service;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午7:20 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface CustomerService {
    String extractContractInfoByIDNumber(String idNumber) throws IOException, ParseException;

    String getStatisticByIDNumber(String idNumber) throws IOException;
}