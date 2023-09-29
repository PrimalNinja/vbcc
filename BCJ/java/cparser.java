package BCJ;

public class cparser {
  private static final String m_name = "cParser";
  private String m_strprojectname;
  private String m_strfile;
  private int m_lnglineid;
  private boolean m_blninobject;
  private boolean m_blninfunc;
  private boolean m_blninsub;
  private boolean m_blninudt;
  private boolean m_blnprivatefunc;
  private boolean m_blnprivateudt;
  private String m_strcurrobject;
  private String m_strcurrfunc;
  private String m_strcurrudt;
  private static final short m_etobject = 1;
  private static final short m_etmodule = 2;
  private static final short m_etfunc = 3;
  private static final short m_etsub = 4;
  private static final short m_etudt = 5;
  private static final short m_etconstant = 6;
  private static final short m_etvariable = 7;
  private static final short m_etudtvariable = 8;
  private static final String m_dtunknown = "0";
  private static final String m_dtbyte = "1";
  private static final String m_dtboolean = "2";
  private static final String m_dtinteger = "3";
  private static final String m_dtlong = "4";
  private static final String m_dtcurrency = "5";
  private static final String m_dtsingle = "6";
  private static final String m_dtdouble = "7";
  private static final String m_dtstring = "8";
  private static final String m_dtobject = "9";
  private static final short m_scopeglobal = 1;
  private static final short m_scopemodule = 2;
  private static final short m_scopelocal = 3;
  private int m_lngindent;
  private int m_lnglinesin;
  private int m_lnglinesout;
  private int m_lngerrors;
  private int m_lngfuncs;
  private int m_lngmodules;
  private int m_lngobjects;
  private int m_lngsubs;
  private int m_lngudts;
  private int m_lngvars;
  private int m_lngcondifs;
  private int m_lngexprns;
  private int m_lngfors;
  private int m_lngifthens;
  private int m_lngselectcases;
  private int m_lngwhiles;
  private int m_lngconsts;
  private ccollection m_objdefines;
  private ccollection m_objelements;
  private boolean m_blnskip;

  private String pvtdatatypeget(String str_a) {
    String sys_result;
    String str;
    String strresult;
    if (<EXPRESSION>left$(str_a, 4) = "new "</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid datatype '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>strresult = m_dtunknown</EXPRESSION>;
    } else {
      if ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"byte"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtbyte</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"boolean"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtboolean</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"integer"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtinteger</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"long"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtlong</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"currency"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtcurrency</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"single"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtsingle</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"double"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtdouble</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"string"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtstring</EXPRESSION>;
      } elseif ((<EXPRESSION>str_a</EXPRESSION>) == (<EXPRESSION>"object"</EXPRESSION>)) {
        <EXPRESSION>strresult = m_dtobject</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = str_a</EXPRESSION>;
      }
    }
    <EXPRESSION>pvtdatatypeget = strresult</EXPRESSION>;
    return (sys_result);
  }

  public int parse(String strprojectname_a, String strfilelist_a) {
    int sys_result;
    boolean blnmore;
    int lnglineid;
    cfile objfilein;
    cfile objfileout;
    String strline;
    String strfile;
    String strfiles;
    String strparsed;
    String strtemp;
    <EXPRESSION>set objfilein = new cfile</EXPRESSION>;
    <EXPRESSION>set objfileout = new cfile</EXPRESSION>;
    <EXPRESSION>strfiles = strfilelist_a</EXPRESSION>;
    <EXPRESSION>fsdirectorycreate app.path & "\" & strprojectname_a</EXPRESSION>;
    if (<EXPRESSION>objfileout.fopen(app.path & "\" & strprojectname_a & "\parser.out", g_fmappend)</EXPRESSION>) {

      while (<EXPRESSION>len(strfiles) > 0</EXPRESSION>) {
        <EXPRESSION>strfile = strtok(strfiles, chr$(g_chardelim))</EXPRESSION>;
        <EXPRESSION>debug.print "processing: " & strfile</EXPRESSION>;
        if (<EXPRESSION>objfilein.fopen(strfile, g_fmread)</EXPRESSION>) {
          <EXPRESSION>lnglineid = 0</EXPRESSION>;
          <EXPRESSION>blnmore = false</EXPRESSION>;

          while (<EXPRESSION>not objfilein.endoffile</EXPRESSION>) {
            <EXPRESSION>strtemp = objfilein.fread()</EXPRESSION>;
            <EXPRESSION>lnglineid = lnglineid + 1</EXPRESSION>;
            if (<EXPRESSION>blnmore</EXPRESSION>) {
              <EXPRESSION>strline = left$(strline, len(strline) - 2) & strtemp</EXPRESSION>;
              <EXPRESSION>blnmore = false</EXPRESSION>;
            } else {
              <EXPRESSION>strline = strtemp</EXPRESSION>;
            }
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, lnglineid, strline, blnmore)</EXPRESSION>;
            if (<EXPRESSION>len(strparsed) > 0</EXPRESSION>) {
              <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strparsed, vbcrlf)</EXPRESSION>;
              <EXPRESSION>objfileout.fwrite strparsed & vbcrlf</EXPRESSION>;
            }
          }
          <EXPRESSION>objfilein.fclose</EXPRESSION>;
        }
        <EXPRESSION>doevents</EXPRESSION>;
      }
      <EXPRESSION>objfileout.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfileout = nothing</EXPRESSION>;
    <EXPRESSION>set objfilein = nothing</EXPRESSION>;
    <EXPRESSION>parse = m_lngerrors</EXPRESSION>;
    return (sys_result);
  }

  public int parsevb6(String strprojectname_a, String strprojectpath_a) {
    int sys_result;
    boolean blnendofcrap;
    boolean blnmore;
    int lnglineid;
    cfile objfilein;
    cfile objfileout;
    String strbasepath;
    String strcondition;
    String strconditions;
    String strline;
    String strfile;
    String strfiles;
    String strmodulename;
    String strparsed;
    String strtemp;
    String strtype;
    <EXPRESSION>set objfilein = new cfile</EXPRESSION>;
    <EXPRESSION>set objfileout = new cfile</EXPRESSION>;
    <EXPRESSION>strfiles = ""</EXPRESSION>;
    <EXPRESSION>strconditions = ""</EXPRESSION>;
    <EXPRESSION>fsdirectorycreate app.path & "\" & strprojectname_a</EXPRESSION>;
    if (<EXPRESSION>len(strprojectpath_a) > 0</EXPRESSION>) {
      <EXPRESSION>strtemp = strreverse(strprojectpath_a)</EXPRESSION>;
      <EXPRESSION>strtok strtemp, "\"</EXPRESSION>;
      <EXPRESSION>strbasepath = strreverse(strtemp) & "\"</EXPRESSION>;
      if (<EXPRESSION>objfilein.fopen(strprojectpath_a, g_fmread)</EXPRESSION>) {

        while (<EXPRESSION>not objfilein.endoffile</EXPRESSION>) {
          <EXPRESSION>strline = objfilein.fread()</EXPRESSION>;
          if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>lcase$(left$(strline, 9)) = "condcomp="</EXPRESSION>)) {
            <EXPRESSION>strtok strline, chr$(g_charquote)</EXPRESSION>;
            <EXPRESSION>strcondition = strtok(strline, chr$(g_charquote))</EXPRESSION>;
            <EXPRESSION>strconditions = tokstr(strconditions, strcondition, ":")</EXPRESSION>;
          } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>lcase$(left$(strline, 7)) = "module="</EXPRESSION>)) {
            <EXPRESSION>strtok strline, " "</EXPRESSION>;
            <EXPRESSION>strfile = strbasepath & strline</EXPRESSION>;
            <EXPRESSION>strtype = "M"</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strtype, chr$(g_chardelim))</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strfile, chr$(g_chardelim))</EXPRESSION>;
          } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>lcase$(left$(strline, 6)) = "class="</EXPRESSION>)) {
            <EXPRESSION>strtok strline, " "</EXPRESSION>;
            <EXPRESSION>strfile = strbasepath & strline</EXPRESSION>;
            <EXPRESSION>strtype = "C"</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strtype, chr$(g_chardelim))</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strfile, chr$(g_chardelim))</EXPRESSION>;
          } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>lcase$(left$(strline, 5)) = "form="</EXPRESSION>)) {
            <EXPRESSION>strtok strline, "="</EXPRESSION>;
            <EXPRESSION>strtype = "C"</EXPRESSION>;
            <EXPRESSION>strfile = strbasepath & strline</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strtype, chr$(g_chardelim))</EXPRESSION>;
            <EXPRESSION>strfiles = tokstr(strfiles, strfile, chr$(g_chardelim))</EXPRESSION>;
          }
        }
        <EXPRESSION>objfilein.fclose</EXPRESSION>;
      }
    }
    if (<EXPRESSION>objfileout.fopen(app.path & "\" & strprojectname_a & "\parser.out", g_fmappend)</EXPRESSION>) {

      while (<EXPRESSION>len(strfiles) > 0</EXPRESSION>) {
        <EXPRESSION>strtype = strtok(strfiles, chr$(g_chardelim))</EXPRESSION>;
        <EXPRESSION>strfile = strtok(strfiles, chr$(g_chardelim))</EXPRESSION>;
        <EXPRESSION>strmodulename = ""</EXPRESSION>;
        <EXPRESSION>debug.print "processing: " & strfile</EXPRESSION>;
        if (<EXPRESSION>objfilein.fopen(strfile, g_fmread)</EXPRESSION>) {
          <EXPRESSION>lnglineid = 0</EXPRESSION>;
          <EXPRESSION>blnmore = false</EXPRESSION>;
          <EXPRESSION>strparsed = ""</EXPRESSION>;

          while (<EXPRESSION>len(strconditions) > 0</EXPRESSION>) {
            <EXPRESSION>strcondition = strtok(strconditions, ":")</EXPRESSION>;
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, 0, "#define " & strcondition, blnmore)</EXPRESSION>;
            if (<EXPRESSION>len(strparsed) > 0</EXPRESSION>) {
              <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strparsed, vbcrlf)</EXPRESSION>;
              <EXPRESSION>objfileout.fwrite strparsed & vbcrlf</EXPRESSION>;
            }
          }
          <EXPRESSION>blnendofcrap = false</EXPRESSION>;

          while (<EXPRESSION>not (objfilein.endoffile or blnendofcrap)</EXPRESSION>) {
            <EXPRESSION>strline = lcase$(trim$(objfilein.fread()))</EXPRESSION>;
            <EXPRESSION>lnglineid = lnglineid + 1</EXPRESSION>;
            if (<EXPRESSION>left$(strline, 21) = "attribute vb_name = " & chr$(g_charquote)</EXPRESSION>) {
              <EXPRESSION>strtok strline, chr$(g_charquote)</EXPRESSION>;
              <EXPRESSION>strmodulename = strtok(strline, chr$(g_charquote))</EXPRESSION>;
            }
            if (<EXPRESSION>strline = "option explicit"</EXPRESSION>) {
              <EXPRESSION>blnendofcrap = true</EXPRESSION>;
            }
          }
          <EXPRESSION>strparsed = ""</EXPRESSION>;
          if ((<EXPRESSION>strtype</EXPRESSION>) == (<EXPRESSION>"C"</EXPRESSION>)) {
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, 0, "object " & strmodulename, blnmore)</EXPRESSION>;
          } elseif ((<EXPRESSION>strtype</EXPRESSION>) == (<EXPRESSION>"M"</EXPRESSION>)) {
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, 0, "module " & strmodulename, blnmore)</EXPRESSION>;
          }
          if (<EXPRESSION>len(strparsed) > 0</EXPRESSION>) {
            <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strparsed, vbcrlf)</EXPRESSION>;
            <EXPRESSION>objfileout.fwrite strparsed & vbcrlf</EXPRESSION>;
          }

          while (<EXPRESSION>not objfilein.endoffile</EXPRESSION>) {
            <EXPRESSION>strtemp = objfilein.fread()</EXPRESSION>;
            <EXPRESSION>lnglineid = lnglineid + 1</EXPRESSION>;
            if (<EXPRESSION>blnmore</EXPRESSION>) {
              <EXPRESSION>strline = left$(strline, len(strline) - 2) & strtemp</EXPRESSION>;
              <EXPRESSION>blnmore = false</EXPRESSION>;
            } else {
              <EXPRESSION>strline = strtemp</EXPRESSION>;
            }
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, lnglineid, strline, blnmore)</EXPRESSION>;
            if (<EXPRESSION>len(strparsed) > 0</EXPRESSION>) {
              <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strparsed, vbcrlf)</EXPRESSION>;
              <EXPRESSION>objfileout.fwrite strparsed & vbcrlf</EXPRESSION>;
            }
          }
          <EXPRESSION>strparsed = ""</EXPRESSION>;
          if ((<EXPRESSION>strtype</EXPRESSION>) == (<EXPRESSION>"C"</EXPRESSION>)) {
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, 0, "end object", blnmore)</EXPRESSION>;
          } elseif ((<EXPRESSION>strtype</EXPRESSION>) == (<EXPRESSION>"M"</EXPRESSION>)) {
            <EXPRESSION>strparsed = parseline(strprojectname_a, strfile, 0, "end module", blnmore)</EXPRESSION>;
          }
          if (<EXPRESSION>len(strparsed) > 0</EXPRESSION>) {
            <EXPRESSION>m_lnglinesout = m_lnglinesout + strtokcount(strparsed, vbcrlf)</EXPRESSION>;
            <EXPRESSION>objfileout.fwrite strparsed & vbcrlf</EXPRESSION>;
          }
          <EXPRESSION>objfilein.fclose</EXPRESSION>;
        }
        <EXPRESSION>doevents</EXPRESSION>;
      }
      <EXPRESSION>objfileout.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfileout = nothing</EXPRESSION>;
    <EXPRESSION>set objfilein = nothing</EXPRESSION>;
    <EXPRESSION>parsevb6 = m_lngerrors</EXPRESSION>;
    return (sys_result);
  }

  public void initialise() {
    <EXPRESSION>m_blnskip = false</EXPRESSION>;
    <EXPRESSION>m_lnglinesin = 0</EXPRESSION>;
    <EXPRESSION>m_lnglinesout = 0</EXPRESSION>;
    <EXPRESSION>m_lngerrors = 0</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = 0</EXPRESSION>;
    <EXPRESSION>m_lngmodules = 0</EXPRESSION>;
    <EXPRESSION>m_lngobjects = 0</EXPRESSION>;
    <EXPRESSION>m_lngsubs = 0</EXPRESSION>;
    <EXPRESSION>m_lngudts = 0</EXPRESSION>;
    <EXPRESSION>m_lngvars = 0</EXPRESSION>;
    <EXPRESSION>m_lngcondifs = 0</EXPRESSION>;
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
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_blninudt = false</EXPRESSION>;
    <EXPRESSION>m_strcurrobject = ""</EXPRESSION>;
    <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    <EXPRESSION>m_strcurrudt = ""</EXPRESSION>;
    <EXPRESSION>set m_objelements = new ccollection</EXPRESSION>;
    <EXPRESSION>pvtelementadd m_etobject, m_scopeglobal, "app", "app", "", m_dtunknown, ""</EXPRESSION>;
    <EXPRESSION>set m_objdefines = new ccollection</EXPRESSION>;
  }

  private String parseline(String strprojectname_a, String strfile_a, int lnglineid_a, String str_a, boolean blnmore_a) {
    String sys_result;
    String str;
    String strresult;
    String strstatement;
    <EXPRESSION>m_strprojectname = strprojectname_a</EXPRESSION>;
    <EXPRESSION>m_strfile = strfile_a</EXPRESSION>;
    <EXPRESSION>m_lnglineid = lnglineid_a</EXPRESSION>;
    <EXPRESSION>str = str_a</EXPRESSION>;

    while (<EXPRESSION>len(str) > 0 and not blnmore_a</EXPRESSION>) {
      <EXPRESSION>str = trim$(str)</EXPRESSION>;
      if (<EXPRESSION>left$(str, 1) <> "'"</EXPRESSION>) {
        if (<EXPRESSION>right$(str, 2) = " _"</EXPRESSION>) {
          <EXPRESSION>blnmore_a = true</EXPRESSION>;
        } else {
          <EXPRESSION>blnmore_a = false</EXPRESSION>;
          <EXPRESSION>m_lnglinesin = m_lnglinesin + 1</EXPRESSION>;
          <EXPRESSION>strstatement = pvtstatementtok(str, ":")</EXPRESSION>;
          if (<EXPRESSION>len(strstatement) > 0</EXPRESSION>) {
            if (<EXPRESSION>len(strresult) > 0</EXPRESSION>) {
              <EXPRESSION>strresult = strresult & vbcrlf & pvtparsestatement(strstatement)</EXPRESSION>;
            } else {
              <EXPRESSION>strresult = pvtparsestatement(strstatement)</EXPRESSION>;
            }
          }
        }
      } else {
        <EXPRESSION>str = ""</EXPRESSION>;
      }
    }
    <EXPRESSION>parseline = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_object(String str_a) {
    String sys_result;
    String str;
    String stridentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>not pvtelementadd(m_etobject, m_scopeglobal, stridentifier, stridentifier, "", m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrobject = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrobject = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & "global_object: <identifier>" & xmlenc(stridentifier) & "</identifier>"</EXPRESSION>;
    <EXPRESSION>m_blninobject = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngobjects = m_lngobjects + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_object = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_module(String str_a) {
    String sys_result;
    String str;
    String stridentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>not pvtelementadd(m_etmodule, m_scopeglobal, stridentifier, stridentifier, "", m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrobject = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrobject = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = vbcrlf & pvtspaces(m_lngindent) & "global_module: <identifier>" & xmlenc(stridentifier) & "</identifier>"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngmodules = m_lngmodules + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_module = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endmodule(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_strcurrobject = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endmodule:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endmodule = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_type(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String stridentifier;
    String strfullidentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>lngscope_a = m_scopeglobal</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivateudt = false</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivateudt = true</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etudt, lngscope_a, strfullidentifier, stridentifier, "", m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrudt = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrudt = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_type: <todo>" & xmlenc(str_a) & "</todo>"</EXPRESSION>;
    <EXPRESSION>m_blninudt = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngudts = m_lngudts + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_type = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_function(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strfullidentifier;
    String stridentifier;
    String strparameters;
    String strresult;
    String strreturntype;
    String strtemp;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, "(")</EXPRESSION>;
    if (<EXPRESSION>lngscope_a = m_scopeglobal</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivatefunc = false</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivatefunc = true</EXPRESSION>;
    }
    <EXPRESSION>strparameters = ""</EXPRESSION>;
    <EXPRESSION>strtemp = strtok(str, ")")</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strparameters = tokstr(strparameters, strtok(strtemp, " "), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtok strtemp, " "</EXPRESSION>;
      <EXPRESSION>strparameters = tokstr(strparameters, pvtdatatypeget(strtok(strtemp, ",")), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtemp = trim$(strtemp)</EXPRESSION>;
    }
    <EXPRESSION>strtok str, " as "</EXPRESSION>;
    <EXPRESSION>strreturntype = pvtdatatypeget(str)</EXPRESSION>;
    if (<EXPRESSION>not pvtelementadd(m_etfunc, lngscope_a, strfullidentifier, stridentifier, strparameters, strreturntype, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrfunc = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_function: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strreturntype & "") & "</datatype>"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & "<parameters>"</EXPRESSION>;
    <EXPRESSION>strtemp = strparameters</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & "<parameter>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramidentifier>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramidentifier>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramdatatype>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramdatatype>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "</parameter>"</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & "</parameters>"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = m_lngfuncs + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_function = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_function(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strfullidentifier;
    String stridentifier;
    String strparameters;
    String strresult;
    String strreturntype;
    String strtemp;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, "(")</EXPRESSION>;
    <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    <EXPRESSION>strparameters = ""</EXPRESSION>;
    <EXPRESSION>strtemp = strtok(str, ")")</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strparameters = tokstr(strparameters, strtok(strtemp, " "), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtok strtemp, " "</EXPRESSION>;
      <EXPRESSION>strparameters = tokstr(strparameters, pvtdatatypeget(strtok(strtemp, ",")), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtemp = trim$(strtemp)</EXPRESSION>;
    }
    <EXPRESSION>strtok str, " as "</EXPRESSION>;
    <EXPRESSION>strreturntype = pvtdatatypeget(str)</EXPRESSION>;
    if (<EXPRESSION>not pvtelementadd(m_etfunc, lngscope_a, strfullidentifier, stridentifier, strparameters, strreturntype, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrfunc = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_function: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strreturntype & "") & "</datatype>"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & "<parameters>"</EXPRESSION>;
    <EXPRESSION>strtemp = strparameters</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & "<parameter>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramidentifier>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramidentifier>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramdatatype>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramdatatype>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "</parameter>"</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & "</parameters>"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = true</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfuncs = m_lngfuncs + 1</EXPRESSION>;
    <EXPRESSION>pvt_object_function = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_propertyget(int lngscope_a, String str_a) {
    String sys_result;
    <EXPRESSION>pvt_object_propertyget = pvt_object_function(lngscope_a, str_a)</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_propertylet(int lngscope_a, String str_a) {
    String sys_result;
    <EXPRESSION>pvt_object_propertylet = pvt_object_sub(lngscope_a, str_a)</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_propertyset(int lngscope_a, String str_a) {
    String sys_result;
    <EXPRESSION>pvt_object_propertyset = pvt_object_sub(lngscope_a, str_a)</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_sub(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strfullidentifier;
    String stridentifier;
    String strparameters;
    String strresult;
    String strtemp;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, "(")</EXPRESSION>;
    <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    <EXPRESSION>strparameters = ""</EXPRESSION>;
    <EXPRESSION>strtemp = strtok(str, ")")</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strparameters = tokstr(strparameters, strtok(strtemp, " "), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtok strtemp, " "</EXPRESSION>;
      <EXPRESSION>strparameters = tokstr(strparameters, pvtdatatypeget(strtok(strtemp, ",")), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtemp = trim$(strtemp)</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etsub, lngscope_a, strfullidentifier, stridentifier, strparameters, m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrfunc = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_sub: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier>"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & "<parameters>"</EXPRESSION>;
    <EXPRESSION>strtemp = strparameters</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & "<parameter>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramidentifier>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramidentifier>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramdatatype>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramdatatype>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "</parameter>"</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & "</parameters>"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngsubs = m_lngsubs + 1</EXPRESSION>;
    <EXPRESSION>pvt_object_sub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_sub(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strfullidentifier;
    String stridentifier;
    String strparameters;
    String strresult;
    String strtemp;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, "(")</EXPRESSION>;
    if (<EXPRESSION>lngscope_a = m_scopeglobal</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivatefunc = false</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
      <EXPRESSION>m_blnprivatefunc = true</EXPRESSION>;
    }
    <EXPRESSION>strparameters = ""</EXPRESSION>;
    <EXPRESSION>strtemp = strtok(str, ")")</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strparameters = tokstr(strparameters, strtok(strtemp, " "), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtok strtemp, " "</EXPRESSION>;
      <EXPRESSION>strparameters = tokstr(strparameters, pvtdatatypeget(strtok(strtemp, ",")), chr$(g_chardelim))</EXPRESSION>;
      <EXPRESSION>strtemp = trim$(strtemp)</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etsub, lngscope_a, strfullidentifier, stridentifier, strparameters, m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
      <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    } else {
      <EXPRESSION>m_strcurrfunc = stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_sub: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier>"</EXPRESSION>;
    <EXPRESSION>strresult = strresult & "<parameters>"</EXPRESSION>;
    <EXPRESSION>strtemp = strparameters</EXPRESSION>;

    while (<EXPRESSION>len(strtemp) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & "<parameter>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramidentifier>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramidentifier>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "<paramdatatype>" & xmlenc(strtok(strtemp, chr$(g_chardelim))) & "</paramdatatype>"</EXPRESSION>;
      <EXPRESSION>strresult = strresult & "</parameter>"</EXPRESSION>;
    }
    <EXPRESSION>strresult = strresult & "</parameters>"</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = true</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngsubs = m_lngsubs + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_sub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_variable(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String strfullidentifier;
    String stridentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>lngscope_a = m_scopeglobal</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = stridentifier</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etvariable, lngscope_a, strfullidentifier, stridentifier, "", strdatatype, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_variable: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype>"</EXPRESSION>;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_constant(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String stridentifier;
    String strfullidentifier;
    String strresult;
    String strvalue;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>lngscope_a = m_scopeglobal</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = stridentifier</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    }
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    if (<EXPRESSION>left$(str, 1) = chr$(g_charquote) and right$(str, 1) = chr$(g_charquote)</EXPRESSION>) {
      <EXPRESSION>strvalue = mid$(str, 2, len(str) - 2)</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        <EXPRESSION>strdatatype = m_dtstring</EXPRESSION>;
      }
    } else {
      <EXPRESSION>strvalue = str</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        if (<EXPRESSION>instr(str, ".") > 0</EXPRESSION>) {
          <EXPRESSION>strdatatype = m_dtsingle</EXPRESSION>;
        } else {
          <EXPRESSION>strdatatype = m_dtinteger</EXPRESSION>;
        }
      }
    }
    if (<EXPRESSION>not pvtelementadd(m_etconstant, lngscope_a, strfullidentifier, stridentifier, "", strdatatype, strvalue)</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_constant: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype><value>" & xmlenc(strvalue) & "</value>"</EXPRESSION>;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>pvt_global_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_variable(int lngscope_a, String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String strfullidentifier;
    String stridentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etvariable, lngscope_a, strfullidentifier, stridentifier, "", strdatatype, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_variable: <scope>" & xmlenc(lngscope_a & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype>"</EXPRESSION>;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>pvt_object_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_constant(String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String strfullidentifier;
    String stridentifier;
    String strresult;
    String strvalue;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & stridentifier</EXPRESSION>;
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    if (<EXPRESSION>left$(str, 1) = chr$(g_charquote) and right$(str, 1) = chr$(g_charquote)</EXPRESSION>) {
      <EXPRESSION>strvalue = mid$(str, 2, len(str) - 2)</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        <EXPRESSION>strdatatype = m_dtstring</EXPRESSION>;
      }
    } else {
      <EXPRESSION>strvalue = str</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        if (<EXPRESSION>instr(str, ".") > 0</EXPRESSION>) {
          <EXPRESSION>strdatatype = m_dtsingle</EXPRESSION>;
        } else {
          <EXPRESSION>strdatatype = m_dtinteger</EXPRESSION>;
        }
      }
    }
    if (<EXPRESSION>not pvtelementadd(m_etconstant, m_scopemodule, strfullidentifier, stridentifier, "", strdatatype, strvalue)</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_constant: <scope>" & xmlenc(m_scopemodule & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype><value>" & xmlenc(strvalue) & "</value>"</EXPRESSION>;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>pvt_object_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_constant(String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String stridentifier;
    String strfullidentifier;
    String strresult;
    String strvalue;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>m_blninobject</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
    } else {
      if (<EXPRESSION>m_blnprivatefunc</EXPRESSION>) {
        <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
      } else {
        <EXPRESSION>strfullidentifier = m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
      }
    }
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    if (<EXPRESSION>left$(str, 1) = chr$(g_charquote) and right$(str, 1) = chr$(g_charquote)</EXPRESSION>) {
      <EXPRESSION>strvalue = mid$(str, 2, len(str) - 2)</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        <EXPRESSION>strdatatype = m_dtstring</EXPRESSION>;
      }
    } else {
      <EXPRESSION>strvalue = str</EXPRESSION>;
      if (<EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>) {
        if (<EXPRESSION>instr(str, ".") > 0</EXPRESSION>) {
          <EXPRESSION>strdatatype = m_dtsingle</EXPRESSION>;
        } else {
          <EXPRESSION>strdatatype = m_dtinteger</EXPRESSION>;
        }
      }
    }
    if (<EXPRESSION>not pvtelementadd(m_etconstant, m_scopelocal, strfullidentifier, stridentifier, "", strdatatype, strvalue)</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_constant: <scope>" & xmlenc(m_scopelocal & "") & "</scope><fullidentifider>" & xmlenc(strfullidentifier) & "</fullidentifider><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype><value>" & xmlenc(strvalue) & "</value>"</EXPRESSION>;
    <EXPRESSION>m_lngconsts = m_lngconsts + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_constant = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_variable(String str_a) {
    String sys_result;
    String str;
    String strdatatype;
    String stridentifier;
    String strfullidentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>m_blninobject</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
    } else {
      if (<EXPRESSION>m_blnprivatefunc</EXPRESSION>) {
        <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
      } else {
        <EXPRESSION>strfullidentifier = m_strcurrfunc & "_" & stridentifier</EXPRESSION>;
      }
    }
    <EXPRESSION>strdatatype = strtok(str, "=")</EXPRESSION>;
    if (<EXPRESSION>len(strdatatype) > 0</EXPRESSION>) {
      <EXPRESSION>strtok strdatatype, " "</EXPRESSION>;
      <EXPRESSION>strdatatype = pvtdatatypeget(strdatatype)</EXPRESSION>;
    } else {
      <EXPRESSION>strdatatype = m_dtunknown</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etvariable, m_scopelocal, strfullidentifier, stridentifier, "", strdatatype, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_variable: <scope>" & xmlenc(m_scopelocal & "") & "</scope><fullidentifier>" & xmlenc(strfullidentifier) & "</fullidentifier><identifier>" & xmlenc(stridentifier) & "</identifier><datatype>" & xmlenc(strdatatype & "") & "</datatype>"</EXPRESSION>;
    <EXPRESSION>m_lngvars = m_lngvars + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_variable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_endfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_endfunction:"</EXPRESSION>;
    <EXPRESSION>pvt_object_endfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endobject(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninobject = false</EXPRESSION>;
    <EXPRESSION>m_strcurrobject = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endobject:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endobject = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_blnprivatefunc = false</EXPRESSION>;
    <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endfunction:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_endsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_endsub:"</EXPRESSION>;
    <EXPRESSION>pvt_object_endsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninfunc = false</EXPRESSION>;
    <EXPRESSION>m_blninsub = false</EXPRESSION>;
    <EXPRESSION>m_blnprivatefunc = false</EXPRESSION>;
    <EXPRESSION>m_strcurrfunc = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endsub:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_endproperty(String str_a) {
    String sys_result;
    String strresult;
    if (<EXPRESSION>m_blninfunc</EXPRESSION>) {
      <EXPRESSION>strresult = pvt_object_endfunction(str_a)</EXPRESSION>;
    } elseif (<EXPRESSION>m_blninsub</EXPRESSION>) {
      <EXPRESSION>strresult = pvt_object_endsub(str_a)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvt_object_endproperty = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_exitproperty(String str_a) {
    String sys_result;
    String strresult;
    if (<EXPRESSION>m_blninfunc</EXPRESSION>) {
      <EXPRESSION>strresult = pvt_object_exitfunction(str_a)</EXPRESSION>;
    } elseif (<EXPRESSION>m_blninsub</EXPRESSION>) {
      <EXPRESSION>strresult = pvt_object_exitsub(str_a)</EXPRESSION>;
    } else {
      <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvt_object_exitproperty = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_endtype(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>m_blninudt = false</EXPRESSION>;
    <EXPRESSION>m_blnprivateudt = false</EXPRESSION>;
    <EXPRESSION>m_strcurrudt = ""</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_endtype:"</EXPRESSION>;
    <EXPRESSION>pvt_global_endtype = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_udt_typevariable(String str_a) {
    String sys_result;
    int lngscope;
    String str;
    String stridentifier;
    String strfullidentifier;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(str, " ")</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    if (<EXPRESSION>m_blnprivateudt</EXPRESSION>) {
      <EXPRESSION>strfullidentifier = m_strcurrobject & "_" & m_strcurrudt & "_" & stridentifier</EXPRESSION>;
      <EXPRESSION>lngscope = m_scopemodule</EXPRESSION>;
    } else {
      <EXPRESSION>strfullidentifier = m_strcurrudt & "_" & stridentifier</EXPRESSION>;
      <EXPRESSION>lngscope = m_scopeglobal</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtelementadd(m_etudtvariable, lngscope, strfullidentifier, stridentifier, "", m_dtunknown, "")</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "udt_typevariable: <todo>" & xmlenc(str_a) & "</todo>"</EXPRESSION>;
    <EXPRESSION>pvt_udt_typevariable = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_exitfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_exitfunction:"</EXPRESSION>;
    <EXPRESSION>pvt_object_exitfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_exitfunction(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_exitfunction:"</EXPRESSION>;
    <EXPRESSION>pvt_global_exitfunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_object_exitsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "object_exitsub:"</EXPRESSION>;
    <EXPRESSION>pvt_object_exitsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_global_exitsub(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "global_exitsub:"</EXPRESSION>;
    <EXPRESSION>pvt_global_exitsub = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtstatementtok(String str_a, String strdelimeter_a) {
    String sys_result;
    boolean blnfound;
    boolean blninspace;
    boolean blninquote;
    int lngi;
    int lngnest;
    String strchar;
    String strnestcharlist;
    String strnestchar;
    String strresult;
    String strtest;
    <EXPRESSION>blnfound = false</EXPRESSION>;
    <EXPRESSION>blninspace = false</EXPRESSION>;
    <EXPRESSION>blninquote = false</EXPRESSION>;
    <EXPRESSION>lngnest = 0</EXPRESSION>;
    <EXPRESSION>lngi = 1</EXPRESSION>;

    while (<EXPRESSION>lngi <= len(str_a) and not blnfound</EXPRESSION>) {
      <EXPRESSION>strchar = mid$(str_a, lngi, 1)</EXPRESSION>;
      if (<EXPRESSION>lngnest = 0</EXPRESSION>) {
        if (<EXPRESSION>strchar = chr$(g_charquote)</EXPRESSION>) {
          <EXPRESSION>lngnest = lngnest + 1</EXPRESSION>;
        } else {
          <EXPRESSION>strtest = strtest & lcase$(strchar)</EXPRESSION>;
        }
      } else {
        if (<EXPRESSION>strchar = chr$(g_charquote)</EXPRESSION>) {
          <EXPRESSION>lngnest = lngnest - 1</EXPRESSION>;
        }
      }
      if (<EXPRESSION>lngnest > 0</EXPRESSION>) {
        if (<EXPRESSION>not blninquote</EXPRESSION>) {
          <EXPRESSION>strresult = strresult & strchar</EXPRESSION>;
        }
      } else {
        if (<EXPRESSION>strchar = " "</EXPRESSION>) {
          if (<EXPRESSION>blninspace</EXPRESSION>) {
            <EXPRESSION>strchar = ""</EXPRESSION>;
          } else {
            <EXPRESSION>blninspace = true</EXPRESSION>;
          }
        } else {
          <EXPRESSION>blninspace = false</EXPRESSION>;
        }
        if (<EXPRESSION>strchar = "'"</EXPRESSION>) {
          <EXPRESSION>blninquote = true</EXPRESSION>;
        }
        if (<EXPRESSION>not blninquote</EXPRESSION>) {
          <EXPRESSION>strresult = strresult & lcase$(strchar)</EXPRESSION>;
        }
      }
      if (<EXPRESSION>right$(strtest, len(strdelimeter_a)) = lcase$(strdelimeter_a)</EXPRESSION>) {
        <EXPRESSION>blnfound = true</EXPRESSION>;
      } else {
        <EXPRESSION>lngi = lngi + 1</EXPRESSION>;
      }
    }
    if (<EXPRESSION>blnfound</EXPRESSION>) {
      if (<EXPRESSION>lngi - len(strdelimeter_a) > 0</EXPRESSION>) {
        <EXPRESSION>strresult = left$(strresult, len(strresult) - len(strdelimeter_a))</EXPRESSION>;
      }
      <EXPRESSION>str_a = right$(str_a, len(str_a) - lngi)</EXPRESSION>;
    } else {
      <EXPRESSION>str_a = ""</EXPRESSION>;
    }
    <EXPRESSION>pvtstatementtok = trim$(strresult)</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparsestatement(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 1) = "'"</EXPRESSION>)) {
    } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>m_blninfunc or m_blninsub</EXPRESSION>)) {
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
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_expression: <expression>" & xmlenc(str_a) & "</expression>"</EXPRESSION>;
    <EXPRESSION>m_lngexprns = m_lngexprns + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_expression = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_selectcase(String str_a) {
    String sys_result;
    String str;
    String strexpression;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strexpression = str</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_selectcase: <expression>" & xmlenc(strexpression) & "</expression>"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngselectcases = m_lngselectcases + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_selectcase = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_caseelse(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_caseelse:"</EXPRESSION>;
    <EXPRESSION>pvt_function_caseelse = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_case(String str_a) {
    String sys_result;
    String str;
    String strexpression;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strexpression = str</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_case: <expression>" & xmlenc(strexpression) & "</expression>"</EXPRESSION>;
    <EXPRESSION>pvt_function_case = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_endselect(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_endselect:"</EXPRESSION>;
    <EXPRESSION>pvt_function_endselect = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_while(String str_a) {
    String sys_result;
    String str;
    String strexpression;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strexpression = str</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_while: <expression>" & xmlenc(strexpression) & "</expression>"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngwhiles = m_lngwhiles + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_while = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_wend(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_wend:"</EXPRESSION>;
    <EXPRESSION>pvt_function_wend = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_for(String str_a) {
    String sys_result;
    String str;
    String strfromexpression;
    String stridentifier;
    String strresult;
    String strstep;
    String strtoexpression;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = trim$(strtok(str, "="))</EXPRESSION>;
    <EXPRESSION>str = trim$(str)</EXPRESSION>;
    <EXPRESSION>stridentifier = strtok(stridentifier, "(")</EXPRESSION>;
    <EXPRESSION>strfromexpression = pvtstatementtok(str, " to ")</EXPRESSION>;
    <EXPRESSION>strtoexpression = pvtstatementtok(str, " step ")</EXPRESSION>;
    <EXPRESSION>strstep = str</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_for: <fromexpression>" & xmlenc(strfromexpression) & "</fromexpression><toexpression>" & xmlenc(strtoexpression) & "</toexpression><step>" & xmlenc(strstep) & "</step>"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngfors = m_lngfors + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_for = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_next(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_next:"</EXPRESSION>;
    <EXPRESSION>pvt_function_next = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_ifthen(String str_a) {
    String sys_result;
    String str;
    String strexpression;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strexpression = left$(str, len(str) - 5)</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_ifthen: <expression>" & xmlenc(strexpression) & "</expression>"</EXPRESSION>;
    <EXPRESSION>m_lngindent = m_lngindent + 1</EXPRESSION>;
    <EXPRESSION>m_lngifthens = m_lngifthens + 1</EXPRESSION>;
    <EXPRESSION>pvt_function_ifthen = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_elseifthen(String str_a) {
    String sys_result;
    String str;
    String strexpression;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>strexpression = left$(str, len(str) - 5)</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_elseifthen: <expression>" & xmlenc(strexpression) & "</expression>"</EXPRESSION>;
    <EXPRESSION>pvt_function_elseifthen = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_else(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_else:"</EXPRESSION>;
    <EXPRESSION>pvt_function_else = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_function_endif(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>m_lngindent = m_lngindent - 1</EXPRESSION>;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "function_endif:"</EXPRESSION>;
    <EXPRESSION>pvt_function_endif = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_condendif(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "condendif: " & str_a</EXPRESSION>;
    <EXPRESSION>m_blnskip = false</EXPRESSION>;
    <EXPRESSION>pvt_condendif = ""</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_condif(String str_a) {
    String sys_result;
    int lngcompvalue;
    int lngvalue;
    String str;
    String stridentifier;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "condif: " & str_a</EXPRESSION>;
    <EXPRESSION>m_lngcondifs = m_lngcondifs + 1</EXPRESSION>;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = trim$(strtok(str, " "))</EXPRESSION>;
    if (<EXPRESSION>instr(str, "=") > 0</EXPRESSION>) {
      <EXPRESSION>strtok str, "="</EXPRESSION>;
      <EXPRESSION>lngcompvalue = val(strtok(str, " then"))</EXPRESSION>;
    } else {
      <EXPRESSION>lngcompvalue = 1</EXPRESSION>;
    }
    if (<EXPRESSION>not pvtdefineget(stridentifier, lngvalue)</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    } else {
      if (<EXPRESSION>lngvalue <> lngcompvalue</EXPRESSION>) {
        <EXPRESSION>m_blnskip = true</EXPRESSION>;
      } else {
        <EXPRESSION>m_blnskip = false</EXPRESSION>;
      }
    }
    <EXPRESSION>pvt_condif = ""</EXPRESSION>;
    return (sys_result);
  }

  private String pvt_conddefine(String str_a) {
    String sys_result;
    int lngvalue;
    String str;
    String stridentifier;
    String strresult;
    <EXPRESSION>strresult = pvtspaces(m_lngindent) & "conddefine: " & str_a</EXPRESSION>;
    <EXPRESSION>m_lngcondifs = m_lngcondifs + 1</EXPRESSION>;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtok str, " "</EXPRESSION>;
    <EXPRESSION>stridentifier = trim$(strtok(str, "="))</EXPRESSION>;
    <EXPRESSION>lngvalue = val(str)</EXPRESSION>;
    if (<EXPRESSION>not pvtdefineadd(stridentifier, lngvalue)</EXPRESSION>) {
      <EXPRESSION>pvterror "invalid identifier '" & str_a & "'"</EXPRESSION>;
    }
    <EXPRESSION>pvt_conddefine = ""</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseobject(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if (<EXPRESSION>not m_blnskip</EXPRESSION>) {
      if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 25) = "private declare function "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 24) = "public declare function "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "private declare sub "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 19) = "public declare sub "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "private enum "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "public enum "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 14) = "private event "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "public event "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "private type "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "public type "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 19) = "private withevents "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 18) = "public withevents "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end object"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_endobject(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 17) = "private function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_function(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 16) = "public function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_function(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 9) = "function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_function(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 16) = "friend function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_function(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "private sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_sub(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 11) = "public sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_sub(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_sub(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 11) = "friend sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_sub(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 21) = "private property get "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyget(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "public property get "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyget(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "friend property get "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyget(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "property get "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyget(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 21) = "private property let "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertylet(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "public property let "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertylet(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "friend property let "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertylet(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "property let "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertylet(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 21) = "private property set "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyset(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "public property set "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyset(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "friend property set "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyset(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "property set "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_propertyset(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 14) = "private const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_constant(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_constant("private " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "private "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_variable(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 7) = "public "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_variable(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "dim "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_variable(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "#define "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_conddefine(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "#if "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "#end if"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 10) = "attribute "</EXPRESSION>)) {
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 11) = "implements "</EXPRESSION>)) {
      } else {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      }
    } else {
      if (<EXPRESSION>str = "#end if"</EXPRESSION>) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      }
    }
    <EXPRESSION>pvtparseobject = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseapp(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if (<EXPRESSION>not m_blnskip</EXPRESSION>) {
      if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 25) = "private declare function "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 24) = "public declare function "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 17) = "declare function "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 20) = "private declare sub "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 19) = "public declare sub "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "private enum "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "public enum "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 7) = "object "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_object(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 7) = "module "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_module(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end module"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_endmodule(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "private type "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_type(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "public type "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_type(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 5) = "type "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_type(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 17) = "private function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_function(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 16) = "public function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_function(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 9) = "function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_function(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 16) = "friend function "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_function(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "private sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_sub(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 11) = "public sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_sub(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_sub(m_scopeglobal, "public " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 11) = "friend sub "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_sub(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 14) = "private const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_constant(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 13) = "public const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_constant(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_constant(m_scopemodule, "private " & str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "private "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_variable(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 7) = "public "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_variable(m_scopeglobal, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "dim "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_variable(m_scopemodule, str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "#define "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_conddefine(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "#if "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "#end if"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 10) = "attribute "</EXPRESSION>)) {
      } else {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      }
    } else {
      if (<EXPRESSION>str = "#end if"</EXPRESSION>) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      }
    }
    <EXPRESSION>pvtparseapp = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparsefunction(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if (<EXPRESSION>not m_blnskip</EXPRESSION>) {
      if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "raise "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "redim "</EXPRESSION>)) {
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "const "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_constant(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end property"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_endproperty(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "exit property"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_object_exitproperty(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end function"</EXPRESSION>)) {
        if (<EXPRESSION>m_blninobject</EXPRESSION>) {
          <EXPRESSION>strresult = pvt_object_endfunction(str)</EXPRESSION>;
        } else {
          <EXPRESSION>strresult = pvt_global_endfunction(str)</EXPRESSION>;
        }
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "exit function"</EXPRESSION>)) {
        if (<EXPRESSION>m_blninobject</EXPRESSION>) {
          <EXPRESSION>strresult = pvt_object_exitfunction(str)</EXPRESSION>;
        } else {
          <EXPRESSION>strresult = pvt_global_exitfunction(str)</EXPRESSION>;
        }
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end sub"</EXPRESSION>)) {
        if (<EXPRESSION>m_blninobject</EXPRESSION>) {
          <EXPRESSION>strresult = pvt_object_endsub(str)</EXPRESSION>;
        } else {
          <EXPRESSION>strresult = pvt_global_endsub(str)</EXPRESSION>;
        }
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "exit sub"</EXPRESSION>)) {
        if (<EXPRESSION>m_blninobject</EXPRESSION>) {
          <EXPRESSION>strresult = pvt_object_exitsub(str)</EXPRESSION>;
        } else {
          <EXPRESSION>strresult = pvt_global_exitsub(str)</EXPRESSION>;
        }
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "dim "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_variable(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 12) = "select case "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_selectcase(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "case else"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_caseelse(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 5) = "case "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_case(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end select"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_endselect(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 6) = "while "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_while(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "wend"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_wend(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "for "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_for(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "next"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_next(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 5) = "next "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_next(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 3) = "if " and right$(str, 5) = " then"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_ifthen(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 3) = "if "</EXPRESSION>)) {
        <EXPRESSION>must come after the if then check above</EXPRESSION>;
        <EXPRESSION>pvterror "syntax error '" & str_a & "'"</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 7) = "elseif " and right$(str, 5) = " then"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_elseifthen(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "else"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_else(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end if"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_function_endif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "#define "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_conddefine(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "#if "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condif(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "#end if"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      } else {
        <EXPRESSION>function calls & assignments)</EXPRESSION>;
        <EXPRESSION>strresult = pvt_function_expression(str)</EXPRESSION>;
      }
    } else {
      if (<EXPRESSION>str = "#end if"</EXPRESSION>) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      }
    }
    <EXPRESSION>pvtparsefunction = strresult</EXPRESSION>;
    return (sys_result);
  }

  private String pvtparseudt(String str_a) {
    String sys_result;
    String str;
    String strresult;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    if (<EXPRESSION>not m_blnskip</EXPRESSION>) {
      if ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>str = "end type"</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_global_endtype(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 8) = "#define "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_conddefine(str)</EXPRESSION>;
      } elseif ((<EXPRESSION>true</EXPRESSION>) == (<EXPRESSION>left$(str, 4) = "#if "</EXPRESSION>)) {
        <EXPRESSION>strresult = pvt_condif(str)</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = pvt_udt_typevariable(str)</EXPRESSION>;
      }
    } else {
      if (<EXPRESSION>str = "#end if"</EXPRESSION>) {
        <EXPRESSION>strresult = pvt_condendif(str)</EXPRESSION>;
      }
    }
    <EXPRESSION>pvtparseudt = strresult</EXPRESSION>;
    return (sys_result);
  }

  private boolean pvtelementadd(int lngelementtype_a, int lngscope_a, String strfullidentifier_a, String stridentifier_a, String strparams_a, String strdatatype_a, String strvalue_a) {
    boolean sys_result;
    celement objelement;
    <EXPRESSION>set objelement = new celement</EXPRESSION>;
    <EXPRESSION>objelement.elementtype = lngelementtype_a</EXPRESSION>;
    <EXPRESSION>objelement.scope = lngscope_a</EXPRESSION>;
    <EXPRESSION>objelement.fullidentifier = strfullidentifier_a</EXPRESSION>;
    <EXPRESSION>objelement.identifier = stridentifier_a</EXPRESSION>;
    <EXPRESSION>objelement.parameters = strparams_a</EXPRESSION>;
    <EXPRESSION>objelement.datatype = strdatatype_a</EXPRESSION>;
    <EXPRESSION>objelement.value = strvalue_a</EXPRESSION>;
    <EXPRESSION>pvtelementadd = m_objelements.append(strfullidentifier_a, objelement)</EXPRESSION>;
    <EXPRESSION>set objelement = nothing</EXPRESSION>;
    return (sys_result);
  }

  private boolean pvtdefineadd(String stridentifier_a, int lngvalue_a) {
    boolean sys_result;
    celement objdefine;
    <EXPRESSION>set objdefine = new celement</EXPRESSION>;
    <EXPRESSION>objdefine.elementtype = 0</EXPRESSION>;
    <EXPRESSION>objdefine.scope = 0</EXPRESSION>;
    <EXPRESSION>objdefine.fullidentifier = stridentifier_a</EXPRESSION>;
    <EXPRESSION>objdefine.identifier = stridentifier_a</EXPRESSION>;
    <EXPRESSION>objdefine.parameters = ""</EXPRESSION>;
    <EXPRESSION>objdefine.datatype = ""</EXPRESSION>;
    <EXPRESSION>objdefine.value = lngvalue_a & ""</EXPRESSION>;
    <EXPRESSION>pvtdefineadd = m_objdefines.append(stridentifier_a, objdefine)</EXPRESSION>;
    <EXPRESSION>set objdefine = nothing</EXPRESSION>;
    return (sys_result);
  }

  private boolean pvtdefineget(String stridentifier_a, int lngvalue_a) {
    boolean sys_result;
    boolean blnresult;
    celement objdefine;
    <EXPRESSION>set objdefine = new celement</EXPRESSION>;
    if (<EXPRESSION>m_objdefines.exists(stridentifier_a)</EXPRESSION>) {
      <EXPRESSION>set objdefine = m_objdefines.read(stridentifier_a)</EXPRESSION>;
      <EXPRESSION>lngvalue_a = val(objdefine.value)</EXPRESSION>;
      <EXPRESSION>blnresult = true</EXPRESSION>;
    } else {
      <EXPRESSION>blnresult = false</EXPRESSION>;
    }
    <EXPRESSION>set objdefine = nothing</EXPRESSION>;
    <EXPRESSION>pvtdefineget = blnresult</EXPRESSION>;
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
    <EXPRESSION>debug.print "writing statistics."</EXPRESSION>;
    if (<EXPRESSION>objfile.fopen(app.path & "\" & m_strprojectname & "\stats.log", g_fmappend)</EXPRESSION>) {
      <EXPRESSION>objfile.fwrite vbcrlf & "PARSER STATS:" & vbcrlf & vbcrlf</EXPRESSION>;
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
      <EXPRESSION>objfile.fwrite "directive count   " & m_lngcondifs & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "expression count  " & m_lngexprns & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "for loop count    " & m_lngfors & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "select case count " & m_lngselectcases & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fwrite "while loop count  " & m_lngwhiles & vbcrlf</EXPRESSION>;
      <EXPRESSION>objfile.fclose</EXPRESSION>;
    }
    <EXPRESSION>set objfile = nothing</EXPRESSION>;
  }

  public void elementssave() {
    cfile objfile;
    celement objelement;
    cnode objnode;
    String stroutput;
    <EXPRESSION>set objfile = new cfile</EXPRESSION>;
    <EXPRESSION>debug.print "writing elements."</EXPRESSION>;
    if (<EXPRESSION>m_objelements.list.size > 0</EXPRESSION>) {
      if (<EXPRESSION>objfile.fopen(app.path & "\" & m_strprojectname & "\elements.xml", g_fmappend)</EXPRESSION>) {
        <EXPRESSION>objfile.fwrite "<elements>" & vbcrlf</EXPRESSION>;
        <EXPRESSION>m_objelements.list.firstnode</EXPRESSION>;
        <EXPRESSION>set objnode = m_objelements.list.read</EXPRESSION>;

        while (<EXPRESSION>not objnode is nothing</EXPRESSION>) {
          <EXPRESSION>set objelement = objnode.item</EXPRESSION>;
          <EXPRESSION>stroutput = "<element>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<fullidentifier>" & xmlenc(objelement.fullidentifier) & "</fullidentifier>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<identifier>" & xmlenc(objelement.identifier) & "</identifier>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<scope>" & xmlenc(objelement.scope & "") & "</scope>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<type>" & xmlenc(objelement.elementtype & "") & "</type>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<parameters>" & xmlenc(objelement.parameters & "") & "</parameters>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "<datatype>" & xmlenc(objelement.datatype & "") & "</datatype>"</EXPRESSION>;
          <EXPRESSION>stroutput = stroutput & "</element>"</EXPRESSION>;
          <EXPRESSION>objfile.fwrite stroutput & vbcrlf</EXPRESSION>;
          <EXPRESSION>m_objelements.list.nextnode</EXPRESSION>;
          <EXPRESSION>set objnode = m_objelements.list.read</EXPRESSION>;
        }
        <EXPRESSION>set objnode = nothing</EXPRESSION>;
        <EXPRESSION>objfile.fwrite "</elements>" & vbcrlf</EXPRESSION>;
        <EXPRESSION>objfile.fclose</EXPRESSION>;
      }
    }
    <EXPRESSION>set objfile = nothing</EXPRESSION>;
  }

  private void class_initialize() {
    <EXPRESSION>initialise</EXPRESSION>;
  }

  public void class_terminate() {
    <EXPRESSION>set m_objelements = nothing</EXPRESSION>;
  }

  public void cparser() {
    <EXPRESSION>class_initialize</EXPRESSION>;
  }

  protected void finalize() {
    <EXPRESSION>class_terminate</EXPRESSION>;
  }
}
