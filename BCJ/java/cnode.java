package BCJ;

public class cnode {
  private static final String m_name = "cNode";
  public String key;
  public cnode previousnode;
  public cnode nextnode;
  public Object item;

  public void class_terminate() {
    <EXPRESSION>key = ""</EXPRESSION>;
    <EXPRESSION>set previousnode = nothing</EXPRESSION>;
    <EXPRESSION>set nextnode = nothing</EXPRESSION>;
    <EXPRESSION>set item = nothing</EXPRESSION>;
  }

  protected void finalize() {
    <EXPRESSION>class_terminate</EXPRESSION>;
  }
}
