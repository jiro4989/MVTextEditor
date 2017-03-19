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

  private final List<String> wordList;
  private final boolean returnOption;
  private final int returnSize;
  private final boolean indentOption;
  private final int indentSize;
  private final boolean bracketsOption;
  private final Brackets brackets;
  private final String indent;

  private static final String SEP = System.lineSeparator();
  private static final char ALPHANUMERIC_CHARACTER = '\u007e';
  private static final char BACK_SLASH_CHARACTER   = '\u00a5';
  private static final char TILDA_CHARACTER        = '\u203e';

  // Builder

  public static class Builder {//{{{

    private final List<String> wordList;
    private boolean returnOption = false;
    private int returnSize = 54;
    private boolean indentOption = false;
    private int indentSize = 0;
    private boolean bracketsOption = false;
    private Brackets brackets;

    public Builder(String str)        { wordList = splitToWord(str); }
    public Builder(StringBuilder sb)  { this(sb.toString());         }
    public Builder(List<String> list) { wordList = list;             }

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

    public FormattableString build() {//{{{
      return new FormattableString(this);
    }//}}}

  }//}}}

  // Constructors

  private FormattableString(Builder builder) {//{{{

    this.wordList       = builder.wordList;
    this.returnOption   = builder.returnOption;
    this.returnSize     = builder.returnSize;
    this.indentOption   = builder.indentOption;
    this.indentSize     = builder.indentSize;
    this.bracketsOption = builder.bracketsOption;
    this.brackets       = builder.brackets;

    StringBuilder sb = new StringBuilder(indentSize);
    for (int i=0; i<indentSize; i++) {
      sb.append(" ");
    }
    this.indent = sb.toString();

  }//}}}

  private FormattableString(FormattableString fs) {//{{{

    this.wordList       = fs.wordList;
    this.returnOption   = fs.returnOption;
    this.returnSize     = fs.returnSize;
    this.indentOption   = fs.indentOption;
    this.indentSize     = fs.indentSize;
    this.bracketsOption = fs.bracketsOption;
    this.brackets       = fs.brackets;
    this.indent         = fs.indent;

  }//}}}

  // Methods

  public FormattableString carriageReturn() {//{{{

    if (returnOption) {

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

      int count = 0;
      StringBuilder sb = new StringBuilder();
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

      if (bracketsOption) {
        int len = stringLength(sb.toString());
        int newLen = len + stringLength(brackets.END);
        if (returnSize < newLen) {
          sb.append(SEP);
        }
        sb.append(brackets.END);
      }

      return new FormattableString.Builder(sb)
        .indent(indentSize)
        .brackets(brackets)
        .build();

    } else {

      return new FormattableString.Builder(toString())
        .indent(indentSize)
        .brackets(brackets)
        .build();

    }

  }//}}}

  public FormattableString brackets() {//{{{

    if (indentOption) {
      wordList.set(0, indent + wordList.get(0));
    }

    int count = 0;
    StringBuilder sb = new StringBuilder();
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

    return new FormattableString.Builder(sb)
      .indent(indentSize)
      .brackets(brackets)
      .build();

  }//}}}

  public FormattableString actorName(String actorName) {//{{{

    String text = wordList.stream().collect(Collectors.joining());
    String[] array = text.split(SEP);
    int lineCount = 0;

    StringBuilder sb = new StringBuilder(text.toCharArray().length);
    for (int i=0; i<array.length; i++) {

      if (i % 3 == 0) {
        sb.append(actorName);
        sb.append(SEP);
      }

      String line = array[i];
      sb.append(line);
      sb.append(SEP);

    }

    return new FormattableString.Builder(sb)
      .indent(indentSize)
      .brackets(brackets)
      .build();

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

  @Override
  public String toString() { return wordList.stream().collect(Collectors.joining()); }

}
