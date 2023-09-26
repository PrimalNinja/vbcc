VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cNode"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private Const m_NAME = "cNode"

Public Key As String
Public PreviousNode As cNode
Public NextNode As cNode
Public Item As Object

Private Sub Class_Terminate()
  Key = ""
  Set PreviousNode = Nothing
  Set NextNode = Nothing
  Set Item = Nothing
End Sub


