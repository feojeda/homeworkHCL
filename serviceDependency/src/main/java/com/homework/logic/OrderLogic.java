package com.homework.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import com.homework.entities.Order;

@Controller
public class OrderLogic {
	@Autowired
	private RestOperations restTemplate;
	private String url;

	@Autowired
	public OrderLogic(@Value("${service.url}") String url) {
		this.url = url;
	}

	public String processOrder(Order order) {
		UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
		
		String urlServiceA = uriTemplate.expand(url, "a").toString();
		String urlServiceAA = uriTemplate.expand(url, "aa").toString();
		String urlServiceB = uriTemplate.expand(url, "b").toString();
		String urlServiceC = uriTemplate.expand(url, "c").toString();
		
		String resultA = restTemplate.getForObject(urlServiceA, String.class);
		String resultAA = restTemplate.getForObject(urlServiceAA, String.class,resultA);
		String resultB = restTemplate.getForObject(urlServiceB, String.class);
		String resultC = restTemplate.getForObject(urlServiceC, String.class, resultAA, resultB);	
		
		
		
		return resultC;
	}

}
