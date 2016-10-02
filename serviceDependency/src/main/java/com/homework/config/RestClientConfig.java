package com.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
/**
 * Class that admin the app config, creating a RestOperations and  ClientHttpRequestFactory beans on the spring context.
 * 
 * @author francisco
 *
 */
@Component
public class RestClientConfig {
	
	/**
	 * Bena use to call Restful services
	 * @param clientHttpRequestFactory
	 * @return
	 */
	@Bean
	public RestOperations createRestTemplate(final ClientHttpRequestFactory  clientHttpRequestFactory) {
		return new RestTemplate(clientHttpRequestFactory);
	}
	
	/**
	 * bean use to create client HTTP request using a connect timeout and read timeout read from the app properties files.
	 * @param connectTimeout
	 * @param readTimeot
	 * @return
	 */
	@Bean
	public ClientHttpRequestFactory createClientHttpRequestFactory(@Value("${connect.timeout}") final int connectTimeout, @Value("${read.timeout}") final int readTimeot) {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout);
		httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeot);
		return httpComponentsClientHttpRequestFactory;
	}
	

}
