# homeworkHCL
This project is a homework that try to improve the excecution time of a 4 dummies services. We have 4 service:
  1. A.
  2. AA: the input is the output of A
  3. B.
  4. C: the inputs are the output of AA and B.
  A lineal excecution must follow those order:
  A -> AA -> B -> C
  or
  A -> B -> AA -> C
  B -> A -> AA -> C
  
  when a<sub>1</sub> -> a<sub>2</sub> -> a<sub>3</sub> -> ... -> a<sub>n</sub> is a lineal excecution of **n** services in order from left to rigth.
  *
  In this execite we have 2 isolates services: A and B, and 2 dependent services: AA depend of A and C depends of AA and C.
  the order of parallel call in this case is:
  {A,B} -> {AA} -> {C}
  when {a<sub>1</sub>,a<sub>2</sub>,a<sub>3</sub>,...,a<sub>n</sub>} is a set  of **n** parallel service.
  
  
  
