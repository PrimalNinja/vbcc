# VBCC - Visual BASIC Cross Compiler (for Visual BASIC 5 or Visual BASIC 6)

VBCC is a cross compiler for the Visual BASIC that supports a substantial subset of the VB5 and VB6 keywords.  It is able to correctly parse itself.

The project was started over 20 years ago but the code generator is incomplete.  I plan to complete it the compiler, however I likely will change the mid-structures to be compatible with that of the JS Cross Compiler so they can use the same code generators.

The infix to postfix logic is incorrect as at the time of coding this, I came up with my own method - I will modify it later to use the shunting algorithm.  Code generators are currently different objects.  One for generation of Z80 code and one for generation of Java code.

Parser Language Support:

(DATATYPES)

	byte, boolean, integer, long, currency, single, double, string, object

(GLOBAL)

	#define <identifer> = <value>
	#if <expression> then
	#end if

	object <identifer>

	module <identifer>
	end module

	dim <identifier> as <datatype>
	public <identifier> as <datatype>
	private <identifier> as <datatype>
	public const <identifer> [as <datatype>] = <value>
	[private] const <identifer> [as <datatype>] = <value>

	friend function <identifer>([<identifer>...]) as <datatype>     note: treated same as public
	[public] function <identifer>([<identifer>...]) as <datatype>   note: withevents not implemented
	private function <identifer>([<identifer>...]) as <datatype>    note: withevents not implemented

	friend sub <identifer>([<identifier>...])                       note: treated same as public
	[public] sub <identifer>([<identifier>...])                     note: withevents not implemented
	private sub <identifer>([<identifier>...])                      note: withevents not implemented

	[public] type <identifer>
	private type <identifer>

(FUNCTION)

	#define <identifer> = <value>
	#if <expression> then
	#end if

	<expression>

	dim <identifer> as <datatype>
	const <identifier> [as <datatype>] = <value>

	end function
	exit function

	end property
	exit property

	end sub
	exit sub

	select case <expression>
	case else
	case <expression>
	end select

	while <expression>
	wend

	for <expression> = <expression> to <expression> [step <step>]
	next
	next <identifier>

	if <expression> then
	elseif <expression> then
	else
	end if


(OBJECT)

	#define <identifer> = <value>
	#if <expression> then
	#end if

	end object

	dim <identifier> as <datatype>
	public <identifier> as <datatype>
	private <identifier> as <datatype>
	[private] const <identifer> [as <datatype>] = <value>

	friend function <identifer>([<identifer>...]) as <datatype>     note: treated same as public
	[public] function <identifer>([<identifer>...]) as <datatype>   note: withevents not implemented
	private function <identifer>([<identifer>...]) as <datatype>    note: withevents not implemented

	[public] property get <identifer>([<identifer>]) as <datatype>  note: same as public function
	private property get <identifer>([<identifer>]) as <datatype>   note: same as private function
	[public] property let <identifer>([<identifier>])               note: same as public sub
	private property let <identifer>([<identifier>])                note: same as private sub
	[public] property set <identifer>([<identifier>])               note: same as public sub
	private property set <identifer>([<identifier>])                note: same as private sub

	friend sub <identifer>([<identifier>...])                       note: treated same as public
	[public] sub <identifer>([<identifier>...])                     note: withevents not implemented
	private sub <identifer>([<identifier>...])                      note: withevents not implemented


(STATEMENT)

	'


(UDT)

	#define <identifer> = <value>
	#if <expression> then
	#end if

	end type

	<identifer> as <datatype>


- Julian
