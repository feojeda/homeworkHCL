package com.homework.logic;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

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

import com.homework.ServiceDependencyApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceDependencyApplication.class)
public class OrderLogicTest {

	@Autowired
	private OrderLogic orderLogic;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${service.url}")
	private String url;
	
	private MockRestServiceServer mockServer;
	
	@Before
	public void setup() throws Exception {
	    mockServer = MockRestServiceServer.createServer(restTemplate);
	}
	
	@Test
	public void testProcessOrder() {
		
		UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
		String urlServiceA = uriTemplate.expand(url, "a").toString();
		String urlServiceAA = uriTemplate.expand(url, "aa").toString();
		String urlServiceB = uriTemplate.expand(url, "b").toString();
		String urlServiceC = uriTemplate.expand(url, "c").toString();

		String resultA = "1";
		String resultAA = "12";
		String resultB = "3";
		String resultC = "123";
		
		mockServer.expect(requestTo(urlServiceA)).
				andRespond(withStatus(HttpStatus.OK).
						body(resultA));
		
		mockServer.expect(requestTo(urlServiceAA)).
		andRespond(withStatus(HttpStatus.OK).
				body(resultAA));
		
		mockServer.expect(requestTo(urlServiceB)).
		andRespond(withStatus(HttpStatus.OK).
				body(resultB));
		
		mockServer.expect(requestTo(urlServiceC)).
		andRespond(withStatus(HttpStatus.OK).
				body(resultC));
		
		String result = orderLogic.processOrder(null);
		assertEquals("123",result);
		
	}

}
