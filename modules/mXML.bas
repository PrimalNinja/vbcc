Attribute VB_Name = "mXML"
Option Explicit

Private Const m_NAME = "mXML"
Public Function XMLDec(str_a As String) As String
  Dim strResult As String
  
  strResult = str_a
  strResult = Replace(strResult, "&lt;", "<")
  strResult = Replace(strResult, "&gt;", ">")
  strResult = Replace(strResult, "&amp;", "&")
  strResult = Replace(strResult, "&apos;", "'")
  strResult = Replace(strResult, "&quot;", Chr$(34))
  
  XMLDec = strResult
End Function
Public Function XMLEnc(str_a As String) As String
  Dim strResult As String
  
  strResult = str_a
  strResult = Replace(strResult, "&", "&amp;")
  strResult = Replace(strResult, "<", "&lt;")
  strResult = Replace(strResult, ">", "&gt;")
  strResult = Replace(strResult, "'", "&apos;")
  strResult = Replace(strResult, Chr$(34), "&quot;")
  
  XMLEnc = strResult
End Function
Public Function XMLTokAppend(str_a As String, strSource_a As String, strToken_a As String) As String
  Dim strValue As String
  Dim strResult As String
  
  strResult = str_a
  strValue = Trim$(XMLTokGet(strSource_a, strToken_a))
  If Len(strValue) > 0 Then
    strResult = strResult & "<" & strToken_a & ">" & XMLEnc(strValue) & "</" & strToken_a & ">"
  End If
  
  XMLTokAppend = strResult
End Function
Public Function XMLTok(str_a As String, strToken_a As String) As String
  Dim blnFound As Boolean
  Dim blnRemove As Boolean
  Dim lngPos As Long
  Dim lngPosN As Long
  Dim strResult As String
  Dim strToken As String
  Dim strTokenN As String
  Dim strTokenE As String
  
  blnFound = False
  blnRemove = False
  
  strResult = ""
  strToken = "<" & strToken_a & ">"
  strTokenN = "<" & strToken_a & " />"
  strTokenE = "</" & strToken_a & ">"
  
  lngPos = InStr(str_a, strToken)
  lngPosN = InStr(str_a, strTokenN)
  
  If lngPos > 0 And lngPosN = 0 Then
    blnFound = True
  ElseIf lngPos > 0 And lngPosN > 0 And lngPos < lngPosN Then
    blnFound = True
  ElseIf lngPos = 0 And lngPosN > 0 Then
    blnRemove = True
  ElseIf lngPos > 0 And lngPosN > 0 And lngPos > lngPosN Then
    blnRemove = True
  End If
  
  If blnFound Then
    StrTok str_a, strToken
    strResult = XMLDec(Trim$(StrTok(str_a, strTokenE)))
  End If
  
  If blnRemove Then
    StrTok str_a, strTokenN
  End If
  
  If Not blnFound And Not blnRemove Then
    str_a = "" 'catch incomplete & invalid tag constructs
  End If
  
  strResult = Replace(strResult, vbCr, "") 'remove cr
  strResult = Replace(strResult, vbLf, "") 'remove lf
  strResult = Replace(strResult, Chr$(9), "") 'remove tabs
  strResult = Trim$(strResult)
  
  XMLTok = strResult
End Function
Public Function XMLTokGet(str_a As String, strToken_a As String) As String
  Dim str As String
  Dim strToken As String
  
  str = str_a
  strToken = strToken_a
  
  XMLTokGet = XMLTok(str, strToken)
End Function
