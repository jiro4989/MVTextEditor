package com.jiro4989.mvte.table;

import java.util.*;

public class Events {

  public final int id;
  public final String name;
  public final String note = "";
  public final List<Pages> pages;
  public final int x = 0;
  public final int y = 0;

  public Events(int id, List<TextDB> dbList) { // {{{
    this.id = id;
    this.name = String.format("EV%03d", id);
    this.pages = new ArrayList<>();
    this.pages.add(new Pages(dbList));
  } // }}}
}
