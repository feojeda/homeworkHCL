# homeworkHCL
This project is a homework that try to improve the excecution time of a 4 dummies services. 

####We have 4 service:
* **A**
* **AA**: the input is the output of **A**
* **B**
* **C**: the inputs are the output of **AA** and **B**.


  ####A lineal excecution must follow those order:
  A -> AA -> B -> C
  or
  A -> B -> AA -> C
  B -> A -> AA -> C
  
  when a<sub>1</sub> -> a<sub>2</sub> -> a<sub>3</sub> -> ... -> a<sub>n</sub> is a lineal excecution of **n** services (tasks) in order from left to right.
  
  In this execite we have:
  - 2 isolated services: 
    + **A** and **B**
  - and 2 dependent services: 
    + **AA** depends of **A** 
    + **C** depends of **AA** and **B**.
    
  the order of parallel call in this case is:
  ----
  
  + {A,B} -> {AA} -> {C}
  
  when {a<sub>1</sub>,a<sub>2</sub>,a<sub>3</sub>,...,a<sub>n</sub>} is a set  of **n** parallel services (tasks.)
  
  **Explain**
   
   if we have a set of isolated and dependent tasks, we can define a set of tasks that can be excetute on parallel.
  
  **witch ones?**
  
  > A set of tasks could be excecute on parallel when all their dependents are done. 
   
   Now we can divide the tasks excecution in a set steps when each step excetuce a set of parallel tasks.
   On this case:
   Step 1:
   
   Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | No | **Yes**
    **B**| none| No | **Yes**
    **AA**| **A**| No | No
    **C**| **AA** and **B**| No | No
    
    We excecute {A,B}
    
    Step 2:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| No | **Yes**
    **C**| **AA** and **B**| No | No
    
    We excecute {AA}
    
    Step 3:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| **Yes** | **Yes**
    **C**| **AA** and **B**| No | **Yes**
    
    We excecute {C}
    
    Step 4:
    
    Task|Dependecies| Done | Ready to call
   ---|---|---|---
    **A**|none | **Yes** | **Yes**
    **B**| none| **Yes** | **Yes**
    **AA**| **A**| **Yes** | **Yes**
    **C**| **AA** and **B**| **Yes** | **Yes**
    
    We dont have more task to excecute.
    
    
    Then we have this list of set of parallel tasks:
    
    + {A,B}
    + {AA}
    + {C}
    
    And must be excecute in order:
    {A,B} -> {AA} -> {C}
    
  
  
  
