package com.jiro4989.mvte.table;

import com.jiro4989.mvte.Main;
import java.io.File;
import java.util.Arrays;

public class EventList {
  public final int code;
  public final int indent;
  public final Object[] parameters;

  public EventList(int code, int indent, TextDB db) { // {{{
    this.code = code;
    this.indent = indent;

    String[] array = db.iconProperty().get().split(":");
    String fileName = "";
    int index = 0;

    if (1 < array.length) {
      String abspath = String.join(":", Arrays.copyOf(array, array.length - 1));
      String name = new File(abspath).getName();
      fileName = getPreffix(name);
      index = Integer.parseInt(array[array.length - 1]);
    }

    String bg = db.backgroundProperty().get();
    String pos = db.positionProperty().get();

    parameters = new Object[4];
    parameters[0] = fileName;
    parameters[1] = index;
    parameters[2] = getIndex("background", bg);
    parameters[3] = getIndex("position", pos);
  } // }}}

  public EventList(int code, int indent, String text) { // {{{
    this.code = code;
    this.indent = indent;
    parameters = new Object[1];
    parameters[0] = text;
  } // }}}

  private static int getIndex(String word, String value) { // {{{
    int index = 0;
    String[] array = Main.resources.getString(word).split(",");
    for (String s : array) {
      if (s.equals(value)) {
        return index;
      }
      index++;
    }
    return 0;
  } // }}}

  private static final String getPreffix(String fileName) { // {{{
    if (fileName == null) return "";

    int index = fileName.lastIndexOf(".");
    if (index != -1) return fileName.substring(0, index);

    return fileName;
  } // }}}
}
