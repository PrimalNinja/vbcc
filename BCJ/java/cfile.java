package BCJ;

public class cfile {
  private static final String m_name = "cFile";
  private boolean m_blnopened;
  private short m_intfile;
  private int m_lngmode;
  private int m_lngtoread;
  private cfaststring m_objlines;
  private static final short m_maxpagesz = 10240;

  public void fclose() {
    if (<EXPRESSION>m_blnopened</EXPRESSION>) {
      <EXPRESSION>m_objlines.clear</EXPRESSION>;
      <EXPRESSION>close #m_intfile</EXPRESSION>;
      <EXPRESSION>m_blnopened = false</EXPRESSION>;
    }
  }

  public String fget(int lngrecordid_a, int lngrecordlength_a) {
    String sys_result;
    String sresult;
    if (<EXPRESSION>m_lngmode = g_fmbinary</EXPRESSION>) {
      <EXPRESSION>sresult = space$(lngrecordlength_a)</EXPRESSION>;
      <EXPRESSION>get #m_intfile, (lngrecordid_a - 1) * lngrecordlength_a + 1, sresult</EXPRESSION>;
    }
    <EXPRESSION>fget = sresult</EXPRESSION>;
    return (sys_result);
  }

  public void fput(int lngrecordid_a, String strrecord_a) {
    if (<EXPRESSION>m_lngmode = g_fmbinary</EXPRESSION>) {
      <EXPRESSION>put #m_intfile, (lngrecordid_a - 1) * len(strrecord_a) + 1, strrecord_a</EXPRESSION>;
    }
  }

  public boolean fopen(String strpath_a, int lngmode_a) {
    boolean sys_result;
    <EXPRESSION>fclose</EXPRESSION>;
    <EXPRESSION>m_lngmode = lngmode_a</EXPRESSION>;
    if ((<EXPRESSION>m_lngmode</EXPRESSION>) == (<EXPRESSION>g_fmappend</EXPRESSION>)) {
      <EXPRESSION>m_intfile = freefile</EXPRESSION>;
      <EXPRESSION>open strpath_a for append as #m_intfile</EXPRESSION>;
      <EXPRESSION>m_blnopened = true</EXPRESSION>;
    } elseif ((<EXPRESSION>m_lngmode</EXPRESSION>) == (<EXPRESSION>g_fmread</EXPRESSION>)) {
      <EXPRESSION>m_objlines.delimeter vblf</EXPRESSION>;
      <EXPRESSION>m_lngtoread = filelen(strpath_a)</EXPRESSION>;
      <EXPRESSION>m_intfile = freefile</EXPRESSION>;
      <EXPRESSION>open strpath_a for binary as #m_intfile</EXPRESSION>;
      <EXPRESSION>m_blnopened = true</EXPRESSION>;
    } elseif ((<EXPRESSION>m_lngmode</EXPRESSION>) == (<EXPRESSION>g_fmbinary</EXPRESSION>)) {
      <EXPRESSION>m_intfile = freefile</EXPRESSION>;
      <EXPRESSION>open strpath_a for binary as #m_intfile</EXPRESSION>;
      <EXPRESSION>m_blnopened = true</EXPRESSION>;
    }
    <EXPRESSION>fopen = m_blnopened</EXPRESSION>;
    return (sys_result);
  }

  public void fwrite(String strvalue_a) {
    <EXPRESSION>print #m_intfile, strvalue_a;</EXPRESSION>;
  }

  public boolean get endoffile() {
    boolean sys_result;
    boolean blnresult;
    <EXPRESSION>blnresult = true</EXPRESSION>;
    if (<EXPRESSION>m_objlines.size > 0</EXPRESSION>) {
      <EXPRESSION>blnresult = false</EXPRESSION>;
    } else {
      <EXPRESSION>blnresult = (m_lngtoread = 0)</EXPRESSION>;
    }
    <EXPRESSION>endoffile = blnresult</EXPRESSION>;
    return (sys_result);
  }

  public String fload() {
    String sys_result;
    boolean blnread;
    int lngchunksize;
    String strresult;
    String strtemp;
    <EXPRESSION>blnread = false</EXPRESSION>;
    if (<EXPRESSION>m_objlines.size > 0</EXPRESSION>) {
      if (<EXPRESSION>instr(m_objlines.fread, vblf) > 0 or m_lngtoread = 0</EXPRESSION>) {
        <EXPRESSION>strresult = m_objlines.fread</EXPRESSION>;
      } else {
        <EXPRESSION>blnread = true</EXPRESSION>;
      }
    } else {
      <EXPRESSION>blnread = true</EXPRESSION>;
    }
    if (<EXPRESSION>blnread</EXPRESSION>) {
      <EXPRESSION>lngchunksize = m_lngtoread</EXPRESSION>;
      if (<EXPRESSION>lngchunksize > 0</EXPRESSION>) {
        <EXPRESSION>strtemp = string$(lngchunksize, chr$(g_charnull))</EXPRESSION>;
        <EXPRESSION>get #m_intfile, , strtemp</EXPRESSION>;
        <EXPRESSION>m_lngtoread = m_lngtoread - lngchunksize</EXPRESSION>;
        <EXPRESSION>strtemp = replace(strtemp, vbcrlf, vblf)</EXPRESSION>;
        if (<EXPRESSION>right$(strtemp, 1) = chr$(g_charcr)</EXPRESSION>) {
          <EXPRESSION>strtemp = strrtrunc(strtemp, chr$(g_charcr))</EXPRESSION>;
        }
        <EXPRESSION>m_objlines.fwrite m_objlines.fread & strtemp</EXPRESSION>;
        <EXPRESSION>strresult = fload()</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = ""</EXPRESSION>;
      }
    }
    <EXPRESSION>fload = strresult</EXPRESSION>;
    return (sys_result);
  }

  public String fread() {
    String sys_result;
    boolean blnread;
    int lngchunksize;
    String strresult;
    String strtemp;
    <EXPRESSION>blnread = false</EXPRESSION>;
    if (<EXPRESSION>m_objlines.size > 0</EXPRESSION>) {
      if (<EXPRESSION>instr(m_objlines.fread, vblf) > 0 or m_lngtoread = 0</EXPRESSION>) {
        <EXPRESSION>strresult = m_objlines.token</EXPRESSION>;
      } else {
        <EXPRESSION>blnread = true</EXPRESSION>;
      }
    } else {
      <EXPRESSION>blnread = true</EXPRESSION>;
    }
    if (<EXPRESSION>blnread</EXPRESSION>) {
      if (<EXPRESSION>m_maxpagesz > m_lngtoread</EXPRESSION>) {
        <EXPRESSION>lngchunksize = m_lngtoread</EXPRESSION>;
      } else {
        <EXPRESSION>lngchunksize = m_maxpagesz</EXPRESSION>;
      }
      if (<EXPRESSION>lngchunksize > 0</EXPRESSION>) {
        <EXPRESSION>strtemp = string$(lngchunksize, chr$(g_charnull))</EXPRESSION>;
        <EXPRESSION>get #m_intfile, , strtemp</EXPRESSION>;
        <EXPRESSION>m_lngtoread = m_lngtoread - lngchunksize</EXPRESSION>;
        <EXPRESSION>strtemp = replace(strtemp, vbcrlf, vblf)</EXPRESSION>;
        if (<EXPRESSION>right$(strtemp, 1) = chr$(g_charcr)</EXPRESSION>) {
          <EXPRESSION>strtemp = strrtrunc(strtemp, chr$(g_charcr))</EXPRESSION>;
        }
        <EXPRESSION>m_objlines.fwrite m_objlines.fread & strtemp</EXPRESSION>;
        <EXPRESSION>strresult = fread()</EXPRESSION>;
      } else {
        <EXPRESSION>strresult = ""</EXPRESSION>;
      }
    }
    <EXPRESSION>fread = strresult</EXPRESSION>;
    return (sys_result);
  }

  private void class_initialize() {
    <EXPRESSION>set m_objlines = new cfaststring</EXPRESSION>;
    <EXPRESSION>m_blnopened = false</EXPRESSION>;
  }

  public void class_terminate() {
    if (<EXPRESSION>m_blnopened</EXPRESSION>) {
      <EXPRESSION>fclose</EXPRESSION>;
    }
    <EXPRESSION>set m_objlines = nothing</EXPRESSION>;
  }

  public void cfile() {
    <EXPRESSION>class_initialize</EXPRESSION>;
  }

  protected void finalize() {
    <EXPRESSION>class_terminate</EXPRESSION>;
  }
}
