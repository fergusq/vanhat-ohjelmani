use lib/math.qs

create new int $luku1
create new int $luku2
create new int $luku3

set $luku1 1
set $luku2 1

create new int $määrä
set $määrä 10
create new int $mones

do
set $luku3 [$luku1 + $luku2]
print $luku3

set $luku1 $luku2
set $luku2 $luku3
set $luku3 0

if $mones = $määrä then goto enddo
set $mones [$mones + 1]
enddo

print ?lag 4; 7
print ?sqrt 4
