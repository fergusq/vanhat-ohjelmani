create new int $count
set $count 2

create new cmd exp
create new int $i
print "Type number to ext (0 to exit)"
set $i input
if $i = 0 then endcreate
create new int $j
set $j [$i ^ 2]
print ($i + " ^ 2 = " + $j)
goto 0
endcreate

set $count [$count - 1]

if $count > 0 then proceed exp
