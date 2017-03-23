package util;

import static util.Texts.*;

import java.io.File;

public final class InitUtils {
  private InitUtils() {}

  public static void mkPropDirs() {//{{{
    File mainProp   = new File(MAIN_PROPERTIES);
    File propDir = mainProp.getParentFile();
    propDir.mkdirs();
  }//}}}

}
