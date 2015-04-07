initui

#uiwindow 6; 3; "--"

uiclear
uidraw 5; 36; "? "

create new int $count
create new int $x
create new int $y
create new int $tmp1
create new int $tmp2

create new cmd demo
#
set $count [$count + 1]
set $x rnd 70
set $y rnd 35
if $x = $y then goto 0
set $tmp1 [$x - $y]
#if $y = $tmp1 then goto 0
set $tmp2 [$y * 3]
if $x = $tmp2 then goto 0
#if $x = 1 then if $y = 1 then goto 0
#if $x = 2 then if $y = 2 then goto 0
#if $x = 3 then if $y = 3 then goto 0
#if $x = 4 then if $y = 4 then goto 0
#if $x = 5 then if $y = 5 then goto 0
#if $x = 0 then if $y = 5 then goto 0
#if $x = 1 then if $y = 4 then goto 0
#if $x = 2 then if $y = 3 then goto 0
#if $x = 3 then if $y = 2 then goto 0
#if $x = 4 then if $y = 1 then goto 0
#if $x = 5 then if $y = 0 then goto 0
if $x < $y then uidraw $x; $y; "!"

if $x > $y then uidraw $x; $y; "?"
if $tmp1 < $y then if $tmp2 > $x then uidraw $x; $y; "!"
if $count < 19000 then goto 0
uidraw 5; 36; "? Q# Demo done. Press any key."
wait press
end
endcreate

proceed demo

wait press

exitui

end
