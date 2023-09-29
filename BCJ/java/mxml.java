package BCJ;

public class mxml {
  private static final String m_name = "mXML";

  public String xmldec(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = str_a</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "&lt;", "<")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "&gt;", ">")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "&amp;", "&")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "'", "'")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, """, chr$(34))</EXPRESSION>;
    <EXPRESSION>xmldec = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String xmlenc(String str_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = str_a</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "&", "&amp;")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "<", "&lt;")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, ">", "&gt;")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, "'", "'")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, chr$(34), """)</EXPRESSION>;
    <EXPRESSION>xmlenc = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String xmltokappend(String str_a, String strsource_a, String strtoken_a) {
    String sys_result;
    String strvalue;
    String strresult;
    <EXPRESSION>strresult = str_a</EXPRESSION>;
    <EXPRESSION>strvalue = trim$(xmltokget(strsource_a, strtoken_a))</EXPRESSION>;
    if (<EXPRESSION>len(strvalue) > 0</EXPRESSION>) {
      <EXPRESSION>strresult = strresult & "<" & strtoken_a & ">" & xmlenc(strvalue) & "</" & strtoken_a & ">"</EXPRESSION>;
    }
    <EXPRESSION>xmltokappend = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String xmltok(String str_a, String strtoken_a) {
    String sys_result;
    boolean blnfound;
    boolean blnremove;
    int lngpos;
    int lngposn;
    String strresult;
    String strtoken;
    String strtokenn;
    String strtokene;
    <EXPRESSION>blnfound = false</EXPRESSION>;
    <EXPRESSION>blnremove = false</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;
    <EXPRESSION>strtoken = "<" & strtoken_a & ">"</EXPRESSION>;
    <EXPRESSION>strtokenn = "<" & strtoken_a & " />"</EXPRESSION>;
    <EXPRESSION>strtokene = "</" & strtoken_a & ">"</EXPRESSION>;
    <EXPRESSION>lngpos = instr(str_a, strtoken)</EXPRESSION>;
    <EXPRESSION>lngposn = instr(str_a, strtokenn)</EXPRESSION>;
    if (<EXPRESSION>lngpos > 0 and lngposn = 0</EXPRESSION>) {
      <EXPRESSION>blnfound = true</EXPRESSION>;
    } elseif (<EXPRESSION>lngpos > 0 and lngposn > 0 and lngpos < lngposn</EXPRESSION>) {
      <EXPRESSION>blnfound = true</EXPRESSION>;
    } elseif (<EXPRESSION>lngpos = 0 and lngposn > 0</EXPRESSION>) {
      <EXPRESSION>blnremove = true</EXPRESSION>;
    } elseif (<EXPRESSION>lngpos > 0 and lngposn > 0 and lngpos > lngposn</EXPRESSION>) {
      <EXPRESSION>blnremove = true</EXPRESSION>;
    }
    if (<EXPRESSION>blnfound</EXPRESSION>) {
      <EXPRESSION>strtok str_a, strtoken</EXPRESSION>;
      <EXPRESSION>strresult = xmldec(trim$(strtok(str_a, strtokene)))</EXPRESSION>;
    }
    if (<EXPRESSION>blnremove</EXPRESSION>) {
      <EXPRESSION>strtok str_a, strtokenn</EXPRESSION>;
    }
    if (<EXPRESSION>not blnfound and not blnremove</EXPRESSION>) {
      <EXPRESSION>str_a = ""</EXPRESSION>;
    }
    <EXPRESSION>strresult = replace(strresult, vbcr, "")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, vblf, "")</EXPRESSION>;
    <EXPRESSION>strresult = replace(strresult, chr$(9), "")</EXPRESSION>;
    <EXPRESSION>strresult = trim$(strresult)</EXPRESSION>;
    <EXPRESSION>xmltok = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String xmltokget(String str_a, String strtoken_a) {
    String sys_result;
    String str;
    String strtoken;
    <EXPRESSION>str = str_a</EXPRESSION>;
    <EXPRESSION>strtoken = strtoken_a</EXPRESSION>;
    <EXPRESSION>xmltokget = xmltok(str, strtoken)</EXPRESSION>;
    return (sys_result);
  }
}
