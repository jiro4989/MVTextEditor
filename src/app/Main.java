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
  private static final int RETURN_SIZE   = 27 * 2;
  private static final int INDENT_SIZE   = 2;
  private static final Brackets BRACKETS = Brackets.TYPE1;
  private static final String ACTOR      = "【ハロルド】";

  // test code

  public static void main(String... args) {//{{{

    show(new File("./input/test1.txt"));
    show(new File("./input/test2.txt"));

  }//}}}

  // test methods

  private static void show(File file) {//{{{

    FormattableString.readTextFrom(file).ifPresent(text -> {

      FormattableString fs = new FormattableString.Builder(text)
        .returnSize(RETURN_SIZE)
        .indent(INDENT_SIZE)
        .brackets(BRACKETS)
        .actorName(ACTOR)
        .actorNameType(ActorNameType.TOP_ONLY)
        .build();
      showText(fs);

    });

  }//}}}

  private static void showText(FormattableString fs) {//{{{

    showLine();
    System.out.println("変換前のテキスト");
    showLine();

    String newString = fs
      .toString();

    System.out.println(newString);
    System.out.println("");

    showLine();
    System.out.println("変換後のテキスト");
    showLine();

    newString = fs
      .format()
      .toString();

    System.out.println(newString);
    System.out.println("");

  }//}}}

  private static void showLine() {//{{{

    StringBuilder sb = new StringBuilder();
    for (int i=0; i<RETURN_SIZE; i++) {
      sb.append('*');
    }
    System.out.println(sb.toString());

  }//}}}

}
