package uk.co.cloudmatica.truproxyapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import uk.co.cloudmatica.truproxyapi.handler.ProxyHandler;
import uk.co.cloudmatica.truproxyapi.repo.CompanyRepoRemote;
import uk.co.cloudmatica.truproxyapi.service.ProxyService;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@Slf4j
@EnableHypermediaSupport(type = HAL)
@Configuration
public class WebConfig {

    private final String proxyEndpoint;
    private final String apiKey;
    private final String apiValue;
    private final static String PROXY = "/proxy";

    public WebConfig(@Value("${proxy.endpoint}") final String proxyEndpoint,
                     @Value("${proxy.apiKey:mockapikey}") final String apiKey,
                     @Value("${proxy.apiValue:mockapivalue}") final String apiValue) {

        this.proxyEndpoint = proxyEndpoint;
        this.apiKey = apiKey;
        this.apiValue = apiValue;
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {

        return WebClient.builder()
            .defaultHeader(apiKey, apiValue)
            .baseUrl(proxyEndpoint)
            .filter(logRequest())
            .filter(logRequest())
            .build();
    }

    @Bean
    WebClient.Builder webClientBuilder(final HypermediaWebClientConfigurer configurer) {
        return configurer.registerHypermediaTypes(WebClient.builder());
    }

    // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return just(clientRequest);
        });
    }

    @Bean
    WebClientCustomizer webClientCustomizer(final HypermediaWebClientConfigurer configurer) {
        return configurer::registerHypermediaTypes;
    }

    @Bean
    CompanyRepoRemote companyRepoRemote(@Value("${proxy.endpoint}") final String url,
                                        final WebClient webClient) {

        return new CompanyRepoRemote(proxyEndpoint, webClient);
    }

    @Bean
    RouterFunction<ServerResponse> route(final ProxyHandler proxyHandler) {

        return RouterFunctions.route(RequestPredicates.POST(PROXY)
                .and(RequestPredicates.accept(APPLICATION_JSON)), proxyHandler::proxy);
    }

    @Bean
    ProxyHandler proxyHandler(final ProxyService proxyService) {

        return new ProxyHandler(proxyService);
    }
}