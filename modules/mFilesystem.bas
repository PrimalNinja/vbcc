Attribute VB_Name = "mFilesystem"
Option Explicit

Private Const m_NAME = "mFilesystem"

Public Const g_FMREAD = 0
Public Const g_FMAPPEND = 1
Public Const g_FMBINARY = 2

Public Sub FSDirectoryCreate(strPath_a As String)
  If Len(Dir(strPath_a, vbDirectory)) = 0 Then
    MkDir strPath_a
  End If
End Sub
