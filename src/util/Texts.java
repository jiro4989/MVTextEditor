package util;

import java.nio.IntBuffer;
import javafx.scene.image.WritablePixelFormat;

public class Texts {

  private static final String JAR_NAME = "mvte";

  public static final String SEP = System.lineSeparator();

  public static final String PROP_DIR      = "properties/" + JAR_NAME;
  public static final String TITLE         = "MV Text Editor";
  public static final String VERSION       = " ver 1.0.0";
  public static final String TITLE_VERSION = TITLE + VERSION;
  public static final String BASIC_CSS     = "/app/res/css/basic.css";
  public static final String APP_ICON      = "/app/res/img/app_icon.png";

  public static final String FORMAT_PROPERTIES      = PROP_DIR + "/format.xml";
  public static final String LOGS_PROPERTIES        = PROP_DIR + "/logs.xml";
  public static final String MAIN_PROPERTIES        = PROP_DIR + "/main.xml";
  public static final String PREFERENCES_PROPERTIES = PROP_DIR + "/preferences.xml";

  public static final String KEY_PROJECT     = "project";
  public static final String IMG_DIR_PATH    = "img/faces";
  public static final String IMG_WINDOW_PATH = "img/system/Window.png";

  public static final String VAR_FILE_PATH    = "data/System.json";
  public static final String ACTORS_FILE_PATH = "data/Actors.json";
  public static final String KEY_VAR          = "variables";

  public static final int COLUMN = 4;
  public static final int WIDTH  = 144;
  public static final int HEIGHT = 144;
  public static final int COLOR_TILE_SIZE = 192 / 16 * 2;
  public static final int COLOR_PICKER_COLUMN_SIZE = 16;

  public static final String DEFAULT_COLOR = "\\c[0]";

  // 画像処理の書式(ARGB)
  public static final WritablePixelFormat<IntBuffer> FORMAT = WritablePixelFormat.getIntArgbInstance();

  public static String createFilePath(String[] array) {//{{{
    String[] newArray = new String[array.length-1];
    for (int i=0; i<array.length-1; i++) {
      newArray[i] = array[i];
    }
    return String.join(":", newArray);
  }//}}}

}
