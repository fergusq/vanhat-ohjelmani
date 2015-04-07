create new int $count #variables in global scope
set $count 2

create new int $luku
set $luku rnd 100

create new cmd game 
#above lines are in local scope
create new int $i 
#line 0 ^

print "Type number x. 0 < x < 100" 
#line 1 ^

set $i input 
#line 2...

if $i = $luku then endcreate
if $i < $luku then print "guest < x"
if $i > $luku then print "guest > x"

goto 1 
#goes to the line 2

endcreate


set $count [$count - 1] #line 6 in global scope
if $count > 0 then proceed game

print "You win!"
