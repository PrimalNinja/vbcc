package BCJ;

public class cjava {
  private static final String m_name = "cJava";
  private static final String m_target = "java";
  private String m_strprojectname;
  private String m_strfile;
  private int m_lnglineid;
  private boolean m_blninobject;
  private boolean m_blninfunc;
  private boolean m_blninudt;
  private int m_lngindent;
  private boolean m_blnconstructor;
  private String m_strobjectname;
  private boolean m_blnfinaliser;
  private boolean m_blnselectcasefirst;
  private String m_strselectcaseexpression;
  private cfile m_objfileout;
  private String m_strfilename;
  private static final short m_etobject = 1;
  private static final short m_etmodule = 2;
  private static final short m_etfunc = 3;
  private static final short m_etsub = 4;
  private static final short m_etudt = 5;
  private static final short m_etconstant = 6;
  private static final short m_etvariable = 7;
  private static final short m_etudtvariable = 8;
  private static final short m_dtunknown = 0;
  private static final short m_dtbyte = 1;
  private static final short m_dtboolean = 2;
  private static final short m_dtinteger = 3;
  private static final short m_dtlong = 4;
  private static final short m_dtcurrency = 5;
  private static final short m_dtsingle = 6;
  private static final short m_dtdouble = 7;
  private static final short m_dtstring = 8;
  private static final short m_dtobject = 9;
  private static final short m_scopeglobal = 1;
  private static final short m_scopemodule = 2;
  private static final short m_scopelocal = 3;
  private int m_lnglinesin;
  private int m_lnglinesout;
  private int m_lngerrors;
  private int m_lngfuncs;
  private int m_lngmodules;
  private int m_lngobjects;
  private int m_lngsubs;
  private int m_lngudts;
  private int m_lngvars;
  private int m_lngexprns;
  private int m_lngfors;
  private int m_lngifthens;
  private int m_lngselectcases;
  private int m_lngwhiles;
  private int m_lngconsts;

  public void initialise() {
    <EXPRESSION>m_lnglinesin = 0</EXPRESSION>;
    <EXPRESSION>m_lnglinesout = 0</EXPRESSION>;
    <EXPRESSION>m_lngerrors = 0</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = 0</EXPRESSION>;
    <EXPRESSION>m_lngmodules = 0</EXPRESSION>;
    <EXPRESSION>m_lngobjects = 0</EXPRESSION>;
    <EXPRESSION>m_lngsubs = 0</EXPRESSION>;
    <EXPRESSION>m_lngudts = 0</EXPRESSION>;
    <EXPRESSION>m_lngvars = 0</EXPRESSION>;
    <EXPRESSION>m_lngexprns = 0</EXPRESSION>;
    <EXPRESSION>m_lngfors = 0</EXPRESSION>;
    <EXPRESSION>m_lngifthens = 0</EXPRESSION>;
    <EXPRESSION>m_lngselectcases = 0</EXPRESSION>;
    <EXPRESSION>m_lngwhiles = 0</EXPRESSION>;
    <EXPRESSION>m_lngconsts = 0</EXPRESSION>;
    <EXPRESSION>m_lngindent = 0</EXPRESSION>;
    <EXPRESSION>m_strprojectname = ""</EXPRESSION>;
    <EXPRESSION>m_strfile = ""</EXPRESSION>;
    <EXPRESSION>m_lnglineid = 0</EXPRESSION>;
    <EXPRESSION>m_blninobject = false</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninudt = false</EXPRESSION>;
  }

  private String compileline(String strprojectname_a, String strfile_a, int lnglineid_a, String str_a) {
    String sys_result;
    String strresult;
    String strstatement;
    <EXPRESSION>m_strprojectname = strprojectname_a</EXPRESSION>;
    <EXPRESSION>m_strfile = strfile_a</EXPRESSION>;
    <EXPRESSION>m_lnglineid = lnglineid_a</EXPRESSION>;
    <EXPRESSION>strstatement = trim$(str_a)</EXPRESSION>;
    <EXPRESSION>m_lnglinesin = m_lnglinesin + 1</EXPRESSION>;
    if (<EXPRESSION>len(strstatement) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = pvtparsestatement(strstatement)</EXPRESSION>;
    }
    <EXPRESSION>compileline = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_object(String str_a) {
    String sys_result;
    String stridentifier;
    String strresult;
    <EXPRESSION>m_blnfinaliser = false</EXPRESSION>;
    <EXPRESSION>m_blnconstructor = false</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>m_strfilename = app.path & "\" & m_strprojectname & "\" & m_target & "\" & stridentifier & ".java"</EXPRESSION>;
    <EXPRESSION>m_objfileout.fopen m_strfilename, g_fmappend</EXPRESSION>;
    <EXPRESSION>m_strobjectname = stridentifier</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "package " & m_strprojectname & ";"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & vbcrlf & pvtspaces(m_lngindent) & "public class " & stridentifier & " {"</EXPRESSION>;
    <EXPRESSION>m_blninobject = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngobjects = m_lngobjects + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_object = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_module(String str_a) {
    String sys_result;
    String stridentifier;
    String strresult;
    <EXPRESSION>m_blnfinaliser = false</EXPRESSION>;
    <EXPRESSION>m_blnconstructor = false</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>m_strfilename = app.path & "\" & m_strprojectname & "\" & m_target & "\" & stridentifier & ".java"</EXPRESSION>;
    <EXPRESSION>m_objfileout.fopen m_strfilename, g_fmappend</EXPRESSION>;
    <EXPRESSION>m_strobjectname = stridentifier</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "package " & m_strprojectname & ";"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & vbcrlf & pvtspaces(m_lngindent) & "public class " & stridentifier & " {"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngmodules = m_lngmodules + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_module = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endmodule(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_global_endmodule = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_type(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_type: " & str_a</EXPRESSION>;
    <EXPRESSION>m_blninudt = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngudts = m_lngudts + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_type = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_function(String str_a) {
    String sys_result;
    String lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strparameter;
    String strparameters;
    String strresult;
    String strscope;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strparameters = xmltokget(str_a, "parameters")</EXPRESSION>;
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & strscope & " " & strdatatype & " " & stridentifier & "("</EXPRESSION>;

    while (<EXPRESSION>len(strparameters) > 0</EXPRESSION>) {
      <EXPRESSION>strparameter = xmltok(strparameters, "parameter")</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtdatatypeget(xmltokget(strparameter, "paramdatatype")) & " " & xmltokget(strparameter, "paramidentifier") & ", "</EXPRESSION>;
    }
    if (<EXPRESSION>right$(strresult, 2) = ", "</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strresult, len(strresult) - 2)</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & ") {"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = m_lngfuncs + 1</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & pvtspaces(m_lngindent) & pvtdatatypeget(strdatatype) & " sys_result;"</EXPRESSION>;
    <EXPRESSION>pvt_global_function = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_function(String str_a) {
    String sys_result;
    String lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strparameter;
    String strparameters;
    String strresult;
    String strscope;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strparameters = xmltokget(str_a, "parameters")</EXPRESSION>;
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & strscope & " " & strdatatype & " " & stridentifier & "("</EXPRESSION>;

    while (<EXPRESSION>len(strparameters) > 0</EXPRESSION>) {
      <EXPRESSION>strparameter = xmltok(strparameters, "parameter")</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtdatatypeget(xmltokget(strparameter, "paramdatatype")) & " " & xmltokget(strparameter, "paramidentifier") & ", "</EXPRESSION>;
    }
    if (<EXPRESSION>right$(strresult, 2) = ", "</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strresult, len(strresult) - 2)</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & ") {"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = m_lngfuncs + 1</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & pvtspaces(m_lngindent) & pvtdatatypeget(strdatatype) & " sys_result;"</EXPRESSION>;
    <EXPRESSION>pvt_object_function = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_sub(String str_a) {
    String sys_result;
    int lngscope;
    String stridentifier;
    String strparameter;
    String strparameters;
    String strresult;
    String strscope;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strparameters = xmltokget(str_a, "parameters")</EXPRESSION>;
    if (<EXPRESSION>stridentifier = "class_initialize"</EXPRESSION>) {
      <EXPRESSION>m_blnconstructor = true</EXPRESSION>;
    }
    if (<EXPRESSION>stridentifier = "class_terminate"</EXPRESSION>) {
      <EXPRESSION>m_blnfinaliser = true</EXPRESSION>;
      <EXPRESSION>strscope = pvtscopeget(m_scopeglobal)</EXPRESSION>;
    }
    if (<EXPRESSION>stridentifier = "finalize"</EXPRESSION>) {
      <EXPRESSION>strscope = "protected"</EXPRESSION>;
    }
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & strscope & " void " & stridentifier & "("</EXPRESSION>;

    while (<EXPRESSION>len(strparameters) > 0</EXPRESSION>) {
      <EXPRESSION>strparameter = xmltok(strparameters, "parameter")</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtdatatypeget(xmltokget(strparameter, "paramdatatype")) & " " & xmltokget(strparameter, "paramidentifier") & ", "</EXPRESSION>;
    }
    if (<EXPRESSION>right$(strresult, 2) = ", "</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strresult, len(strresult) - 2)</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & ") {"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngsubs = m_lngsubs + 1</EXPRESSION>;
    <EXPRESSION>pvt_object_sub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_sub(String str_a) {
    String sys_result;
    int lngscope;
    String stridentifier;
    String strparameter;
    String strparameters;
    String strresult;
    String strscope;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strparameters = xmltokget(str_a, "parameters")</EXPRESSION>;
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & strscope & " void " & stridentifier & "("</EXPRESSION>;

    while (<EXPRESSION>len(strparameters) > 0</EXPRESSION>) {
      <EXPRESSION>strparameter = xmltok(strparameters, "parameter")</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtdatatypeget(xmltokget(strparameter, "paramdatatype")) & " " & xmltokget(strparameter, "paramidentifier") & ", "</EXPRESSION>;
    }
    if (<EXPRESSION>right$(strresult, 2) = ", "</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strresult, len(strresult) - 2)</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & ") {"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngsubs = m_lngsubs + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_sub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_variable(String str_a) {
    String sys_result;
    int lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strresult;
    String strscope;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & strscope & " " & strdatatype & " " & stridentifier & ";"</EXPRESSION>;
    <EXPRESSION>pvt_global_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_constant(String str_a) {
    String sys_result;
    int lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strresult;
    String strscope;
    String strvalue;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strvalue = xmltokget(str_a, "value")</EXPRESSION>;
    if (<EXPRESSION>lngdatatype = m_dtstring</EXPRESSION>) {
      <EXPRESSION>strvalue = chr$(34) & strvalue & chr$(34)</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & strscope & " static final " & strdatatype & " " & stridentifier & " = " & strvalue & ";"</EXPRESSION>;
    <EXPRESSION>pvt_global_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_variable(String str_a) {
    String sys_result;
    int lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strresult;
    String strscope;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & strscope & " " & strdatatype & " " & stridentifier & ";"</EXPRESSION>;
    <EXPRESSION>pvt_object_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_constant(String str_a) {
    String sys_result;
    int lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strresult;
    String strscope;
    String strvalue;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strvalue = xmltokget(str_a, "value")</EXPRESSION>;
    if (<EXPRESSION>lngdatatype = m_dtstring</EXPRESSION>) {
      <EXPRESSION>strvalue = chr$(34) & strvalue & chr$(34)</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & strscope & " static final " & strdatatype & " " & stridentifier & " = " & strvalue & ";"</EXPRESSION>;
    <EXPRESSION>pvt_object_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_variable(String str_a) {
    String sys_result;
    int lngdatatype;
    String strdatatype;
    String stridentifier;
    String strresult;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & strdatatype & " " & stridentifier & ";"</EXPRESSION>;
    <EXPRESSION>pvt_function_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_endfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return (sys_result);"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_object_endfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endobject(String str_a) {
    String sys_result;
    String strresult;
    if (<EXPRESSION>m_blnconstructor</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & pvtparsestatement("object_sub: <scope>1</scope><fullidentifier>cnode_" & m_strobjectname & "</fullidentifier><identifier>" & m_strobjectname & "</identifier><parameters></parameters>") & vbcrlf</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtparsestatement("function_expression: <expression>class_initialize</expression>") & vbcrlf</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtparsestatement("object_endsub:") & vbcrlf</EXPRESSION>;
    }
    if (<EXPRESSION>m_blnfinaliser</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & pvtparsestatement("object_sub: <scope>2</scope><fullidentifier>cnode_finalize</fullidentifier><identifier>finalize</identifier><parameters></parameters>") & vbcrlf</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtparsestatement("function_expression: <expression>class_terminate</expression>") & vbcrlf</EXPRESSION>;
      <EXPRESSION>strresult = strresult & pvtparsestatement("object_endsub:") & vbcrlf</EXPRESSION>;
    }
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninobject = false</EXPRESSION>;
    <EXPRESSION>strresult = strresult & pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_global_endobject = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return (sys_result);"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>strresult = strresult & vbcrlf & pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_global_endfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_endsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_object_endsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_global_endsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endtype(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninudt = false</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endtype:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endtype = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_udt_typevariable(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "udt_typevariable: " & str_a</EXPRESSION>;
    <EXPRESSION>pvt_udt_typevariable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_exitfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return (sys_result);"</EXPRESSION>;
    <EXPRESSION>pvt_object_exitfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_exitfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return (sys_result);"</EXPRESSION>;
    <EXPRESSION>pvt_global_exitfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_exitsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return;"</EXPRESSION>;
    <EXPRESSION>pvt_object_exitsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_exitsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "return;"</EXPRESSION>;
    <EXPRESSION>pvt_global_exitsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtdatatypeget(String strdatatype_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtbyte</EXPRESSION>)) {
      <EXPRESSION>strresult = "byte"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtboolean</EXPRESSION>)) {
      <EXPRESSION>strresult = "boolean"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtinteger</EXPRESSION>)) {
      <EXPRESSION>strresult = "short"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtlong</EXPRESSION>)) {
      <EXPRESSION>strresult = "int"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtcurrency</EXPRESSION>)) {
      <EXPRESSION>strresult = "float"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtsingle</EXPRESSION>)) {
      <EXPRESSION>strresult = "float"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtdouble</EXPRESSION>)) {
      <EXPRESSION>strresult = "double"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtstring</EXPRESSION>)) {
      <EXPRESSION>strresult = "String"</EXPRESSION>;
    } elseif ((<EXPRESSION>strdatatype_a</EXPRESSION>) == (<EXPRESSION>m_dtobject</EXPRESSION>)) {
      <EXPRESSION>strresult = "Object"</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = strdatatype_a</EXPRESSION>;
    }
    <EXPRESSION>pvtdatatypeget = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtexpressionget(String strexpression_a) {
    String sys_result;
    <EXPRESSION>pvtexpressionget = "<EXPRESSION>" & strexpression_a & "</EXPRESSION>"</EXPRESSION>;
    return (sys_result);
  }

  private String pvtscopeget(String strscope_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>strscope_a</EXPRESSION>) == (<EXPRESSION>m_scopeglobal</EXPRESSION>)) {
      <EXPRESSION>strresult = "public"</EXPRESSION>;
    } elseif ((<EXPRESSION>strscope_a</EXPRESSION>) == (<EXPRESSION>m_scopemodule</EXPRESSION>)) {
      <EXPRESSION>strresult = "private"</EXPRESSION>;
    } elseif ((<EXPRESSION>strscope_a</EXPRESSION>) == (<EXPRESSION>m_scopelocal</EXPRESSION>)) {
      <EXPRESSION>strresult = ""</EXPRESSION>;
    }
    <EXPRESSION>pvtscopeget = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparsestatement(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>m_blninfunc</EXPRESSION>)) {
      <EXPRESSION>strresult = pvtparsefunction(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>m_blninudt</EXPRESSION>)) {
      <EXPRESSION>strresult = pvtparseudt(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>m_blninobject</EXPRESSION>)) {
      <EXPRESSION>strresult = pvtparseobject(str)</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = pvtparseapp(str)</EXPRESSION>;
    }
    <EXPRESSION>pvtparsestatement = strresult</EXPRESSION>;
    return (sys_result);
  }

  private void pvterror(String strerror_a) {
    cfile objfile;
    <EXPRESSION>set objfile = new cfile</EXPRESSION>;
    <EXPRESSION>m_lngerrors = m_lngerrors + 1</EXPRESSION>;
    if (<EXPRESSION>objfile.fopen(app.path & "\" & m_strprojectname & "\error.log", g_fmappend)</EXPRESSION>) {
      <EXPRESSION>objfile.fwrite "file '" & m_strfile & "' " & " line " & m_lnglineid & " " & strerror_a & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfile = nothing</EXPRESSION>;
  }

  private String pvt_function_expression(String str_a) {
    String sys_result;
    String strexpression;
    String strresult;
    <EXPRESSION>m_lngexprns = m_lngexprns + 1</EXPRESSION>;
    <EXPRESSION>strexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & pvtexpressionget(strexpression) & ";"</EXPRESSION>;
    <EXPRESSION>pvt_function_expression = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_selectcase(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_strselectcaseexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    <EXPRESSION>m_blnselectcasefirst = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngselectcases = m_lngselectcases + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_selectcase = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_caseelse(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent - 1) & "} else {"</EXPRESSION>;
    <EXPRESSION>pvt_function_caseelse = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_case(String str_a) {
    String sys_result;
    String strexpression;
    String strresult;
    <EXPRESSION>strexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    if (<EXPRESSION>m_blnselectcasefirst</EXPRESSION>) {
      <EXPRESSION>strresult = pvtspaces(m_lngindent - 1) & "if ((" & pvtexpressionget(m_strselectcaseexpression) & ") == (" & pvtexpressionget(strexpression) & ")) {"</EXPRESSION>;
      <EXPRESSION>m_blnselectcasefirst = false</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = pvtspaces(m_lngindent - 1) & "} elseif ((" & pvtexpressionget(m_strselectcaseexpression) & ") == (" & pvtexpressionget(strexpression) & ")) {"</EXPRESSION>;
    }
    <EXPRESSION>pvt_function_case = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_endselect(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_function_endselect = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_while(String str_a) {
    String sys_result;
    String strexpression;
    String strresult;
    <EXPRESSION>strexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & "while (" & pvtexpressionget(strexpression) & ") {"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngwhiles = m_lngwhiles + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_while = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_wend(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_function_wend = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_for(String str_a) {
    String sys_result;
    String strfromexpression;
    String strresult;
    String strstep;
    String strtoexpression;
    <EXPRESSION>strfromexpression = xmltokget(str_a, "fromexpression")</EXPRESSION>;
    <EXPRESSION>strtoexpression = xmltokget(str_a, "toexpression")</EXPRESSION>;
    <EXPRESSION>strstep = xmltokget(str_a, "step")</EXPRESSION>;
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & "for (" & pvtexpressionget(strfromexpression) & ";" & pvtexpressionget(strtoexpression) & ";" & pvtexpressionget(strstep) & ") {"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfors = m_lngfors + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_for = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_next(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_function_next = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_ifthen(String str_a) {
    String sys_result;
    String strexpression;
    String strresult;
    <EXPRESSION>strexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "if (" & pvtexpressionget(strexpression) & ") {"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngifthens = m_lngifthens + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_ifthen = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_elseifthen(String str_a) {
    String sys_result;
    String strexpression;
    String strresult;
    <EXPRESSION>strexpression = xmltokget(str_a, "expression")</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent - 1) & "} elseif (" & pvtexpressionget(strexpression) & ") {"</EXPRESSION>;
    <EXPRESSION>pvt_function_elseifthen = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_else(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent - 1) & "} else {"</EXPRESSION>;
    <EXPRESSION>pvt_function_else = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_endif(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "}"</EXPRESSION>;
    <EXPRESSION>pvt_function_endif = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseobject(String str_a) {
    String sys_result;
    String str;
    String strcmd;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strcmd = trim$(strtok(str, ":"))</EXPRESSION>;
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_endobject"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_endobject(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_function"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_function(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_sub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_sub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_constant"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_constant(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_variable"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_variable(str)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvtparseobject = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseapp(String str_a) {
    String sys_result;
    String str;
    String strcmd;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strcmd = trim$(strtok(str, ":"))</EXPRESSION>;
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_object"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_object(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_module"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_module(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_endmodule"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_endmodule(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_type"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_type(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_function"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_function(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_sub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_sub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_constant"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_constant(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_variable"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_variable(str)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvtparseapp = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparsefunction(String str_a) {
    String sys_result;
    String str;
    String strcmd;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strcmd = trim$(strtok(str, ":"))</EXPRESSION>;
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_constant"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_constant(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_endfunction"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_endfunction(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_endfunction"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_endfunction(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_exitfunction"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_exitfunction(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_exitfunction"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_exitfunction(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_endsub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_endsub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_endsub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_endsub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "object_exitsub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_object_exitsub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_exitsub"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_exitsub(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_variable"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_variable(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_selectcase"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_selectcase(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_caseelse"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_caseelse(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_case"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_case(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_endselect"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_endselect(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_while"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_while(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_wend"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_wend(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_for"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_for(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_next"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_next(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_ifthen"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_ifthen(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_elseifthen"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_elseifthen(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_else"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_else(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_endif"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_function_endif(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "function_expression"</EXPRESSION>)) {
      <EXPRESSION>function calls & assignments)</EXPRESSION>;
      <EXPRESSION>strresult = pvt_function_expression(str)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvtparsefunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_constant(String str_a) {
    String sys_result;
    int lngdatatype;
    int lngscope;
    String strdatatype;
    String stridentifier;
    String strresult;
    String strscope;
    String strvalue;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>lngdatatype = val(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>strdatatype = pvtdatatypeget(xmltokget(str_a, "datatype"))</EXPRESSION>;
    <EXPRESSION>lngscope = val(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>strscope = pvtscopeget(xmltokget(str_a, "scope"))</EXPRESSION>;
    <EXPRESSION>stridentifier = xmltokget(str_a, "identifier")</EXPRESSION>;
    <EXPRESSION>strvalue = xmltokget(str_a, "value")</EXPRESSION>;
    if (<EXPRESSION>lngdatatype = m_dtstring</EXPRESSION>) {
      <EXPRESSION>strvalue = chr$(34) & strvalue & chr$(34)</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "static final " & strdatatype & " " & stridentifier & " = " & strvalue & ";"</EXPRESSION>;
    <EXPRESSION>pvt_function_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseudt(String str_a) {
    String sys_result;
    String str;
    String strcmd;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strcmd = trim$(strtok(str, ":"))</EXPRESSION>;
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "global_endtype"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_global_endtype(str)</EXPRESSION>;
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>strcmd = "udt_typevariable"</EXPRESSION>)) {
      <EXPRESSION>strresult = pvt_udt_typevariable(str)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvtparseudt = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtspaces(int lngindent_a) {
    String sys_result;
    <EXPRESSION>pvtspaces = space$(lngindent_a * 2)</EXPRESSION>;
    return (sys_result);
  }

  public void statisticssave() {
    cfile objfile;
    <EXPRESSION>set objfile = new cfile</EXPRESSION>;
    if (<EXPRESSION>objfile.fopen(app.path & "\" & m_strprojectname & "\" & m_target & "\stats.log", g_fmappend)</EXPRESSION>) {
      <EXPRESSION>objfile.fwrite vbcrlf & "COMPILER STATS:" & vbcrlf & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "line in count     " & m_lnglinesin & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "line out count    " & m_lnglinesout & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "error count       " & m_lngerrors & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "module count      " & m_lngmodules & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "object count      " & m_lngobjects & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "function count    " & m_lngfuncs & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "subroutine count  " & m_lngsubs & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "variable count    " & m_lngvars & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "constant count    " & m_lngconsts & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "UDT count         " & m_lngudts & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "condition count   " & m_lngifthens & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "expression count  " & m_lngexprns & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "for loop count    " & m_lngfors & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "select case count " & m_lngselectcases & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "while loop count  " & m_lngwhiles & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfile = nothing</EXPRESSION>;
  }

  public int compile(String strprojectname_a) {
    int sys_result;
    int lnglineid;
    cfile objfilein;
    String strcompiled;
    String strline;
    String strfile;
    String strfiles;
    String strtemp;
    <EXPRESSION>set objfilein = new cfile</EXPRESSION>;
    <EXPRESSION>set m_objfileout = new cfile</EXPRESSION>;
    <EXPRESSION>strfiles = ""</EXPRESSION>;
    <EXPRESSION>fsdirectorycreate app.path & "\" & strprojectname_a</EXPRESSION>;
    <EXPRESSION>fsdirectorycreate app.path & "\" & strprojectname_a & "\" & m_target</EXPRESSION>;
    <EXPRESSION>m_strfilename = app.path & "\" & strprojectname_a & "\" & m_target & "\compiler.out"</EXPRESSION>;
    if (<EXPRESSION>m_objfileout.fopen(m_strfilename, g_fmappend)</EXPRESSION>) {
      <EXPRESSION>strfile = app.path & "\" & strprojectname_a & "\parser.out"</EXPRESSION>;
      <EXPRESSION>debug.print "processing: " & strfile</EXPRESSION>;
      if (<EXPRESSION>objfilein.fopen(strfile, g_fmread)</EXPRESSION>) {
        <EXPRESSION>lnglineid = 0</EXPRESSION>;

        while (<EXPRESSION>not objfilein.endoffile</EXPRESSION>) {
          <EXPRESSION>strline = objfilein.fread()</EXPRESSION>;
          <EXPRESSION>lnglineid = lnglineid + 1</EXPRESSION>;
          <EXPRESSION>strcompiled = compileline(strprojectname_a, strfile, lnglineid, strline)</EXPRESSION>;
          if (<EXPRESSION>len(strcompiled) > 0</EXPRESSION>) {
            <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strcompiled, vbcrlf)</EXPRESSION>;
            <EXPRESSION>m_objfileout.fwrite strcompiled & vbcrlf</EXPRESSION>;
          }
        }
        <EXPRESSION>objfilein.fclose</EXPRESSION>;
      }
      <EXPRESSION>doevents</EXPRESSION>;
      <EXPRESSION>m_objfileout.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfilein = nothing</EXPRESSION>;
    <EXPRESSION>set m_objfileout = nothing</EXPRESSION>;
    <EXPRESSION>compile = m_lngerrors</EXPRESSION>;
    return (sys_result);
  }

  private void class_initialize() {
    <EXPRESSION>initialise</EXPRESSION>;
  }

  public void cjava() {
    <EXPRESSION>class_initialize</EXPRESSION>;
  }
}
