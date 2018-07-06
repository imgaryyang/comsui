package com.suidifu.dowjones.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 17:15 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@Data
@Component
public class HTTPCallBack {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String url;
    private String endPoint;

    public static HTTPCallBack getInstance() {
        return new HTTPCallBack();
    }

    private static int getResponseStatus(Response response) {
        if (response.getStatus() != 200 && response.getStatus() != 204) {
            throw new RuntimeException("Failed with HTTP error code: " + response.getStatus());
        }
        log.info("response body is:{}", response.readEntity(String.class));

        return response.getStatus();
    }

    private static String getResponseMessage(Response response) {
        if (response.getStatus() != 200 && response.getStatus() != 204) {
            throw new RuntimeException("Failed with HTTP error code: " + response.getStatus());
        }

        return response.readEntity(String.class);
    }

    public String post(List<String> fileNameList, String dataStreamUuid) throws JsonProcessingException {
        log.info("url is:{}", url);
        log.info("endPoint is:{}", endPoint);

        Client client = ClientBuilder.newClient();

        String jsonString = OBJECT_MAPPER.writeValueAsString(fileNameList);

        WebTarget target = client.target(url).path(endPoint + Constants.SLASH + dataStreamUuid);
        target.property("Content-Type", "application/json;charset=utf-8");

        return getResponseMessage(target.request().
                acceptEncoding(Constants.UTF_8).
                accept(MediaType.APPLICATION_JSON).
                post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE)));
    }
}