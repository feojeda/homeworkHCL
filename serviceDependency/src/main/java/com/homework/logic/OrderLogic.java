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


/**
 * Class provides two method to process Orders: processOrder (single thread) and processOrderParallel (multithread).
 * To process a order needs call 4 services: A, AA, B and C. the processOrder executes a lineal secuencial call of this services,
 * and processOrderParallel executes 3 thread to parallelized the services call.
 * 
 * @author francisco
 *
 */
@Controller
public class OrderLogic {
	@Autowired
	private RestOperations restTemplate;
	private String url;
	/**
	 * Is a Executor service that execute a set of Callables objects on diferences threads.
	 */
	private static final ExecutorService threadpool =  Executors.newFixedThreadPool(3);

	/**
	 * get the Restful url services from the app properties files.
	 * @param url
	 */
	@Autowired
	public OrderLogic(@Value("${service.url}") String url) {
		this.url = url;
	}

	
	/**
	 * This method uses a single thread to call the 4 services on a lineal way.
	 * to call it used a restTemplate object defined on RestClientConfig class.
	 * @param order: order to process
	 * @return
	 */
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
	
	/**
	 * This method uses a RestTask objects to call every Restful service on a parallel thread.
	 * The RestTask class is a class create into this class and implements the Calleble Interface.
	 * This RestTask object are executed by a ExecutorServices called threadpool.
	 * Order of execution:
	 * A and B are executed on parallel theads
	 * AA is executed on a parallel thread after finish the execution of A
	 * C is executed on a parallel thread after finish the execution of AA and B
	 * @param order: order to process
	 * @return
	 */
	public String processOrderParallel(Order order) throws InterruptedException, ExecutionException {
		UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
		
		/*
		 * create the urls of the restful services using a url base
		 */
		String urlServiceA = uriTemplate.expand(url, "a").toString();
		String urlServiceAA = uriTemplate.expand(url, "aa").toString();
		String urlServiceB = uriTemplate.expand(url, "b").toString();
		String urlServiceC = uriTemplate.expand(url, "c").toString();
		
		/*
		 * creating 2 RestTask object to services A and B
		 */
		RestTask<String> taskA = new RestTask<String>(restTemplate, urlServiceA, String.class);
		RestTask<String> taskB = new RestTask<String>(restTemplate, urlServiceB, String.class);
		
		/*
		 * execute the call of services A and B using the ExecutorServices. This executorServices return a Future Object
		 * and from it we can get the services call result.
		 */
		Future<String> futureA = threadpool.submit(taskA);
		Future<String> futureB = threadpool.submit(taskB);
		
		
		/*
		 * get the result of call A and B. 
		 */
		String resultA = futureA.get();
		String resultB = futureB.get();
		
		/*
		 * in this point the call of A and B services are finish and we can call the AA services.
		 * create a RestTask to AA services.
		 */		
		RestTask<String> taskAA = new RestTask<String>(restTemplate, urlServiceAA, String.class, resultA);
		
		/*
		 * Execute the call of AA services using the ExecutorServices called threadpool
		 */		
		Future<String> futureAA = threadpool.submit(taskAA);
		
		/*
		 * get result of call AA services.
		 */
		String resultAA = futureAA.get();
		
		/*
		 * In this point the call of AA and B are finish and we can call the C services.
		 * Create a RestTask to C services.
		 */		
		RestTask<String> taskC = new RestTask<String>(restTemplate, urlServiceC, String.class, resultAA,resultB);
		
		/*
		 * Execute the call of C services using the ExecutorServices.
		 */
		Future<String> futureC = threadpool.submit(taskC);
		
		/*
		 * get result of the call C services.
		 */
		String resultC = futureC.get();
		
				
		return resultC;
	}
	
	/**
	 * This class implements the Callable interfaces and can be used to execute the call method and return a T class
	 * on a asynchronous way (parallel trhead). On the call method is invoke a restful services.
	 * @author francisco
	 *
	 * @param <T>
	 */
	class RestTask<T> implements Callable<T> {

		private String url;
		private Class<T> responseType;
		private Object[] params;
		private RestOperations restTemplate;
		
		/**
		 * Constructor
		 * @param restTemplate used to call a restful service
		 * @param url url of the restful service.
		 * @param responseType type of response of the restful services and the call methods.
		 * @param params parameters list of the restful service
		 */
		public RestTask(RestOperations restTemplate, String url, Class<T> responseType, Object ...params ) { 
			this.restTemplate = restTemplate;
			this.url = url;
			this.responseType = responseType;
			this.params = params;
		}
		
		@Override
		public T call() throws Exception {
			T result;
			/*
			 * if the parameters list is null or empty the rest service is called without parameters, else is called using
			 * the parameter list (params)
			 */
			if(params == null || params.length == 0) {
				result = restTemplate.getForObject(url,responseType ); 
			} else {
				result = restTemplate.getForObject(url,responseType, params );
			}
			return result;

		}
		
	}

}
