package jiro.java.lang;

import static java.util.stream.IntStream.range;
import static jiro.java.lang.UtilsString.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 整形可能なテキストクラス。
 *
 * <p>このクラスは不変クラスである。<br>
 * Getterは常に新しいインスタンスを生成して返却するため、 Getterの値を変更してもこのインスタンスのフィールドは変化しない。
 */
public class FormattableText {

  private final List<List<String>> textList;
  private static final String SEP = System.lineSeparator();

  private final boolean actorNameOption;

  private final boolean returnOption;
  private final int returnSize;

  private final boolean indentOption;
  private final int indentSize;
  private final String indent;

  private final boolean bracketsOption;
  private final Brackets brackets;

  private final boolean joiningOption;

  // builder

  public static class Builder { // {{{

    private final List<List<String>> textList;

    private boolean actorNameOption = false;

    private boolean returnOption = false;
    private int returnSize = 27 * 2;

    private boolean indentOption = false;
    private int indentSize = 0;

    private boolean bracketsOption = false;
    private Brackets brackets = null;

    private boolean joiningOption = false;

    // constructor

    public Builder(List<List<String>> list) { // {{{
      textList = list;
    } // }}}

    public Builder(FormattableText ft, List<List<String>> list) { // {{{
      textList = list;
      returnOption = ft.returnOption;
      returnSize = ft.returnSize;
      indentOption = ft.indentOption;
      indentSize = ft.indentSize;
      bracketsOption = ft.bracketsOption;
      brackets = ft.brackets;
      joiningOption = ft.joiningOption;
    } // }}}

    public Builder(File file) throws IOException { // {{{
      this(createParagraphListFrom(file));
    } // }}}

    // option methods

    public Builder actorNameOption(boolean bool) { // {{{
      this.actorNameOption = bool;
      return this;
    } // }}}

    public Builder returnOption(boolean bool) { // {{{
      this.returnOption = bool;
      return this;
    } // }}}

    public Builder returnSize(int returnSize) { // {{{
      this.returnSize = returnSize;
      return this;
    } // }}}

    public Builder indentOption(boolean bool) { // {{{
      this.indentOption = bool;
      return this;
    } // }}}

    public Builder indentSize(int indentSize) { // {{{
      this.indentSize = indentSize;
      return this;
    } // }}}

    public Builder bracketsOption(boolean bool) { // {{{
      this.bracketsOption = bool;
      return this;
    } // }}}

    public Builder brackets(Brackets brackets) { // {{{
      this.brackets = brackets;
      return this;
    } // }}}

    public Builder joiningOption(boolean bool) { // {{{
      this.joiningOption = bool;
      return this;
    } // }}}

    public FormattableText build() { // {{{
      if (returnSize < 0)
        throw new IllegalArgumentException(
            "returnSizeに負の数を指定することはできません。 - returnSize : " + returnSize);
      if (indentOption && indentSize < 0)
        throw new IllegalArgumentException(
            "indentSizeに負の数を指定することはできません。 - indentSize : " + indentSize);
      if (bracketsOption && brackets == null)
        throw new NullPointerException("bracketsはnull以外の値で初期化される必要があります。");
      return new FormattableText(this);
    } // }}}
  } // }}}

  // private constructor

  private FormattableText(Builder builder) { // {{{
    this.textList = builder.textList;
    this.actorNameOption = builder.actorNameOption;
    this.returnOption = builder.returnOption;
    this.returnSize = builder.returnSize;
    this.indentOption = builder.indentOption;
    this.indentSize = builder.indentSize;
    this.indent =
        createIndentString(
            builder.bracketsOption ? stringLength(builder.brackets.START) : builder.indentSize);
    this.bracketsOption = builder.bracketsOption;
    this.brackets = builder.brackets;
    this.joiningOption = builder.joiningOption;
  } // }}}

  // public methods

  public FormattableText format() { // {{{
    return addActorName()
        .replaceActorName()
        .formatPutBrackets()
        .joining()
        .formatCarriageReturn()
        .splitToParagraph()
        .deleteEmptyList();
  } // }}}

  public FormattableText addActorName() { // {{{
    if (actorNameOption) {
      List<List<String>> newListList = new ArrayList<>();

      String name = "";
      for (List<String> list : textList) {
        List<String> newList = new ArrayList<>();

        String top = list.get(0);
        if (top.startsWith("#")) {
          name = top.replaceAll("^# *", "");
        } else {
          newList.add("# " + name);
        }

        newList.addAll(list);
        newListList.add(newList);
      }

      return new FormattableText.Builder(this, newListList).build();
    }

    return this;
  } // }}}

  public FormattableText splitToParagraph() { // {{{
    List<List<String>> newListList = new ArrayList<>();

    textList
        .stream()
        .forEach(
            l -> {
              if (l.size() <= 4) {
                newListList.add(l);
              } else {
                List<String> newList = new ArrayList<>();
                for (int i = 1; i <= l.size(); i++) {
                  newList.add(l.get(i - 1));
                  if (i % 4 == 0) {
                    newListList.add(newList);
                    newList = new ArrayList<>();
                  }
                }
                newListList.add(newList);
              }
            });

    return new FormattableText.Builder(this, newListList).build();
  } // }}}

  public FormattableText replaceActorName() { // {{{
    List<List<String>> newListList = new ArrayList<>();

    for (List<String> l : textList) {
      List<String> newList = new ArrayList<>();
      String name = "";

      for (String line : l) {
        if (line.startsWith("#")) {
          name = line.replaceAll("^# *", "");
        }

        String newLine = line.replaceAll("@name", name);
        newList.add(newLine);
      }

      newListList.add(newList);
    }

    return new FormattableText.Builder(this, newListList).build();
  } // }}}

  public FormattableText joining() { // {{{
    if (joiningOption) {
      List<List<String>> newList =
          textList.stream().map(this::createJoinedListWith).collect(Collectors.toList());
      return new FormattableText.Builder(this, newList).build();
    }
    return this;
  } // }}}

  public FormattableText formatPutBrackets() { // {{{
    if (bracketsOption) {
      List<List<String>> formedList =
          textList.stream().map(this::createWrappedListWith).collect(Collectors.toList());

      return new FormattableText.Builder(this, formedList).build();
    }
    return this;
  } // }}}

  public FormattableText formatCarriageReturn() { // {{{
    if (returnOption) {
      List<List<String>> formedList =
          textList
              .stream()
              .map(
                  list -> {
                    List<String> newList = new ArrayList<>();

                    list.stream()
                        .filter(s -> s.startsWith("#"))
                        .findFirst()
                        .ifPresent(
                            s -> {
                              newList.add(s);
                            });

                    AtomicInteger atom = new AtomicInteger(0);
                    list.stream()
                        .filter(s -> !s.startsWith("#"))
                        .forEach(
                            s -> {
                              if (atom.getAndIncrement() != 0 && indentOption) {
                                s = indent + s;
                              }
                              List<String> crl = createCarriageReturnedListWith(s);
                              newList.addAll(crl);
                            });

                    return newList;
                  })
              .collect(Collectors.toList());

      return new FormattableText.Builder(this, formedList).build();
    }
    return this;
  } // }}}

  /** 空のリストを削除する。 */
  public FormattableText deleteEmptyList() {
    List<List<String>> listList =
        textList.stream().filter(l -> l.size() != 0).collect(Collectors.toList());
    return new FormattableText.Builder(this, listList).build();
  }

  public void show() { // {{{
    AtomicInteger atom = new AtomicInteger(0);
    textList
        .stream()
        .forEach(
            l -> {
              atom.getAndIncrement();

              l.stream()
                  .forEach(
                      s -> {
                        int paragraphNumber = atom.get();
                        System.out.println(
                            String.format("paragraph %03d : %s", paragraphNumber, s));
                      });
            });
  } // }}}

  /**
   * 渡された数値からインデント文字列を生成する。
   *
   * @param size インデントサイズ
   * @return インデント文字列
   */
  public static String createIndentString(int size) { // {{{
    StringBuilder sb = new StringBuilder(size);
    range(0, size).forEach(i -> sb.append(" "));
    return sb.toString();
  } // }}}

  // private methods

  private List<String> createCarriageReturnedListWith(String text) { // {{{
    List<String> newList = new ArrayList<>();

    List<String> wordList = splitToWord(text);
    int count = 0;
    StringBuilder sb = new StringBuilder();

    for (String word : wordList) {
      int length = stringLength(word);
      count += length;

      if (returnSize < count) {
        newList.add(sb.toString());
        sb.setLength(0);
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

    newList.add(sb.toString());
    return newList;
  } // }}}

  private List<String> createJoinedListWith(List<String> list) { // {{{
    List<String> nl = new ArrayList<>();
    list.stream()
        .filter(s -> s.startsWith("#"))
        .findFirst()
        .ifPresent(
            actor -> {
              nl.add(actor);
            });

    String text = list.stream().filter(s -> !s.startsWith("#")).collect(Collectors.joining());
    nl.add(text);

    return nl;
  } // }}}

  private List<String> createWrappedListWith(List<String> list) { // {{{
    List<String> newList = new ArrayList<>();
    list.stream()
        .filter(s -> s.startsWith("#"))
        .findFirst()
        .ifPresent(
            actor -> {
              newList.add(actor);
            });

    List<String> filteredList =
        list.stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

    int i = 0;
    int listSize = filteredList.size();

    for (String str : filteredList) {
      if (i == 0) {
        str = brackets.START + str;
      }

      if (listSize - 1 <= i) {
        str += brackets.END;
      }

      newList.add(str);
      i++;
    }

    return newList;
  } // }}}

  private static List<List<String>> splitToParagraphFrom(List<String> list) { // {{{
    List<List<String>> paragraphList = new ArrayList<>();
    List<String> paragraph = new ArrayList<>();

    for (String line : list) {
      if (line.length() <= 0) {
        if (0 < paragraph.size()) paragraphList.add(paragraph);
        paragraph = new ArrayList<>();
        continue;
      }
      paragraph.add(line);
    }

    return paragraphList;
  } // }}}

  private static List<List<String>> createParagraphListFrom(File file) throws IOException { // {{{
    Path path = file.toPath();
    BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"));

    List<String> list = br.lines().filter(s -> !s.startsWith("##")).collect(Collectors.toList());

    br.close();
    List<List<String>> listList = splitToParagraphFrom(list);
    return listList;
  } // }}}

  // Getter

  /**
   * 保持するテキストリストのコピーを返却する。<br>
   *
   * <p>これは参照ではなく、新しいインスタンスを返却することを意味する。
   *
   * <p>つまり、このGetterによって取得したインスタンスの値を変更しても、 このFormattableTextインスタンスのフィールドの値が変更されないことを意味する 。
   *
   * @return 保持するテキストリストの新しいインスタンス
   */
  public List<List<String>> getTextList() { // {{{
    return new ArrayList<List<String>>(textList);
  } // }}}

  @Override
  public String toString() { // {{{
    AtomicInteger atom = new AtomicInteger(0);
    return textList
        .stream()
        .map(
            l -> {
              int paragraphNumber = atom.incrementAndGet();
              return l.stream()
                  .map(s -> String.format("paragraph %03d : %s", paragraphNumber, s + SEP))
                  .collect(Collectors.joining());
            })
        .collect(Collectors.joining());
  } // }}}
}
