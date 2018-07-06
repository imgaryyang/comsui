package com.suidifu.morganstanley.configuration;

import com.suidifu.mq.config.RabbitMqConfig;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中属性配置文件
 *
 * @author lisf
 */
@Configuration
public class PropertiesConfig {
    @Bean(name = "cfg_rabbitmq_producer")
    @ConfigurationProperties(prefix = "rabbitmq.producer")
    public RabbitMqConfig mq1() {
        return new RabbitMqConfig();
    }


    @Bean
    public EmbeddedServletContainerCustomizer jettyCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof JettyEmbeddedServletContainerFactory) {
                    JettyEmbeddedServletContainerFactory factory = (JettyEmbeddedServletContainerFactory) container;
                    factory.addServerCustomizers(new JettyServerCustomizer() {

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

                    factory.addServerCustomizers(new JettyServerCustomizer() {
                        @Override
                        public void customize(Server server) {
                            jettyServerCustomizer(server);
                        }
                        private void jettyServerCustomizer(Server server) {
                            final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
                            threadPool.setMaxThreads(110);
                            threadPool.setMinThreads(20);
                        }
                    });
                }
            }
        };
    }
}

