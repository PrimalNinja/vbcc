Attribute VB_Name = "mMain"
Option Explicit

Private Const m_NAME = "mMain"

Public Sub Main()
  Dim blnCompile As Boolean
  Dim blnParse As Boolean
  Dim lngErrors As Long
  Dim objCompiler As cCompiler 'declare as OBJECT to allow multiple target types
  Dim objParser As cParser
  Dim strFiles As String
  Dim strProjectName As String
  Dim strProjectPath As String
'Exit Sub

  Set objParser = New cParser
  lngErrors = 0
  blnCompile = True
  blnParse = True
  
  strProjectName = "BC"
  strProjectPath = "Z:\vbcc\pBC5.vbp"
  'strFiles = "F:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cCompiler.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cElement.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cFastString.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cFile.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cParser.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\modules\mMain.basF:\_julian\iria\dev\Projects.2007\Vb6comp\modules\mFilesystem.basF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cNode.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cList.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\classes\cCollection.clsF:\_julian\iria\dev\Projects.2007\Vb6comp\modules\mFastString.basF:\_julian\iria\dev\Projects.2007\Vb6comp\modules\mConstants.basF:\_julian\iria\dev\Projects.2007\Vb6comp\modules\mXML.bas"
  
  If blnParse Then
    'parse
    If Len(strProjectPath) > 0 Then
      lngErrors = objParser.ParseVB6(strProjectName, strProjectPath)
    Else
      lngErrors = objParser.Parse(strProjectName, strFiles)
    End If
    objParser.StatisticsSave
    objParser.ElementsSave
  End If

  If blnCompile Then
    If lngErrors = 0 Then
      
      'compile
  '    Set objCompiler = New cJava
  '    lngErrors = objCompiler.Compile(strProjectName)
  '    objCompiler.StatisticsSave
  
      Set objCompiler = New cCompiler
      lngErrors = objCompiler.Compile(strProjectName)
      objCompiler.StatisticsSave
    End If
  End If

  Set objParser = Nothing
  Set objCompiler = Nothing
End Sub
