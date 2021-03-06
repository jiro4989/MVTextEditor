package com.jiro4989.mvte.table;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;

public class MapInfos {
  public final int id;
  public boolean expanded = false;
  public final String name;
  public final int order;
  public final int parentId;
  public double scrollX = 977.3;
  public double scrollY = 641.3;

  public MapInfos(int id, String name, int order, int parentId) { // {{{
    this.id = id;
    this.name = name;
    this.order = order;
    this.parentId = parentId;
  } // }}}

  public MapInfos(
      int id,
      boolean expanded,
      String name,
      int order,
      int parentId,
      double scrollX,
      double scrollY) { // {{{
    this.id = id;
    this.expanded = expanded;
    this.name = name;
    this.order = order;
    this.parentId = parentId;
    this.scrollX = scrollX;
    this.scrollY = scrollY;
  } // }}}

  public static List<MapInfos> readFile(File jsonFile) throws IOException { // {{{
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(jsonFile);
    List<MapInfos> list = new ArrayList<>();
    for (int i = 0; i < root.size(); i++) {
      JsonNode child = root.get(i);
      if (child == null || child.isNull()) {
        list.add(null);
        continue;
      }

      int anid = child.get("id").asInt();
      boolean anExpanded = child.get("expanded").asBoolean();
      String aName = child.get("name").asText();
      int anOrder = child.get("order").asInt();
      int aParentId = child.get("parentId").asInt();
      double aScrollX = child.get("scrollX").asDouble();
      double aScrollY = child.get("scrollY").asDouble();
      list.add(new MapInfos(anid, anExpanded, aName, anOrder, aParentId, aScrollX, aScrollY));
    }
    return list;
  } // }}}

  @Override
  public String toString() { // {{{
    return String.format(
        "MapInfos: { id = %d, expanded = %s, name = %s, order = %d, parentId = %d, scrollX = %f, scrollY = %f };",
        id, expanded, name, order, parentId, scrollX, scrollY);
  } // }}}
}
