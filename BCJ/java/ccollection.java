package BCJ;

public class ccollection {
  private static final String m_name = "cCollection";
  private clist m_objlist;
  public boolean dictionary;

  public boolean append(String strkey_a, Object objitem_a) {
    boolean sys_result;
    boolean blnresult;
    <EXPRESSION>blnresult = true</EXPRESSION>;
    if (<EXPRESSION>dictionary</EXPRESSION>) {
      <EXPRESSION>remove strkey_a</EXPRESSION>;
    }
    if (<EXPRESSION>exists(strkey_a)</EXPRESSION>) {
      <EXPRESSION>blnresult = false</EXPRESSION>;
    } else {
      <EXPRESSION>m_objlist.append strkey_a, objitem_a</EXPRESSION>;
      <EXPRESSION>blnresult = true</EXPRESSION>;
    }
    <EXPRESSION>append = blnresult</EXPRESSION>;
    return (sys_result);
  }

  public void clear() {
    <EXPRESSION>m_objlist.clear</EXPRESSION>;
  }

  public clist list() {
    clist sys_result;
    <EXPRESSION>set list = m_objlist</EXPRESSION>;
    return (sys_result);
  }

  public void remove(String strkey_a) {
    <EXPRESSION>m_objlist.firstnode</EXPRESSION>;
    <EXPRESSION>m_objlist.find strkey_a</EXPRESSION>;
    if (<EXPRESSION>not m_objlist.read is nothing</EXPRESSION>) {
      <EXPRESSION>m_objlist.remove</EXPRESSION>;
    }
  }

  public boolean exists(String strkey_a) {
    boolean sys_result;
    <EXPRESSION>m_objlist.firstnode</EXPRESSION>;
    <EXPRESSION>m_objlist.find strkey_a</EXPRESSION>;
    <EXPRESSION>exists = not m_objlist.read is nothing</EXPRESSION>;
    return (sys_result);
  }

  public Object read(String strkey_a) {
    Object sys_result;
    <EXPRESSION>m_objlist.firstnode</EXPRESSION>;
    <EXPRESSION>m_objlist.find strkey_a</EXPRESSION>;
    if (<EXPRESSION>not m_objlist.read is nothing</EXPRESSION>) {
      <EXPRESSION>set read = m_objlist.read.item</EXPRESSION>;
    }
    return (sys_result);
  }

  public int size() {
    int sys_result;
    <EXPRESSION>size = m_objlist.size</EXPRESSION>;
    return (sys_result);
  }

  private void class_initialize() {
    <EXPRESSION>set m_objlist = new clist</EXPRESSION>;
    <EXPRESSION>dictionary = false</EXPRESSION>;
  }

  public void class_terminate() {
    <EXPRESSION>set m_objlist = nothing</EXPRESSION>;
  }

  public void ccollection() {
    <EXPRESSION>class_initialize</EXPRESSION>;
  }

  protected void finalize() {
    <EXPRESSION>class_terminate</EXPRESSION>;
  }
}
