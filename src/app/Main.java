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

  // TEST CODE
  public static void main(String... args) {//{{{

    show(new File("./input/test1.txt"));
    show(new File("./input/test2.txt"));

  }//}}}

  private static void show(File file) {//{{{

    FormattableString.readTextFrom(file).ifPresent(text -> {

      FormattableString fs = new FormattableString.Builder(text)
        .returnSize(RETURN_SIZE)
        .indent(INDENT_SIZE)
        .brackets(BRACKETS)
        .build();
      showText(fs);

    });

  }//}}}

  private static void showText(FormattableString fs) {//{{{

    System.out.println("************************************************************");
    System.out.println("変換前のテキスト");
    System.out.println("************************************************************");
    String newString = fs
      .toString();
    System.out.println(newString);
    System.out.println("");

    System.out.println("************************************************************");
    System.out.println("変換後のテキスト");
    System.out.println("************************************************************");
    newString = fs
      .carriageReturn()
      .actorName(ACTOR)
      .toString();
    System.out.println(newString);
    System.out.println("");

  }//}}}

}
