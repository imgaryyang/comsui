package com.zufangbao.earth.api.test.post;

import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class CommandUploadApiPost extends BaseApiTestPost{
	
	private final Log LOGGER = LogFactory.getLog(CommandUploadApiPost.class);

	@Test
	public void testCommandUploadApiPost() throws IOException {

		Map<String, String> requestParams = new HashMap<String, String>();
		CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost("http://yunxin.5veda.net/pre/api/upload/HA0100/10001");
//		HttpPost httppost = new HttpPost("http://127.0.0.1:9090/pre/api/upload/HA0100/10002");
//		HttpPost httppost = new HttpPost("http://127.0.0.1:9090/pre/api/upload/zhonghang/10001");
		HttpPost httppost = new HttpPost("http://127.0.0.1:9090/api/v3/appendices");
//		HttpPost httppost = new HttpPost("http://192.168.0.204/pre/api/upload/HA0100/10001");

//		File file = FileUtils.getFile("/Users/veda/work/docs/123.txt");
		File file = FileUtils.getFile("/Users/veda/work/docs/123.pdf");
		File file2 = FileUtils.getFile("/Users/veda/work/docs/456.pdf");

		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.setCharset(Charset.forName(HTTP.UTF_8));


		FileBody bin = new FileBody(file);
		FileBody bin2 = new FileBody(file2);
		reqEntity.addPart("file1", bin);
		reqEntity.addPart("file2", bin);
//		reqEntity.addPart("file3", bin2);
//		reqEntity.addPart("file4", bin);
//		reqEntity.addPart("file5", bin);
//		reqEntity.addPart("file6", bin2);

		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("contractUniqueId", "07e2afab-928b-4713-81a2-9e0f3b21f44b");
		requestParams.put("productCode", "G00003");
//		requestParams.put("size", "1" );

        for (Entry<String, String> e : requestParams.entrySet()) {
        	StringBody stringBody =  new StringBody(e.getValue(), contentType);
			reqEntity.addPart(e.getKey(), stringBody);
		}
        httppost.setEntity(reqEntity.build());  
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = "";
//		sign = ApiSignUtils.rsaSign(signContent, privateKey);
		LOGGER.info("【sign】" + sign);
//        httppost.addHeader("merId", "systemdeduct");
//        httppost.addHeader("secret", "628c8b28bb6fdf5c5add6f18da47f1a6");
        httppost.addHeader("merId", "t_test_zfb");
        httppost.addHeader("secret", "123456");
        httppost.addHeader("sign", sign);
        HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}
}
