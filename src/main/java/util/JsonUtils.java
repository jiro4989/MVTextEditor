package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jiro4989.mvte.table.*;
import java.io.*;
import java.util.*;

public class JsonUtils {
  private JsonUtils() {}

  public static void writeValue(File outFile, Object data) throws FileNotFoundException, IOException {//{{{
    FileOutputStream fos = new FileOutputStream(outFile);
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new OutputStreamWriter(fos, "UTF-8"), data);
    fos.close();
  }//}}}

  public static void updateMapInfos(File file, String mapName, int id) throws IOException {//{{{
    List<MapInfos> list = MapInfos.readFile(file);
    int order = 0;
    int parentId = 0;
    for (MapInfos mi : list) {
      if (mi != null) {
        order = Math.max(order, mi.order);
        parentId = mi.parentId;
      }
    }
    order++;
    parentId = parentId == 0 ? 0 : 1;
    String mn = "MVTextEditor" + id;
    if (id < list.size())
      list.set(id, new MapInfos(id, mn, order, parentId));
    else
      list.add(new MapInfos(id, mn, order, parentId));
    JsonUtils.writeValue(file, list);
  }//}}}

}
