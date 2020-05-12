package app;

import jiro.java.lang.Brackets;
import jiro.java.lang.FormattableText;
import jiro.java.util.MyProperties;

import java.io.File;

public class FormatOptions {

  public final boolean returnOption;
  public final int returnSize;
  public final boolean indentOption;
  public final int indentSize;
  public final String indent;
  public final boolean bracketsOption;
  public final Brackets brackets;
  public final boolean joiningOption;

  public FormatOptions(File file) {//{{{
    MyProperties mp = new MyProperties(file);
    mp.load();

    returnOption       = mp.getBooleanValue("returnOption"   , "false");
    returnSize         = mp.getIntegerValue("returnSize"     , "54");
    indentOption       = mp.getBooleanValue("indentOption"   , "false");
    indentSize         = mp.getIntegerValue("indentSize"     , "0");
    indent             = FormattableText . createIndentString(indentSize);
    bracketsOption     = mp.getBooleanValue("bracketsOption" , "false");
    String bracketsStr = mp.getProperty("brackets").orElse("TYPE1");
    brackets           = Brackets. fromString(bracketsStr).orElse(Brackets . TYPE1);
    joiningOption      = mp.getBooleanValue("joiningOption"  , "false");
  }//}}}

}
