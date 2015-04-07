#########################################
#                                       #
# VANHENTUNUT! Käytä muita esimerkkejä! #
#                                       #
#########################################

#comment: array example, help program

CREATE NEW VARARRAY $Help: 
	"test">"Show example message" 
	"HELP">"Print help text"
	"PRINT">"Prints something"

ENDCREATE

CREATE NEW CMD Help: 
	PRINT GETFROM $Help PARAM#0# 
ENDCREATE

#comment: run program

PROCEED Help test

#comment: prints

#comment: Show example message
#comment: End of macro



#comment: hello world program

CREATE NEW CMD HelloWorld:
	PRINT "Hello world!"
ENDCREATE

#comment: run program

PROCEED HelloWorld

#comment: prints

#comment: Hello world!
#comment: End of macro



#comment: parameter example

CREATE NEW CMD ParamExample: 
	PRINT PARAM#0#
	PRINT PARAM#1#
	PRINT PARAM#2#
ENDCREATE

#comment: run program

PROCEED ParamExample example1 param2 test3

#comment: prints

#comment: example1
#comment: param2
#comment: test3
#comment: End of macro



#comment: functionexample program

CREATE NEW FUNCTION ?GetUser:
	RETURN GETFROM !Sys?GetUserName
ENDCREATE

CREATE NEW CMD FuncExample:
	PRINT GETFROM ?GetUser
ENDCREATE

#comment: run program

PROCEED FuncExample

#comment: prints

#comment: mat_the_user
#comment: End of macro



#comment: loopexample program

CREATE NEW CMD LoopExample:
	CREATE NEW VAR $text ENDCREATE
	CREATE NEW INT $i ENDCREATE
	DO
		SET $i [$i + 1]
		SET $text ($text + $i)
		IF $i = 9 THEN GOTO ENDDO ENDIF
	ENDDO

	CREATE NEW VAR $text2 ENDCREATE
	CREATE NEW INT $j ENDCREATE
	FOR $j = 0 TO 9
		SET $text2 ($text2 + $j)
	ENDFOR

	CREATE NEW VAR $text3 ENDCREATE
	CREATE NEW INT $k ENDCREATE
	WHILE $k < 10
		SET $text3 ($text3 + $k)
	ENDWHILE

	PRINT $text
	PRINT $text2
	PRINT $text3
ENDCREATE

#comment: run program

PROCEED LoopExample

#comment: prints

#comment: 123456789
#comment: 0123456789
#comment: 0123456789
#comment: End of macro



#comment: QSharpVersion

USE !Sys

CREATE NEW CMD QSharpVersion:
	PRINT "Q# V0.7"
ENDCREATE
