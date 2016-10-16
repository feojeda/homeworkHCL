package com.homework.logic;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.homework.ServiceDependencyApplication;

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

import java.util.concurrent.ExecutionException;


/**
 * test case that implements a benchmark between of the single and multi threads method of the
 * OrderLogic class. The OrderLogic is a class with 2 method, both are restful services client. to
 * test this methods we used a mock class called MockRestServiceServer provide by Srping. Each unit
 * test print a line with the total time of 10000 execution in miliseconds.
 * 
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
  private String urlServiceAa;
  private String urlServiceB;
  private String urlServiceC;
  private String resultA = "1";
  private String resultAa = "12";
  private String resultB = "3";
  private String resultC = "123";

  private MockRestServiceServer mockServer;

  /**
   * setting the mockServer object and define it like a unsorted request mock because we dont
   * control when a restful service will be called by a parallel thread. Setting The url rest
   * services.
   * 
   * @throws Exception when occur a error on build a mock rest server.
   */
  @Before
  public void setup() throws Exception {
    mockServer =
        MockRestServiceServer.bindTo(restTemplate).build(new UnorderedRequestExpectationManager());

    UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
    urlServiceA = uriTemplate.expand(url, "a").toString();
    urlServiceAa = uriTemplate.expand(url, "aa").toString();
    urlServiceB = uriTemplate.expand(url, "b").toString();
    urlServiceC = uriTemplate.expand(url, "c").toString();
  }

  /**
   * this test execute 10000 times the processOrder method and print a line with the total time in
   * Milliseconds.
   */
  @Test
  public void testProcessOrder() {
    
    
    /*
     * reset the mock expectation
     */
    mockServer.reset();
    mockServer.expect(manyTimes(), requestTo(urlServiceA))
        .andRespond(withStatus(HttpStatus.OK).body(resultA));
    mockServer.expect(manyTimes(), requestTo(urlServiceB))
        .andRespond(withStatus(HttpStatus.OK).body(resultB));
    mockServer.expect(manyTimes(), requestTo(urlServiceAa))
        .andRespond(withStatus(HttpStatus.OK).body(resultAa));
    mockServer.expect(manyTimes(), requestTo(urlServiceC))
        .andRespond(withStatus(HttpStatus.OK).body(resultC));
    
    int count = loadAmount;
    long start = System.currentTimeMillis();
    while (count-- > 0) {
      orderLogic.processOrder(null);
    }
    long end = System.currentTimeMillis();
    System.out.println("DEBUG: Lineal process " + (end - start) + " MilliSeconds");
    assertTrue(true);
  }

  /**
   * this test execute 10000 times the processOrderParallel method and print a line with the total
   * time in milliseconds.
   */
  @Test
  public void testProcessOrderParallel() throws InterruptedException, ExecutionException {

    /*
     * reset the mock expectation
     */
    mockServer.reset();
    mockServer.expect(manyTimes(), requestTo(urlServiceA))
        .andRespond(withStatus(HttpStatus.OK).body(resultA));
    mockServer.expect(manyTimes(), requestTo(urlServiceB))
        .andRespond(withStatus(HttpStatus.OK).body(resultB));
    mockServer.expect(manyTimes(), requestTo(urlServiceAa))
        .andRespond(withStatus(HttpStatus.OK).body(resultAa));
    mockServer.expect(manyTimes(), requestTo(urlServiceC))
        .andRespond(withStatus(HttpStatus.OK).body(resultC));
    int count = loadAmount;
    long start = System.currentTimeMillis();
    while (count-- > 0) {
      orderLogic.processOrderParallel(null);
    }
    long end = System.currentTimeMillis();
    System.out.println("DEBUG: Parallel process " + (end - start) + " MilliSeconds");
    assertTrue(true);

  }

  /**
   * this test execute 10000 times the processOrderParallel method and print a line with the total
   * time in milliseconds.
   */
  @Test
  public void testProcessOrderLinealParallel() throws InterruptedException, ExecutionException {
    /*
     * reset the mock expectation
     */
    mockServer.reset();
    mockServer.expect(manyTimes(), requestTo(urlServiceA))
        .andRespond(withStatus(HttpStatus.OK).body(resultA));
    mockServer.expect(manyTimes(), requestTo(urlServiceB))
        .andRespond(withStatus(HttpStatus.OK).body(resultB));
    mockServer.expect(manyTimes(), requestTo(urlServiceAa))
        .andRespond(withStatus(HttpStatus.OK).body(resultAa));
    mockServer.expect(manyTimes(), requestTo(urlServiceC))
        .andRespond(withStatus(HttpStatus.OK).body(resultC));
    int count = loadAmount;
    long start = System.currentTimeMillis();
    while (count-- > 0) {
      orderLogic.processOrderLinealParallel(null);
    }
    long end = System.currentTimeMillis();
    System.out.println("DEBUG: Lineal Parallel process " + (end - start) + " MilliSeconds");
    assertTrue(true);

  }

}
