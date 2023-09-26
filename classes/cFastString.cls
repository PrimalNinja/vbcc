VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cFastString"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private Const m_NAME = "cFastString"

Private m_strBuffer As String 'string is stored in here
Private m_lngStart As Long 'start of buffer where actual string is
Private m_lngSize As Long 'size of string in the buffer
Private m_blnEmpty As Boolean 'is the string empty? (the buffer may not be)
Private m_lngTkStart As Long 'the start before the first tk (reset sets the start back to this)
Private m_lngTkSize As Long 'the size before the first tk (reset sets the size back to this)

Private m_strDelimeter As String 'currently set delimter
Private m_lngGrowSize As Long 'currently set buffer grow size

Private Const m_DEFGROWSIZE = 1024 'default buffer grow size
Private Const m_DEFDELIMETER = "~" 'default delimeter
Public Sub Append(strValue_a As String)
  Dim lngFreeSpace As Long
  
  If Len(strValue_a) > 0 Then
  
    If m_blnEmpty Then
      lngFreeSpace = Len(m_strBuffer)
    Else
      lngFreeSpace = Len(m_strBuffer) - m_lngStart - m_lngSize + 1
    End If
    
    'if we need to grow the buffer do it
    If Len(strValue_a) > lngFreeSpace Then
      m_strBuffer = m_strBuffer & Space$(m_lngGrowSize + Len(strValue_a))
    End If
    
    If m_blnEmpty Then
      m_blnEmpty = False
      m_lngStart = 1
      m_lngSize = Len(strValue_a)
      
      Mid$(m_strBuffer, m_lngStart, m_lngSize) = strValue_a
    Else
      Mid$(m_strBuffer, m_lngStart + m_lngSize, Len(strValue_a)) = strValue_a
      m_lngSize = m_lngSize + Len(strValue_a)
    End If
  End If
End Sub
Public Sub Clear()
  m_strBuffer = ""
  m_lngStart = 0
  m_lngSize = 0
  m_blnEmpty = True
End Sub
Public Sub Reset()
  If m_lngTkStart > 0 Then
    m_lngStart = m_lngTkStart
    m_lngSize = m_lngTkSize
    m_lngTkStart = 0
    m_lngTkSize = 0
  End If
End Sub
Public Function TokenCount() As Long
  Dim lngResult As Long
  
  Reset
  lngResult = 0
  While Size > 0
    Token
    lngResult = lngResult + 1
  Wend
  
  TokenCount = lngResult
End Function
Public Sub Delimeter(strValue_a As String)
  m_strDelimeter = strValue_a
End Sub
Public Function Size() As Long
  Size = m_lngSize
End Function

Public Sub fWrite(strValue_a As String)
  m_blnEmpty = False
  m_lngStart = 1
  m_lngSize = Len(strValue_a)
  m_strBuffer = strValue_a
End Sub
Public Function fRead() As String
  If m_blnEmpty Then
    fRead = ""
  Else
    If m_lngSize > 0 Then
      fRead = Mid$(m_strBuffer, m_lngStart, m_lngSize)
    End If
  End If
End Function
Public Function Token() As String
  Dim lngPosition As Long
  Dim strResult As String
  Dim strToken As String
  
  If m_lngTkStart = 0 Then
    m_lngTkStart = m_lngStart 'reserve the tk start for reset
    m_lngTkSize = m_lngSize
  End If
  
  If m_lngSize > 0 Then
    lngPosition = InStr(m_lngStart, m_strBuffer, m_strDelimeter)
    If lngPosition > 0 Then
      lngPosition = lngPosition - m_lngStart + 1
      If lngPosition > m_lngSize Then
        lngPosition = 0
      End If
    End If
    
    If lngPosition > 0 Then
      strToken = Mid$(m_strBuffer, m_lngStart, lngPosition - 1)
      strResult = strToken
      
      m_lngStart = m_lngStart + Len(strToken) + Len(m_strDelimeter)
      m_lngSize = m_lngSize - Len(strToken) - Len(m_strDelimeter)
    Else
      strResult = Mid$(m_strBuffer, m_lngStart, m_lngSize)
      
      m_lngStart = 0
      m_lngSize = 0
    End If
  End If
  
  Token = strResult
End Function

Private Sub Class_Initialize()
  m_strDelimeter = m_DEFDELIMETER
  m_lngGrowSize = m_DEFGROWSIZE
  
  Clear
End Sub
