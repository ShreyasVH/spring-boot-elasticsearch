package com.example.demo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ESConfig extends ElasticsearchConfiguration {
    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private String elasticsearchPort;

    @Value("${elasticsearch.username}")
    private String elasticsearchUsername;

    @Value("${elasticsearch.password}")
    private String elasticsearchPassword;

    @Value("${elasticsearch.scheme}")
    private String elasticsearchScheme;

    @Value("${elasticsearch.use.credentials}")
    private String elasticsearchUseCredentials;

    @Override
    public ClientConfiguration clientConfiguration() {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder secureBuilder = ClientConfiguration.builder()
                .connectedTo( elasticsearchHost + ":" + elasticsearchPort);

        if("https".equals(elasticsearchScheme))
        {
            secureBuilder.usingSsl();
        }

        ClientConfiguration.TerminalClientConfigurationBuilder builder = secureBuilder;

        if("1".equals(elasticsearchUseCredentials))
        {
            builder = builder.withBasicAuth(elasticsearchUsername, elasticsearchPassword);
        }

        return builder
                .build();
    }
}
