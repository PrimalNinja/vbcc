package BCJ;

public class mfaststring {
  private static final String m_name = "mFastString";

  public String replace(String stroldstring_a, String stroldvalue_a, String strnewvalue_a) {
    String sys_result;
    short intpos;
    String strresult;
    String strtemp;
    <EXPRESSION>strtemp = stroldstring_a</EXPRESSION>;
    <EXPRESSION>strresult = ""</EXPRESSION>;

    while (<EXPRESSION>(len(strtemp) > 0)</EXPRESSION>) {
      <EXPRESSION>intpos = instr(strtemp, stroldvalue_a)</EXPRESSION>;
      if (<EXPRESSION>(intpos > 0)</EXPRESSION>) {
        <EXPRESSION>strresult = strresult + left(strtemp, intpos - 1) + strnewvalue_a</EXPRESSION>;
        <EXPRESSION>strtemp = right$(strtemp, len(strtemp) - intpos - len(stroldvalue_a) + 1)</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = strresult + strtemp</EXPRESSION>;
        <EXPRESSION>strtemp = ""</EXPRESSION>;
      }
    }
    <EXPRESSION>replace = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String strreverse(String str_a) {
    String sys_result;
    int lngi;
    String strresult;
    <EXPRESSION>strresult = ""</EXPRESSION>;

    for (<EXPRESSION>1</EXPRESSION>;<EXPRESSION>len(str_a)</EXPRESSION>;<EXPRESSION></EXPRESSION>) {
      <EXPRESSION>strresult = mid$(str_a, lngi, 1) & strresult</EXPRESSION>;
    }
    <EXPRESSION>strreverse = strresult</EXPRESSION>;
    return (sys_result);
  }

  public int strtokcount(String strlist_a, String strdelimeter_a) {
    int sys_result;
    cfaststring objlist;
    <EXPRESSION>set objlist = new cfaststring</EXPRESSION>;
    <EXPRESSION>objlist.fwrite strlist_a</EXPRESSION>;
    <EXPRESSION>objlist.delimeter strdelimeter_a</EXPRESSION>;
    <EXPRESSION>strtokcount = objlist.tokencount</EXPRESSION>;
    <EXPRESSION>set objlist = nothing</EXPRESSION>;
    return (sys_result);
  }

  public String tokstr(String strlist_a, String strvalue_a, String strdelimeter_a) {
    String sys_result;
    String strresult;
    if (<EXPRESSION>len(strlist_a) = 0</EXPRESSION>) {
      <EXPRESSION>strresult = strvalue_a</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = strlist_a & strdelimeter_a & strvalue_a</EXPRESSION>;
    }
    <EXPRESSION>tokstr = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String strtok(String strlist_a, String strdelimeter_a) {
    String sys_result;
    int lngpos;
    String strresult;
    <EXPRESSION>lngpos = instr(strlist_a, strdelimeter_a)</EXPRESSION>;
    if (<EXPRESSION>lngpos > 0</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strlist_a, lngpos - 1)</EXPRESSION>;
      <EXPRESSION>strlist_a = right$(strlist_a, len(strlist_a) - lngpos - len(strdelimeter_a) + 1)</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = strlist_a</EXPRESSION>;
      <EXPRESSION>strlist_a = ""</EXPRESSION>;
    }
    <EXPRESSION>strtok = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String strrtrunc(String strvalue_a, String strtruncatevalue_a) {
    String sys_result;
    String strresult;
    <EXPRESSION>strresult = strvalue_a</EXPRESSION>;
    if (<EXPRESSION>len(strtruncatevalue_a) < len(strvalue_a)</EXPRESSION>) {
      if (<EXPRESSION>right$(strvalue_a, len(strtruncatevalue_a)) = strtruncatevalue_a</EXPRESSION>) {
        <EXPRESSION>strresult = left$(strvalue_a, len(strvalue_a) - len(strtruncatevalue_a))</EXPRESSION>;
      }
    }
    <EXPRESSION>strrtrunc = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String strpad(String strvalue_a, int lnglength_a) {
    String sys_result;
    String strresult;
    if (<EXPRESSION>len(strvalue_a) > lnglength_a</EXPRESSION>) {
      <EXPRESSION>strresult = left$(strvalue_a, lnglength_a)</EXPRESSION>;
    } else {
      <EXPRESSION>strresult = strvalue_a & space$(lnglength_a - len(strvalue_a))</EXPRESSION>;
    }
    <EXPRESSION>strpad = strresult</EXPRESSION>;
    return (sys_result);
  }
}
