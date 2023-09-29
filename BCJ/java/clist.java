package BCJ;

public class clist {
  private static final String m_name = "cList";
  private cnode m_objfirstnode;
  private cnode m_objlastnode;
  private cnode m_objcurrentnode;
  private int m_lngsize;

  public void append(String strkey_a, Object objitem_a) {
    cnode objnode;
    <EXPRESSION>set objnode = new cnode</EXPRESSION>;
    if (<EXPRESSION>m_objlastnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objfirstnode = objnode</EXPRESSION>;
    } else {
      <EXPRESSION>set m_objlastnode.nextnode = objnode</EXPRESSION>;
      <EXPRESSION>set objnode.previousnode = m_objlastnode</EXPRESSION>;
    }
    <EXPRESSION>set m_objlastnode = objnode</EXPRESSION>;
    <EXPRESSION>set objnode.item = objitem_a</EXPRESSION>;
    <EXPRESSION>objnode.key = strkey_a</EXPRESSION>;
    if (<EXPRESSION>m_objcurrentnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objcurrentnode = objnode</EXPRESSION>;
    }
    <EXPRESSION>m_lngsize = m_lngsize + 1</EXPRESSION>;
    <EXPRESSION>set objnode = nothing</EXPRESSION>;
  }

  public void clear() {
    <EXPRESSION>firstnode</EXPRESSION>;

    while (<EXPRESSION>not read is nothing</EXPRESSION>) {
      <EXPRESSION>firstnode</EXPRESSION>;
      <EXPRESSION>remove</EXPRESSION>;
    }
  }

  public void nextnode() {
    if (<EXPRESSION>not m_objcurrentnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objcurrentnode = m_objcurrentnode.nextnode</EXPRESSION>;
    }
  }

  public void previousnode() {
    if (<EXPRESSION>not m_objcurrentnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objcurrentnode = m_objcurrentnode.previousnode</EXPRESSION>;
    }
  }

  public cnode read() {
    cnode sys_result;
    <EXPRESSION>set read = m_objcurrentnode</EXPRESSION>;
    return (sys_result);
  }

  public void remove() {
    cnode objnode;
    <EXPRESSION>set objnode = m_objcurrentnode</EXPRESSION>;
    if (<EXPRESSION>not objnode.previousnode is nothing</EXPRESSION>) {
      <EXPRESSION>set objnode.previousnode.nextnode = objnode.nextnode</EXPRESSION>;
    } else {
      <EXPRESSION>set m_objfirstnode = objnode.nextnode</EXPRESSION>;
    }
    if (<EXPRESSION>not objnode.nextnode is nothing</EXPRESSION>) {
      <EXPRESSION>set objnode.nextnode.previousnode = objnode.previousnode</EXPRESSION>;
    } else {
      <EXPRESSION>set m_objlastnode = objnode.previousnode</EXPRESSION>;
    }
    <EXPRESSION>set m_objcurrentnode = objnode.nextnode</EXPRESSION>;
    <EXPRESSION>m_lngsize = m_lngsize - 1</EXPRESSION>;
  }

  public int size() {
    int sys_result;
    <EXPRESSION>size = m_lngsize</EXPRESSION>;
    return (sys_result);
  }

  public void firstnode() {
    <EXPRESSION>set m_objcurrentnode = m_objfirstnode</EXPRESSION>;
  }

  public void lastnode() {
    <EXPRESSION>set m_objcurrentnode = m_objlastnode</EXPRESSION>;
  }

  public void prepend(String strkey_a, Object objitem_a) {
    cnode objnode;
    <EXPRESSION>set objnode = new cnode</EXPRESSION>;
    if (<EXPRESSION>m_objfirstnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objlastnode = objnode</EXPRESSION>;
    } else {
      <EXPRESSION>set m_objfirstnode.previousnode = objnode</EXPRESSION>;
      <EXPRESSION>set objnode.nextnode = m_objfirstnode</EXPRESSION>;
    }
    <EXPRESSION>set m_objfirstnode = objnode</EXPRESSION>;
    <EXPRESSION>set objnode.item = objitem_a</EXPRESSION>;
    <EXPRESSION>objnode.key = strkey_a</EXPRESSION>;
    if (<EXPRESSION>m_objcurrentnode is nothing</EXPRESSION>) {
      <EXPRESSION>set m_objcurrentnode = objnode</EXPRESSION>;
    }
    <EXPRESSION>m_lngsize = m_lngsize + 1</EXPRESSION>;
    <EXPRESSION>set objnode = nothing</EXPRESSION>;
  }

  public void find(String strkey_a) {
    boolean blnfound;
    <EXPRESSION>blnfound = false</EXPRESSION>;

    while (<EXPRESSION>not read is nothing and not blnfound</EXPRESSION>) {
      if (<EXPRESSION>read.key = strkey_a</EXPRESSION>) {
        <EXPRESSION>blnfound = true</EXPRESSION>;
      } else {
        <EXPRESSION>nextnode</EXPRESSION>;
      }
    }
  }
}
