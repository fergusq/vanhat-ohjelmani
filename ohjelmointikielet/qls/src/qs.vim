" Vim syntax file
" Language:     Alef++
" Maintainer:   Hong Yu <hyu9910@yahoo.com>
" Last Change:  2009 Jul 31
" Remark:	Part of SourceForge project 'Alef++'


" Quit when a syntax file was already loaded
if !exists("main_syntax")
  if version < 600
    syntax clear
  elseif exists("b:current_syntax")
    finish
  endif
  " we define it here so that included files can test for it
  let main_syntax='app'
endif

" don't use standard HiLink, it will not work with included syntax files
if version < 508
  command! -nargs=+ AppHiLink hi link <args>
else
  command! -nargs=+ AppHiLink hi def link <args>
endif


" keyword definitions
syn keyword appExternal		use
syn keyword appKeywords		must last catch module next end go ref :: walo ret synchro die eval loop !loop sub to $ISA nit autoload each jump goto miss true false
syn keyword appMain		main
syn keyword appMy		my

syn match appRepeat		"\<!loop\>"
syn match appRepeat		"\<loop\>"
syn match appSpecialChar	"\\\(\\\|\'\)"

syn region appCommentL		start="__" end="$" keepend 
syn region appString		start=+'+ end=+'+ contains=appSpecialChar,@Spell

syn match appNumber		"\<[+-]\=\d\+[lL]\=\>"
syn match appNumber		"\<[+-]\=\d\+[\.\d*]\=[dD]\=\>"
syn match appNumber		"\<0[xX]\x\+\>"
syn match appNumber		"\<0[oO][0-7]\+\>"

syn match appVariable		"\$[a-zA-Z]\+[a-zA-Z0-9]*"
syn match appVariable		"\$[a-zA-Z]\+[[a-zA-Z0-9]\+_]*[a-zA-Z0-9]*"

highlight AppCommentScheme	ctermfg=darkGreen guifg=darkGreen
highlight AppIncludeScheme	guifg=#ab5ab1	
highlight AppMainScheme		ctermfg=red guifg=red 
highlight AppMyScheme		guifg=blue
highlight AppVarScheme		guifg=darkOrange


AppHiLink appKeywords	Statement
AppHiLink appMain	AppMainScheme
AppHiLink appMy		AppMyScheme
AppHiLink appCommentL	AppCommentScheme
AppHiLink appExternal	AppIncludeScheme
AppHiLink appNumber	Number
AppHiLink appRepeat 	Repeat
AppHiLink appString	String
AppHiLink appVariable   AppVarScheme


delcommand AppHiLink

let b:current_syntax = "app"

if main_syntax == 'app'
  unlet main_syntax
endif

let b:spell_options="contained"

" vim: ts=8
