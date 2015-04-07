
echo "koe"

create new cmd deleteFiles

print "Are you sure?"
create new string $sf
set $sf input

if $sf = "n" then proceed ask

endcreate

create new cmd ask

print "Press any key to continue..."
wait press

print "Save files?"
create new string $sf
set $sf input

if $sf = "n" then proceed deleteFiles

print "Files saved!"

endcreate

proceed ask
