package com.homework.logic;


import com.homework.entities.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;



import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Class provides two method to process Orders: processOrder (single thread) and
 * processOrderParallel (multithread). To process a order needs call 4 services: A, AA, B and C. the
 * processOrder executes a lineal secuencial call of this services, and processOrderParallel
 * executes 3 thread to parallelized the services call.
 * 
 * @author francisco
 *
 */
@Controller
public class OrderLogic {
  @Autowired
  private RestOperations restTemplate;

  private String urlServiceA;
  private String urlServiceAa;
  private String urlServiceB;
  private String urlServiceC;

  /**
   * Is a Executor service that execute a set of Callables objects on diferences threads.
   */
  private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);

  /**
   * get the Restful url services from the app properties files.
   * 
   * @param url url base of server
   */
  @Autowired
  public OrderLogic(@Value("${service.url}") String url) {
    /*
     * create the urls of the restful services using a url base
     */
    UriTemplateHandler uriTemplate = new DefaultUriTemplateHandler();
    this.urlServiceA = uriTemplate.expand(url, "a").toString();
    this.urlServiceAa = uriTemplate.expand(url, "aa").toString();
    this.urlServiceB = uriTemplate.expand(url, "b").toString();
    this.urlServiceC = uriTemplate.expand(url, "c").toString();

  }


  /**
   * This method uses a single thread to call the 4 services on a lineal way. to call it used a
   * restTemplate object defined on RestClientConfig class.
   * 
   * @param order: order to process.
   * @return result of process order
   */
  public String processOrder(Order order) {

    String resultA = restTemplate.getForObject(urlServiceA, String.class);
    String resultAa = restTemplate.getForObject(urlServiceAa, String.class, resultA);
    String resultB = restTemplate.getForObject(urlServiceB, String.class);
    String resultC = restTemplate.getForObject(urlServiceC, String.class, resultAa, resultB);

    return resultC;
  }

  /**
   * This method uses a RestTask objects to call every Restful service on a parallel thread. The
   * RestTask class is a class create into this class and implements the Calleble Interface. This
   * RestTask object are executed by a ExecutorServices called threadpool. Order of execution: A and
   * B are executed on parallel theads AA is executed on a parallel thread after finish the
   * execution of A C is executed on a parallel thread after finish the execution of AA and B
   * 
   * @param order: order to process
   * @return result of parallel process order
   */
  public String processOrderParallel(Order order) throws InterruptedException, ExecutionException {
    /*
     * creating 2 RestTask object to services A and B
     */
    RestTask<String> taskA = new RestTask<String>(restTemplate, urlServiceA, String.class);
    RestTask<String> taskB = new RestTask<String>(restTemplate, urlServiceB, String.class);

    /*
     * execute the call of services A and B using the ExecutorServices. This executorServices return
     * a Future Object and from it we can get the services call result.
     */
    Future<String> futureA = threadpool.submit(taskA);
    Future<String> futureB = threadpool.submit(taskB);


    /*
     * get the result of call A and B.
     */
    String resultA = futureA.get();
    String resultB = futureB.get();

    /*
     * in this point the call of A and B services are finish and we can call the AA services. create
     * a RestTask to AA services.
     */
    RestTask<String> taskAa =
        new RestTask<String>(restTemplate, urlServiceAa, String.class, resultA);

    /*
     * Execute the call of AA services using the ExecutorServices called threadpool
     */
    Future<String> futureAa = threadpool.submit(taskAa);

    /*
     * get result of call AA services.
     */
    String resultAa = futureAa.get();

    /*
     * In this point the call of AA and B are finish and we can call the C services. Create a
     * RestTask to C services.
     */
    RestTask<String> taskC =
        new RestTask<String>(restTemplate, urlServiceC, String.class, resultAa, resultB);

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
   * This method uses a RestTask objects to call every Restful service on a parallel thread. The
   * RestTask class is a class create into this class and implements the Callable Interface. This
   * RestTask object are executed by a ExecutorServices called threadpool. Order of execution: A and
   * B are executed on parallel threads AA is executed on a parallel thread after finish the
   * execution of A C is executed on a parallel thread after finish the execution of AA and B
   * 
   * @param order order to process
   * @return result of lineal parallel process order
   * @throws InterruptedException when a thread is interrupted by a wrong way.
   * @throws ExecutionException when a execution of a thread have a problem.
   */
  public String processOrderLinealParallel(Order order)
      throws InterruptedException, ExecutionException {
    Callable<String> thread1 = () -> {
      String resultA = restTemplate.getForObject(urlServiceA, String.class);
      return restTemplate.getForObject(urlServiceAa, String.class, resultA);
    };

    Callable<String> thread2 = () -> {
      return restTemplate.getForObject(urlServiceB, String.class);
    };

    Future<String> futureT1 = threadpool.submit(thread1);
    Future<String> futureT2 = threadpool.submit(thread2);

    String resultAa = futureT1.get();
    String resultB = futureT2.get();
    String resultC = restTemplate.getForObject(urlServiceC, String.class, resultAa, resultB);

    return resultC;
  }

  /**
   * This class implements the Callable interfaces and can be used to execute the call method and
   * return a T class on a asynchronous way (parallel trhead). On the call method is invoke a
   * restful services.
   * 
   * @author francisco
   *
   * @param <T> class that return the services
   */
  class RestTask<T> implements Callable<T> {

    private String url;
    private Class<T> responseType;
    private Object[] params;
    private RestOperations restTemplate;

    /**
     * Constructor
     * 
     * @param restTemplate used to call a restful service
     * @param url url of the restful service.
     * @param responseType type of response of the restful services and the call methods.
     * @param params parameters list of the restful service
     */
    public RestTask(RestOperations restTemplate, String url, Class<T> responseType,
        Object... params) {
      this.restTemplate = restTemplate;
      this.url = url;
      this.responseType = responseType;
      this.params = params;
    }

    @Override
    public T call() throws Exception {
      T result;
      /*
       * if the parameters list is null or empty the rest service is called without parameters, else
       * is called using the parameter list (params)
       */
      if (params == null || params.length == 0) {
        result = restTemplate.getForObject(url, responseType);
      } else {
        result = restTemplate.getForObject(url, responseType, params);
      }
      return result;

    }

  }

}
