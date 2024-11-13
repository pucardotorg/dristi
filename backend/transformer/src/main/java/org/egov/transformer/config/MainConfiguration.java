package org.egov.transformer.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.egov.tracer.config.TracerConfiguration;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.TimeZone;

@Import({TracerConfiguration.class})
@Configuration
@ComponentScan(basePackages = {"org.egov"})
@Slf4j
public class MainConfiguration {

    @Value("${app.timezone}")
    private String timeZone;

    @Autowired
    private DashboardReportProperties properties;

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }


    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public ElasticsearchClient getRestClient(){
        RestClientBuilder restClientBuilder= RestClient.builder(new HttpHost(properties.getElasticsearchServerHost(),new Integer(properties.getElasticsearchServerPort()), properties.getElasticsearchConnectionType()));

        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback= new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                final CredentialsProvider credentialsProvider= new BasicCredentialsProvider();
                AuthScope authScope= new AuthScope(HttpHost.create(properties.getElasticsearchServerHost()));
                UsernamePasswordCredentials usernamePasswordCredentials= new UsernamePasswordCredentials(properties.getElasticsearchUserName(),properties.getElasticsearchUserPassword());
                credentialsProvider.setCredentials(authScope,usernamePasswordCredentials);
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        };

        restClientBuilder.setHttpClientConfigCallback(httpClientConfigCallback);
        RestClient restClient= restClientBuilder.build();


        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}