package app.table;

import static util.Texts.SEP;

import app.Main;

import java.util.*;

public class JsonMap {
  public final int id;
  public final String name;
  public final String note = "";
  public final List<Pages> pages;
  public final int x;
  public final int y;

  public JsonMap(int id, int x, int y, List<TextDB> dbList) {//{{{
    this.id = id;
    this.x = x;
    this.y = y;
    this.name = String.format("EV%03d", id);
    this.pages = new ArrayList<>();
    this.pages.add(new Pages(dbList));
  }//}}}

}

