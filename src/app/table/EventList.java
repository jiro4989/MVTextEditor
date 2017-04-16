package app.table;

import app.Main;

public class EventList {
  public final int code;
  public final int indent;
  public final Object[] parameters;

  public EventList(int code, int indent, TextDB db) {//{{{
    this.code   = code;
    this.indent = indent;

    String[] array = db.iconProperty().get().split(":");
    String fileName = "";
    int index = 0;

    if (1 < array.length) {
      fileName = array[0];
      index = Integer.parseInt(array[1]);
    }

    String bg   = db.backgroundProperty().get();
    String pos  = db.positionProperty()  .get();

    parameters = new Object[4];
    parameters[0] = fileName;
    parameters[1] = index;
    parameters[2] = getIndex("background", bg);
    parameters[3] = getIndex("position"  , pos);
  }//}}}

  public EventList(int code, int indent, String text) {//{{{
    this.code     = code;
    this.indent   = indent;
    parameters    = new Object[1];
    parameters[0] = text;
  }//}}}

  private static int getIndex(String word, String value) {//{{{
    int index = 0;
    String[] array = Main.resources.getString(word).split(",");
    for (String s : array) {
      if (s.equals(value)) {
        return index;
      }
      index++;
    }
    return 0;
  }//}}}

}

