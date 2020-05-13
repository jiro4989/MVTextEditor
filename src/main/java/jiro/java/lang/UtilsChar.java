package jiro.java.lang;

import java.util.regex.*;

/** 文字の種類を判別するユーティリティメソッドクラス。 */
public final class UtilsChar {

  private static final Pattern PATTERN_ALPHABET = Pattern.compile("[a-zA-Z0-9_\\-]");
  private static final Pattern PATTERN_WHITE_SPACE = Pattern.compile("\\s");
  private static final String SEP = System.lineSeparator();

  private UtilsChar() {}

  /**
   * 引数に渡された文字がアルファベットまたは英数字かどうかを判別する。
   *
   * @param ch 検査対象文字
   * @return 検査結果 {@code true} または {@code false}
   */
  public static boolean isAlphabet(char ch) { // {{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_ALPHABET.matcher(str);
    return m.find();
  } // }}}

  /**
   * 引数に渡された文字が区切り文字かどうかを判別する。
   *
   * <p>区切り文字:
   *
   * <ul>
   *   <li>!
   *   <li>?
   *   <li>,
   *   <li>.
   * </ul>
   *
   * @param ch 検査対象文字
   * @return 検査結果 {@code true} または {@code false}
   */
  public static boolean isSeparator(char ch) { // {{{

    return (ch == '!' || ch == '?' || ch == ',' || ch == '.' || String.valueOf(ch).equals(SEP));
  } // }}}

  /**
   * 引数に渡された文字が空白文字かどうかを判別する。
   *
   * @param ch 検査対象文字
   * @return 検査結果 {@code true} または {@code false}
   */
  public static boolean isWhiteSpace(char ch) { // {{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_WHITE_SPACE.matcher(str);
    return m.find();
  } // }}}
}
