package BCJ;

public class cfaststring {
  private static final String m_name = "cFastString";
  private String m_strbuffer;
  private int m_lngstart;
  private int m_lngsize;
  private boolean m_blnempty;
  private int m_lngtkstart;
  private int m_lngtksize;
  private String m_strdelimeter;
  private int m_lnggrowsize;
  private static final short m_defgrowsize = 1024;
  private static final String m_defdelimeter = "~";

  public void append(String strvalue_a) {
    int lngfreespace;
    if (<EXPRESSION>len(strvalue_a) > 0</EXPRESSION>) {
      if (<EXPRESSION>m_blnempty</EXPRESSION>) {
        <EXPRESSION>lngfreespace = len(m_strbuffer)</EXPRESSION>;
      } else {
        <EXPRESSION>lngfreespace = len(m_strbuffer) - m_lngstart - m_lngsize + 1</EXPRESSION>;
      }
      if (<EXPRESSION>len(strvalue_a) > lngfreespace</EXPRESSION>) {
        <EXPRESSION>m_strbuffer = m_strbuffer & space$(m_lnggrowsize + len(strvalue_a))</EXPRESSION>;
      }
      if (<EXPRESSION>m_blnempty</EXPRESSION>) {
        <EXPRESSION>m_blnempty = false</EXPRESSION>;
        <EXPRESSION>m_lngstart = 1</EXPRESSION>;
        <EXPRESSION>m_lngsize = len(strvalue_a)</EXPRESSION>;
        <EXPRESSION>mid$(m_strbuffer, m_lngstart, m_lngsize) = strvalue_a</EXPRESSION>;
      } else {
        <EXPRESSION>mid$(m_strbuffer, m_lngstart + m_lngsize, len(strvalue_a)) = strvalue_a</EXPRESSION>;
        <EXPRESSION>m_lngsize = m_lngsize + len(strvalue_a)</EXPRESSION>;
      }
    }
  }

  public void clear() {
    <EXPRESSION>m_strbuffer = ""</EXPRESSION>;
    <EXPRESSION>m_lngstart = 0</EXPRESSION>;
    <EXPRESSION>m_lngsize = 0</EXPRESSION>;
    <EXPRESSION>m_blnempty = true</EXPRESSION>;
  }

  public void reset() {
    if (<EXPRESSION>m_lngtkstart > 0</EXPRESSION>) {
      <EXPRESSION>m_lngstart = m_lngtkstart</EXPRESSION>;
      <EXPRESSION>m_lngsize = m_lngtksize</EXPRESSION>;
      <EXPRESSION>m_lngtkstart = 0</EXPRESSION>;
      <EXPRESSION>m_lngtksize = 0</EXPRESSION>;
    }
  }

  public int tokencount() {
    int sys_result;
    int lngresult;
    <EXPRESSION>reset</EXPRESSION>;
    <EXPRESSION>lngresult = 0</EXPRESSION>;

    while (<EXPRESSION>size > 0</EXPRESSION>) {
      <EXPRESSION>token</EXPRESSION>;
      <EXPRESSION>lngresult = lngresult + 1</EXPRESSION>;
    }
    <EXPRESSION>tokencount = lngresult</EXPRESSION>;
    return (sys_result);
  }

  public void delimeter(String strvalue_a) {
    <EXPRESSION>m_strdelimeter = strvalue_a</EXPRESSION>;
  }

  public int size() {
    int sys_result;
    <EXPRESSION>size = m_lngsize</EXPRESSION>;
    return (sys_result);
  }

  public void fwrite(String strvalue_a) {
    <EXPRESSION>m_blnempty = false</EXPRESSION>;
    <EXPRESSION>m_lngstart = 1</EXPRESSION>;
    <EXPRESSION>m_lngsize = len(strvalue_a)</EXPRESSION>;
    <EXPRESSION>m_strbuffer = strvalue_a</EXPRESSION>;
  }

  public String fread() {
    String sys_result;
    if (<EXPRESSION>m_blnempty</EXPRESSION>) {
      <EXPRESSION>fread = ""</EXPRESSION>;
    } else {
      if (<EXPRESSION>m_lngsize > 0</EXPRESSION>) {
        <EXPRESSION>fread = mid$(m_strbuffer, m_lngstart, m_lngsize)</EXPRESSION>;
      }
    }
    return (sys_result);
  }

  public String token() {
    String sys_result;
    int lngposition;
    String strresult;
    String strtoken;
    if (<EXPRESSION>m_lngtkstart = 0</EXPRESSION>) {
      <EXPRESSION>m_lngtkstart = m_lngstart</EXPRESSION>;
      <EXPRESSION>m_lngtksize = m_lngsize</EXPRESSION>;
    }
    if (<EXPRESSION>m_lngsize > 0</EXPRESSION>) {
      <EXPRESSION>lngposition = instr(m_lngstart, m_strbuffer, m_strdelimeter)</EXPRESSION>;
      if (<EXPRESSION>lngposition > 0</EXPRESSION>) {
        <EXPRESSION>lngposition = lngposition - m_lngstart + 1</EXPRESSION>;
        if (<EXPRESSION>lngposition > m_lngsize</EXPRESSION>) {
          <EXPRESSION>lngposition = 0</EXPRESSION>;
        }
      }
      if (<EXPRESSION>lngposition > 0</EXPRESSION>) {
        <EXPRESSION>strtoken = mid$(m_strbuffer, m_lngstart, lngposition - 1)</EXPRESSION>;
        <EXPRESSION>strresult = strtoken</EXPRESSION>;
        <EXPRESSION>m_lngstart = m_lngstart + len(strtoken) + len(m_strdelimeter)</EXPRESSION>;
        <EXPRESSION>m_lngsize = m_lngsize - len(strtoken) - len(m_strdelimeter)</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = mid$(m_strbuffer, m_lngstart, m_lngsize)</EXPRESSION>;
        <EXPRESSION>m_lngstart = 0</EXPRESSION>;
        <EXPRESSION>m_lngsize = 0</EXPRESSION>;
      }
    }
    <EXPRESSION>token = strresult</EXPRESSION>;
    return (sys_result);
  }

  private void class_initialize() {
    <EXPRESSION>m_strdelimeter = m_defdelimeter</EXPRESSION>;
    <EXPRESSION>m_lnggrowsize = m_defgrowsize</EXPRESSION>;
    <EXPRESSION>clear</EXPRESSION>;
  }

  public void cfaststring() {
    <EXPRESSION>class_initialize</EXPRESSION>;
  }
}
