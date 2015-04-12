create new string $answer

create new cmd wrongCode
uimsg $bank; "Sorry, but the number is wrong!"; "Quit bank"
set $answer uiyesno "Item market"; ($bank + $enter + "has cancelled purchase"); "Continue shopping"; "Quit"
if $answer | "Quit" then proceed shop
exitui
end
endcreate

create new cmd cancel
uimsg $bank; "Cancelled"; "Quit bank"
set $answer uiyesno "Item market"; ($bank + $enter + "has cancelled purchase"); "Continue shopping"; "Quit"
if $answer | "Quit" then proceed shop
exitui
end
endcreate

create new cmd pay
uimsg $bank; ("Ready to pay" + $enter + "First enter your security code."); "Continue"

set $secnum $empty

uinewmenu $m3 $bank; ("    Please enter" + $enter + "your security number")

uimenu $m3 add "0"
uimenu $m3 add "1"
uimenu $m3 add "2"
uimenu $m3 add "3"
uimenu $m3 add "4"
uimenu $m3 add "5"
uimenu $m3 add "6"
uimenu $m3 add "7"
uimenu $m3 add "8"
uimenu $m3 add "9"


uimenu $m3 show
set $secnum ($secnum + uimenu $m3 index)

uimenu $m3 show
set $secnum ($secnum + uimenu $m3 index)

uimenu $m3 show
set $secnum ($secnum + uimenu $m3 index)

uimenu $m3 show
set $secnum ($secnum + uimenu $m3 index)

if $secnum | "5678" then proceed wrongCode

set $money [$money - $cost]

uimsg $bank; "Transmission complete."; "Quit bank"

set $answer uiyesno "Item market"; ($bank + " thanks you" + $enter + " "); "Continue shopping"; "Quit"
if $answer | "Quit" then proceed shop

exitui
end
endcreate

create new string $bank
create new string $item
create new int $money
create new int $many
create new int $cost
create new string $cardnum
create new string $secnum

set $money 1000

initui

uiwindow 1; 1; " "

create new cmd shop

set $bank $empty
set $cost 0
set $cardnum $empty
set $secnum $empty

uimsg "Item Market"; "Please select the item to purchase."; "Continue"

uinewmenu $m1 "Items"

uimenu $m1 add "Book"
uimenu $m1 add "Book 2"
uimenu $m1 add "Phone"
uimenu $m1 add "Mobilephone"
uimenu $m1 add "GameComputer"
uimenu $m1 add "Television"
uimenu $m1 add "Laptop"
uimenu $m1 add "Toy Robot"
uimenu $m1 add "Computer"
uimenu $m1 add "Home Robot"

uimenu $m1 show


set $cost uimenu $m1 index
set $cost [$cost * 100]

uinewmenu $m4 "Item market"; ("How many " + uimenu $m1 text + "s you want?")

uimenu $m4 add "0"
uimenu $m4 add "1"
uimenu $m4 add "2"
uimenu $m4 add "3"
uimenu $m4 add "4"
uimenu $m4 add "5"
uimenu $m4 add "6"
uimenu $m4 add "7"
uimenu $m4 add "8"
uimenu $m4 add "9"
uimenu $m4 add "10"

uimenu $m4 show


set $many uimenu $m4 index
set $cost [$cost * $many]

set $item uimenu $m1 text

uimsg "Item Market"; ("Item selected: " + $item); "Continue"
uimsg "Item Market"; ("Your cart costs " + $cost + " e"); "Proceed to checkout"
uimsg "Item Market"; "Please select your bank"; "Continue"

uinewmenu $m2 "Select bank"

uimenu $m2 add "Visa Debit"
uimenu $m2 add "Visa Credit"
uimenu $m2 add "Omnibank"
uimenu $m2 add "The Bank Of Evil"
uimenu $m2 add "Novice"
uimenu $m2 add "Masterhard"
uimenu $m2 add "Innerbanken"
uimenu $m2 add "The Bank Of Call Of Money"
uimenu $m2 add "The Big Bank Theory"

uimenu $m2 show

set $bank uimenu $m2 text

uimsg "Item Market"; ("Bank selected: " + $bank); "Continue"
uimsg $bank; "Please enter your card number."; "Continue"


uinewmenu $m3 $bank; ("  Please enter" + $enter + "your card number")

uimenu $m3 add "0"
uimenu $m3 add "1"
uimenu $m3 add "2"
uimenu $m3 add "3"
uimenu $m3 add "4"
uimenu $m3 add "5"
uimenu $m3 add "6"
uimenu $m3 add "7"
uimenu $m3 add "8"
uimenu $m3 add "9"

set $cardnum $empty

uimenu $m3 show
set $cardnum ($cardnum + uimenu $m3 index)

uimenu $m3 show
set $cardnum ($cardnum + uimenu $m3 index)

uimenu $m3 show
set $cardnum ($cardnum + uimenu $m3 index)

uimenu $m3 show
set $cardnum ($cardnum + uimenu $m3 index)

if $cardnum | "1234" then proceed wrongCode

uimsg $bank; "Card number accepted!"; "Continue"
uimsg $bank; ("	" + "Cart             	" + $cost + " e" + $enter + "	" + "Delivery costs   	" + "10 e" + $enter + "	" + "Money in bank  		" + $money + " e" + $enter + "	" + "Money after pay 		" + [$money - $cost - 10] + " e"); "Continue"
set $cost [$cost + 10]
set $answer uiyesno $bank; ("Are you sure to pay 	" + $cost + " e" + $enter + "Money:             	" + $money + " e"); "Yes"; "No"

if $answer = "No" then proceed cancel
		
if $money > $cost then proceed pay
	
uimsg $bank; "Sorry, but you didn't have enought money!"; "Quit bank"
set $answer uiyesno "Item market"; ($bank + $enter + "has cancelled purchase"); "Continue shopping"; "Quit"
if $answer | "Quit" then goto 4
exitui
end

endcreate

uimsg "Item Market"; ("Welcome to Item Market!"); "Enter to the market"

proceed shop
