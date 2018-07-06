package com.suidifu.watchman.common.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.EnableCaching;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author louguanyang at 2018/1/10 18:25
 * @mail louguanyang@hzsuidifu.com
 */
@EnableCaching
public class IpConverter extends ClassicConverter {

//  @Autowired
//  private CacheManager cacheManager;

//  private static final String IP_ADDRESS_KEY = "ipAddress";
//  private final ConcurrentMap<String, String> cacheMap = new ConcurrentHashMap<>(2);

    @Override
    public String convert(ILoggingEvent event) {
        return getIpAddress();
    }

    private String getIpAddress() {
        try {
//      Cache cache = cacheManager.getCache("log");
//      String ip = cache.get("ip", String.class);
//
//      if (ip == null) {
//        System.out.println("cache miss, get from cacheMap");
//        ip = InetAddress.getLocalHost().getHostAddress();
//        cache.put("ip", ip);
//      }
//      return ip;
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

}
