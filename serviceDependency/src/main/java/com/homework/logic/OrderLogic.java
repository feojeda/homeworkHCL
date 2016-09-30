package com.homework.logic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	
	private static final ExecutorService threadpool =  Executors.newFixedThreadPool(3);

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
	
	public String processOrderParallel(Order order) throws InterruptedException, ExecutionException {
		UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
		
		String urlServiceA = uriTemplate.expand(url, "a").toString();
		String urlServiceAA = uriTemplate.expand(url, "aa").toString();
		String urlServiceB = uriTemplate.expand(url, "b").toString();
		String urlServiceC = uriTemplate.expand(url, "c").toString();
		
		RestTask<String> taskA = new RestTask<String>(restTemplate, urlServiceA, String.class);
		RestTask<String> taskB = new RestTask<String>(restTemplate, urlServiceB, String.class);
		
		Future<String> futureA = threadpool.submit(taskA);
		Future<String> futureB = threadpool.submit(taskB);
		
		
		String resultA = futureA.get();
		String resultB = futureB.get();
		
		RestTask<String> taskAA = new RestTask<String>(restTemplate, urlServiceAA, String.class, resultA);
		
		Future<String> futureAA = threadpool.submit(taskAA);
		
		String resultAA = futureAA.get();
		
		RestTask<String> taskC = new RestTask<String>(restTemplate, urlServiceC, String.class, resultAA,resultB);
		Future<String> futureC = threadpool.submit(taskC);
		
		String resultC = futureC.get();
		
		
		
		return resultC;
	}
	
	class RestTask<T> implements Callable<T> {

		private String url;
		private Class<T> responseType;
		private Object[] params;
		private RestOperations restTemplate;
		
		public RestTask(RestOperations restTemplate, String url, Class<T> responseType, Object ...params ) { 
			this.restTemplate = restTemplate;
			this.url = url;
			this.responseType = responseType;
			this.params = params;
		}
		
		@Override
		public T call() throws Exception {
			T result;
			if(params == null || params.length == 0) {
				result = restTemplate.getForObject(url,responseType ); 
			} else {
				result = restTemplate.getForObject(url,responseType, params );
			}
			return result;

		}
		
	}

}
