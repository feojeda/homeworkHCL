package com.homework.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller class that provide a set of restful services: A, AA, B and C.
 * 
 * @author francisco
 *
 */
@RestController
public class Service {

  /**
   * Implements of A service.
   * 
   * @return "1"
   */
  @RequestMapping("a")
  public String serviceA() {
    return "1";
  }

  /**
   * implements of AA service.
   * 
   * @param inputFromA string must be the output of A service.
   * @return a + "2" String
   */
  @RequestMapping("aa")
  public String serviceAa(@RequestParam(value = "a") String inputFromA) {
    return inputFromA + "2";
  }

  /**
   * Implements of B service.
   * 
   * @return "3"
   */
  @RequestMapping("b")
  public String serviceB() {
    return "3";
  }

  /**
   * Implements of C service.
   * 
   * @param inputFromAa string must be the output of AA service.
   * @param inputFromB string must be the output of B service.
   * @return aa + b
   */
  @RequestMapping("c")
  public String serviceC(@RequestParam(value = "aa") String inputFromAa,
      @RequestParam(value = "b") String inputFromB) {
    return inputFromAa + inputFromB;
  }
}
