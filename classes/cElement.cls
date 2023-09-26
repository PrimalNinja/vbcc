VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cElement"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private Const m_NAME = "cElement"

Public ElementType As Long
Public Scope As Long
Public FullIdentifier As String
Public Identifier As String

Public Parameters As String 'for functions
Public DataType As String 'for variables/constants & also function result
Public Value As String 'value for constants
