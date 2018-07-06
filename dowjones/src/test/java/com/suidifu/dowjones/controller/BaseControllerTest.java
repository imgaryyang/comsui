package com.suidifu.dowjones.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.dowjones.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User:Administrator(吴峻申)
 * Date:2016-4-7
 * Time:13:51
 * Mail:frank_wjs@hotmail.com
 */
@Slf4j
public class BaseControllerTest {
    private static final String END_POINT = "http://192.168.1.44:8080/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private WebTarget target;
    private Client client;
    private List<String> fileNameList;
    private String jsonString;
    private String dataStreamUuid;

    private static int getResponseStatus(Response response) {
        if (response.getStatus() != 200 && response.getStatus() != 204) {
            throw new RuntimeException("Failed with HTTP error code: " + response.getStatus());
        }
        log.info("response body is:{}", response.readEntity(String.class));

        return response.getStatus();
    }

    private void initTarget(String path) {
        target = client.target(END_POINT).path(path);
        target.property("Content-Type", "application/json;charset=utf-8");
    }

    @Before
    public void setUp() throws JsonProcessingException {
        client = ClientBuilder.newClient();

        fileNameList = new ArrayList<>();
        fileNameList.add("data.csv");

        jsonString = OBJECT_MAPPER.writeValueAsString(fileNameList);
        dataStreamUuid = "123456";
    }

    @After
    public void tearDown() {
        client = null;
        target = null;

        fileNameList = null;
        jsonString = null;
        dataStreamUuid = null;
    }

    @Test
    public void post() {
        initTarget("datastream/tasknotify/" + dataStreamUuid);

        assertThat(getResponseStatus(target.request().
                acceptEncoding(Constants.UTF_8).
                accept(MediaType.APPLICATION_JSON).
                post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE))), is(HttpStatus.SC_OK));
    }
}