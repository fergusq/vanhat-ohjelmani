	Q#
      QSharp

 Made by FergusQ.

 Programming: IH.

 Current version:
       0.7

-----------------
SYNTAX MINI HOWTO

Hello world:

->	CREATE NEW CMD helloWorld
->		PRINT "Hello World!"
->	ENDCREATE
->	
->	PROCEED helloWorld

Keywords:

->	USE
->	DO ... ENDDO
->	WHILE ... ENDWHILE
->	IF ... THEN ... ELSE ... ENDIF
->	CREATE ... ENDCREATE
->	NEW ... ENDNEW
->	SET
->	GETFROM
->	PRINT
->	TYPE
->	PROCEED
->	GOTO
->	WITHPARAMS

Data types:

->	INT
->	STRING
->	VARARRAY
->	
->	CMD
->	FUNCTION

Create variable:

->	CREATE NEW <type> $<name>

Setting variable value:

->	SET $<name> <value>

Variable use example:

->	CREATE NEW INT $i
->	SET $i 2
->	PRINT $i

Prints "2".

Creating array:

->	CREATE NEW VARARRAY $<name>

Getting value from array:

->	GETFROM $<name>

Setting value of array:

->	SET GETFROM $<name> <index> <value>

Init array:

->	SET NEW VARARRAY <size> ENDNEW $<name>

Array use example:

->	CREATE NEW VARARRAY $arr
->	1
->	2
->	"3"
->	"4"
->	ENDCREATE
->	
->	CREATE NEW INT $i
->	SET $i GETFROM $arr 2
->	PRINT $i

Prints "2".

Create cmd:

->	CREATE NEW CMD <name>
->	# ...
->	# Code
->	# ...
->	ENDCREATE

Proceed (run) cmd:

->	PROCEED <name>

Create function:

->	CREATE NEW FUNCTION ?<name> WITHPARAMS [ <params> ]
->	# ...
->	# Code
->	# ...
->	RETURN <value>
->	ENDCREATE

Function use example:

->	CREATE NEW FUNCTION ?Two
->	RETURN 2
->	ENDCREATE
->
->	PRINT ?Two

Prints "2".

Parameter use example:

->	CREATE NEW FUNCTION ?Div WITHPARAMS [INT a, INT b]
->	RETURN [$a / $b]
->	ENDCREATE
->
->	PRINT ?Div 4; 2

Prints "2".

Dice-function:

->	RND <value>

Rnd use example:

->	CREATE NEW INT $i
->	
->	SET $i RND 4
->	PRINT $i

Prints random integer between 0 and 4.

Expressions:

->	[<int_value> + <int_value>] # ADDITION
->	[<int_value> - <int_value>] # SUBTRACTION
->	[<int_value> * <int_value>] # MULTIPLICATION
->	[<int_value> / <int_value>] # DIVISION
->	[<int_value> ^ <int_value>] # EXPONENT
->
->	(<string_value> + <string_value>) # ADDITION

Expression example:

->	CREATE NEW INT $i
->	
->	SET $i 1
->	SET $i [$i + 1]
->	PRINT $i

Prints "2".
