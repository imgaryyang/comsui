package com.suidifu.morganstanley.utils;

import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Log4j2
public class PostTestUtils {
    private PostTestUtils() {
    }

    public static String sendPost(String url,
                                  Map<String, String> params,
                                  Map<String, String> headerMap) {
        StringBuilder result = new StringBuilder();
        URLConnection conn = null;

        try {
            // 打开和URL之间的连接
            conn = new URL(url).openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            if (headerMap != null) {
                for (Entry<String, String> entry : headerMap.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } catch (IOException e) {
            log.error("发送 POST 请求出现异常: {}", ExceptionUtils.getStackTrace(e));
        }

        assert conn != null;
        try (// 获取URLConnection对象对应的输出流
             PrintWriter out = new PrintWriter(conn.getOutputStream());
             //定义BufferedReader输入流来读取URL的响应
             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            // 发送请求参数
            out.print(buildParams(params));
            // flush输出流的缓冲
            out.flush();

            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (NullPointerException | IOException e) {
            log.error("发送 POST 请求出现异常: {}", ExceptionUtils.getStackTrace(e));
        }
        return result.toString();
    }

    private static String buildParams(Map<String, String> params) {
        StringBuilder buffer = new StringBuilder();
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        for (Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            value = StringUtils.isEmpty(value) ? "" : value;
            buffer.append(entry.getKey()).append("=").append(value).append("&");
        }
        return buffer.toString();
    }
}