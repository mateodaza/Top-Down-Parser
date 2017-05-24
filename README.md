# Top-Down-Parser
Given a context-free grammar on a txt file, the app will remove left recursion and apply left factoring if needed. 
Then based on FIRST and FOLLOW sets, it will generate a parse table which can be tested with some words examples. 
You can test it with the exemple.txt file that's in the folder.

#Grammar Productions Syntax
- NonTerminals: UpperCase letters
- Terminals: Any symbol except for > or Uppercase letters

# Grammar File Example 
```
E->E+T

E->T

T->T*F

T->F

F->i

F->(E)
```


This was a Compilers class project and the UI is on Spanish. I hope its useful somehow!

:)
