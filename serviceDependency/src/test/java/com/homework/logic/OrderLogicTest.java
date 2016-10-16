package com.homework.logic;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;



import com.homework.ServiceDependencyApplication;
import com.homework.entities.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import java.util.concurrent.ExecutionException;





/**
 * test case to OrderLogic class. The OrderLogic is a class with 2 method, both are restful services
 * client. to test this methods we used a mock class called MockRestServiceServer provide by Srping.
 * 
 * @author francisco
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceDependencyApplication.class)
public class OrderLogicTest {

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

  private Order dummyOrder;

  /**
   * setting: - the mockServer using the restTemplate object wired from the Spring context and the
   * same using by OrderLogic Class. - The url rest services.
   * 
   * @throws Exception some exception on createServer
   */
  @Before
  public void setup() throws Exception {
    mockServer = MockRestServiceServer.createServer(restTemplate);

    UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
    urlServiceA = uriTemplate.expand(url, "a").toString();
    urlServiceAa = uriTemplate.expand(url, "aa").toString();
    urlServiceB = uriTemplate.expand(url, "b").toString();
    urlServiceC = uriTemplate.expand(url, "c").toString();

    dummyOrder = new Order();

  }

  /**
   * Test of lineal process order using a mock rest server.
   */
  @Test
  public void testProcessOrder() {
    /*
     * define the expected rest request and their result
     */

    mockServer.expect(requestTo(urlServiceA)).andRespond(withStatus(HttpStatus.OK).body(resultA));

    mockServer.expect(requestTo(urlServiceAa)).andRespond(withStatus(HttpStatus.OK).body(resultAa));

    mockServer.expect(requestTo(urlServiceB)).andRespond(withStatus(HttpStatus.OK).body(resultB));

    mockServer.expect(requestTo(urlServiceC)).andRespond(withStatus(HttpStatus.OK).body(resultC));
    /*
     * call the processOrder method.
     */
    String result = orderLogic.processOrder(dummyOrder);
    assertEquals("123", result);

  }

  /**
   * Test of multithread process order using a mock rest server.
   */
  @Test
  public void testProcessOrderParallel() throws InterruptedException, ExecutionException {
    /*
     * define the expected rest request and their result
     */
    mockServer.expect(requestTo(urlServiceA)).andRespond(withStatus(HttpStatus.OK).body(resultA));
    mockServer.expect(requestTo(urlServiceB)).andRespond(withStatus(HttpStatus.OK).body(resultB));

    mockServer.expect(requestTo(urlServiceAa)).andRespond(withStatus(HttpStatus.OK).body(resultAa));

    mockServer.expect(requestTo(urlServiceC)).andRespond(withStatus(HttpStatus.OK).body(resultC));

    /*
     * call the processOrder method.
     */
    String result = orderLogic.processOrderParallel(dummyOrder);
    assertEquals("123", result);

  }

  /**
   * Test of linear multithread process order using a mock rest server.
   */
  @Test
  public void testProcessOrderLinealParallel() throws InterruptedException, ExecutionException {
    /*
     * define the expected rest request and their result
     */
    mockServer.expect(requestTo(urlServiceA)).andRespond(withStatus(HttpStatus.OK).body(resultA));

    mockServer.expect(requestTo(urlServiceAa)).andRespond(withStatus(HttpStatus.OK).body(resultAa));

    mockServer.expect(requestTo(urlServiceB)).andRespond(withStatus(HttpStatus.OK).body(resultB));

    mockServer.expect(requestTo(urlServiceC)).andRespond(withStatus(HttpStatus.OK).body(resultC));

    /*
     * call the processOrder method.
     */
    String result = orderLogic.processOrderLinealParallel(dummyOrder);
    assertEquals("123", result);

  }

}
