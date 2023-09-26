VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cJava"
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
'IP   = IN PROGRESS
'
'***** LIMITATIONS:
'
'       1 - need to implement pvtExpressionGet() to translate from VB expressions to Java expressions
'       2 - when object explicitly set to nothing, need to explicitly invoke destructor
'       3 - m_lngIndent - 1 should have range checking
'       4 - PRESENTLY CANNOT HAVE NESTED SELECT CASE CONSTRUCTS (see variables m_blnSelectCaseFirst,m_strSelectCaseExpression below) - should to change to stack arrangement
'       5 - UDTs are not translated to anything, should translate to a class
'
'(DATATYPES)
'
'DONE   byte, boolean, integer, long, currency, single, double, string, object
'
'(GLOBAL)
'
'DONE   object <identifer>
'DONE   module <identifer>
'DONE   end module
'       private/public type <identifer>
'DONE   private/public function <identifer>([<identifer>...]) as <datatype>
'DONE   private/public sub <identifer>([<identifier>...])
'DONE   private/public const <identifer> [as <datatype>] = <value>
'DONE   private/public <identifier> as <datatype>
'
'(FUNCTION)
'
'DONE   const <identifer> [as <datatype>] = <value>
'DONE   end function
'DONE   exit function
'DONE   end sub
'DONE   exit sub
'DONE   dim <identifer> as <datatype>
'DONE   select case <expression>
'DONE   case else
'DONE   case <expression>
'DONE   end select
'DONE   while <expression>
'DONE   wend
'DONE   for <expression> = <expression> to <expression>
'DONE   next
'DONE   if <expression> then
'DONE   elseif <expression> then
'DONE   else
'DONE   end if
'DONE   <expression>
'
'(OBJECT)
'
'DONE   end object
'       private type <identifer>
'DONE   private/public function <identifer>([<identifer>...]) as <datatype>
'DONE   private/public sub <identifer>([<identifier>...])
'DONE   private const <identifer> [as <datatype>] = <value>
'DONE   private/public <identifier> as <datatype>
'
'(UDT)
'
'       end type
'       <identifer> as <datatype>
'
Option Explicit

Private Const m_NAME = "cJava"

Private Const m_TARGET = "java"

Private m_strProjectName As String
Private m_strFile As String
Private m_lngLineID As Long

Private m_blnInObject As Boolean
Private m_blnInFunc As Boolean
Private m_blnInUDT As Boolean

Private m_lngIndent As Long

Private m_blnConstructor As Boolean 'do we have a constructor for this object?
Private m_strObjectName As String 'needed for invoking constructor

Private m_blnFinaliser As Boolean 'do we have a finaliser for this object?
Private m_blnSelectCaseFirst As Boolean
Private m_strSelectCaseExpression As String 'temporary store for select case expression
                                            'since Java cannot evaluate expressions in the switch, we must turn it into an if elseif

Private m_objFileOut As cFile
Private m_strFilename As String

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
  
  m_lngIndent = 0
  
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
  Dim strIdentifier As String
  Dim strResult As String
  
  m_blnFinaliser = False
  m_blnConstructor = False
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  m_strFilename = App.Path & "\" & m_strProjectName & "\" & m_TARGET & "\" & strIdentifier & ".java"
  m_objFileOut.fOpen m_strFilename, g_FMAPPEND
  
  m_strObjectName = strIdentifier
  
  strResult = pvtSpaces(m_lngIndent) & "package " & m_strProjectName & ";"
  strResult = strResult & vbCrLf & vbCrLf & pvtSpaces(m_lngIndent) & "public class " & strIdentifier & " {"
  
  m_blnInObject = True
  m_lngIndent = m_lngIndent + 1
  m_lngObjects = m_lngObjects + 1
  
  pvt_global_object = strResult
End Function
Private Function pvt_global_module(str_a As String) As String
  Dim strIdentifier As String
  Dim strResult As String
  
  m_blnFinaliser = False
  m_blnConstructor = False
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  m_strFilename = App.Path & "\" & m_strProjectName & "\" & m_TARGET & "\" & strIdentifier & ".java"
  m_objFileOut.fOpen m_strFilename, g_FMAPPEND
  
  m_strObjectName = strIdentifier
  
  strResult = pvtSpaces(m_lngIndent) & "package " & m_strProjectName & ";"
  strResult = strResult & vbCrLf & vbCrLf & pvtSpaces(m_lngIndent) & "public class " & strIdentifier & " {"
  
  m_lngIndent = m_lngIndent + 1
  m_lngModules = m_lngModules + 1
  
  pvt_global_module = strResult
End Function
Private Function pvt_global_endmodule(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_global_endmodule = strResult
End Function
Private Function pvt_global_type(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "global_type: " & str_a
  
  m_blnInUDT = True
  m_lngIndent = m_lngIndent + 1
  m_lngUDTs = m_lngUDTs + 1
  
  pvt_global_type = strResult
End Function
Private Function pvt_global_function(str_a As String) As String
  Dim lngDataType As String
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strParameter As String
  Dim strParameters As String
  Dim strResult As String
  Dim strScope As String
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strParameters = XMLTokGet(str_a, "parameters")
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & strScope & " " & strDataType & " " & strIdentifier & "("
  While Len(strParameters) > 0
    strParameter = XMLTok(strParameters, "parameter")
    strResult = strResult & pvtDataTypeGet(XMLTokGet(strParameter, "paramdatatype")) & " " & XMLTokGet(strParameter, "paramidentifier") & ", "
  Wend
  If Right$(strResult, 2) = ", " Then
    strResult = Left$(strResult, Len(strResult) - 2)
  End If
  strResult = strResult & ") {"
  
  m_blnInFunc = True
  m_lngIndent = m_lngIndent + 1
  m_lngFuncs = m_lngFuncs + 1
  
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & pvtDataTypeGet(strDataType) & " sys_result;"
  
  pvt_global_function = strResult
End Function
Private Function pvt_object_function(str_a As String) As String
  Dim lngDataType As String
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strParameter As String
  Dim strParameters As String
  Dim strResult As String
  Dim strScope As String
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strParameters = XMLTokGet(str_a, "parameters")
  
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & strScope & " " & strDataType & " " & strIdentifier & "("
  While Len(strParameters) > 0
    strParameter = XMLTok(strParameters, "parameter")
    strResult = strResult & pvtDataTypeGet(XMLTokGet(strParameter, "paramdatatype")) & " " & XMLTokGet(strParameter, "paramidentifier") & ", "
  Wend
  If Right$(strResult, 2) = ", " Then
    strResult = Left$(strResult, Len(strResult) - 2)
  End If
  strResult = strResult & ") {"
  
  m_blnInFunc = True
  m_lngIndent = m_lngIndent + 1
  m_lngFuncs = m_lngFuncs + 1
  
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & pvtDataTypeGet(strDataType) & " sys_result;"
  
  pvt_object_function = strResult
End Function
Private Function pvt_object_sub(str_a As String) As String
  Dim lngScope As Long
  Dim strIdentifier As String
  Dim strParameter As String
  Dim strParameters As String
  Dim strResult As String
  Dim strScope As String
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strParameters = XMLTokGet(str_a, "parameters")
  
  If strIdentifier = "class_initialize" Then
    m_blnConstructor = True
  End If
  
  If strIdentifier = "class_terminate" Then
    m_blnFinaliser = True
    strScope = pvtScopeGet(m_SCOPEGLOBAL) 'must force this to be public so java can explicitly invoke it when out of scope
  End If
  
  'finalizer stuff
  If strIdentifier = "finalize" Then
    strScope = "protected"
  End If
  
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & strScope & " void " & strIdentifier & "("
  While Len(strParameters) > 0
    strParameter = XMLTok(strParameters, "parameter")
    strResult = strResult & pvtDataTypeGet(XMLTokGet(strParameter, "paramdatatype")) & " " & XMLTokGet(strParameter, "paramidentifier") & ", "
  Wend
  If Right$(strResult, 2) = ", " Then
    strResult = Left$(strResult, Len(strResult) - 2)
  End If
  strResult = strResult & ") {"
  
  m_blnInFunc = True
  m_lngIndent = m_lngIndent + 1
  m_lngSubs = m_lngSubs + 1
  
  pvt_object_sub = strResult
End Function
Private Function pvt_global_sub(str_a As String) As String
  Dim lngScope As Long
  Dim strIdentifier As String
  Dim strParameter As String
  Dim strParameters As String
  Dim strResult As String
  Dim strScope As String
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strParameters = XMLTokGet(str_a, "parameters")
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & strScope & " void " & strIdentifier & "("
  While Len(strParameters) > 0
    strParameter = XMLTok(strParameters, "parameter")
    strResult = strResult & pvtDataTypeGet(XMLTokGet(strParameter, "paramdatatype")) & " " & XMLTokGet(strParameter, "paramidentifier") & ", "
  Wend
  If Right$(strResult, 2) = ", " Then
    strResult = Left$(strResult, Len(strResult) - 2)
  End If
  strResult = strResult & ") {"
  
  m_blnInFunc = True
  m_lngIndent = m_lngIndent + 1
  m_lngSubs = m_lngSubs + 1
  
  pvt_global_sub = strResult
End Function
Private Function pvt_global_variable(str_a As String) As String
  Dim lngDataType As Long
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strScope As String
  
  m_lngVars = m_lngVars + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strResult = pvtSpaces(m_lngIndent) & strScope & " " & strDataType & " " & strIdentifier & ";"
  
  pvt_global_variable = strResult
End Function
Private Function pvt_global_constant(str_a As String) As String
  Dim lngDataType As Long
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strScope As String
  Dim strValue As String
  
  m_lngConsts = m_lngConsts + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strValue = XMLTokGet(str_a, "value")
  If lngDataType = m_DTSTRING Then
    strValue = Chr$(34) & strValue & Chr$(34)
  End If
  strResult = pvtSpaces(m_lngIndent) & strScope & " static final " & strDataType & " " & strIdentifier & " = " & strValue & ";"
  
  pvt_global_constant = strResult
End Function

Private Function pvt_object_variable(str_a As String) As String
  Dim lngDataType As Long
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strScope As String
  
  m_lngVars = m_lngVars + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strResult = pvtSpaces(m_lngIndent) & strScope & " " & strDataType & " " & strIdentifier & ";"
  
  pvt_object_variable = strResult
End Function
Private Function pvt_object_constant(str_a As String) As String
  Dim lngDataType As Long
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strScope As String
  Dim strValue As String
  
  m_lngConsts = m_lngConsts + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strValue = XMLTokGet(str_a, "value")
  If lngDataType = m_DTSTRING Then
    strValue = Chr$(34) & strValue & Chr$(34)
  End If
  strResult = pvtSpaces(m_lngIndent) & strScope & " static final " & strDataType & " " & strIdentifier & " = " & strValue & ";"
  
  pvt_object_constant = strResult
End Function
Private Function pvt_function_variable(str_a As String) As String
  Dim lngDataType As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  
  m_lngVars = m_lngVars + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strResult = pvtSpaces(m_lngIndent) & strDataType & " " & strIdentifier & ";"
  
  pvt_function_variable = strResult
End Function
Private Function pvt_object_endfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return (sys_result);"
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & "}"
  
  pvt_object_endfunction = strResult
End Function
Private Function pvt_global_endobject(str_a As String) As String
  Dim strResult As String
  
  If m_blnConstructor Then
    'protected void <objectname>() { class_initialize(); }
    strResult = strResult & pvtParseStatement("object_sub: <scope>1</scope><fullidentifier>cnode_" & m_strObjectName & "</fullidentifier><identifier>" & m_strObjectName & "</identifier><parameters></parameters>") & vbCrLf
    strResult = strResult & pvtParseStatement("function_expression: <expression>class_initialize</expression>") & vbCrLf
    strResult = strResult & pvtParseStatement("object_endsub:") & vbCrLf
  End If
  
  If m_blnFinaliser Then
    'protected void finalize() { class_terminate(); }
    strResult = strResult & pvtParseStatement("object_sub: <scope>2</scope><fullidentifier>cnode_finalize</fullidentifier><identifier>finalize</identifier><parameters></parameters>") & vbCrLf
    strResult = strResult & pvtParseStatement("function_expression: <expression>class_terminate</expression>") & vbCrLf
    strResult = strResult & pvtParseStatement("object_endsub:") & vbCrLf
  End If
    
  m_lngIndent = m_lngIndent - 1
  m_blnInObject = False
  
  strResult = strResult & pvtSpaces(m_lngIndent) & "}"
  
  pvt_global_endobject = strResult
End Function
Private Function pvt_global_endfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return (sys_result);"
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  
  strResult = strResult & vbCrLf & pvtSpaces(m_lngIndent) & "}"
  
  pvt_global_endfunction = strResult
End Function
Private Function pvt_object_endsub(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_object_endsub = strResult
End Function
Private Function pvt_global_endsub(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_global_endsub = strResult
End Function
Private Function pvt_global_endtype(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInUDT = False
  
  strResult = pvtSpaces(m_lngIndent) & "global_endtype:"
  
  pvt_global_endtype = strResult
End Function
Private Function pvt_udt_typevariable(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "udt_typevariable: " & str_a
  
  pvt_udt_typevariable = strResult
End Function
Private Function pvt_object_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return (sys_result);"
  
  pvt_object_exitfunction = strResult
End Function
Private Function pvt_global_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return (sys_result);"
  
  pvt_global_exitfunction = strResult
End Function
Private Function pvt_object_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return;"
  
  pvt_object_exitsub = strResult
End Function
Private Function pvt_global_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "return;"
  
  pvt_global_exitsub = strResult
End Function
Private Function pvtDataTypeGet(strDataType_a As String) As String
  Dim strResult As String
  
  strResult = ""
  
  Select Case strDataType_a
    Case m_DTBYTE
      strResult = "byte"
    
    Case m_DTBOOLEAN
      strResult = "boolean"
    
    Case m_DTINTEGER
      strResult = "short"
    
    Case m_DTLONG
      strResult = "int"
    
    Case m_DTCURRENCY
      strResult = "float"
    
    Case m_DTSINGLE
      strResult = "float"
    
    Case m_DTDOUBLE
      strResult = "double"
    
    Case m_DTSTRING
      strResult = "String"
    
    Case m_DTOBJECT
      strResult = "Object"
    
    Case Else
      strResult = strDataType_a
  
  End Select
  
  pvtDataTypeGet = strResult
End Function

Private Function pvtExpressionGet(strExpression_a As String) As String
  pvtExpressionGet = "<EXPRESSION>" & strExpression_a & "</EXPRESSION>"
End Function

Private Function pvtScopeGet(strScope_a As String) As String
  Dim strResult As String
  
  strResult = ""
  
  Select Case strScope_a
    Case m_SCOPEGLOBAL
      strResult = "public"
    
    Case m_SCOPEMODULE
      strResult = "private"
    
    Case m_SCOPELOCAL
      strResult = ""
    
  End Select
  
  pvtScopeGet = strResult
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
  Dim strExpression As String
  Dim strResult As String
  
  m_lngExprns = m_lngExprns + 1
  
  strExpression = XMLTokGet(str_a, "expression")
  strResult = pvtSpaces(m_lngIndent) & pvtExpressionGet(strExpression) & ";"
  
  pvt_function_expression = strResult
End Function
Private Function pvt_function_selectcase(str_a As String) As String
  Dim strResult As String
  
  m_strSelectCaseExpression = XMLTokGet(str_a, "expression")
  m_blnSelectCaseFirst = True
  
  m_lngIndent = m_lngIndent + 1
  m_lngSelectCases = m_lngSelectCases + 1
  
  pvt_function_selectcase = strResult
End Function
Private Function pvt_function_caseelse(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent - 1) & "} else {"
  
  pvt_function_caseelse = strResult
End Function
Private Function pvt_function_case(str_a As String) As String
  Dim strExpression As String
  Dim strResult As String
  
  strExpression = XMLTokGet(str_a, "expression")
  If m_blnSelectCaseFirst Then
    strResult = pvtSpaces(m_lngIndent - 1) & "if ((" & pvtExpressionGet(m_strSelectCaseExpression) & ") == (" & pvtExpressionGet(strExpression) & ")) {"
    m_blnSelectCaseFirst = False
  Else
    strResult = pvtSpaces(m_lngIndent - 1) & "} elseif ((" & pvtExpressionGet(m_strSelectCaseExpression) & ") == (" & pvtExpressionGet(strExpression) & ")) {"
  End If
  
  pvt_function_case = strResult
End Function
Private Function pvt_function_endselect(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_function_endselect = strResult
End Function
Private Function pvt_function_while(str_a As String) As String
  Dim strExpression As String
  Dim strResult As String
  
  strExpression = XMLTokGet(str_a, "expression")
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & "while (" & pvtExpressionGet(strExpression) & ") {"
  
  m_lngIndent = m_lngIndent + 1
  m_lngWhiles = m_lngWhiles + 1
  
  pvt_function_while = strResult
End Function
Private Function pvt_function_wend(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_function_wend = strResult
End Function
Private Function pvt_function_for(str_a As String) As String
  Dim strFromExpression As String
  Dim strResult As String
  Dim strStep As String
  Dim strToExpression As String
  
  strFromExpression = XMLTokGet(str_a, "fromexpression")
  strToExpression = XMLTokGet(str_a, "toexpression")
  strStep = XMLTokGet(str_a, "step")
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & "for (" & pvtExpressionGet(strFromExpression) & ";" & pvtExpressionGet(strToExpression) & ";" & pvtExpressionGet(strStep) & ") {"
  
  m_lngIndent = m_lngIndent + 1
  m_lngFors = m_lngFors + 1
  
  pvt_function_for = strResult
End Function
Private Function pvt_function_next(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
  pvt_function_next = strResult
End Function
Private Function pvt_function_ifthen(str_a As String) As String
  Dim strExpression As String
  Dim strResult As String
  
  strExpression = XMLTokGet(str_a, "expression")
  strResult = pvtSpaces(m_lngIndent) & "if (" & pvtExpressionGet(strExpression) & ") {"
  
  m_lngIndent = m_lngIndent + 1
  m_lngIfThens = m_lngIfThens + 1
  
  pvt_function_ifthen = strResult
End Function
Private Function pvt_function_elseifthen(str_a As String) As String
  Dim strExpression As String
  Dim strResult As String
  
  strExpression = XMLTokGet(str_a, "expression")
  strResult = pvtSpaces(m_lngIndent - 1) & "} elseif (" & pvtExpressionGet(strExpression) & ") {"
  
  pvt_function_elseifthen = strResult
End Function
Private Function pvt_function_else(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent - 1) & "} else {"
  
  pvt_function_else = strResult
End Function
Private Function pvt_function_endif(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "}"
  
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
  Dim lngDataType As Long
  Dim lngScope As Long
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strScope As String
  Dim strValue As String
    
  m_lngConsts = m_lngConsts + 1
  
  lngDataType = Val(XMLTokGet(str_a, "datatype"))
  strDataType = pvtDataTypeGet(XMLTokGet(str_a, "datatype"))
  
  lngScope = Val(XMLTokGet(str_a, "scope"))
  strScope = pvtScopeGet(XMLTokGet(str_a, "scope"))
  
  strIdentifier = XMLTokGet(str_a, "identifier")
  strValue = XMLTokGet(str_a, "value")
  If lngDataType = m_DTSTRING Then
    strValue = Chr$(34) & strValue & Chr$(34)
  End If
  strResult = pvtSpaces(m_lngIndent) & "static final " & strDataType & " " & strIdentifier & " = " & strValue & ";"
  
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
  Dim strCompiled As String
  Dim strLine As String
  Dim strFile As String
  Dim strFiles As String
  Dim strTemp As String
  
  Set objFileIn = New cFile
  Set m_objFileOut = New cFile
  
  strFiles = ""
  FSDirectoryCreate App.Path & "\" & strProjectName_a
  FSDirectoryCreate App.Path & "\" & strProjectName_a & "\" & m_TARGET
  
  m_strFilename = App.Path & "\" & strProjectName_a & "\" & m_TARGET & "\compiler.out"
  If m_objFileOut.fOpen(m_strFilename, g_FMAPPEND) Then
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
          m_objFileOut.fWrite strCompiled & vbCrLf
        End If
      Wend
      
      objFileIn.fClose
    End If
    DoEvents
    m_objFileOut.fClose
  End If
  
  Set objFileIn = Nothing
  Set m_objFileOut = Nothing
  
  Compile = m_lngErrors
End Function

Private Sub Class_Initialize()
  Initialise
End Sub

