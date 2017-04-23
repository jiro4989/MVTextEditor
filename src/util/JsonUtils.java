package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.table.*;
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

  public static void updateMapInfos(File file, String mapName) throws IOException {//{{{
    List<MapInfos> list = MapInfos.readFile(file);
    int id = 0;
    int order = 0;
    int parentId = 0;
    for (MapInfos mi : list) {
      if (mi != null) {
        id = mi.id;
        order = Math.max(order, mi.order);
        parentId = mi.parentId;
      }
    }
    id++;
    order++;
    parentId = parentId == 0 ? 0 : 1;
    list.add(new MapInfos(id, mapName.substring(0, mapName.length()-5), order, parentId));
    JsonUtils.writeValue(file, list);
  }//}}}

}
