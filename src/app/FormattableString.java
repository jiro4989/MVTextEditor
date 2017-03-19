package app;

import static app.CharUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * テキストフォーマットメソッドを持つ不変ラッパーStringクラス。
 */
public class FormattableString {

  // 単語単位で区切られた整形対象の文字列リスト
  private final List<String> wordList;

  // 改行
  private final boolean returnOption;
  private final int returnSize;

  // インデント
  private final boolean indentOption;
  private final int indentSize;
  private final String indent;

  // 括弧で囲む
  private final boolean bracketsOption;
  private final Brackets brackets;

  // アクター名を表示
  private final boolean actorNameOption;
  private final String actorName;
  private final ActorNameType actorNameType;

  // 定数//{{{

  private static final String SEP = System.lineSeparator();
  private static final char ALPHANUMERIC_CHARACTER = '\u007e';
  private static final char BACK_SLASH_CHARACTER   = '\u00a5';
  private static final char TILDA_CHARACTER        = '\u203e';

  //}}}

  // builder

  public static class Builder {//{{{

    private final List<String> wordList;

    private boolean returnOption = false;
    private int returnSize = 27 * 2;

    private boolean indentOption = false;
    private int indentSize = 0;

    private boolean bracketsOption = false;
    private Brackets brackets;

    private boolean actorNameOption = false;
    private String actorName = null;
    private ActorNameType actorNameType;

    public Builder(String str)        { wordList = splitToWord(str); }
    public Builder(StringBuilder sb)  { this(sb.toString());         }
    public Builder(List<String> list) { wordList = list;             }

    public Builder(FormattableString fs, StringBuilder sb) {
      this(fs, sb.toString());
    }

    public Builder(FormattableString fs, String str) {//{{{

      wordList        = splitToWord(str);
      returnOption    = fs.returnOption;
      returnSize      = fs.returnSize;
      indentOption    = fs.indentOption;
      indentSize      = fs.indentSize;
      bracketsOption  = fs.bracketsOption;
      brackets        = fs.brackets;
      actorNameOption = fs.actorNameOption;
      actorName       = fs.actorName;
      actorNameType   = fs.actorNameType;

    }//}}}

    public Builder returnSize(int size) {//{{{
      this.returnOption = true;
      this.returnSize = size;
      return this;
    }//}}}

    public Builder indent(int indentSize) {//{{{
      this.indentOption = true;
      this.indentSize = indentSize;
      return this;
    }//}}}

    public Builder brackets(Brackets b) {//{{{
      this.bracketsOption = true;
      this.brackets = b;
      return this;
    }//}}}

    public Builder actorName(String name) {//{{{
      this.actorNameOption = true;
      this.actorName = name;
      return this;
    }//}}}

    public Builder actorNameType(ActorNameType type) {//{{{
      this.actorNameOption = true;
      this.actorNameType = type;
      return this;
    }//}}}

    public FormattableString build() {//{{{

      if (actorNameOption && actorName == null)
        throw new NullPointerException(
            String.format(
              "actorNameOptionを有効にしている時はactorNameを設定しなければなりません。"
              + "- actorNameOption = %s, actorName = %s, ActorNameType = %s"
              , actorNameOption, actorName, actorNameType)
            );

      if (actorNameOption && actorNameType == null)
        throw new NullPointerException(
            String.format(
              "actorNameOptionを有効にしている時はactorNameTypeを設定しなければなりません。"
              + "- actorNameOption = %s, actorName = %s, ActorNameType = %s"
              , actorNameOption, actorName, actorNameType)
            );

      return new FormattableString(this);
    }//}}}

  }//}}}

  // constructors

  private FormattableString(Builder builder) {//{{{

    this.wordList        = builder.wordList;
    this.returnOption    = builder.returnOption;
    this.returnSize      = builder.returnSize;
    this.indentOption    = builder.indentOption;
    this.indentSize      = builder.indentSize;
    this.bracketsOption  = builder.bracketsOption;
    this.brackets        = builder.brackets;
    this.actorNameOption = builder.actorNameOption;
    this.actorName       = builder.actorName;
    this.actorNameType   = builder.actorNameType;

    StringBuilder sb = new StringBuilder(indentSize);
    for (int i=0; i<indentSize; i++) {
      sb.append(" ");
    }
    this.indent = sb.toString();

  }//}}}

  // methods

  public FormattableString format() {//{{{

    return formatCarriageReturn().formatActorName();

  }//}}}

  private FormattableString formatCarriageReturn() {//{{{

    StringBuilder sb = new StringBuilder();
    if (returnOption) {

      putStartBrackets();

      int count = 0;
      for (String word : wordList) {

        int length = stringLength(word);
        count += length;

        if (returnSize < count) {

          sb.append(SEP);
          count = 0;

          if (word.startsWith(" ") || word.startsWith("　")) {
            word = (new StringBuilder(word)).deleteCharAt(0).toString();
            length = stringLength(word);
          }

          if (indentOption) {
            sb.append(indent);
            count += stringLength(indent);
          }

          sb.append(word);
          count += length;
          continue;

        }

        sb.append(word);

      }

      putEndBrackets(sb);

      return new FormattableString.Builder(this, sb).build();

    } else {

      return new FormattableString.Builder(this, toString()).build();

    }

  }//}}}

  private FormattableString formatActorName() {//{{{

    if (actorNameOption) {

      String text = toString();
      String[] array = text.split(SEP);
      int lineCount = 0;
      int actorNameCount = 0;

      StringBuilder sb = new StringBuilder(text.toCharArray().length);
      for (int i=0; i<array.length; i++) {

        if (i % 3 == 0) {
          actorNameCount = insertActorName(sb, actorNameCount);
        }

        String line = array[i];
        sb.append(line);
        sb.append(SEP);

      }

      return new FormattableString.Builder(this, sb).build();

    } else {

      return new FormattableString.Builder(this, toString()).build();

    }

  }//}}}

  private int insertActorName(StringBuilder sb, int count) {//{{{

    if (actorNameType == ActorNameType.TOP_ONLY) {
      if (0 < count) return ++count;
    }

    sb.append(actorName);
    sb.append(SEP);

    return ++count;
  }//}}}

  public static Optional<String> readTextFrom(File file) {//{{{

    String str = null;
    Path path = file.toPath();
    try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

      str = br.lines()
        .map(s -> {
          char ch = s.charAt(s.length()-1);
          return isAlphabet(ch) ? s + " " : s;
        })
      .collect(Collectors.joining());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(str);

  }//}}}

  // private methods

  private void putStartBrackets() {//{{{

    if (indentOption) {
      if (bracketsOption) {

        StringBuilder indentSb = new StringBuilder(indent);
        int len = stringLength(brackets.START);
        indentSb.delete(0, len);
        indentSb.insert(0, brackets.START);
        String newIndent = indentSb.toString();
        wordList.set(0, newIndent + wordList.get(0));

      } else {
        wordList.set(0, indent + wordList.get(0));
      }
    }

  }//}}}

  private void putEndBrackets(StringBuilder sb) {//{{{

    if (bracketsOption) {

      String str      = sb.toString();
      String[] array  = str.split(SEP);
      String lastLine = array[array.length-1];

      int len    =       stringLength(lastLine);
      int newLen = len + stringLength(brackets.END);

      if (returnSize < newLen) {
        sb.append(SEP);
      }

      sb.append(brackets.END);

    }

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

  private static List<String> splitToWord(String text) {//{{{

    char[] chars = text.toCharArray();
    String[] array = text.split("");

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

  private static List<String> splitToWordList(List<String> list) {//{{{

    List<String> newList = new ArrayList<>(list.size());
    list.stream().forEach(str -> {

      splitToWord(str).stream().forEach(s -> {
        newList.add(s);
      });

    });

    return newList;

  }//}}}

  @Override
  public String toString() {//{{{
    return wordList.stream().collect(Collectors.joining());
  }//}}}

}
