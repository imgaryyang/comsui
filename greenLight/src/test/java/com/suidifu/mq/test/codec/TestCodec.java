package com.suidifu.mq.test.codec;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestCodec {
	private Request request;

	@Before
	public void init() {
		request = new Request();
		request.setBean("TestBean");
		request.setMethod("test");
		request.setParams(new Object[] { "hello测试..." });
		request.setParamTypes(new String[] { String.class.getTypeName() });
	}

	@Test
	public void test_kyro() {
		String encodestr = CodecType.KYRO.getCodec().encodeRequest(request);
		Request decodeRequest = CodecType.KYRO.getCodec().decodeRequest(encodestr);
		Assert.assertEquals("kyro序列化反序列化结果不一致", request.getBean(), decodeRequest.getBean());
	}

	@Test
	public void test_kyro_exception() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Exception error = new NullPointerException("空指针异常测试");
		Response response = new Response();
		response.setResult(request);
		response.setError(error, null, null);
		String encodestr = CodecType.KYRO.getCodec().encodeObject(response);
		Response decodeResponse = CodecType.KYRO.getCodec().decodeObject(encodestr, Response.class);
		System.out.println(decodeResponse.getStackTrace());
		Assert.assertEquals("kyro序列化反序列化结果不一致", request.getBean(), ((Request)decodeResponse.getResult()).getBean());
		Assert.assertTrue((decodeResponse.newThrowable() instanceof NullPointerException));
	}

	@Test
	public void test_json() {
		String encodestr = CodecType.JSON.getCodec().encodeRequest(request);
		Request decodeRequest = CodecType.JSON.getCodec().decodeRequest(encodestr);
		Assert.assertEquals("json序列化反序列化结果不一致", request.getBean(), decodeRequest.getBean());
	}

}
