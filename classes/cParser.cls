VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "cParser"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
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
Option Explicit

Private Const m_NAME = "cParser"

Private m_strProjectName As String
Private m_strFile As String
Private m_lngLineID As Long

Private m_blnInObject As Boolean
Private m_blnInFunc As Boolean
Private m_blnInSub As Boolean
Private m_blnInUDT As Boolean

Private m_blnPrivateFunc As Boolean 'needed for private members of private/public functions
Private m_blnPrivateUDT As Boolean 'needed for private members of private/public udt

Private m_strCurrObject As String 'or module
Private m_strCurrFunc As String 'or sub
Private m_strCurrUDT As String

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
Private Const m_DTUNKNOWN = "0"
Private Const m_DTBYTE = "1"
Private Const m_DTBOOLEAN = "2"
Private Const m_DTINTEGER = "3"
Private Const m_DTLONG = "4"
Private Const m_DTCURRENCY = "5"
Private Const m_DTSINGLE = "6"
Private Const m_DTDOUBLE = "7"
Private Const m_DTSTRING = "8"
Private Const m_DTOBJECT = "9"

'scopes
Private Const m_SCOPEGLOBAL = 1
Private Const m_SCOPEMODULE = 2
Private Const m_SCOPELOCAL = 3

Private m_lngIndent As Long

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
Private m_lngCondifs As Long
Private m_lngExprns As Long
Private m_lngFors As Long
Private m_lngIfThens As Long
Private m_lngSelectCases As Long
Private m_lngWhiles As Long
Private m_lngConsts As Long

Private m_objDefines As cCollection
Private m_objElements As cCollection

Private m_blnSkip As Boolean
Private Function pvtDataTypeGet(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  If Left$(str_a, 4) = "new " Then
    pvtError "invalid datatype '" & str_a & "'"
    strResult = m_DTUNKNOWN
  Else
    Select Case str_a
      Case "byte"
        strResult = m_DTBYTE
        
      Case "boolean"
        strResult = m_DTBOOLEAN
        
      Case "integer"
        strResult = m_DTINTEGER
        
      Case "long"
        strResult = m_DTLONG
        
      Case "currency"
        strResult = m_DTCURRENCY
        
      Case "single"
        strResult = m_DTSINGLE
        
      Case "double"
        strResult = m_DTDOUBLE
        
      Case "string"
        strResult = m_DTSTRING
        
      Case "object"
        strResult = m_DTOBJECT
        
      Case Else
        strResult = str_a
        
    End Select
  End If

  pvtDataTypeGet = strResult
End Function
Public Function Parse(strProjectName_a As String, strFileList_a As String) As Long
  Dim blnMore As Boolean
  Dim lngLineID As Long
  Dim objFileIn As cFile
  Dim objFileOut As cFile
  Dim strLine As String
  Dim strFile As String
  Dim strFiles As String
  Dim strParsed As String
  Dim strTemp As String
  
  Set objFileIn = New cFile
  Set objFileOut = New cFile
  
  strFiles = strFileList_a
  FSDirectoryCreate App.Path & "\" & strProjectName_a
  
  If objFileOut.fOpen(App.Path & "\" & strProjectName_a & "\parser.out", g_FMAPPEND) Then
    
    While Len(strFiles) > 0
      strFile = StrTok(strFiles, Chr$(g_CHARDELIM))
      Debug.Print "processing: " & strFile
      If objFileIn.fOpen(strFile, g_FMREAD) Then
        lngLineID = 0
        blnMore = False
        
        While Not objFileIn.EndOfFile
          strTemp = objFileIn.fRead()
          
          lngLineID = lngLineID + 1
          If blnMore Then
            strLine = Left$(strLine, Len(strLine) - 2) & strTemp
            blnMore = False
          Else
            strLine = strTemp
          End If
          strParsed = ParseLine(strProjectName_a, strFile, lngLineID, strLine, blnMore)
          If Len(strParsed) > 0 Then
            m_lngLinesOut = m_lngLinesOut + StrTokCount(strParsed, vbCrLf)
            objFileOut.fWrite strParsed & vbCrLf
          End If
        Wend
        
        objFileIn.fClose
      End If
      DoEvents
    Wend
    
    objFileOut.fClose
  End If
  
  Set objFileOut = Nothing
  Set objFileIn = Nothing
  
  Parse = m_lngErrors
End Function
Public Function ParseVB6(strProjectName_a As String, strProjectPath_a As String) As Long
  Dim blnEndofCrap As Boolean
  Dim blnMore As Boolean
  Dim lngLineID As Long
  Dim objFileIn As cFile
  Dim objFileOut As cFile
  Dim strBasePath As String
  Dim strCondition As String
  Dim strConditions As String
  Dim strLine As String
  Dim strFile As String
  Dim strFiles As String
  Dim strModuleName As String
  Dim strParsed As String
  Dim strTemp As String
  Dim strType As String
  
  Set objFileIn = New cFile
  Set objFileOut = New cFile
  
  strFiles = ""
  strConditions = ""
  FSDirectoryCreate App.Path & "\" & strProjectName_a
  
  If Len(strProjectPath_a) > 0 Then
    strTemp = StrReverse(strProjectPath_a)
    StrTok strTemp, "\"
    strBasePath = StrReverse(strTemp) & "\"
    
    If objFileIn.fOpen(strProjectPath_a, g_FMREAD) Then
      While Not objFileIn.EndOfFile
        strLine = objFileIn.fRead()
        
        Select Case True
          Case LCase$(Left$(strLine, 9)) = "condcomp="
            StrTok strLine, Chr$(g_CHARQUOTE)
            strCondition = StrTok(strLine, Chr$(g_CHARQUOTE))
            strConditions = TokStr(strConditions, strCondition, ":")
          
          Case LCase$(Left$(strLine, 7)) = "module="
            StrTok strLine, " "
            strFile = strBasePath & strLine
            strType = "M"
            strFiles = TokStr(strFiles, strType, Chr$(g_CHARDELIM))
            strFiles = TokStr(strFiles, strFile, Chr$(g_CHARDELIM))
            
          Case LCase$(Left$(strLine, 6)) = "class="
            StrTok strLine, " "
            strFile = strBasePath & strLine
            strType = "C"
            strFiles = TokStr(strFiles, strType, Chr$(g_CHARDELIM))
            strFiles = TokStr(strFiles, strFile, Chr$(g_CHARDELIM))
          
          Case LCase$(Left$(strLine, 5)) = "form="
            StrTok strLine, "="
            strType = "C"
            strFile = strBasePath & strLine
            strFiles = TokStr(strFiles, strType, Chr$(g_CHARDELIM))
            strFiles = TokStr(strFiles, strFile, Chr$(g_CHARDELIM))
          
        End Select
      Wend
      objFileIn.fClose
    End If
  End If
  
  If objFileOut.fOpen(App.Path & "\" & strProjectName_a & "\parser.out", g_FMAPPEND) Then
    
    While Len(strFiles) > 0
      strType = StrTok(strFiles, Chr$(g_CHARDELIM))
      strFile = StrTok(strFiles, Chr$(g_CHARDELIM))
      strModuleName = ""
      Debug.Print "processing: " & strFile
      If objFileIn.fOpen(strFile, g_FMREAD) Then
        lngLineID = 0
        blnMore = False
        
        strParsed = ""
        While Len(strConditions) > 0
          strCondition = StrTok(strConditions, ":")
          strParsed = ParseLine(strProjectName_a, strFile, 0, "#define " & strCondition, blnMore)
          If Len(strParsed) > 0 Then
            m_lngLinesOut = m_lngLinesOut + StrTokCount(strParsed, vbCrLf)
            objFileOut.fWrite strParsed & vbCrLf
          End If
        Wend
      
        'skip junk lines at the start
        blnEndofCrap = False
        While Not (objFileIn.EndOfFile Or blnEndofCrap)
          strLine = LCase$(Trim$(objFileIn.fRead()))
          
          lngLineID = lngLineID + 1
          If Left$(strLine, 21) = "attribute vb_name = " & Chr$(g_CHARQUOTE) Then
            StrTok strLine, Chr$(g_CHARQUOTE)
            strModuleName = StrTok(strLine, Chr$(g_CHARQUOTE))
          End If
          If strLine = "option explicit" Then
            blnEndofCrap = True
          End If
        Wend
        
        strParsed = ""
        Select Case strType
          Case "C"
            strParsed = ParseLine(strProjectName_a, strFile, 0, "object " & strModuleName, blnMore)
        
          Case "M"
            strParsed = ParseLine(strProjectName_a, strFile, 0, "module " & strModuleName, blnMore)
        
        End Select
        If Len(strParsed) > 0 Then
          m_lngLinesOut = m_lngLinesOut + StrTokCount(strParsed, vbCrLf)
          objFileOut.fWrite strParsed & vbCrLf
        End If
        
        While Not objFileIn.EndOfFile
          strTemp = objFileIn.fRead()
          
          lngLineID = lngLineID + 1
          If blnMore Then
            strLine = Left$(strLine, Len(strLine) - 2) & strTemp
            blnMore = False
          Else
            strLine = strTemp
          End If
          strParsed = ParseLine(strProjectName_a, strFile, lngLineID, strLine, blnMore)
          If Len(strParsed) > 0 Then
            m_lngLinesOut = m_lngLinesOut + StrTokCount(strParsed, vbCrLf)
            objFileOut.fWrite strParsed & vbCrLf
          End If
        Wend
        
        strParsed = ""
        Select Case strType
          Case "C"
            strParsed = ParseLine(strProjectName_a, strFile, 0, "end object", blnMore)
            
          Case "M"
            strParsed = ParseLine(strProjectName_a, strFile, 0, "end module", blnMore)
        
        End Select
        If Len(strParsed) > 0 Then
          m_lngLinesOut = m_lngLinesOut + StrTokCount(strParsed, vbCrLf)
          objFileOut.fWrite strParsed & vbCrLf
        End If
        
        objFileIn.fClose
      End If
      DoEvents
    Wend
    
    objFileOut.fClose
  End If
  
  Set objFileOut = Nothing
  Set objFileIn = Nothing
  
  ParseVB6 = m_lngErrors
End Function
Public Sub Initialise()
  m_blnSkip = False
  
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
  m_lngCondifs = 0
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
  m_blnInSub = False
  m_blnInUDT = False
  
  m_strCurrObject = ""
  m_strCurrFunc = ""
  m_strCurrUDT = ""
  
  Set m_objElements = New cCollection
  pvtElementAdd m_ETOBJECT, m_SCOPEGLOBAL, "app", "app", "", m_DTUNKNOWN, ""
  
  Set m_objDefines = New cCollection
End Sub
Private Function ParseLine(strProjectName_a As String, strFile_a As String, lngLineID_a As Long, str_a As String, blnMore_a As Boolean) As String
  Dim str As String
  Dim strResult As String
  Dim strStatement As String
   
  m_strProjectName = strProjectName_a
  m_strFile = strFile_a
  m_lngLineID = lngLineID_a
  str = str_a
  While Len(str) > 0 And Not blnMore_a
    str = Trim$(str)
    If Left$(str, 1) <> "'" Then 'ignore comments
      If Right$(str, 2) = " _" Then
        blnMore_a = True
      Else
        blnMore_a = False
        
        m_lngLinesIn = m_lngLinesIn + 1
        
        strStatement = pvtStatementTok(str, ":")
        
        If Len(strStatement) > 0 Then
          If Len(strResult) > 0 Then
            strResult = strResult & vbCrLf & pvtParseStatement(strStatement)
          Else
            strResult = pvtParseStatement(strStatement)
          End If
        End If
      End If
    Else
      str = ""
    End If
  Wend
  
  ParseLine = strResult
End Function
Private Function pvt_global_object(str_a As String) As String
  Dim str As String
  Dim strIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If Not pvtElementAdd(m_ETOBJECT, m_SCOPEGLOBAL, strIdentifier, strIdentifier, "", m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrObject = ""
  Else
    m_strCurrObject = strIdentifier
  End If
  
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & "global_object: <identifier>" & XMLEnc(strIdentifier) & "</identifier>"
  
  m_blnInObject = True
  m_lngIndent = m_lngIndent + 1
  m_lngObjects = m_lngObjects + 1
  
  pvt_global_object = strResult
End Function
Private Function pvt_global_module(str_a As String) As String
  Dim str As String
  Dim strIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If Not pvtElementAdd(m_ETMODULE, m_SCOPEGLOBAL, strIdentifier, strIdentifier, "", m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrObject = ""
  Else
    m_strCurrObject = strIdentifier
  End If
  
  strResult = vbCrLf & pvtSpaces(m_lngIndent) & "global_module: <identifier>" & XMLEnc(strIdentifier) & "</identifier>"
  
  m_lngIndent = m_lngIndent + 1
  m_lngModules = m_lngModules + 1
  
  pvt_global_module = strResult
End Function
Private Function pvt_global_endmodule(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_strCurrObject = ""
  
  strResult = pvtSpaces(m_lngIndent) & "global_endmodule:"
  
  pvt_global_endmodule = strResult
End Function
Private Function pvt_global_type(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strIdentifier As String
  Dim strFullIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If lngScope_a = m_SCOPEGLOBAL Then
    strFullIdentifier = strIdentifier
    m_blnPrivateUDT = False
  Else
    strFullIdentifier = m_strCurrObject & "_" & strIdentifier
    m_blnPrivateUDT = True
  End If
  If Not pvtElementAdd(m_ETUDT, lngScope_a, strFullIdentifier, strIdentifier, "", m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrUDT = ""
  Else
    m_strCurrUDT = strIdentifier
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "global_type: <todo>" & XMLEnc(str_a) & "</todo>"
  
  m_blnInUDT = True
  m_lngIndent = m_lngIndent + 1
  m_lngUDTs = m_lngUDTs + 1
  
  pvt_global_type = strResult
End Function
Private Function pvt_global_function(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strParameters As String
  Dim strResult As String
  Dim strReturnType As String
  Dim strTemp As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, "(")
  If lngScope_a = m_SCOPEGLOBAL Then
    strFullIdentifier = strIdentifier
    m_blnPrivateFunc = False
  Else
    strFullIdentifier = m_strCurrObject & "_" & strIdentifier
    m_blnPrivateFunc = True
  End If
  
  strParameters = ""
  strTemp = StrTok(str, ")")
  While Len(strTemp) > 0
    strParameters = TokStr(strParameters, StrTok(strTemp, " "), Chr$(g_CHARDELIM))
    StrTok strTemp, " "
    strParameters = TokStr(strParameters, pvtDataTypeGet(StrTok(strTemp, ",")), Chr$(g_CHARDELIM))
    strTemp = Trim$(strTemp)
  Wend
  
  StrTok str, " as "
  strReturnType = pvtDataTypeGet(str)
  
  If Not pvtElementAdd(m_ETFUNC, lngScope_a, strFullIdentifier, strIdentifier, strParameters, strReturnType, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrFunc = ""
  Else
    m_strCurrFunc = strIdentifier
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "global_function: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strReturnType & "") & "</datatype>"
  strResult = strResult & "<parameters>"
  strTemp = strParameters
  While Len(strTemp) > 0
    strResult = strResult & "<parameter>"
    strResult = strResult & "<paramidentifier>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramidentifier>"
    strResult = strResult & "<paramdatatype>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramdatatype>"
    strResult = strResult & "</parameter>"
  Wend
  strResult = strResult & "</parameters>"
  
  m_blnInFunc = True
  m_blnInSub = False
  m_lngIndent = m_lngIndent + 1
  m_lngFuncs = m_lngFuncs + 1
  
  pvt_global_function = strResult
End Function
Private Function pvt_object_function(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strParameters As String
  Dim strResult As String
  Dim strReturnType As String
  Dim strTemp As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, "(")
  strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  
  strParameters = ""
  strTemp = StrTok(str, ")")
  While Len(strTemp) > 0
    strParameters = TokStr(strParameters, StrTok(strTemp, " "), Chr$(g_CHARDELIM))
    StrTok strTemp, " "
    strParameters = TokStr(strParameters, pvtDataTypeGet(StrTok(strTemp, ",")), Chr$(g_CHARDELIM))
    strTemp = Trim$(strTemp)
  Wend
  
  StrTok str, " as "
  strReturnType = pvtDataTypeGet(str)
  
  If Not pvtElementAdd(m_ETFUNC, lngScope_a, strFullIdentifier, strIdentifier, strParameters, strReturnType, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrFunc = ""
  Else
    m_strCurrFunc = strIdentifier
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "object_function: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strReturnType & "") & "</datatype>"
  strResult = strResult & "<parameters>"
  strTemp = strParameters
  While Len(strTemp) > 0
    strResult = strResult & "<parameter>"
    strResult = strResult & "<paramidentifier>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramidentifier>"
    strResult = strResult & "<paramdatatype>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramdatatype>"
    strResult = strResult & "</parameter>"
  Wend
  strResult = strResult & "</parameters>"
  
  m_blnInFunc = True
  m_blnInSub = False
  m_lngIndent = m_lngIndent + 1
  m_lngFuncs = m_lngFuncs + 1
  
  pvt_object_function = strResult
End Function
Private Function pvt_object_propertyget(lngScope_a As Long, str_a As String) As String
  pvt_object_propertyget = pvt_object_function(lngScope_a, str_a)
End Function
Private Function pvt_object_propertylet(lngScope_a As Long, str_a As String) As String
  pvt_object_propertylet = pvt_object_sub(lngScope_a, str_a)
End Function
Private Function pvt_object_propertyset(lngScope_a As Long, str_a As String) As String
  pvt_object_propertyset = pvt_object_sub(lngScope_a, str_a)
End Function
Private Function pvt_object_sub(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strParameters As String
  Dim strResult As String
  Dim strTemp As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, "(")
  strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  
  strParameters = ""
  strTemp = StrTok(str, ")")
  While Len(strTemp) > 0
    strParameters = TokStr(strParameters, StrTok(strTemp, " "), Chr$(g_CHARDELIM))
    StrTok strTemp, " "
    strParameters = TokStr(strParameters, pvtDataTypeGet(StrTok(strTemp, ",")), Chr$(g_CHARDELIM))
    strTemp = Trim$(strTemp)
  Wend
  
  If Not pvtElementAdd(m_ETSUB, lngScope_a, strFullIdentifier, strIdentifier, strParameters, m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrFunc = ""
  Else
    m_strCurrFunc = strIdentifier
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "object_sub: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier>"
  strResult = strResult & "<parameters>"
  strTemp = strParameters
  While Len(strTemp) > 0
    strResult = strResult & "<parameter>"
    strResult = strResult & "<paramidentifier>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramidentifier>"
    strResult = strResult & "<paramdatatype>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramdatatype>"
    strResult = strResult & "</parameter>"
  Wend
  strResult = strResult & "</parameters>"
  
  m_blnInFunc = False
  m_blnInSub = True
  m_lngIndent = m_lngIndent + 1
  m_lngSubs = m_lngSubs + 1
  
  pvt_object_sub = strResult
End Function
Private Function pvt_global_sub(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strParameters As String
  Dim strResult As String
  Dim strTemp As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, "(")
  If lngScope_a = m_SCOPEGLOBAL Then
    strFullIdentifier = strIdentifier
    m_blnPrivateFunc = False
  Else
    strFullIdentifier = m_strCurrObject & "_" & strIdentifier
    m_blnPrivateFunc = True
  End If
  
  strParameters = ""
  strTemp = StrTok(str, ")")
  While Len(strTemp) > 0
    strParameters = TokStr(strParameters, StrTok(strTemp, " "), Chr$(g_CHARDELIM))
    StrTok strTemp, " "
    strParameters = TokStr(strParameters, pvtDataTypeGet(StrTok(strTemp, ",")), Chr$(g_CHARDELIM))
    strTemp = Trim$(strTemp)
  Wend
  
  If Not pvtElementAdd(m_ETSUB, lngScope_a, strFullIdentifier, strIdentifier, strParameters, m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
    m_strCurrFunc = ""
  Else
    m_strCurrFunc = strIdentifier
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "global_sub: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier>"
  strResult = strResult & "<parameters>"
  strTemp = strParameters
  While Len(strTemp) > 0
    strResult = strResult & "<parameter>"
    strResult = strResult & "<paramidentifier>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramidentifier>"
    strResult = strResult & "<paramdatatype>" & XMLEnc(StrTok(strTemp, Chr$(g_CHARDELIM))) & "</paramdatatype>"
    strResult = strResult & "</parameter>"
  Wend
  strResult = strResult & "</parameters>"
  
  m_blnInFunc = False
  m_blnInSub = True
  m_lngIndent = m_lngIndent + 1
  m_lngSubs = m_lngSubs + 1
  
  pvt_global_sub = strResult
End Function
Private Function pvt_global_variable(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If lngScope_a = m_SCOPEGLOBAL Then
    strFullIdentifier = strIdentifier
  Else
    strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  End If
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  If Not pvtElementAdd(m_ETVARIABLE, lngScope_a, strFullIdentifier, strIdentifier, "", strDataType, "") Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "global_variable: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype>"
  
  m_lngVars = m_lngVars + 1
  
  pvt_global_variable = strResult
End Function
Private Function pvt_global_constant(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strFullIdentifier As String
  Dim strResult As String
  Dim strValue As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If lngScope_a = m_SCOPEGLOBAL Then
    strFullIdentifier = strIdentifier
  Else
    strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  End If
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  str = Trim$(str)
  If Left$(str, 1) = Chr$(g_CHARQUOTE) And Right$(str, 1) = Chr$(g_CHARQUOTE) Then
    strValue = Mid$(str, 2, Len(str) - 2)
    If strDataType = m_DTUNKNOWN Then
      strDataType = m_DTSTRING
    End If
  Else
    strValue = str
    If strDataType = m_DTUNKNOWN Then
      If InStr(str, ".") > 0 Then
        strDataType = m_DTSINGLE
      Else
        strDataType = m_DTINTEGER
      End If
    End If
  End If
  If Not pvtElementAdd(m_ETCONSTANT, lngScope_a, strFullIdentifier, strIdentifier, "", strDataType, strValue) Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "global_constant: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype><value>" & XMLEnc(strValue) & "</value>"
  
  m_lngConsts = m_lngConsts + 1
  
  pvt_global_constant = strResult
End Function
Private Function pvt_object_variable(lngScope_a As Long, str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  If Not pvtElementAdd(m_ETVARIABLE, lngScope_a, strFullIdentifier, strIdentifier, "", strDataType, "") Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "object_variable: <scope>" & XMLEnc(lngScope_a & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype>"
  
  m_lngVars = m_lngVars + 1
  
  pvt_object_variable = strResult
End Function
Private Function pvt_object_constant(str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strFullIdentifier As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strValue As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  strFullIdentifier = m_strCurrObject & "_" & strIdentifier
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  str = Trim$(str)
  If Left$(str, 1) = Chr$(g_CHARQUOTE) And Right$(str, 1) = Chr$(g_CHARQUOTE) Then
    strValue = Mid$(str, 2, Len(str) - 2)
    If strDataType = m_DTUNKNOWN Then
      strDataType = m_DTSTRING
    End If
  Else
    strValue = str
    If strDataType = m_DTUNKNOWN Then
      If InStr(str, ".") > 0 Then
        strDataType = m_DTSINGLE
      Else
        strDataType = m_DTINTEGER
      End If
    End If
  End If
  If Not pvtElementAdd(m_ETCONSTANT, m_SCOPEMODULE, strFullIdentifier, strIdentifier, "", strDataType, strValue) Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "object_constant: <scope>" & XMLEnc(m_SCOPEMODULE & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype><value>" & XMLEnc(strValue) & "</value>"
  
  m_lngConsts = m_lngConsts + 1
  
  pvt_object_constant = strResult
End Function
Private Function pvt_function_constant(str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strFullIdentifier As String
  Dim strResult As String
  Dim strValue As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If m_blnInObject Then
    strFullIdentifier = m_strCurrObject & "_" & m_strCurrFunc & "_" & strIdentifier
  Else
    If m_blnPrivateFunc Then
      strFullIdentifier = m_strCurrObject & "_" & m_strCurrFunc & "_" & strIdentifier
    Else
      strFullIdentifier = m_strCurrFunc & "_" & strIdentifier
    End If
  End If
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  str = Trim$(str)
  If Left$(str, 1) = Chr$(g_CHARQUOTE) And Right$(str, 1) = Chr$(g_CHARQUOTE) Then
    strValue = Mid$(str, 2, Len(str) - 2)
    If strDataType = m_DTUNKNOWN Then
      strDataType = m_DTSTRING
    End If
  Else
    strValue = str
    If strDataType = m_DTUNKNOWN Then
      If InStr(str, ".") > 0 Then
        strDataType = m_DTSINGLE
      Else
        strDataType = m_DTINTEGER
      End If
    End If
  End If
  If Not pvtElementAdd(m_ETCONSTANT, m_SCOPELOCAL, strFullIdentifier, strIdentifier, "", strDataType, strValue) Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "function_constant: <scope>" & XMLEnc(m_SCOPELOCAL & "") & "</scope><fullidentifider>" & XMLEnc(strFullIdentifier) & "</fullidentifider><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype><value>" & XMLEnc(strValue) & "</value>"
  
  m_lngConsts = m_lngConsts + 1
  
  pvt_function_constant = strResult
End Function

Private Function pvt_function_variable(str_a As String) As String
  Dim str As String
  Dim strDataType As String
  Dim strIdentifier As String
  Dim strFullIdentifier As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If m_blnInObject Then
    strFullIdentifier = m_strCurrObject & "_" & m_strCurrFunc & "_" & strIdentifier
  Else
    If m_blnPrivateFunc Then
      strFullIdentifier = m_strCurrObject & "_" & m_strCurrFunc & "_" & strIdentifier
    Else
      strFullIdentifier = m_strCurrFunc & "_" & strIdentifier
    End If
  End If
  
  'get the datatype & value
  strDataType = StrTok(str, "=")
  If Len(strDataType) > 0 Then
    StrTok strDataType, " "
    strDataType = pvtDataTypeGet(strDataType)
  Else
    strDataType = m_DTUNKNOWN
  End If
  
  If Not pvtElementAdd(m_ETVARIABLE, m_SCOPELOCAL, strFullIdentifier, strIdentifier, "", strDataType, "") Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "function_variable: <scope>" & XMLEnc(m_SCOPELOCAL & "") & "</scope><fullidentifier>" & XMLEnc(strFullIdentifier) & "</fullidentifier><identifier>" & XMLEnc(strIdentifier) & "</identifier><datatype>" & XMLEnc(strDataType & "") & "</datatype>"
  
  m_lngVars = m_lngVars + 1
  
  pvt_function_variable = strResult
End Function
Private Function pvt_object_endfunction(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  m_blnInSub = False
  m_strCurrFunc = ""
  
  strResult = pvtSpaces(m_lngIndent) & "object_endfunction:"
  
  pvt_object_endfunction = strResult
End Function
Private Function pvt_global_endobject(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInObject = False
  m_strCurrObject = ""
  
  strResult = pvtSpaces(m_lngIndent) & "global_endobject:"
  
  pvt_global_endobject = strResult
End Function
Private Function pvt_global_endfunction(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  m_blnInSub = False
  m_blnPrivateFunc = False
  m_strCurrFunc = ""
  
  strResult = pvtSpaces(m_lngIndent) & "global_endfunction:"
  
  pvt_global_endfunction = strResult
End Function
Private Function pvt_object_endsub(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  m_blnInSub = False
  m_strCurrFunc = ""
  
  strResult = pvtSpaces(m_lngIndent) & "object_endsub:"
  
  pvt_object_endsub = strResult
End Function
Private Function pvt_global_endsub(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInFunc = False
  m_blnInSub = False
  m_blnPrivateFunc = False
  m_strCurrFunc = ""
  
  strResult = pvtSpaces(m_lngIndent) & "global_endsub:"
  
  pvt_global_endsub = strResult
End Function
Private Function pvt_object_endproperty(str_a As String) As String
  Dim strResult As String
  
  If m_blnInFunc Then
    strResult = pvt_object_endfunction(str_a)
  ElseIf m_blnInSub Then
    strResult = pvt_object_endsub(str_a)
  Else
    pvtError "syntax error '" & str_a & "'"
  End If
  
  pvt_object_endproperty = strResult
End Function
Private Function pvt_object_exitproperty(str_a As String) As String
  Dim strResult As String
  
  If m_blnInFunc Then
    strResult = pvt_object_exitfunction(str_a)
  ElseIf m_blnInSub Then
    strResult = pvt_object_exitsub(str_a)
  Else
    pvtError "syntax error '" & str_a & "'"
  End If
  
  pvt_object_exitproperty = strResult
End Function
Private Function pvt_global_endtype(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  m_blnInUDT = False
  m_blnPrivateUDT = False
  m_strCurrUDT = ""
  
  strResult = pvtSpaces(m_lngIndent) & "global_endtype:"
  
  pvt_global_endtype = strResult
End Function
Private Function pvt_udt_typevariable(str_a As String) As String
  Dim lngScope As Long
  Dim str As String
  Dim strIdentifier As String
  Dim strFullIdentifier As String
  Dim strResult As String
  
  str = str_a
  strIdentifier = StrTok(str, " ")
  strIdentifier = StrTok(strIdentifier, "(")
  If m_blnPrivateUDT Then
    strFullIdentifier = m_strCurrObject & "_" & m_strCurrUDT & "_" & strIdentifier
    lngScope = m_SCOPEMODULE
  Else
    strFullIdentifier = m_strCurrUDT & "_" & strIdentifier
    lngScope = m_SCOPEGLOBAL
  End If
  If Not pvtElementAdd(m_ETUDTVARIABLE, lngScope, strFullIdentifier, strIdentifier, "", m_DTUNKNOWN, "") Then
    pvtError "invalid identifier '" & str_a & "'"
  End If
  
  strResult = pvtSpaces(m_lngIndent) & "udt_typevariable: <todo>" & XMLEnc(str_a) & "</todo>"
  
  pvt_udt_typevariable = strResult
End Function
Private Function pvt_object_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "object_exitfunction:"
  
  pvt_object_exitfunction = strResult
End Function
Private Function pvt_global_exitfunction(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "global_exitfunction:"
  
  pvt_global_exitfunction = strResult
End Function
Private Function pvt_object_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "object_exitsub:"
  
  pvt_object_exitsub = strResult
End Function
Private Function pvt_global_exitsub(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "global_exitsub:"
  
  pvt_global_exitsub = strResult
End Function
Private Function pvtStatementTok(str_a As String, strDelimeter_a As String) As String
  Dim blnFound As Boolean
  Dim blnInSpace As Boolean
  Dim blnInQuote As Boolean
  Dim lngI As Long
  Dim lngNest As Long
  Dim strChar As String
  Dim strNestCharList As String
  Dim strNestchar As String
  Dim strResult As String
  Dim strTest As String
  
  blnFound = False
  blnInSpace = False
  blnInQuote = False
  lngNest = 0
  lngI = 1
  While lngI <= Len(str_a) And Not blnFound
    strChar = Mid$(str_a, lngI, 1)
    
    If lngNest = 0 Then 'not in nest
      If strChar = Chr$(g_CHARQUOTE) Then
        lngNest = lngNest + 1
      Else
        strTest = strTest & LCase$(strChar)
      End If
    Else 'in nest already
      If strChar = Chr$(g_CHARQUOTE) Then
        lngNest = lngNest - 1
      End If
    End If
    
    If lngNest > 0 Then
      If Not blnInQuote Then
        strResult = strResult & strChar
      End If
    Else
      If strChar = " " Then
        If blnInSpace Then
          strChar = ""
        Else
          blnInSpace = True
        End If
      Else
        blnInSpace = False
      End If
      
      If strChar = "'" Then
        blnInQuote = True
      End If
      
      If Not blnInQuote Then
        strResult = strResult & LCase$(strChar)
      End If
    End If
      
    
    If Right$(strTest, Len(strDelimeter_a)) = LCase$(strDelimeter_a) Then
      blnFound = True
    Else
      lngI = lngI + 1
    End If
  Wend
  
  If blnFound Then
    If lngI - Len(strDelimeter_a) > 0 Then
      'strResult = Left$(str_a, lngI - Len(strDelimeter_a))
      strResult = Left$(strResult, Len(strResult) - Len(strDelimeter_a))
    End If
    str_a = Right$(str_a, Len(str_a) - lngI)
  Else
    str_a = ""
  End If
  
  pvtStatementTok = Trim$(strResult)
End Function

Private Function pvtParseStatement(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""
  
  Select Case True
    Case Left$(str, 1) = "'" 'ignore comments
    
    Case m_blnInFunc Or m_blnInSub
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
  
  strResult = pvtSpaces(m_lngIndent) & "function_expression: <expression>" & XMLEnc(str_a) & "</expression>"
  
  m_lngExprns = m_lngExprns + 1
  
  pvt_function_expression = strResult
End Function
Private Function pvt_function_selectcase(str_a As String) As String
  Dim str As String
  Dim strExpression As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  StrTok str, " "
  strExpression = str
  
  strResult = pvtSpaces(m_lngIndent) & "function_selectcase: <expression>" & XMLEnc(strExpression) & "</expression>"
  
  m_lngIndent = m_lngIndent + 1
  m_lngSelectCases = m_lngSelectCases + 1
  
  pvt_function_selectcase = strResult
End Function
Private Function pvt_function_caseelse(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "function_caseelse:"
  
  pvt_function_caseelse = strResult
End Function
Private Function pvt_function_case(str_a As String) As String
  Dim str As String
  Dim strExpression As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strExpression = str
  
  strResult = pvtSpaces(m_lngIndent) & "function_case: <expression>" & XMLEnc(strExpression) & "</expression>"
  
  pvt_function_case = strResult
End Function
Private Function pvt_function_endselect(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "function_endselect:"
  
  pvt_function_endselect = strResult
End Function
Private Function pvt_function_while(str_a As String) As String
  Dim str As String
  Dim strExpression As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strExpression = str
  
  strResult = pvtSpaces(m_lngIndent) & "function_while: <expression>" & XMLEnc(strExpression) & "</expression>"
  
  m_lngIndent = m_lngIndent + 1
  m_lngWhiles = m_lngWhiles + 1
  
  pvt_function_while = strResult
End Function
Private Function pvt_function_wend(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "function_wend:"
  
  pvt_function_wend = strResult
End Function
Private Function pvt_function_for(str_a As String) As String
  Dim str As String
  Dim strFromExpression As String
  Dim strIdentifier As String
  Dim strResult As String
  Dim strStep As String
  Dim strToExpression As String
  
  str = str_a
  StrTok str, " "
  strIdentifier = Trim$(StrTok(str, "="))
  str = Trim$(str)
  strIdentifier = StrTok(strIdentifier, "(")
  strFromExpression = pvtStatementTok(str, " to ")
  strToExpression = pvtStatementTok(str, " step ")
  strStep = str
  
  strResult = pvtSpaces(m_lngIndent) & "function_for: <fromexpression>" & XMLEnc(strFromExpression) & "</fromexpression><toexpression>" & XMLEnc(strToExpression) & "</toexpression><step>" & XMLEnc(strStep) & "</step>"
  
  m_lngIndent = m_lngIndent + 1
  m_lngFors = m_lngFors + 1
  
  pvt_function_for = strResult
End Function
Private Function pvt_function_next(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "function_next:"
  
  pvt_function_next = strResult
End Function
Private Function pvt_function_ifthen(str_a As String) As String
  Dim str As String
  Dim strExpression As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strExpression = Left$(str, Len(str) - 5)
  
  strResult = pvtSpaces(m_lngIndent) & "function_ifthen: <expression>" & XMLEnc(strExpression) & "</expression>"
  
  m_lngIndent = m_lngIndent + 1
  m_lngIfThens = m_lngIfThens + 1
  
  pvt_function_ifthen = strResult
End Function
Private Function pvt_function_elseifthen(str_a As String) As String
  Dim str As String
  Dim strExpression As String
  Dim strResult As String
  
  str = str_a
  StrTok str, " "
  strExpression = Left$(str, Len(str) - 5)
  
  strResult = pvtSpaces(m_lngIndent) & "function_elseifthen: <expression>" & XMLEnc(strExpression) & "</expression>"
  
  pvt_function_elseifthen = strResult
End Function
Private Function pvt_function_else(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "function_else:"
  
  pvt_function_else = strResult
End Function
Private Function pvt_function_endif(str_a As String) As String
  Dim strResult As String
  
  m_lngIndent = m_lngIndent - 1
  
  strResult = pvtSpaces(m_lngIndent) & "function_endif:"
  
  pvt_function_endif = strResult
End Function
Private Function pvt_condendif(str_a As String) As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "condendif: " & str_a
  
  m_blnSkip = False
  
  pvt_condendif = ""
End Function
Private Function pvt_condif(str_a As String) As String
  Dim lngCompValue As Long
  Dim lngValue As Long
  Dim str As String
  Dim strIdentifier As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "condif: " & str_a
  m_lngCondifs = m_lngCondifs + 1
  
  str = str_a
  StrTok str, " "
  strIdentifier = Trim$(StrTok(str, " "))
  If InStr(str, "=") > 0 Then
    StrTok str, "="
    lngCompValue = Val(StrTok(str, " then"))
  Else
    lngCompValue = 1
  End If
  If Not pvtDefineGet(strIdentifier, lngValue) Then
    pvtError "invalid identifier '" & str_a & "'"
  Else
    If lngValue <> lngCompValue Then
      m_blnSkip = True
    Else
      m_blnSkip = False
    End If
  End If
  
  pvt_condif = ""
End Function
Private Function pvt_conddefine(str_a As String) As String
  Dim lngValue As Long
  Dim str As String
  Dim strIdentifier As String
  Dim strResult As String
  
  strResult = pvtSpaces(m_lngIndent) & "conddefine: " & str_a
  m_lngCondifs = m_lngCondifs + 1
  
  str = str_a
  StrTok str, " "
  strIdentifier = Trim$(StrTok(str, "="))
  lngValue = Val(str)
  If Not pvtDefineAdd(strIdentifier, lngValue) Then
    pvtError "invalid identifier '" & str_a & "'"
  End If

  pvt_conddefine = ""
End Function
'note: forced errors at the top so that identifier declarations are not confused
Private Function pvtParseObject(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""
  
  If Not m_blnSkip Then
    Select Case True
      Case Left$(str, 25) = "private declare function " 'declare private declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 24) = "public declare function " 'declare public declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 20) = "private declare sub " 'declare private declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 19) = "public declare sub " 'declare public declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 13) = "private enum " 'declare private enum (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 12) = "public enum " 'declare public enum (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 14) = "private event " 'declare private event (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 13) = "public event " 'declare public event (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 13) = "private type " 'declare private udt (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 12) = "public type " 'declare public udt (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 19) = "private withevents " 'declare private event (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 18) = "public withevents " 'declare public event (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case str = "end object"
        strResult = pvt_global_endobject(str)
      
      Case Left$(str, 17) = "private function " 'declare private function
        strResult = pvt_object_function(m_SCOPEMODULE, str)
      
      Case Left$(str, 16) = "public function " 'declare public function
        strResult = pvt_object_function(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 9) = "function " 'declare public function
        strResult = pvt_object_function(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 16) = "friend function " 'declare friend function (same as public for now)
        strResult = pvt_object_function(m_SCOPEGLOBAL, str)
     
      Case Left$(str, 12) = "private sub " 'declare private sub
        strResult = pvt_object_sub(m_SCOPEMODULE, str)
      
      Case Left$(str, 11) = "public sub " 'declare public sub
        strResult = pvt_object_sub(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 4) = "sub " 'declare public sub
        strResult = pvt_object_sub(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 11) = "friend sub " 'declare friend sub (same as public for now)
        strResult = pvt_object_sub(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 21) = "private property get " 'private property get
        strResult = pvt_object_propertyget(m_SCOPEMODULE, str)
      
      Case Left$(str, 20) = "public property get " 'public property get
        strResult = pvt_object_propertyget(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 20) = "friend property get " 'friend property get (same as public for now)
        strResult = pvt_object_propertyget(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 13) = "property get " 'public property get
        strResult = pvt_object_propertyget(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 21) = "private property let " 'private property let
        strResult = pvt_object_propertylet(m_SCOPEMODULE, str)
      
      Case Left$(str, 20) = "public property let " 'public property let
        strResult = pvt_object_propertylet(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 20) = "friend property let " 'friend property let (same as public for now)
        strResult = pvt_object_propertylet(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 13) = "property let " 'public property let
        strResult = pvt_object_propertylet(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 21) = "private property set " 'private property set
        strResult = pvt_object_propertyset(m_SCOPEMODULE, str)
      
      Case Left$(str, 20) = "public property set " 'public property set
        strResult = pvt_object_propertyset(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 20) = "friend property set " 'friend property set (same as public for now)
        strResult = pvt_object_propertyset(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 13) = "property set " 'public property set
        strResult = pvt_object_propertyset(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 14) = "private const " 'declare private constant
        strResult = pvt_object_constant(str)
      
      Case Left$(str, 6) = "const " 'declare private constant
        strResult = pvt_object_constant("private " & str)
      
      Case Left$(str, 8) = "private " 'declare private property
        strResult = pvt_object_variable(m_SCOPEMODULE, str)
      
      Case Left$(str, 7) = "public " 'declare public property
        strResult = pvt_object_variable(m_SCOPEGLOBAL, str)
         
      Case Left$(str, 4) = "dim " 'declare private property
        strResult = pvt_object_variable(m_SCOPEMODULE, str)
        
      Case Left$(str, 8) = "#define " 'conditional define
        strResult = pvt_conddefine(str)
     
      Case Left$(str, 4) = "#if " 'conditional if
        strResult = pvt_condif(str)
     
      Case str = "#end if" 'conditional end if
        strResult = pvt_condendif(str)
      
      Case Left$(str, 10) = "attribute " 'ignore
      
      Case Left$(str, 11) = "implements " 'ignore
      
      Case Else
        pvtError "syntax error '" & str_a & "'"
      
    End Select
  Else
    If str = "#end if" Then
      strResult = pvt_condendif(str) 'conditional end if
    End If
  End If
  
  pvtParseObject = strResult
End Function
'note: forced errors at the top so that identifier declarations are not confused
Private Function pvtParseApp(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""
  
  If Not m_blnSkip Then
    Select Case True
      Case Left$(str, 25) = "private declare function " 'declare private declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 24) = "public declare function " 'declare public declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 17) = "declare function " 'declare public declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 20) = "private declare sub " 'declare private declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 19) = "public declare sub " 'declare public declaration (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 13) = "private enum " 'declare private enum (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 12) = "public enum " 'declare public enum (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 7) = "object " 'declare object
        strResult = pvt_global_object(str)
      
      Case Left$(str, 7) = "module " 'declare module
        strResult = pvt_global_module(str)
      
      Case str = "end module" 'end module
        strResult = pvt_global_endmodule(str)
      
      Case Left$(str, 13) = "private type " 'declare private udt
        strResult = pvt_global_type(m_SCOPEMODULE, str)
      
      Case Left$(str, 12) = "public type " 'declare public udt
        strResult = pvt_global_type(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 5) = "type " 'declare public udt
        strResult = pvt_global_type(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 17) = "private function " 'declare private function
        strResult = pvt_global_function(m_SCOPEMODULE, str)
      
      Case Left$(str, 16) = "public function " 'declare public function
        strResult = pvt_global_function(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 9) = "function " 'declare public function
        strResult = pvt_global_function(m_SCOPEGLOBAL, "public " & str)
      
      Case Left$(str, 16) = "friend function " 'declare friend function (same as public for now)
        strResult = pvt_global_function(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 12) = "private sub " 'declare private sub
        strResult = pvt_global_sub(m_SCOPEMODULE, str)
      
      Case Left$(str, 11) = "public sub " 'declare public sub
        strResult = pvt_global_sub(m_SCOPEGLOBAL, str)
        
      Case Left$(str, 4) = "sub " 'declare public sub
        strResult = pvt_global_sub(m_SCOPEGLOBAL, "public " & str)
        
      Case Left$(str, 11) = "friend sub " 'declare friend sub (same as public for now)
        strResult = pvt_global_sub(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 14) = "private const " 'declare private constant
        strResult = pvt_global_constant(m_SCOPEMODULE, str)
      
      Case Left$(str, 13) = "public const " 'declare public constant
        strResult = pvt_global_constant(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 6) = "const " 'declare private constant
        strResult = pvt_global_constant(m_SCOPEMODULE, "private " & str)
      
      Case Left$(str, 8) = "private " 'declare private variable
        strResult = pvt_global_variable(m_SCOPEMODULE, str)
      
      Case Left$(str, 7) = "public " 'declare public variable
        strResult = pvt_global_variable(m_SCOPEGLOBAL, str)
      
      Case Left$(str, 4) = "dim " 'declare private variable
        strResult = pvt_global_variable(m_SCOPEMODULE, str)
      
      Case Left$(str, 8) = "#define " 'conditional define
        strResult = pvt_conddefine(str)
     
      Case Left$(str, 4) = "#if " 'conditional if
        strResult = pvt_condif(str)
      
      Case str = "#end if" 'conditional end if
        strResult = pvt_condendif(str)
      
      Case Left$(str, 10) = "attribute " 'ignore
      
      Case Else
        pvtError "syntax error '" & str_a & "'"
      
    End Select
  Else
    If str = "#end if" Then
      strResult = pvt_condendif(str) 'conditional end if
    End If
  End If
  
  pvtParseApp = strResult
End Function
'note: forced errors at the top so that identifier declarations are not confused
Private Function pvtParseFunction(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""
  
  If Not m_blnSkip Then
    Select Case True
      Case Left$(str, 6) = "raise " 'raise (syntax error)
        pvtError "syntax error '" & str_a & "'"
        
      Case Left$(str, 6) = "redim " 'declare private variable (syntax error)
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 6) = "const " 'declare private constant
        strResult = pvt_function_constant(str)
      
      Case str = "end property" 'end property
        strResult = pvt_object_endproperty(str)
        
      Case str = "exit property" 'exit property
        strResult = pvt_object_exitproperty(str)
      
      Case str = "end function" 'end function
        If m_blnInObject Then
          strResult = pvt_object_endfunction(str)
        Else
          strResult = pvt_global_endfunction(str)
        End If
        
      Case str = "exit function" 'exit function
        If m_blnInObject Then
          strResult = pvt_object_exitfunction(str)
        Else
          strResult = pvt_global_exitfunction(str)
        End If
      
      Case str = "end sub" 'end sub
        If m_blnInObject Then
          strResult = pvt_object_endsub(str)
        Else
          strResult = pvt_global_endsub(str)
        End If
      
      Case str = "exit sub" 'exit sub
        If m_blnInObject Then
          strResult = pvt_object_exitsub(str)
        Else
          strResult = pvt_global_exitsub(str)
        End If
      
      Case Left$(str, 4) = "dim " 'declare private variable
        strResult = pvt_function_variable(str)
      
      Case Left$(str, 12) = "select case " 'select case
        strResult = pvt_function_selectcase(str)
      
      Case str = "case else" 'case else
        strResult = pvt_function_caseelse(str)
      
      Case Left$(str, 5) = "case " 'case
        strResult = pvt_function_case(str)
      
      Case str = "end select" 'end select
        strResult = pvt_function_endselect(str)
      
      Case Left$(str, 6) = "while " 'while
        strResult = pvt_function_while(str)
      
      Case str = "wend" 'wend
        strResult = pvt_function_wend(str)
      
      Case Left$(str, 4) = "for " 'for
        strResult = pvt_function_for(str)
      
      Case str = "next" 'next
        strResult = pvt_function_next(str)
      
      Case Left$(str, 5) = "next " 'next
        strResult = pvt_function_next(str)
      
      Case Left$(str, 3) = "if " And Right$(str, 5) = " then" 'if then
        strResult = pvt_function_ifthen(str)
      
      Case Left$(str, 3) = "if " 'if then [else] 'note: must come after the if then check above
        pvtError "syntax error '" & str_a & "'"
      
      Case Left$(str, 7) = "elseif " And Right$(str, 5) = " then" 'elseif
        strResult = pvt_function_elseifthen(str)
      
      Case str = "else" 'else
        strResult = pvt_function_else(str)
      
      Case str = "end if" 'endif
        strResult = pvt_function_endif(str)
      
      Case Left$(str, 8) = "#define " 'conditional define
        strResult = pvt_conddefine(str)
     
      Case Left$(str, 4) = "#if " 'conditional if
        strResult = pvt_condif(str)
      
      Case str = "#end if" 'conditional end if
        strResult = pvt_condendif(str)
      
      Case Else 'logic (ie: function calls & assignments)
        strResult = pvt_function_expression(str)
      
    End Select
  Else
    If str = "#end if" Then
      strResult = pvt_condendif(str) 'conditional end if
    End If
  End If
  
  pvtParseFunction = strResult
End Function
Private Function pvtParseUDT(str_a As String) As String
  Dim str As String
  Dim strResult As String
  
  str = str_a
  strResult = ""
  
  If Not m_blnSkip Then
    Select Case True
      Case str = "end type" 'end udt
        strResult = pvt_global_endtype(str)
      
      Case Left$(str, 8) = "#define " 'conditional define
        strResult = pvt_conddefine(str)
     
      Case Left$(str, 4) = "#if " 'conditional if
        strResult = pvt_condif(str)
     
      Case Else
        strResult = pvt_udt_typevariable(str)
      
    End Select
  Else
    If str = "#end if" Then
      strResult = pvt_condendif(str) 'conditional end if
    End If
  End If
  
  pvtParseUDT = strResult
End Function
Private Function pvtElementAdd(lngElementType_a As Long, lngScope_a As Long, strFullIdentifier_a As String, strIdentifier_a As String, strParams_a As String, strDataType_a As String, strValue_a As String) As Boolean
  Dim objElement As cElement
  
  Set objElement = New cElement
  
  objElement.ElementType = lngElementType_a
  objElement.Scope = lngScope_a
  objElement.FullIdentifier = strFullIdentifier_a
  objElement.Identifier = strIdentifier_a
  
  objElement.Parameters = strParams_a
  objElement.DataType = strDataType_a
  objElement.Value = strValue_a
  pvtElementAdd = m_objElements.Append(strFullIdentifier_a, objElement)

  Set objElement = Nothing
End Function
Private Function pvtDefineAdd(strIdentifier_a As String, lngValue_a As Long) As Boolean
  Dim objDefine As cElement
  
  Set objDefine = New cElement
  
  objDefine.ElementType = 0
  objDefine.Scope = 0
  objDefine.FullIdentifier = strIdentifier_a
  objDefine.Identifier = strIdentifier_a
  objDefine.Parameters = ""
  objDefine.DataType = ""
  objDefine.Value = lngValue_a & ""
  pvtDefineAdd = m_objDefines.Append(strIdentifier_a, objDefine)

  Set objDefine = Nothing
End Function
Private Function pvtDefineGet(strIdentifier_a As String, lngValue_a As Long) As Boolean
  Dim blnResult As Boolean
  Dim objDefine As cElement
  
  Set objDefine = New cElement
  
  If m_objDefines.Exists(strIdentifier_a) Then
    Set objDefine = m_objDefines.Read(strIdentifier_a)
    lngValue_a = Val(objDefine.Value)
    blnResult = True
  Else
    blnResult = False
  End If
  
  Set objDefine = Nothing
  
  pvtDefineGet = blnResult
End Function
Private Function pvtSpaces(lngIndent_a As Long) As String
  pvtSpaces = Space$(lngIndent_a * 2)
End Function
Public Sub StatisticsSave()
  Dim objFile As cFile
  
  Set objFile = New cFile
  
  Debug.Print "writing statistics."
  If objFile.fOpen(App.Path & "\" & m_strProjectName & "\stats.log", g_FMAPPEND) Then
    
    objFile.fWrite vbCrLf & "PARSER STATS:" & vbCrLf & vbCrLf
    
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
    objFile.fWrite "directive count   " & m_lngCondifs & vbCrLf
    
    objFile.fWrite "expression count  " & m_lngExprns & vbCrLf
    objFile.fWrite "for loop count    " & m_lngFors & vbCrLf
    objFile.fWrite "select case count " & m_lngSelectCases & vbCrLf
    objFile.fWrite "while loop count  " & m_lngWhiles & vbCrLf
    
    objFile.fClose
  End If

  Set objFile = Nothing
End Sub
Public Sub ElementsSave()
  Dim objFile As cFile
  Dim objElement As cElement
  Dim objNode As cNode
  Dim strOutput As String
  
  Set objFile = New cFile
  
  Debug.Print "writing elements."
  If m_objElements.List.Size > 0 Then
  
    If objFile.fOpen(App.Path & "\" & m_strProjectName & "\elements.xml", g_FMAPPEND) Then
      objFile.fWrite "<elements>" & vbCrLf
      m_objElements.List.FirstNode
      Set objNode = m_objElements.List.Read
      While Not objNode Is Nothing
        Set objElement = objNode.Item
        
        strOutput = "<element>"
        strOutput = strOutput & "<fullidentifier>" & XMLEnc(objElement.FullIdentifier) & "</fullidentifier>"
        strOutput = strOutput & "<identifier>" & XMLEnc(objElement.Identifier) & "</identifier>"
        strOutput = strOutput & "<scope>" & XMLEnc(objElement.Scope & "") & "</scope>"
        strOutput = strOutput & "<type>" & XMLEnc(objElement.ElementType & "") & "</type>"
        strOutput = strOutput & "<parameters>" & XMLEnc(objElement.Parameters & "") & "</parameters>"
        strOutput = strOutput & "<datatype>" & XMLEnc(objElement.DataType & "") & "</datatype>"
        strOutput = strOutput & "</element>"
        objFile.fWrite strOutput & vbCrLf
        
        m_objElements.List.NextNode
        Set objNode = m_objElements.List.Read
      Wend
      Set objNode = Nothing
      objFile.fWrite "</elements>" & vbCrLf
      objFile.fClose
    End If
  End If

  Set objFile = Nothing
End Sub

Private Sub Class_Initialize()
  Initialise
End Sub
Private Sub Class_Terminate()
  Set m_objElements = Nothing
End Sub
