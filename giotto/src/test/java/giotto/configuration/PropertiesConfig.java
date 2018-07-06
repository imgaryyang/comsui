package giotto.configuration;

import com.suidifu.hathaway.configurations.RedisConfig;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中属性配置文件
 * 
 * @author lisf
 *
 */
@Configuration
public class PropertiesConfig {

	@Bean(name = "cfg_datasource")
	@ConfigurationProperties(prefix = "hikaricp")
	public HikariConfig datasource() {
		return new HikariConfig();
	}

	@Bean(name = "cfg_redis")
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisConfig redis() {
		return new RedisConfig();
	}

}