# Part of QS System lib

create new function ?lag withparams [int a, int b]
create new int $c
if $a < $b then set $c [$a + $b]
if $a > $b then set $c [$a - $b]
if $a = $b then set $c [$a - $b]
return $c
endcreate

create new function ?sqrt withparams [int a]
#
return [$a ^ 0.5]

endcreate
