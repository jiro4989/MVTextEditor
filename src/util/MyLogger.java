package util;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
  private static final Logger LOG = Logger.getLogger("Logs");

  public static void log(Exception ex) {//{{{
    log("例外発生", ex);
  }//}}}

  public static void log(String errorText, Exception ex) {//{{{
    // エラー発生時の日付のファイル名で保存する。
    LocalDateTime date = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    String today = date.format(formatter);

    try {
      // 第二引数をtrueにするとファイル末尾追加モードでエラーを出力する。
      FileHandler fh = new FileHandler(today + ".log", true);
      fh.setFormatter(new SimpleFormatter());
      LOG.addHandler(fh);

      LOG.log(Level.WARNING, errorText, ex);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

}
