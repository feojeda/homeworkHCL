# homeworkHCL
This project is a homework that try to improve the execution time of 4 dummies services by a multi thread approach.

# Table of Contents
* Theorical Model
 1. [The Problem](#the-problem)
 2. [Lineal Solution](#lineal-solution)
 3. [Parallel Solution](#parallel-solution)
* Java Implements
 1. [Implements Dummies Services](#implements-dummies-services)


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
    
  
## Implements Dummies Services

To implements the dummies services we used restful service using Spring Boot. The 4 services was implement on the class [Service] when:

* Service A return 1
* Service B return 3
* Service AA has a String parameter called **a** and return the String **a** + 2
* Service C has two String parameters, **aa** and **b** and return the String **aa** + **b** 
  
