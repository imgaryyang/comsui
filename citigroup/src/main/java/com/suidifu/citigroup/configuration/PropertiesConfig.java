package com.suidifu.citigroup.configuration;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.hathaway.configurations.RedisConfig;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.mq.config.RabbitMqConnectionConfig;
import com.zaxxer.hikari.HikariConfig;

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

	@Bean(name = "cfg_notify_sever")
	@ConfigurationProperties(prefix = "notifyserver.mq")
	public NotifyServerConfig notifyServer() {
		return new NotifyServerConfig();
	}
	
	@Bean(name = "rabbit_mq_connection")
	@ConfigurationProperties(prefix = "rabbit_mq_connection")
	public RabbitMqConnectionConfig connectionConfig() {
		return new RabbitMqConnectionConfig();
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer jettyCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {

	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	            if (container instanceof JettyEmbeddedServletContainerFactory) {
	                ((JettyEmbeddedServletContainerFactory) container)
	                        .addServerCustomizers(new JettyServerCustomizer() {

	                    @Override
	                    public void customize(Server server) {
	                        setHandlerMaxHttpPostSize(200 * 1024 * 1024, server.getHandlers());
	                    }

	                    private void setHandlerMaxHttpPostSize(int maxHttpPostSize,
	                            Handler... handlers) {
	                        for (Handler handler : handlers) {
	                            if (handler instanceof ContextHandler) {
	                                ((ContextHandler) handler)
	                                        .setMaxFormContentSize(maxHttpPostSize);
	                            }
	                            else if (handler instanceof HandlerWrapper) {
	                                setHandlerMaxHttpPostSize(maxHttpPostSize,
	                                        ((HandlerWrapper) handler).getHandler());
	                            }
	                            else if (handler instanceof HandlerCollection) {
	                                setHandlerMaxHttpPostSize(maxHttpPostSize,
	                                        ((HandlerCollection) handler).getHandlers());
	                            }
	                        }
	                    }
	                });
	            }
	        }
	    };
	}
}