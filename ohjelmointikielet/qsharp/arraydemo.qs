create new vararray $arr
"koira"
"kettu"
"kissa"
4
3
6
endcreate

create new int $i
create new string $s

create new function ?fMuotoile
return ($i + " " + $s + "a")
endcreate


set $i getfrom $arr 3
set $i [$i * 2]

set $s getfrom $arr 0
set $s ?fMuotoile
print $s


set $i getfrom $arr 4
set $i [$i * 2]

set $s getfrom $arr 1
set $s ?fMuotoile
print $s


set $i getfrom $arr 5
set $i [$i * 2]

set $s getfrom $arr 2
set $s ?fMuotoile
print $s
