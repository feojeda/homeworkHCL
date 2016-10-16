package com.homework.services;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


import com.homework.ServiceDependencyApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;




/**
 * Test cases of Service class. The Services class is a restController class and we test it using a
 * mock class called MockMvc provided by Spring MVC.
 * 
 * @author francisco
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceDependencyApplication.class)
@WebAppConfiguration
public class ServiceTest {


  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  /**
   * setting the mockMvc object using a web application Context wired from the Spring context.
   * 
   */
  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();

  }

  /**
   * test service A. By define must be return "1"
   * 
   * @throws Exception when a error mock perform occur.
   */
  @Test
  public void testA() throws Exception {
    mockMvc.perform(get("/a")).andExpect(status().isOk()).andExpect(content().string("1"));
  }

  /**
   * test service AA. By define must be return a + "2", in this case a = "test" then we expect
   * "test2"
   * 
   * @throws Exception when a error mock perform occur.
   */
  @Test
  public void testAa() throws Exception {
    String input = "test";
    String output = "test2";
    mockMvc.perform(get("/aa").param("a", input)).andExpect(status().isOk())
        .andExpect(content().string(output));

  }

  /**
   * test service B. By define must be return "3"
   * 
   * @throws Exception when a error mock perform occur.
   */
  @Test
  public void testB() throws Exception {
    mockMvc.perform(get("/b")).andExpect(status().isOk()).andExpect(content().string("3"));
  }

  /**
   * test service C. By define must be return aa + b, in this case aa = "aa" and b = "b" then we
   * expect "aab"
   * 
   * @throws Exception when a error mock perform occur.
   */
  @Test
  public void testC() throws Exception {
    String input1 = "aa";
    String input2 = "b";
    String output = "aab";
    mockMvc.perform(get("/c").param("aa", input1).param("b", input2)).andExpect(status().isOk())
        .andExpect(content().string(output));
  }

}
