Attribute VB_Name = "mFastString"
Option Explicit

Private Const m_NAME = "mFastString"

Public Function Replace(strOldString_a As String, strOldValue_a As String, strNewValue_a As String) As String
  Dim intPos As Integer
  Dim strResult As String
  Dim strTemp As String
  
  strTemp = strOldString_a
  strResult = ""
  While (Len(strTemp) > 0)
    intPos = InStr(strTemp, strOldValue_a)
    If (intPos > 0) Then
      strResult = strResult + Left(strTemp, intPos - 1) + strNewValue_a
      strTemp = Right$(strTemp, Len(strTemp) - intPos - Len(strOldValue_a) + 1)
    Else
      strResult = strResult + strTemp
      strTemp = ""
    End If
  Wend

  Replace = strResult
End Function
Public Function StrReverse(str_a As String) As String
  Dim lngI As Long
  Dim strResult As String
  
  strResult = ""
  For lngI = 1 To Len(str_a)
    strResult = Mid$(str_a, lngI, 1) & strResult
  Next
  
  StrReverse = strResult
End Function


Public Function StrTokCount(strList_a As String, strDelimeter_a As String) As Long
  Dim objList As cFastString
  
  Set objList = New cFastString
  
  objList.fWrite strList_a
  objList.Delimeter strDelimeter_a
  
  StrTokCount = objList.TokenCount

  Set objList = Nothing
End Function
Public Function TokStr(strList_a As String, strValue_a As String, strDelimeter_a As String) As String
  Dim strResult As String
  
  If Len(strList_a) = 0 Then
    strResult = strValue_a
  Else
    strResult = strList_a & strDelimeter_a & strValue_a
  End If
  
  TokStr = strResult
End Function
Public Function StrTok(strList_a As String, strDelimeter_a As String) As String
  Dim lngPos As Long
  Dim strResult As String
  
  lngPos = InStr(strList_a, strDelimeter_a)
  If lngPos > 0 Then
    strResult = Left$(strList_a, lngPos - 1)
    strList_a = Right$(strList_a, Len(strList_a) - lngPos - Len(strDelimeter_a) + 1)
  Else
    strResult = strList_a
    strList_a = ""
  End If
  
  StrTok = strResult
End Function
Public Function StrRTrunc(strValue_a As String, strTruncateValue_a As String) As String
  Dim strResult As String
  
  strResult = strValue_a
  If Len(strTruncateValue_a) < Len(strValue_a) Then
    If Right$(strValue_a, Len(strTruncateValue_a)) = strTruncateValue_a Then
      strResult = Left$(strValue_a, Len(strValue_a) - Len(strTruncateValue_a))
    End If
  End If
  
  StrRTrunc = strResult
End Function
Public Function StrPad(strValue_a As String, lngLength_a As Long) As String
  Dim strResult As String
  
  If Len(strValue_a) > lngLength_a Then
    strResult = Left$(strValue_a, lngLength_a)
  Else
    strResult = strValue_a & Space$(lngLength_a - Len(strValue_a))
  End If
  
  StrPad = strResult
End Function

