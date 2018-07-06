package com.zufangbao.earth.cucumber.LoanCallBack;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by dzz on 17-4-1.
 */
public class HttpServer1 implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            InputStream is = exchange.getRequestBody();
            //read(is); // .. read the request body
            String response = "This is the response";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8888),0);
        server.createContext("/testUrl", new HttpServer1());
        server.setExecutor(null); // creates a default executor
        server.start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
