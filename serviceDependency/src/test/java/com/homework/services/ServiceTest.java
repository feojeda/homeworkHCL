package com.homework.services;

import static org.junit.Assert.fail;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.homework.ServiceDependencyApplication;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceDependencyApplication.class)
@WebAppConfiguration
public class ServiceTest {
	

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
       
    }

	@Test
	public void testA() throws Exception {
		mockMvc.perform(get("/a"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
	}

	@Test
	public void testAa() throws Exception {
		String input = "test";
		String output = "test2";
		mockMvc.perform(get("/aa").param("a", input))
        .andExpect(status().isOk())
        .andExpect(content().string(output));
		
	}

	@Test
	public void testB() throws Exception {
		mockMvc.perform(get("/b"))
        .andExpect(status().isOk())
        .andExpect(content().string("3"));
	}

	@Test
	public void testC() throws Exception {
		String input1 = "aa";
		String input2 = "b";
		String output = "aab";
		mockMvc.perform(get("/c").param("aa", input1).param("b", input2))
        .andExpect(status().isOk())
        .andExpect(content().string(output));
	}

}
