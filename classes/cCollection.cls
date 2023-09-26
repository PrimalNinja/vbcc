VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cCollection"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private Const m_NAME = "cCollection"

Private m_objList As cList

Public Dictionary As Boolean


Public Function Append(strKey_a As String, objItem_a As Object) As Boolean
  Dim blnResult As Boolean
  
  blnResult = True
  
  If Dictionary Then
    Remove strKey_a
  End If
  
  If Exists(strKey_a) Then
    blnResult = False
  Else
    m_objList.Append strKey_a, objItem_a
    blnResult = True
  End If
  
  Append = blnResult
End Function
Public Sub Clear()
  m_objList.Clear
End Sub
Public Function List() As cList
  Set List = m_objList
End Function

Public Sub Remove(strKey_a As String)
  m_objList.FirstNode
  m_objList.Find strKey_a
  If Not m_objList.Read Is Nothing Then
    m_objList.Remove
  End If
End Sub

Public Function Exists(strKey_a As String) As Boolean
  m_objList.FirstNode
  m_objList.Find strKey_a
  Exists = Not m_objList.Read Is Nothing
End Function

Public Function Read(strKey_a As String) As Object
  m_objList.FirstNode
  m_objList.Find strKey_a
  If Not m_objList.Read Is Nothing Then
    Set Read = m_objList.Read.Item
  End If
End Function
Public Function Size() As Long
  Size = m_objList.Size
End Function


Private Sub Class_Initialize()
  Set m_objList = New cList
  Dictionary = False
End Sub


Private Sub Class_Terminate()
  Set m_objList = Nothing
End Sub


