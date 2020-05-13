package jiro.java.lang;

import static jiro.java.lang.UtilsChar.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/** 文字列の操作用のユーティリティメソッドクラス。 */
public class UtilsString {

  private static final String SEP = System.lineSeparator();
  private static final char ALPHANUMERIC_CHARACTER = '\u007e';
  private static final char BACK_SLASH_CHARACTER = '\u00a5';
  private static final char TILDA_CHARACTER = '\u203e';

  private UtilsString() {}

  /**
   * 引数に渡された文字の文字幅の値を返す。
   *
   * <p>文字幅は半角文字の場合は1、全角文字の場合は2を返す。<br>
   * これは等幅フォントにおける横幅の値を意味する。
   *
   * @param ch 検査対象文字
   * @return 文字の幅, 1 または 2
   */
  public static int charLength(char ch) { // {{{

    if ((ch <= ALPHANUMERIC_CHARACTER)
        || (ch == BACK_SLASH_CHARACTER)
        || (ch == TILDA_CHARACTER)
        || ('\uff61' <= ch && ch <= '\uff9f') // 半角カナ
    ) return 1;
    else return 2;
  } // }}}

  /**
   * 引数に渡された文字列の文字幅の合計値を返す。
   *
   * <p>文字幅は半角文字の場合は1、全角文字の場合は2を返す。<br>
   * これは等幅フォントにおける横幅の値を意味する。
   *
   * @param str 検査対象文字列
   * @return 文字列の幅
   */
  public static int stringLength(String str) { // {{{

    int count = 0;
    for (char ch : str.toCharArray()) {
      count += charLength(ch);
    }
    return count;
  } // }}}

  /**
   * 引数に渡された文字列を単語単位で区切ったリストとして返す。
   *
   * <p>アルファベットにおいてはスペースを区切り文字とし、ハイフン、アンダースコア が続く文字は一つの文字として処理する。
   *
   * <p>それ以外の文字(日本語を含む)はすべて全角文字として判定し、全角文字は１つの 文字を１つの単語として区切る。
   *
   * @param text 区切り対象の文字列
   * @return 単語単位で区切られたリスト
   */
  public static List<String> splitToWord(String text) { // {{{

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
  } // }}}

  /**
   * 引数に渡された文字列リストを単語単位で区切ったリストとして返す。
   *
   * <p>アルファベットにおいてはスペースを区切り文字とし、ハイフン、アンダー スコアが続く文字は一つの文字として処理する。
   *
   * <p>それ以外の文字(日本語を含む)はすべて全角文字として判定し、全角文字 は１つの 文字を１つの単語として区切る。
   *
   * @param list 区切り対象の文字列リスト
   * @return 単語単位で区切られたリスト
   */
  public static List<String> splitToWordList(List<String> list) { // {{{

    List<String> newList = new ArrayList<>(list.size());
    list.stream()
        .forEach(
            str -> {
              splitToWord(str)
                  .stream()
                  .forEach(
                      s -> {
                        newList.add(s);
                      });
            });

    return newList;
  } // }}}

  /**
   * 引数に渡されたテキストファイルからテキストを読み取り、１行の文字列に連結 してOptionalでラッピングして返却する。
   *
   * <p>この時、改行文字の直前の文字がアルファベットであった場合、改行文字を半角ス ペースに変換して連結する。
   *
   * @param file 読み取り対象のテキストファイル
   * @return 連結文字列
   */
  public static Optional<String> readTextFrom(File file) { // {{{

    String str = null;
    Path path = file.toPath();
    try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

      str =
          br.lines()
              .map(
                  s -> {
                    char ch = s.charAt(s.length() - 1);
                    return isAlphabet(ch) ? s + " " : s;
                  })
              .collect(Collectors.joining());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(str);
  } // }}}

  public static Optional<List<String>> readLineFrom(File file) { // {{{

    Path path = file.toPath();
    try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

      List<String> list = br.lines().filter(s -> !s.startsWith("##")).collect(Collectors.toList());
      return Optional.ofNullable(list);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  } // }}}

  public static List<List<String>> splitWithParagraph(List<String> list) { // {{{

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

  public static List<List<String>> replaceActorName(List<List<String>> list) { // {{{

    List<List<String>> newListList = new ArrayList<>();
    for (List<String> l : list) {

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

    return newListList;
  } // }}}

  public static List<List<String>> addActorName(List<List<String>> listList) { // {{{

    List<List<String>> newListList = new ArrayList<>();

    String name = "";
    for (List<String> list : listList) {

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

    return newListList;
  } // }}}
}
