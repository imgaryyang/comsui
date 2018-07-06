package com.zufangbao.earth.web.controller.spark;

import com.demo2do.core.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zufangbao.earth.response.BaseResponse;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zufangbao.gluon.opensdk.HttpClientUtils.executePostRequest;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/12/13 <br>
 * @time: 15:35 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Controller
@RequestMapping("/spark")
@Slf4j
public class SparkController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("#{config['dynamicOverdueRatePath']}")
    private String dynamicOverdueRatePath = "";

    @Value("#{config['staticOverdueRateOfMonthPath']}")
    private String staticOverdueRateOfMonthPath = "";

    @PostMapping(value = "/OverdueAnalyze/DynamicOverdueRate")
    @ResponseBody
    public BaseResponse dynamicOverdueRate(@RequestBody String jsonString) throws IOException {
        log.info("\njsonString is:{}\ndynamicOverdueRatePath is:{}\n", jsonString, dynamicOverdueRatePath);
        return getBaseResponse(jsonString, dynamicOverdueRatePath);
    }

    @PostMapping(value = "/OverdueAnalyze/StaticOverdueRateOfMonth")
    @ResponseBody
    public BaseResponse staticOverdueRateOfMonth(@RequestBody String jsonString) throws IOException {
        log.info("\njsonString is:{}\nstaticOverdueRateOfMonthPath is:{}\n", jsonString, staticOverdueRateOfMonthPath);
        return getBaseResponse(jsonString, staticOverdueRateOfMonthPath);
    }

    private BaseResponse getBaseResponse(String jsonString, String path) throws IOException {
        try {
            Result result = this.executePostRequest(path, jsonString);
            String json = (String) result.getData().get(HttpClientUtils.DATA_RESPONSE_PACKET);
            return OBJECT_MAPPER.readValue(json, BaseResponse.class);
        } catch (Exception e){
            e.printStackTrace();
            BaseResponse br = new BaseResponse();
            br.setCode(200);
            br.setMessage("系统繁忙");
            return br;
        }
    }

    private Result executePostRequest(String url, String requestBody){

        HttpPost httpPost = new HttpPost(url);
        Result result = new Result();
        try {
            httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            CloseableHttpClient httpclient = HttpClients.createDefault();

            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000*3).setConnectTimeout(60000*3).setConnectionRequestTimeout(60000*3).build();
            httpPost.setConfig(requestConfig);

            HttpResponse httpResp = httpclient.execute(httpPost);

            int statusCode = httpResp.getStatusLine().getStatusCode();
            HttpEntity rspEntity = httpResp.getEntity();

            InputStream in = rspEntity.getContent();
            String strResp = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            result.data(HttpClientUtils.DATA_RESPONSE_PACKET,strResp);
            result.data(HttpClientUtils.DATA_RESPONSE_HTTP_STATUS, "" + statusCode);
            if (statusCode != HttpStatus.SC_OK
                && statusCode != HttpStatus.SC_BAD_GATEWAY
                && statusCode != HttpStatus.SC_GATEWAY_TIMEOUT) {
                return result.message("http响应失败！(" + statusCode + ")");
            }
            return result.success();
        } catch (ConnectException e) {
            return result.message("服务器请求超时！");
        } catch (SocketTimeoutException e) {
            return result.message("服务器响应超时!url:" + url);
        } catch (IOException e) {
            e.printStackTrace();
            return result.message("网络异常!url:" + url);
        } catch (Exception e) {
            e.printStackTrace();
            return result.message(e.getMessage() + "url:" + url);
        } finally {
            //释放连接
            httpPost.releaseConnection();
        }
    }
}