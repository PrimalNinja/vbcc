VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cFile"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit
 
Private Const m_NAME = "cFile"

Private m_blnOpened As Boolean
Private m_intFile As Integer
Private m_lngMode As Long 'file mode

Private m_lngToRead As Long
Private m_objLines As cFastString 'contains multiple lines when a unix file is read

Private Const m_MAXPAGESZ = 10240
Public Sub fClose()
  If m_blnOpened Then
    m_objLines.Clear
    Close #m_intFile
    
    m_blnOpened = False
  End If
End Sub
Public Function fGet(lngRecordID_a As Long, lngRecordLength_a As Long) As String
  Dim sResult As String
  
  If m_lngMode = g_FMBINARY Then
    sResult = Space$(lngRecordLength_a)
    Get #m_intFile, (lngRecordID_a - 1) * lngRecordLength_a + 1, sResult
  End If
  
  fGet = sResult
End Function
Public Sub fPut(lngRecordID_a As Long, strRecord_a As String)
  If m_lngMode = g_FMBINARY Then
    Put #m_intFile, (lngRecordID_a - 1) * Len(strRecord_a) + 1, strRecord_a
  End If
End Sub
Public Function fOpen(strPath_a As String, lngMode_a As Long) As Boolean
  fClose
  
  m_lngMode = lngMode_a
  
  Select Case m_lngMode
    Case g_FMAPPEND
      m_intFile = FreeFile
      Open strPath_a For Append As #m_intFile
      m_blnOpened = True
      
    Case g_FMREAD
      m_objLines.Delimeter vbLf 'set the line deliminter for reading the file
      m_lngToRead = FileLen(strPath_a) 'set the number of bytes to read to completely read the file
      
      m_intFile = FreeFile
      Open strPath_a For Binary As #m_intFile
      m_blnOpened = True
      
    Case g_FMBINARY
      m_intFile = FreeFile
      Open strPath_a For Binary As #m_intFile
      m_blnOpened = True
  End Select
  
  fOpen = m_blnOpened
End Function
Public Sub fWrite(strValue_a As String)
  Print #m_intFile, strValue_a;
End Sub
Public Property Get EndOfFile() As Boolean
  Dim blnResult As Boolean
  
  blnResult = True
  'if we have some of the file in our buffer then
  If m_objLines.Size > 0 Then
    'we are not at the end yet
    blnResult = False
  Else
    'we are at the end if we have no more to read
    blnResult = (m_lngToRead = 0)
  End If
  
  EndOfFile = blnResult
End Property
Public Function fLoad() As String
  Dim blnRead As Boolean
  Dim lngChunkSize As Long
  Dim strResult As String
  Dim strTemp As String
  
  blnRead = False
  
  If m_objLines.Size > 0 Then
    'if we have either a whole line in memory or the last line in memory
    If InStr(m_objLines.fRead, vbLf) > 0 Or m_lngToRead = 0 Then
      'read it
      strResult = m_objLines.fRead
    Else
      blnRead = True
    End If
  Else
    blnRead = True
  End If
    
  If blnRead Then
    'load the entire file
    lngChunkSize = m_lngToRead
      
    If lngChunkSize > 0 Then
      'read the chunk
      strTemp = String$(lngChunkSize, Chr$(g_CHARNULL))
      Get #m_intFile, , strTemp
    
      'subtract the read amount from the amount still to read
      m_lngToRead = m_lngToRead - lngChunkSize
      
      'turn the DOS file into a unix file (ie: remove cr/lf and put lf)
      strTemp = Replace(strTemp, vbCrLf, vbLf)
      If Right$(strTemp, 1) = Chr$(g_CHARCR) Then
        strTemp = StrRTrunc(strTemp, Chr$(g_CHARCR))
      End If
      
      'append the read data onto the buffer
      m_objLines.fWrite m_objLines.fRead & strTemp
      strResult = fLoad() 'we now have data in our buffer to satisfy reading, so read it
    Else
      strResult = ""
    End If
  End If
  
  fLoad = strResult
End Function
Public Function fRead() As String
  Dim blnRead As Boolean
  Dim lngChunkSize As Long
  Dim strResult As String
  Dim strTemp As String
  
  blnRead = False
  
  If m_objLines.Size > 0 Then
    'if we have either a whole line in memory or the last line in memory
    If InStr(m_objLines.fRead, vbLf) > 0 Or m_lngToRead = 0 Then
      'read it
      strResult = m_objLines.Token
    Else
      blnRead = True
    End If
  Else
    blnRead = True
  End If
    
  If blnRead Then
    'we have to get the next page from disc
    If m_MAXPAGESZ > m_lngToRead Then
      lngChunkSize = m_lngToRead
    Else
      lngChunkSize = m_MAXPAGESZ
    End If
      
    If lngChunkSize > 0 Then
      'read the chunk
      strTemp = String$(lngChunkSize, Chr$(g_CHARNULL))
      Get #m_intFile, , strTemp
    
      'subtract the read amount from the amount still to read
      m_lngToRead = m_lngToRead - lngChunkSize
      
      'turn the DOS file into a unix file (ie: remove cr/lf and put lf)
      strTemp = Replace(strTemp, vbCrLf, vbLf)
      If Right$(strTemp, 1) = Chr$(g_CHARCR) Then
        strTemp = StrRTrunc(strTemp, Chr$(g_CHARCR))
      End If
      
      'append the read data onto the buffer
      m_objLines.fWrite m_objLines.fRead & strTemp
      strResult = fRead() 'we now have data in our buffer to satisfy reading, so read it
    Else
      strResult = ""
    End If
  End If
  
  fRead = strResult
End Function
Private Sub Class_Initialize()
  Set m_objLines = New cFastString
  
  m_blnOpened = False
End Sub
Private Sub Class_Terminate()
  If m_blnOpened Then
    fClose
  End If
  
  Set m_objLines = Nothing
End Sub

