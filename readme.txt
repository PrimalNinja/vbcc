'VB6 Parser
'
'***** STATUSES:
'
'IP = IN PROGRESS
'DONE = IMPLEMENTED
'AUTO = AUTOMATICLY IMPLEMENTED WHEN RELATED FUNCTION IS IMPLEMENTED


'(DATATYPES)
'
'       byte, boolean, integer, long, currency, single, double, string, object
'
'(GLOBAL)
'
'DONE   #define <identifer> = <value>
'DONE   #if <expression> then
'DONE   #end if
'
'DONE   object <identifer>
'
'DONE   module <identifer>
'DONE   end module
'
'DONE   dim <identifier> as <datatype>
'DONE   public <identifier> as <datatype>
'DONE   private <identifier> as <datatype>
'DONE   public const <identifer> [as <datatype>] = <value>
'DONE   [private] const <identifer> [as <datatype>] = <value>
'
'AUTO   friend function <identifer>([<identifer>...]) as <datatype>     note: treated same as public
'DONE   [public] function <identifer>([<identifer>...]) as <datatype>   note: withevents not implemented
'DONE   private function <identifer>([<identifer>...]) as <datatype>    note: withevents not implemented
'
'AUTO   friend sub <identifer>([<identifier>...])                       note: treated same as public
'DONE   [public] sub <identifer>([<identifier>...])                     note: withevents not implemented
'DONE   private sub <identifer>([<identifier>...])                      note: withevents not implemented
'
'DONE   [public] type <identifer>
'DONE   private type <identifer>


'(FUNCTION)
'
'DONE   #define <identifer> = <value>
'DONE   #if <expression> then
'DONE   #end if
'
'DONE   <expression>
'
'DONE   dim <identifer> as <datatype>
'DONE   const <identifier> [as <datatype>] = <value>
'
'DONE   end function
'DONE   exit function
'
'DONE   end property
'DONE   exit property
'
'DONE   end sub
'DONE   exit sub
'
'DONE   select case <expression>
'DONE   case else
'DONE   case <expression>
'DONE   end select
'
'DONE   while <expression>
'DONE   wend
'
'DONE   for <expression> = <expression> to <expression> [step <step>]
'DONE   next
'AUTO   next <identifier>
'
'DONE   if <expression> then
'DONE   elseif <expression> then
'DONE   else
'DONE   end if


'(OBJECT)
'
'DONE   #define <identifer> = <value>
'DONE   #if <expression> then
'DONE   #end if
'
'DONE   end object
'
'DONE   dim <identifier> as <datatype>
'DONE   public <identifier> as <datatype>
'DONE   private <identifier> as <datatype>
'DONE   [private] const <identifer> [as <datatype>] = <value>
'
'AUTO   friend function <identifer>([<identifer>...]) as <datatype>     note: treated same as public
'DONE   [public] function <identifer>([<identifer>...]) as <datatype>   note: withevents not implemented
'DONE   private function <identifer>([<identifer>...]) as <datatype>    note: withevents not implemented
'
'AUTO   [public] property get <identifer>([<identifer>]) as <datatype>  note: same as public function
'AUTO   private property get <identifer>([<identifer>]) as <datatype>   note: same as private function
'AUTO   [public] property let <identifer>([<identifier>])               note: same as public sub
'AUTO   private property let <identifer>([<identifier>])                note: same as private sub
'AUTO   [public] property set <identifer>([<identifier>])               note: same as public sub
'AUTO   private property set <identifer>([<identifier>])                note: same as private sub
'
'AUTO   friend sub <identifer>([<identifier>...])                       note: treated same as public
'DONE   [public] sub <identifer>([<identifier>...])                     note: withevents not implemented
'DONE   private sub <identifer>([<identifier>...])                      note: withevents not implemented


'(STATEMENT)
'
'DONE   '


'(UDT)
'
'DONE   #define <identifer> = <value>
'DONE   #if <expression> then
'DONE   #end if
'
'DONE   end type
'
'DONE   <identifer> as <datatype>
'
