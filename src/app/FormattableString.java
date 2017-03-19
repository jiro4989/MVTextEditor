package app;

import static app.CharUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * テキストフォーマットメソッドを持つ不変ラッパーStringクラス。
 */
public class FormattableString {

  private final String string;
  private final List<String> wordList;

  private static final boolean indentOption = true;

  private static final String INDENT = "    ";
  private static final String SEP = System.lineSeparator();
  private static final char ALPHANUMERIC_CHARACTER = '\u007e';
  private static final char BACK_SLASH_CHARACTER   = '\u00a5';
  private static final char TILDA_CHARACTER        = '\u203e';

  // Constructors

  public FormattableString(String str) {//{{{
    string = str;
    wordList = splitToWord(string);
  }//}}}

  public FormattableString(StringBuilder sb) {//{{{
    this(sb.toString());
  }//}}}

  public FormattableString(File file) {//{{{

    String str = "Couldn't read text file.";
    Path path = file.toPath();
    try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
      str = br.lines().collect(Collectors.joining());
    } catch (IOException e) {
      e.printStackTrace();
    }

    string = str;
    wordList = splitToWord(string);

  }//}}}

  // Methods

  public FormattableString indent(int indentSize) {//{{{

    //StringBuilder sb = new StringBuilder(string.length());
    //for (int i=0; i<count; i++) {
    //  sb.append(" ");
    //}
    //String indent = sb.toString();
    return null;

  }//}}}

  public FormattableString carriageReturn(int max) {//{{{

    char[] chars = string.toCharArray();
    int count = 0;
    StringBuilder sb = new StringBuilder(chars.length);

    for (String word : wordList) {

      int length = stringLength(word);
      count += length;

      if (max < count) {

        sb.append(SEP);
        sb.append(word);
        count = length;
        continue;

      }

      sb.append(word);
    }

    return new FormattableString(sb);

  }//}}}

  private int charLength(char ch) {//{{{

    if (
        ( ch <= ALPHANUMERIC_CHARACTER        )
        || ( ch == BACK_SLASH_CHARACTER       )
        || ( ch == TILDA_CHARACTER            )
        || ( '\uff61' <= ch && ch <= '\uff9f' ) // 半角カナ
       )
      return 1;
    else
      return 2;

  }//}}}

  private int stringLength(String str) {//{{{

    int count = 0;
    for (char ch : str.toCharArray()) {
      count += charLength(ch);
    }
    return count;

  }//}}}

  private List<String> splitToWord(String text) {//{{{

    char[] chars = text.toCharArray();

    List<String> list = new ArrayList<>();
    StringBuilder sb = new StringBuilder();

    for (char ch : chars) {

      if (isAlphabet(ch)) {
        sb.append(ch);
        continue;
      }

      if (isSeparator(ch) || isWhiteSpace(ch)) {
        sb.append(ch);
        list.add(sb.toString());
        sb.setLength(0);
        continue;
      }

      sb.append(ch);
      list.add(sb.toString());
      sb.setLength(0);

    }

    return list;

  }//}}}

  @Override
  public String toString() { return string; }

}
