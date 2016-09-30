package com.homework.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service {

	@RequestMapping("a")
	public String a() {
		return "1";
	}
	@RequestMapping("aa")
	public String aa(@RequestParam(value="a") String a) {
		return a + "2";
	}
	
	@RequestMapping("b")
	public String b() {
		return "3";
	}
	
	@RequestMapping("c")
	public String c(@RequestParam(value="aa") String aa,@RequestParam(value="b") String b) {
		return aa + b;
	}
}
