package BCJ;

public class mfilesystem {
  private static final String m_name = "mFilesystem";
  public static final short g_fmread = 0;
  public static final short g_fmappend = 1;
  public static final short g_fmbinary = 2;

  public void fsdirectorycreate(String strpath_a) {
    if (<EXPRESSION>len(dir(strpath_a, vbdirectory)) = 0</EXPRESSION>) {
      <EXPRESSION>mkdir strpath_a</EXPRESSION>;
    }
  }
}
