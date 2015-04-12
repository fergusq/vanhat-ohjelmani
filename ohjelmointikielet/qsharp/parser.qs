option explicit false
create new int $i
create new string $j
set $j "0"
set $i [1 / $j] # Ohjelma muutta nollan yhdeksi koska explicit on false
print $i # Jos kaikki meni oikein, i on 2

while $i < 10
set $i [$i + 1]
print $i
endwhile

# i:n pitäisi olla nyt 10

print "PAM"

option explicit true
option gc true

create new cmd muuttujat
create new int $muuttuja
type "Muuttuja: "
print $muuttuja
endcreate

proceed muuttujat
print $muuttuja # Virhe! $muuttuja luotiin funktiossa, ja gc pyyhki sen pois
