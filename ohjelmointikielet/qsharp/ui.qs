create new string $koe

print "Test text, please?"
set $koe input

initui
uimsg "Example"; ($koe + $enter + "Press enter"); "Continue"
exitui

print "Test text, please?"
set $koe input

create new string $name

initui
uiclear
uidraw 5; 5; $koe
uidraw 5; 6; "Press any key to continue"
wait press
print "Enter your name"
set $name input
print ("Hello, " + $name)
wait press
exitui

end
