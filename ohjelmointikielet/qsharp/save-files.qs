create new cmd deleteFiles

print "Are you sure?"
create new string $sf
set $sf input

if $sf = "n" then proceed ask

end

endcreate

create new cmd ask

print "Press any key to continue..."

print "a"

print "Save files?"
create new string $sf
set $sf input

if $sf = "n" then proceed deleteFiles

print "Files saved!"

endcreate

proceed ask
