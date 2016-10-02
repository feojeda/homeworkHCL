package com.homework.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller class that provide a set of restful services: A, AA, B and C.
 * @author francisco
 *
 */
@RestController
public class Service {

	/**
	 * Implements of A service
	 * @return "1"
	 */
	@RequestMapping("a")
	public String a() {
		return "1";
	}
	
	/**
	 * implements of AA service
	 * @param a
	 * @return  a + "2" String
	 */
	@RequestMapping("aa")
	public String aa(@RequestParam(value="a") String a) {
		return a + "2";
	}
	
	/**
	 * Implements of B service
	 * @return "3"
	 */
	@RequestMapping("b")
	public String b() {
		return "3";
	}
	
	/**
	 * Implements of C service
	 * @param aa
	 * @param b
	 * @return aa + b
	 */
	@RequestMapping("c")
	public String c(@RequestParam(value="aa") String aa,@RequestParam(value="b") String b) {
		return aa + b;
	}
}
