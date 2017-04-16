package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class JsonUtils {
  private JsonUtils() {}

  public static void writeValue(File outFile, Object data) throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(outFile);
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new OutputStreamWriter(fos, "UTF-8"), data);
    fos.close();
  }

}
