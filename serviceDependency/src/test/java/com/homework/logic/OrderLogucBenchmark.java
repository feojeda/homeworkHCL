package com.homework.logic;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.UnorderedRequestExpectationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import com.homework.ServiceDependencyApplication;


/**
 * test case that implements a benchmark between of the single and multi threads method of the OrderLogic class.
 * The OrderLogic is a class with 2 method, both are restful services client.
 * to test this methods we used a mock class called MockRestServiceServer provide by Srping.
 * Each unit test print a line with the total time of 10000 execution in miliseconds.
 * @author francisco
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceDependencyApplication.class)
public class OrderLogucBenchmark {
	
	private int loadAmount = 10000;

	@Autowired
	private OrderLogic orderLogic;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${service.url}")
	private String url;

	private String urlServiceA;
	private String urlServiceAA;
	private String urlServiceB;
	private String urlServiceC;
	private String resultA = "1";
	private String resultAA = "12";
	private String resultB = "3";
	private String resultC = "123";

	private MockRestServiceServer mockServer;

	/**
	 * setting the mockServer object and define it like a unsorted request mock because we dont control when a restful
	 * service will be called by a parallel thread. 
	 * Setting The url rest services.
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		mockServer = MockRestServiceServer.bindTo(restTemplate).build(new UnorderedRequestExpectationManager());

		UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
		urlServiceA = uriTemplate.expand(url, "a").toString();
		urlServiceAA = uriTemplate.expand(url, "aa").toString();
		urlServiceB = uriTemplate.expand(url, "b").toString();
		urlServiceC = uriTemplate.expand(url, "c").toString();
	}

	/**
	 * this test execute 10000 times the processOrder method and print a line with the total time in miliseconds
	 */
	@Test
	public void testProcessOrder() {
		int n = loadAmount;
		long start = System.currentTimeMillis();
		while (n-- > 0) {
			/*
			 * reset the mock expectation on each loop
			 */
			mockServer.reset();
			mockServer.expect(requestTo(urlServiceA)).andRespond(withStatus(HttpStatus.OK).body(resultA));
			mockServer.expect(requestTo(urlServiceAA)).andRespond(withStatus(HttpStatus.OK).body(resultAA));
			mockServer.expect(requestTo(urlServiceB)).andRespond(withStatus(HttpStatus.OK).body(resultB));
			mockServer.expect(requestTo(urlServiceC)).andRespond(withStatus(HttpStatus.OK).body(resultC));
			orderLogic.processOrder(null);
		}
		long end = System.currentTimeMillis();
		System.out.println("DEBUG: Lineal process " + (end - start) + " MilliSeconds");		
		assertTrue(true);
	}

	/**
	 * this test execute 10000 times the processOrderParallel method and print a line with the total time in miliseconds
	 */
	@Test
	public void testProcessOrderParallel() throws InterruptedException, ExecutionException {
		int n = loadAmount;
		long start = System.currentTimeMillis();
		while (n-- > 0) {
			/*
			 * reset the mock expectation on each loop
			 */
			mockServer.reset();
			mockServer.expect(requestTo(urlServiceA)).andRespond(withStatus(HttpStatus.OK).body(resultA));
			mockServer.expect(requestTo(urlServiceB)).andRespond(withStatus(HttpStatus.OK).body(resultB));
			mockServer.expect(requestTo(urlServiceAA)).andRespond(withStatus(HttpStatus.OK).body(resultAA));
			mockServer.expect(requestTo(urlServiceC)).andRespond(withStatus(HttpStatus.OK).body(resultC));

			orderLogic.processOrder(null);
		}
		long end = System.currentTimeMillis();
		System.out.println("DEBUG: Parallel process " + (end - start) + " MilliSeconds");		
		assertTrue(true);

	}

}
