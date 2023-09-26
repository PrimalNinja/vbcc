VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cCompiler"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
'Compiler
'
'***** STATUSES:
'
'DONE = IMPLEMENTED
'AUTO = AUTOMATIC IMPLEMENTATION BASED ON DEPENDING ITEM
'
'(DATATYPES)
'
'       byte, boolean, integer, long, currency, single, double, string, object
'
'(GLOBAL)
'
'       object <identifer>
'       module <identifer>
'       end module
'       private/public type <identifer>
'       private/public function <identifer>([<identifer>...]) as <datatype>
'       private/public sub <identifer>([<identifier>...])
'       private/public const <identifer> [as <datatype>] = <value>
'       private/public <identifier> as <datatype>
'
'(FUNCTION)
'
'       const <identifer> [as <datatype>] = <value>
'       end function
'       exit function
'       end sub
'       exit sub
'       dim <identifer> as <datatype>
'       select case <expression>
'       case else
'       case <expression>
'       end select
'       while <expression>
'       wend
'       for <expression> = <expression> to <expression>
'       next
'       if <expression> then
'       elseif <expression> then
'       else
'       end if
'       <expression>
'
'(OBJECT)
'
'       end object
'       private type <identifer>
'       private/public function <identifer>([<identifer>...]) as <datatype>
'       private/public sub <identifer>([<identifier>...])
'       private const <identifer> [as <datatype>] = <value>
'       private/public <identifier> as <datatype>
'
'(UDT)
'
'       end type
'       <identifer> as <datatype>
'
Option Explicit

Private Const m_NAME = "cCompiler"

Private Const m_TARGET = "asm"

Private m_strProjectName As String
Private m_strFile As String
Private m_lngLineID As Long

Private m_blnInObject As Boolean
Private m_blnInFunc As Boolean
Private m_blnInUDT As Boolean

Private m_lngIndent As Long

Private Const m_INDENT_SIZE = 4

'element types
Private Const m_ETOBJECT = 1
Private Const m_ETMODULE = 2
Private Const m_ETFUNC = 3
Private Const m_ETSUB = 4
Private Const m_ETUDT = 5
Private Const m_ETCONSTANT = 6
Private Const m_ETVARIABLE = 7
Private Const m_ETUDTVARIABLE = 8

'datatypes
Private Const m_DTUNKNOWN = 0
Private Const m_DTBYTE = 1
Private Const m_DTBOOLEAN = 2
Private Const m_DTINTEGER = 3
Private Const m_DTLONG = 4
Private Const m_DTCURRENCY = 5
Private Const m_DTSINGLE = 6
Private Const m_DTDOUBLE = 7
Private Const m_DTSTRING = 8
Private Const m_DTOBJECT = 9

'scopes
Private Const m_SCOPEGLOBAL = 1
Private Const m_SCOPEMODULE = 2
Private Const m_SCOPELOCAL = 3

'stats
Private m_lngLinesIn As Long
Private m_lngLinesOut As Long
Private m_lngErrors As Long
Private m_lngFuncs As Long
Private m_lngModules As Long
Private m_lngObjects As Long
Private m_lngSubs As Long
Private m_lngUDTs As Long
Private m_lngVars As Long
Private m_lngExprns As Long
Private m_lngFors As Long
Private m_lngIfThens As Long
Private m_lngSelectCases As Long
Private m_lngWhiles As Long
Private m_lngConsts As Long
Private Function pvt__ASMCallFunc(strLabel_a As String) As String
  Dim strResult As String

  strResult = pvtSpaces(m_lngIndent) & "CALL " & strLabel_a

  pvt__ASMCallFunc = strResult
End Function

Private Function pvt__ASMCallSub(strLabel_a As String) As String
  Dim strResult As String

  strResult = pvtSpaces(m_lngIndent) & "CALL " & strLabel_a

  pvt__ASMCallSub = strResult
End Function

Private Function pvt__ASMComment(strComment_a As String) As String
  Dim strResult As String

  If Len(strComment_a) > 0 Then
    strResult = pvtSpaces(m_lngIndent * 4) & ";" & Trim$(strComment_a)
  End If

  pvt__ASMComment = strResult
End Function

Private Function pvt__ASMDEFS(strSpace_a As String) As String
  Dim strResult As String

  strResult = pvtSpaces(m_lngIndent) & "DEFS " & strSpace_a

  pvt__ASMDEFS = strResult
End Function

Private Function pvt__ASMEQU(strIdentifier_a As String, strValue_a As String) As String
  Dim strResult As String

  strResult = strIdentifier_a & " EQU " & strValue_a

  pvt__ASMEQU = strResult
End Function

Private Function pvt__ASMFuncRet(strIdentifier_a As String, strDataType_a As String) As String
  Dim strResult As String

  strResult = pvtSpaces(m_lngIndent) & "MEMCPY " & strIdentifier_a & ",retval," & pvtGetDataTypeSize(strDataType_a)
  strResult = strResult & vbCrLf & pvt__ASMStackFree(pvtGetStackUsage() & "")
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & "JP (retaddr)"
  strResult = strResult & vbCrLf

  pvt__ASMFuncRet = strResult
End Function
Private Function pvt__ASMFuncStart(strLabel_a As String) As String
  pvt__ASMFuncStart = pvt__ASMLabel(strLabel_a)
End Function
Private Function pvt__ASMInclude(strFilename_a As String) As String
  Dim strResult As String

  strResult = "#INCLUDE " & Chr$(g_CHARQUOTE) & strFilename_a & g_CHARQUOTE

  pvt__ASMInclude = strResult
End Function

Private Function pvt__ASMLabel(strLabel_a As String) As String
  Dim strResult As String

  strResult = strLabel_a & ":"

  pvt__ASMLabel = strResult
End Function

Private Function pvt__ASMStackAlloc(strIdentifier_a As String, strDataType_a As String) As String
  Dim strResult As String

  strResult = pvtSpaces(m_lngIndent) & "STALLOC " & strIdentifier_a & "," & pvtGetDataTypeSize(strDataType_a)

  pvt__ASMStackAlloc = strResult
End Function

Private Function pvt__ASMStackFree(lngSize_a As Long) As String
  Dim strResult As String

  If (lngSize_a > 0) Then
    strResult = pvtSpaces(m_lngIndent) & "STFREE " & lngSize_a
  End If

  pvt__ASMStackFree = strResult
End Function

Private Function pvt__ASMSubRet() As String
  Dim strResult As String

  strResult = pvt__ASMStackFree(pvtGetStackUsage())
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & "RET"

  pvt__ASMSubRet = strResult
End Function

Private Function pvt__ASMSubStart(strLabel_a As String) As String
  pvt__ASMSubStart = pvt__ASMLabel(strLabel_a)
End Function



Public Sub Initialise()
  'stats
  m_lngLinesIn = 0
  m_lngLinesOut = 0
  m_lngErrors = 0
  m_lngFuncs = 0
  m_lngModules = 0
  m_lngObjects = 0
  m_lngSubs = 0
  m_lngUDTs = 0
  m_lngVars = 0
  m_lngExprns = 0
  m_lngFors = 0
  m_lngIfThens = 0
  m_lngSelectCases = 0
  m_lngWhiles = 0
  m_lngConsts = 0
  
  m_lngIndent = m_INDENT_SIZE
  
  m_strProjectName = ""
  m_strFile = ""
  m_lngLineID = 0
  
  m_blnInObject = False
  m_blnInFunc = False
  m_blnInUDT = False
End Sub
Private Function CompileLine(strProjectName_a As String, strFile_a As String, lngLineID_a As Long, str_a As String) As String
  Dim strResult As String
  Dim strStatement As String
   
  m_strProjectName = strProjectName_a
  m_strFile = strFile_a
  m_lngLineID = lngLineID_a
  strStatement = Trim$(str_a)
  
  m_lngLinesIn = m_lngLinesIn + 1
  If Len(strStatement) > 0 Then
    strResult = pvtParseStatement(strStatement)
  End If
  
  CompileLine = strResult
End Function
Private Function pvt_global_object(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_object: " & str_a)
  
  m_blnInObject = True
  m_lngObjects = m_lngObjects + 1
  
  pvt_global_object = strResult
End Function
Private Function pvt_global_module(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_module: " & str_a)
  
  m_lngModules = m_lngModules + 1
  
  pvt_global_module = strResult
End Function
Private Function pvt_global_endmodule(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_endmodule: " & str_a)
  
  pvt_global_endmodule = strResult
End Function
Private Function pvt_global_type(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_type: " & str_a)
  
  m_blnInUDT = True
  m_lngUDTs = m_lngUDTs + 1
  
  pvt_global_type = strResult
End Function
Private Function pvt_global_function(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_function: " & str_a)
  
  m_blnInFunc = True
  m_lngFuncs = m_lngFuncs + 1
  
  pvt_global_function = strResult
End Function
Private Function pvt_object_function(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("object_function: " & str_a)
  
  m_blnInFunc = True
  m_lngFuncs = m_lngFuncs + 1
  
  pvt_object_function = strResult
End Function
Private Function pvt_object_sub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("object_sub: " & str_a)
  
  m_blnInFunc = True
  m_lngSubs = m_lngSubs + 1
  
  pvt_object_sub = strResult
End Function
Private Function pvt_global_sub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_sub: " & str_a)
  
  m_blnInFunc = True
  m_lngSubs = m_lngSubs + 1
  
  pvt_global_sub = strResult
End Function
Private Function pvt_global_variable(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_variable: " & str_a)
  
  m_lngVars = m_lngVars + 1
  
  pvt_global_variable = strResult
End Function
Private Function pvt_global_constant(str_a As String) As String
  Dim strResult As String
  
  m_lngConsts = m_lngConsts + 1
  
  strResult = pvt__ASMComment("global_constant: " & str_a)
  
  pvt_global_constant = strResult
End Function

Private Function pvt_object_variable(str_a As String) As String
  Dim strResult As String
  
  m_lngVars = m_lngVars + 1
  
  strResult = pvt__ASMComment("object_variable: " & str_a)
  
  pvt_object_variable = strResult
End Function
Private Function pvt_object_constant(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("object_constant: " & str_a)
  
  m_lngConsts = m_lngConsts + 1
  
  pvt_object_constant = strResult
End Function

Private Function pvt_function_variable(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_variable: " & str_a)
  
  m_lngVars = m_lngVars + 1
  
  pvt_function_variable = strResult
End Function
Private Function pvt_object_endfunction(str_a As String) As String
  Dim strResult As String
  
  m_blnInFunc = False
  
  strResult = pvt__ASMComment("object_endfunction: " & str_a)
  
  pvt_object_endfunction = strResult
End Function
Private Function pvt_global_endobject(str_a As String) As String
  Dim strResult As String
  
  m_blnInObject = False
  
  strResult = pvt__ASMComment("global_endobject: " & str_a)
  
  pvt_global_endobject = strResult
End Function
Private Function pvt_global_endfunction(str_a As String) As String
  Dim strResult As String
  
  m_blnInFunc = False
  
  strResult = pvt__ASMComment("global_endfunction: " & str_a)
  
  pvt_global_endfunction = strResult
End Function
Private Function pvt_object_endsub(str_a As String) As String
  Dim strResult As String
  
  m_blnInFunc = False
  
  strResult = pvt__ASMComment("object_endsub: " & str_a)
  
  pvt_object_endsub = strResult
End Function
Private Function pvt_global_endsub(str_a As String) As String
  Dim strResult As String
  
  m_blnInFunc = False
  
  strResult = pvt__ASMComment("global_endsub: " & str_a)
  
  pvt_global_endsub = strResult
End Function
Private Function pvt_global_endtype(str_a As String) As String
  Dim strResult As String
  
  m_blnInUDT = False
  
  strResult = pvt__ASMComment("global_endtype: " & str_a)
  
  pvt_global_endtype = strResult
End Function
Private Function pvt_udt_typevariable(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("udt_typevariable: " & str_a)
 
  pvt_udt_typevariable = strResult
End Function
Private Function pvt_object_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("object_exitfunction: " & str_a)
  
  pvt_object_exitfunction = strResult
End Function
Private Function pvt_global_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_exitfunction: " & str_a)
  
  pvt_global_exitfunction = strResult
End Function
Private Function pvt_object_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("object_exitsub: " & str_a)
  
  pvt_object_exitsub = strResult
End Function
Private Function pvt_global_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("global_exitsub: " & str_a)
  
  pvt_global_exitsub = strResult
End Function
Private Function pvtGetDataTypeSize(strDataType_a As String) As Long
End Function

Private Function pvtGetStackUsage() As Long
End Function

Private Function pvtParseStatement(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""

  Select Case True
    Case m_blnInFunc
      strResult = pvtParseFunction(str)
    
    Case m_blnInUDT
      strResult = pvtParseUDT(str)
      
    Case m_blnInObject
      strResult = pvtParseObject(str)
    
    Case Else
      strResult = pvtParseApp(str)
    
  End Select
  
  pvtParseStatement = strResult
End Function
Private Sub pvtError(strError_a As String)
  Dim objFile As cFile
  
  Set objFile = New cFile
  
  m_lngErrors = m_lngErrors + 1
  If objFile.fOpen(App.Path & "\" & m_strProjectName & "\error.log", g_FMAPPEND) Then
    objFile.fWrite "file '" & m_strFile & "' " & " line " & m_lngLineID & " " & strError_a & vbCrLf
    objFile.fClose
  End If

  Set objFile = Nothing
End Sub
Private Function pvt_function_expression(str_a As String) As String
  Dim strResult As String
  
  m_lngExprns = m_lngExprns + 1
  
  strResult = pvt__ASMComment("function_expression: " & str_a)
  
  pvt_function_expression = strResult
End Function
Private Function pvt_function_selectcase(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_selectcase: " & str_a)
  
  m_lngSelectCases = m_lngSelectCases + 1
  
  pvt_function_selectcase = strResult
End Function
Private Function pvt_function_caseelse(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_caseelse: " & str_a)
  
  pvt_function_caseelse = strResult
End Function
Private Function pvt_function_case(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_case: " & str_a)
  
  pvt_function_case = strResult
End Function
Private Function pvt_function_endselect(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_endselect: " & str_a)
  
  pvt_function_endselect = strResult
End Function
Private Function pvt_function_while(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_while: " & str_a)
  
  m_lngWhiles = m_lngWhiles + 1
  
  pvt_function_while = strResult
End Function
Private Function pvt_function_wend(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_wend: " & str_a)
  
  pvt_function_wend = strResult
End Function
Private Function pvt_function_for(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_for: " & str_a)
  
  m_lngFors = m_lngFors + 1
  
  pvt_function_for = strResult
End Function
Private Function pvt_function_next(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_next: " & str_a)
  
  pvt_function_next = strResult
End Function
Private Function pvt_function_ifthen(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_ifthen: " & str_a)
  
  m_lngIfThens = m_lngIfThens + 1
  
  pvt_function_ifthen = strResult
End Function
Private Function pvt_function_elseifthen(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_elseifthen: " & str_a)
  
  pvt_function_elseifthen = strResult
End Function
Private Function pvt_function_else(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_else: " & str_a)
  
  pvt_function_else = strResult
End Function
Private Function pvt_function_endif(str_a As String) As String
  Dim strResult As String
  
  strResult = pvt__ASMComment("function_endif: " & str_a)
  
  pvt_function_endif = strResult
End Function
Private Function pvtParseObject(str_a As String) As String
  Dim str As String
  Dim strCMD As String
  Dim strResult As String
  
  str = str_a
  strCMD = Trim$(StrTok(str, ":"))
  str = Trim$(str)
  strResult = ""
  
  Select Case True
    Case strCMD = "global_endobject"
      strResult = pvt_global_endobject(str)
    
    Case strCMD = "object_function" 'declare function
      strResult = pvt_object_function(str)
    
    Case strCMD = "object_sub" 'declare sub
      strResult = pvt_object_sub(str)
    
    Case strCMD = "object_constant" 'declare constant
      strResult = pvt_object_constant(str)
    
    Case strCMD = "object_variable" 'declare variable
      strResult = pvt_object_variable(str)
    
    Case Else
      pvtError "syntax error '" & str_a & "'"
    
  End Select
  
  pvtParseObject = strResult
End Function
Private Function pvtParseApp(str_a As String) As String
  Dim str As String
  Dim strCMD As String
  Dim strResult As String
  
  str = str_a
  strCMD = Trim$(StrTok(str, ":"))
  str = Trim$(str)
  strResult = ""
  
  Select Case True
    Case strCMD = "global_object" 'declare object
      strResult = pvt_global_object(str)
    
    Case strCMD = "global_module" 'declare module
      strResult = pvt_global_module(str)
    
    Case strCMD = "global_endmodule" 'end module
      strResult = pvt_global_endmodule(str)
    
    Case strCMD = "global_type" 'declare udt
      strResult = pvt_global_type(str)
    
    Case strCMD = "global_function" 'declare function
      strResult = pvt_global_function(str)
    
    Case strCMD = "global_sub" 'declare sub
      strResult = pvt_global_sub(str)
    
    Case strCMD = "global_constant" 'declare constant
      strResult = pvt_global_constant(str)
    
    Case strCMD = "global_variable" 'declare variable
      strResult = pvt_global_variable(str)
    
    Case Else
      pvtError "syntax error '" & str_a & "'"
    
  End Select
  
  pvtParseApp = strResult
End Function
Private Function pvtParseFunction(str_a As String) As String
  Dim str As String
  Dim strCMD As String
  Dim strResult As String
  
  str = str_a
  strCMD = Trim$(StrTok(str, ":"))
  str = Trim$(str)
  strResult = ""
  
  Select Case True
    Case strCMD = "function_constant" 'declare private constant
      strResult = pvt_function_constant(str)
      
    Case strCMD = "object_endfunction" 'end function
      strResult = pvt_object_endfunction(str)
      
    Case strCMD = "global_endfunction" 'end function
      strResult = pvt_global_endfunction(str)
      
    Case strCMD = "object_exitfunction" 'exit function
      strResult = pvt_object_exitfunction(str)
      
    Case strCMD = "global_exitfunction" 'exit function
      strResult = pvt_global_exitfunction(str)
    
    Case strCMD = "object_endsub" 'end sub
      strResult = pvt_object_endsub(str)
      
    Case strCMD = "global_endsub" 'end sub
      strResult = pvt_global_endsub(str)
    
    Case strCMD = "object_exitsub" 'exit sub
      strResult = pvt_object_exitsub(str)
      
    Case strCMD = "global_exitsub" 'exit sub
      strResult = pvt_global_exitsub(str)
    
    Case strCMD = "function_variable" 'declare private variable
      strResult = pvt_function_variable(str)
    
    Case strCMD = "function_selectcase" 'select case
      strResult = pvt_function_selectcase(str)
    
    Case strCMD = "function_caseelse" 'case else
      strResult = pvt_function_caseelse(str)
    
    Case strCMD = "function_case" 'case
      strResult = pvt_function_case(str)
    
    Case strCMD = "function_endselect" 'end select
      strResult = pvt_function_endselect(str)
    
    Case strCMD = "function_while" 'while
      strResult = pvt_function_while(str)
    
    Case strCMD = "function_wend" 'wend
      strResult = pvt_function_wend(str)
    
    Case strCMD = "function_for" 'for
      strResult = pvt_function_for(str)
    
    Case strCMD = "function_next" 'next
      strResult = pvt_function_next(str)
    
    Case strCMD = "function_ifthen" 'if then
      strResult = pvt_function_ifthen(str)
    
    Case strCMD = "function_elseifthen" 'elseif
      strResult = pvt_function_elseifthen(str)
    
    Case strCMD = "function_else" 'else
      strResult = pvt_function_else(str)
    
    Case strCMD = "function_endif" 'endif
      strResult = pvt_function_endif(str)
    
    Case strCMD = "function_expression" 'logic (ie: function calls & assignments)
      strResult = pvt_function_expression(str)
    
    Case Else
      pvtError "syntax error '" & str_a & "'"
      
  End Select
  
  pvtParseFunction = strResult
End Function
Private Function pvt_function_constant(str_a As String) As String
  Dim strResult As String
  
  m_lngConsts = m_lngConsts + 1
  
  strResult = pvt__ASMComment("function_constant: " & str_a)
  
  pvt_function_constant = strResult
End Function

Private Function pvtParseUDT(str_a As String) As String
  Dim str As String
  Dim strCMD As String
  Dim strResult As String
  
  str = str_a
  strCMD = Trim$(StrTok(str, ":"))
  str = Trim$(str)
  strResult = ""
  
  Select Case True
    Case strCMD = "global_endtype" 'end udt
      strResult = pvt_global_endtype(str)
    
    Case strCMD = "udt_typevariable" 'type variable
      strResult = pvt_udt_typevariable(str)
    
    Case Else
      pvtError "syntax error '" & str_a & "'"
    
  End Select
  
  pvtParseUDT = strResult
End Function
Private Function pvtSpaces(lngIndent_a As Long) As String
  pvtSpaces = Space$(lngIndent_a * 2)
End Function
Public Sub StatisticsSave()
  Dim objFile As cFile
  
  Set objFile = New cFile
  
  If objFile.fOpen(App.Path & "\" & m_strProjectName & "\" & m_TARGET & "\stats.log", g_FMAPPEND) Then
    
    objFile.fWrite vbCrLf & "COMPILER STATS:" & vbCrLf & vbCrLf
    
    objFile.fWrite "line in count     " & m_lngLinesIn & vbCrLf
    objFile.fWrite "line out count    " & m_lngLinesOut & vbCrLf
    objFile.fWrite "error count       " & m_lngErrors & vbCrLf
    objFile.fWrite "module count      " & m_lngModules & vbCrLf
    objFile.fWrite "object count      " & m_lngObjects & vbCrLf
    
    objFile.fWrite "function count    " & m_lngFuncs & vbCrLf
    objFile.fWrite "subroutine count  " & m_lngSubs & vbCrLf
    
    objFile.fWrite "variable count    " & m_lngVars & vbCrLf
    objFile.fWrite "constant count    " & m_lngConsts & vbCrLf
    objFile.fWrite "UDT count         " & m_lngUDTs & vbCrLf
    
    objFile.fWrite "condition count   " & m_lngIfThens & vbCrLf
    
    objFile.fWrite "expression count  " & m_lngExprns & vbCrLf
    objFile.fWrite "for loop count    " & m_lngFors & vbCrLf
    objFile.fWrite "select case count " & m_lngSelectCases & vbCrLf
    objFile.fWrite "while loop count  " & m_lngWhiles & vbCrLf
    
    objFile.fClose
  End If

  Set objFile = Nothing
End Sub
Public Function Compile(strProjectName_a As String) As Long
  Dim lngLineID As Long
  Dim objFileIn As cFile
  Dim objFileOut As cFile
  Dim strCompiled As String
  Dim strLine As String
  Dim strFile As String
  Dim strFiles As String
  Dim strTemp As String
  
  Set objFileIn = New cFile
  Set objFileOut = New cFile
  
  strFiles = ""
  FSDirectoryCreate App.Path & "\" & strProjectName_a
  FSDirectoryCreate App.Path & "\" & strProjectName_a & "\" & m_TARGET
  
  If objFileOut.fOpen(App.Path & "\" & strProjectName_a & "\" & m_TARGET & "\main.asm", g_FMAPPEND) Then
    strFile = App.Path & "\" & strProjectName_a & "\parser.out"
    Debug.Print "processing: " & strFile
    If objFileIn.fOpen(strFile, g_FMREAD) Then
      lngLineID = 0
      
      While Not objFileIn.EndOfFile
        strLine = objFileIn.fRead()
        
        lngLineID = lngLineID + 1
        strCompiled = CompileLine(strProjectName_a, strFile, lngLineID, strLine)
        If Len(strCompiled) > 0 Then
          m_lngLinesOut = m_lngLinesOut + StrTokCount(strCompiled, vbCrLf)
          objFileOut.fWrite strCompiled & vbCrLf
        End If
      Wend
      
      objFileIn.fClose
    End If
    DoEvents
    objFileOut.fClose
  End If
  
  Set objFileIn = Nothing
  Set objFileOut = Nothing
  
  Compile = m_lngErrors
End Function
Private Sub Class_Initialize()
  Initialise
End Sub
