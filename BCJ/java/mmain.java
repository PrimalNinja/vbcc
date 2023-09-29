package BCJ;

public class mmain {
  private static final String m_name = "mMain";

  public void main() {
    boolean blncompile;
    boolean blnparse;
    int lngerrors;
    cjava objcompiler;
    cparser objparser;
    String strfiles;
    String strprojectname;
    String strprojectpath;
    <EXPRESSION>set objparser = new cparser</EXPRESSION>;
    <EXPRESSION>lngerrors = 0</EXPRESSION>;
    <EXPRESSION>blncompile = true</EXPRESSION>;
    <EXPRESSION>blnparse = true</EXPRESSION>;
    <EXPRESSION>strprojectname = "BCJ"</EXPRESSION>;
    <EXPRESSION>strprojectpath = "Z:\vbcc\pBC5.vbp"</EXPRESSION>;
    if (<EXPRESSION>blnparse</EXPRESSION>) {
      if (<EXPRESSION>len(strprojectpath) > 0</EXPRESSION>) {
        <EXPRESSION>lngerrors = objparser.parsevb6(strprojectname, strprojectpath)</EXPRESSION>;
      } else {
        <EXPRESSION>lngerrors = objparser.parse(strprojectname, strfiles)</EXPRESSION>;
      }
      <EXPRESSION>objparser.statisticssave</EXPRESSION>;
      <EXPRESSION>objparser.elementssave</EXPRESSION>;
    }
    if (<EXPRESSION>blncompile</EXPRESSION>) {
      if (<EXPRESSION>lngerrors = 0</EXPRESSION>) {
        <EXPRESSION>set objcompiler = new cjava</EXPRESSION>;
        <EXPRESSION>lngerrors = objcompiler.compile(strprojectname)</EXPRESSION>;
        <EXPRESSION>objcompiler.statisticssave</EXPRESSION>;
      }
    }
    <EXPRESSION>set objparser = nothing</EXPRESSION>;
    <EXPRESSION>set objcompiler = nothing</EXPRESSION>;
  }
}
