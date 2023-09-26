# VBCC - Visual BASIC Cross Compiler (for Visual BASIC 5 or Visual BASIC 6)

VBCC is a cross compiler for the Visual BASIC that supports a substantial subset of the VB5 and VB6 keywords.  It is able to correctly parse itself.

The project was started over 20 years ago but the code generator is incomplete.  I plan to complete it the compiler, however I likely will change the mid-structures to be compatible with that of the JS Cross Compiler so they can use the same code generators.

The infix to postfix logic is incorrect as at the time of coding this, I came up with my own method - I will modify it later to use the shunting algorithm.  Code generators are currently different objects.  One for generation of Z80 code and one for generation of Java code.

- Julian
