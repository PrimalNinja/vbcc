VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cList"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private Const m_NAME = "cList"

Private m_objFirstNode As cNode
Private m_objLastNode As cNode
Private m_objCurrentNode As cNode

Private m_lngSize As Long
Public Sub Append(strKey_a As String, objItem_a As Object)
  Dim objNode As cNode
  
  Set objNode = New cNode
  
  If m_objLastNode Is Nothing Then
    Set m_objFirstNode = objNode
  Else
    Set m_objLastNode.NextNode = objNode
    Set objNode.PreviousNode = m_objLastNode
  End If
  
  Set m_objLastNode = objNode
  Set objNode.Item = objItem_a
  objNode.Key = strKey_a
  If m_objCurrentNode Is Nothing Then
    Set m_objCurrentNode = objNode
  End If
  m_lngSize = m_lngSize + 1

  Set objNode = Nothing
End Sub
Public Sub Clear()
  FirstNode
  While Not Read Is Nothing
    FirstNode
    Remove
  Wend
End Sub
Public Sub NextNode()
  If Not m_objCurrentNode Is Nothing Then
    Set m_objCurrentNode = m_objCurrentNode.NextNode
  End If
End Sub
Public Sub PreviousNode()
  If Not m_objCurrentNode Is Nothing Then
    Set m_objCurrentNode = m_objCurrentNode.PreviousNode
  End If
End Sub
Public Function Read() As cNode
  Set Read = m_objCurrentNode
End Function
Public Sub Remove()
  Dim objNode As cNode
  
  Set objNode = m_objCurrentNode
  If Not objNode.PreviousNode Is Nothing Then
    Set objNode.PreviousNode.NextNode = objNode.NextNode
  Else
    Set m_objFirstNode = objNode.NextNode
  End If
  
  If Not objNode.NextNode Is Nothing Then
    Set objNode.NextNode.PreviousNode = objNode.PreviousNode
  Else
    Set m_objLastNode = objNode.PreviousNode
  End If
  
  Set m_objCurrentNode = objNode.NextNode
  m_lngSize = m_lngSize - 1
End Sub
Public Function Size() As Long
  Size = m_lngSize
End Function
Public Sub FirstNode()
  Set m_objCurrentNode = m_objFirstNode
End Sub
Public Sub LastNode()
  Set m_objCurrentNode = m_objLastNode
End Sub
Public Sub Prepend(strKey_a As String, objItem_a As Object)
  Dim objNode As cNode
  
  Set objNode = New cNode
  
  If m_objFirstNode Is Nothing Then
    Set m_objLastNode = objNode
  Else
    Set m_objFirstNode.PreviousNode = objNode
    Set objNode.NextNode = m_objFirstNode
  End If
  
  Set m_objFirstNode = objNode
  Set objNode.Item = objItem_a
  objNode.Key = strKey_a
  If m_objCurrentNode Is Nothing Then
    Set m_objCurrentNode = objNode
  End If
  m_lngSize = m_lngSize + 1

  Set objNode = Nothing
End Sub

Public Sub Find(strKey_a As String)
  Dim blnFound As Boolean
  
  blnFound = False
  While Not Read Is Nothing And Not blnFound
    If Read.Key = strKey_a Then
      blnFound = True
    Else
      NextNode
    End If
  Wend
End Sub
