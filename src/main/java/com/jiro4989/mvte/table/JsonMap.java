package com.jiro4989.mvte.table;

import java.util.*;

public class JsonMap {

  public final boolean autoplayBgm = false;
  public final boolean autoplayBgs = false;
  public final String battleback1Name = "";
  public final String battleback2Name = "";
  public final Map<String, Object> bgm = new HashMap<String, Object>() {{
    put("name"   , "");
    put("pan"    , 0);
    put("pitch"  , 100);
    put("volume" , 90);
  }};
  public final Map<String, Object> bgs = new HashMap<String, Object>() {{
    put("name"   , "");
    put("pan"    , 0);
    put("pitch"  , 100);
    put("volume" , 90);
  }};
  public final boolean disableDashing = false;
  public final String displayName = "";
  public final List<String> encounterList = new ArrayList<>();
  public final int encounterStep = 30;
  public final int height = 1;
  public final String note = "";
  public final boolean parallaxLoopX = false;
  public final boolean parallaxLoopY = false;
  public final String parallaxName = "";
  public final boolean parallaxShow = true;
  public final int parallaxSx = 0;
  public final int parallaxSy = 0;
  public final int scrollType = 0;
  public final boolean specifyBattleback = false;
  public final int tilesetId = 1;
  public final int width = 1;
  public final int[] data = {0, 0, 0, 0, 0, 0};
  public final List<Events> events;

  public JsonMap(int id, List<TextDB> dbList) {
    events = new ArrayList<>();
    events.add(null);
    events.add(new Events(id, dbList));
  }

}

