use java lang.

create new cmd math
	local i to 2.
	println(('i+1       = ' & (i + 1)))->System,out.

	while i < 9 then
		set i to i + 1.
	end

	println(('w(i<9)i++ = ' & i))->System,out.

end

create new cmd msg
	local i to 1.
	set i to i + 1.
	println(('i*2       = ' & (i*2)))->System,out.
end

main
	proceed math.
	proceed msg.
	println('i         = ?')->System,out.
end
