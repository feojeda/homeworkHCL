# homeworkHCL
This project is a homework that try to improve the execution time of 4 dummies services by a multi thread approach.

# Table of Contents
* Theorical Model
 1. [The Problem](#the-problem)
 2. [Lineal Solution](#lineal-solution)
 3. [Parallel Solution](#parallel-solution)
 4. [Lineal-Parallel Solution](#lineal-parallel-solution)
 
* Java Implements
 1. [Implements Dummies Services](#implements-dummies-services)
 2. [Implements Logic Tier](#implements-logic-tier)
 3. [Testing](#testing)
 4. [Benchmark Result](benchmark-result)
* [Conclusions](#conclusions)

## The Problem

####We have 4 service:
* **A**
* **AA**: the input is the output of **A**
* **B**
* **C**: the inputs are the output of **AA** and **B**.

## Lineal Solution
####A lineal execution must follow those order:
  **A** -> **AA** -> **B** -> **C**execute
  or
  **A** -> **B** -> **AA** -> **C**
  **B** -> **A** -> **AA** -> **C**
  
  when a<sub>1</sub> -> a<sub>2</sub> -> a<sub>3</sub> -> ... -> a<sub>n</sub> is a lineal execution of **n** services (tasks) in order from left to right.

## Parallel Solution

  In this execite we have:
  - 2 isolated services: 
    + **A** and **B**
  - and 2 dependent services: 
    + **AA** depends of **A** 
    + **C** depends of **AA** and **B**.
    
  **the order of parallel call in this case is:**
 
  
  + {**A**,**B**} -> {**AA**} -> {**C**}
  
  when {a<sub>1</sub>,a<sub>2</sub>,a<sub>3</sub>,...,a<sub>n</sub>} is a set  of **n** parallel services (tasks.)
  
  **Explain**
   
   if we have a set of isolated and dependent tasks, we can define a set of tasks that can be excetute on parallel.
  
  **witch ones?**
  
  > A set of tasks could be execute on parallel when all their dependents are done. 
   
   Now we can divide the tasks execution in a set steps when each step execute a set of parallel tasks.
   On this case:
   
   ###Step 1:
   
   Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | No | **Yes**
    **B**| none| No | **Yes**
    **AA**| **A**| No | No
    **C**| **AA** and **B**| No | No
    
    We execute {**A**,**B**}
    
    ###Step 2:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| No | **Yes**
    **C**| **AA** and **B**| No | No
    
    We execute {**AA**}
    
    ###Step 3:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| **Yes** | **Yes**
    **C**| **AA** and **B**| No | **Yes**
    
    We execute {**C**}
    
    ###Step 4:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| **Yes** | **Yes**
    **C**| **AA** and **B**| **Yes** | **Yes**
    
    We dont have more task to execute.
    
    
    Then we have this list of set of parallel tasks:
    
    + {**A**,**B**}
    + {**AA**}
    + {**C**}
    
    And must be execute in order:
    {**A**,**B**} -> {**AA**} -> {**C**}
    
## Lineal-Parallel Solution

We can mix the lineal and parallel solution by create a parallelizable set of lineal tasks.

Let  {**A** -> **AA**, **B**} -> {**C**} 

when {**A** -> **AA**, **B**} is: **A** -> **AA** lineal execution of A and AA, and **B** is execute in a parallel thread to **A** -> **AA**.

 Now we can divide the tasks execution in a set steps when each step execute a set of parallel tasks.
   On this case:
   
###Step 1:
   
   Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A** -> **AA**|none | No | **Yes**
    **B**| none| No | **Yes**
    **C**| **AA** and **B**| No | No
    
We execute {**A** -> **AA**, **B**}

###Step 2:
   
   Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A** -> **AA**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **C**| **AA** and **B**| No | **Yes**
    
We execute {**C**}

###Step 3:
   
   Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A** -> **AA**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **C**| **AA** and **B**| **Yes** | **Yes**
    
We dont have more task to execute.
    

    
  
## Implements Dummies Services

To implements the dummies services we used restful service using Spring Boot. The 4 services was implement on the class [Service](serviceDependency/src/main/java/com/homework/services/Service.java) when:

* Service A return 1
* Service B return 3
* Service AA has a String parameter called **a** and return the String **a** + 2
* Service C has two String parameters, **aa** and **b** and return the String **aa** + **b** 

## Implements Logic Tier
The logic tier is the place that call the dummies services and was implement on the class [OrderLogic](serviceDependency/src/main/java/com/homework/logic/OrderLogic.java) and has two methods:

* public String processOrder(Order order): Lineal execution
* public String processOrderParallel(Order order): Parallel execution
* public String processOrderLinealParallel(Order order): Lineal Parallel execution

**processOrder** call the Restful services in a lineal way and using a single thread.

**processOrderParallel** group the call of service **A** and **B** and execute it on parallel using one thread for each of it (**Step 1**). When finish the step 1 call the **AA** service on a single thread (**Step 2**). Whe finish the step 2 call the **C** service on a single thread.  

**processOrderLinealParallel** group the call of services (**A**,**AA**) and **B** and execute it on parallel using one thread for each of it (**Step 1**). When finish the step 1 call the **C** service on a single thread (**Step 2**).  

## Testing

The tests was split in 2:
+ Unit test
+ Benchmark test

The unit test class are:
 + [OrderLogicTest](serviceDependency/src/test/java/com/homework/logic/OrderLogicTest.java)
 + [ServiceTest](serviceDependency/src/test/java/com/homework/services/ServiceTest.java)

The benchmark test is:
 + [OrderLogucBenchmark](serviceDependency/src/test/java/com/homework/logic/OrderLogucBenchmark.java)
 
 
**[OrderLogicTest](serviceDependency/src/test/java/com/homework/logic/OrderLogicTest.java)** class need to test a restful client and need to simulate a restfulServer, for this the solution use the mock class [MockRestServiceServer](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/client/MockRestServiceServer.html)
  

**[ServiceTest](serviceDependency/src/test/java/com/homework/services/ServiceTest.java)** class need to test like a restful server, for this the solution use the mock class [MockMvc](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)

**[OrderLogucBenchmark](serviceDependency/src/test/java/com/homework/logic/OrderLogucBenchmark.java)** class has 2 test methods that execute 10000 times the lineal and parallel solutions, and each method prints a line with the total time in miliseconds.

## Benchmark Result

After run the benchmark test several times the parallel solutions always be more quick.

**This is a comparative table**


Lineal Time|Parallel Time|LP time|diff Parallel|diff LP|% Parallel|% LP
---|---|---|---|---|---|---|---
2059|	1163|	1061|	896|	998|	177%|	194%
2079|	1292|	1109|	787|	970|	161%|	187%
2058|	1148|	1040|	910|	1018|	179%|	198%
2245|	1864|	986|	381|	1259|	120%|	228%
1548|	1306|	1147|	242|	401|	119%|	135%



**The average % of the parallel solution is 151%**

**The average % of the lineal parallel solution is 188%**

Parallel solution is a 151% more faster than lineal solution. In other words parallel solutions is 1.5x more faster.
Lineal Parallel solution is a 188% more faster than lineal solution. In other words lineal parallel solutions is 1.8x more faster.


## Conclusions

The multi thread (parallel) approach improve the original and lineal solution. On this particular case we can define a "static" and unique solution predefine the order and grouping of tasks to parallelized. **This solution can't be extends to others scenarios with others services and others dependency**. To avoid from to make the same analysis for earch potecial scenario we can implement a dimamic solution that create a dependecy graph when each node is a task and the parents node are the dependency task. With this data structure we can use the graph or tree algorithms to determite when a service or task can be execute.

#### Graph's approaches:

+ [Topological sorting](https://en.wikipedia.org/wiki/Topological_sorting)
+ [Dependency graph](https://en.wikipedia.org/wiki/Dependency_graph)
