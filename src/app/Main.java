package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  // 全角27文字で折り返し
  private static final int MAX = 27 * 2;
  private static final String ACTOR = "【ハロルド】";

  private static final String SEP = System.lineSeparator();
  private static final char ALPHANUMERIC_CHARACTER = '\u007e';
  private static final char BACK_SLASH_CHARACTER   = '\u00a5';
  private static final char TILDA_CHARACTER        = '\u203e';

  // TEST CODE
  public static void main(String... args) {//{{{

    File test1 = new File("./input/test1.txt");
    showFormedText(test1);

    System.out.println("");

    File test2 = new File("./input/test2.txt");
    showFormedText(test2);

  }//}}}

  private static void showFormedText(File file) {//{{{

    Path path = file.toPath();
    try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

      String text = br.lines().collect(Collectors.joining());
      text = text.replaceAll(SEP, "");

      String formedText = createFormedText(text, MAX);
      System.out.println("折り返し後のテキスト = " + SEP + formedText);

      System.out.println("");

      String addedText = createAddedActorNameText(formedText, ACTOR);
      System.out.println("アクター名追加後のテキスト = " + SEP + addedText);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}

  private static String createFormedText(String text, int max) {//{{{

    char[] chars = text.toCharArray();

    int count = 0;
    StringBuilder sb = new StringBuilder(chars.length);
    for (char ch : chars) {

      int length = charLength(ch);
      count += length;
      sb.append(ch);
      if (count % max == 0) sb.append(SEP);

    }

    return sb.toString();

  }//}}}

  private static int charLength(char ch) {//{{{

    if (
           ( ch <= ALPHANUMERIC_CHARACTER     )
        || ( ch == BACK_SLASH_CHARACTER       )
        || ( ch == TILDA_CHARACTER            )
        || ( '\uff61' <= ch && ch <= '\uff9f' ) // 半角カナ
       )
      return 1;
    else
      return 2;

  }//}}}

  private static String createAddedActorNameText(String text, String actor) {//{{{

    String[] array = text.split(SEP);
    int lineCount = 0;

    StringBuilder sb = new StringBuilder(text.toCharArray().length);
    for (int i=0; i<array.length; i++) {

      if (i % 3 == 0) {
        sb.append(actor);
        sb.append(SEP);
      }

      String line = array[i];
      sb.append(line);
      sb.append(SEP);

    }

    return sb.toString();

  }//}}}

}
