create new int $global
set $global 3

block

create new int $local
set $local 4

print $global
print $local

endblock

print $global
print $local #VIRHE
