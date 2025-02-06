package site.soobin.myselectshop.commons.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
        .requestFactory(
            () -> {
              HttpComponentsClientHttpRequestFactory factory =
                  new HttpComponentsClientHttpRequestFactory();
              RequestConfig requestConfig =
                  RequestConfig.custom()
                      .setConnectTimeout(Timeout.ofSeconds(5)) // 연결 타임아웃
                      .setResponseTimeout(Timeout.ofSeconds(5)) // 읽기 타임아웃
                      .build();
              factory.setHttpClient(
                  HttpClients.custom().setDefaultRequestConfig(requestConfig).build());
              return factory;
            })
        .build();
  }
}
