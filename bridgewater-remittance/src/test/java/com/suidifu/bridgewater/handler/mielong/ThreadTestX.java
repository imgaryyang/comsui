package com.suidifu.bridgewater.handler.mielong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import com.suidifu.bridgewater.api.test.post.RemittanceMieLongPost;
import com.zufangbao.sun.utils.DateUtils;
public class ThreadTestX {
	public static final String TEST_MERID = "t_test_zfb";
	public static final String TEST_SECRET = "123456";
	public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
	public static LongAdder sucLongAdder = new LongAdder();
	public static LongAdder failLongAdder = new LongAdder();
	private static final String notifyUrl = "http://192.168.1.147:9092/common/xxxx"; 
	public static List<String> productCodes = Arrays.asList("G31700","G32000G","G26700");

	public static void main(String[] args) throws InterruptedException, IOException {

//		xxx();
//		run_start(args[0], args[1], args[2], args[3]);
		run_start("http://192.168.0.159:30203/api/command", "1", "1",notifyUrl);
	}
	public static void run_start(String b1, String b2, String b3, String b4) throws InterruptedException {
		
		String url = b1;
		int threadNum = Integer.valueOf(b2);
		int execCount = Integer.valueOf(b3);
		
		String start_date = DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
		long start = System.currentTimeMillis();
		RemittanceMieLongPost c = new RemittanceMieLongPost();
		System.out.println("url:[" + url + "],threanNum:[" + threadNum + "],execCount:[" + execCount + "].开始时间:["+start_date+"].");
		
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < threadNum; i++) {
			final int x = i;
			Thread thread = new Thread() {
				@Override
				public void run() {
					System.out.println(x);
//					c.testApiCommandRemittanceYX11111(url,execCount, notifyUrl, productCodes);
//					c.testApiCommandRemittanceYX22222(url,execCount, notifyUrl, productCodes);
					c.testApiCommandRemittanceYX33333(url,execCount, notifyUrl, productCodes);

				}
			};
			threads.add(thread);
			thread.start();
		}
		for(Thread t : threads) {
			t.join();
		}
		String end_date = DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
		long end = System.currentTimeMillis();
		System.out.println(threadNum+"x"+execCount+"测试结束.use time :["+(end-start)+"]ms.结束时间:["+end_date+"].成功:["+sucLongAdder+"],失败:["+failLongAdder+"].");
		
	}
	
	public static void xxx() {
		
		
		
		
		
		long i1 = 899995062000l;
		long i2 = 899995008000l;
		long i3 = 899994930000l;
		long total_i = i1 + i2 + i3 ;
		long total = 3*9000000000l;
		System.out.println(total - total_i);
		
		long i4 = 49911000l;
		long i5 = 49837500l;
		long i6 = 50251500l;
		
		System.out.println(i1 + i4);
		System.out.println(i2 + i5);
		System.out.println(i3 + i6);
		
	}
}