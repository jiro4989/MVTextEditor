package jiro.java.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** 対応する括弧クラス。 テキストファイルに分離しても良いかも */
public enum Brackets {
  TYPE1("「", "」"),
  TYPE2("『", "』"),
  TYPE3("（", "）"),
  TYPE4("【", "】"),
  TYPE5("〈", "〉"),
  TYPE6("《", "》"),
  TYPE7("［", "］"),
  TYPE8(" «", "» "),
  TYPE9(" {", "} "),
  TYPE10(" \"", "\" "),
  TYPE11(" '", "' ");

  /** 括弧の開始文字列 */
  public final String START;

  /** 括弧の終了文字列 */
  public final String END;

  private static final Map<String, Brackets> stringToEnum = new HashMap<>();

  static {
    for (Brackets b : values()) stringToEnum.put(b.name(), b);
  }

  private Brackets(String start, String end) { // {{{
    START = start;
    END = end;
  } // }}}

  /**
   * TYPE名文字列から対応するBracketsインスタンスを返す。
   *
   * @param key TYPE名キー文字列
   * @return Brackets
   */
  public static Optional<Brackets> fromString(String key) { // {{{
    return Optional.ofNullable(stringToEnum.get(key));
  } // }}}

  @Override
  public String toString() { // {{{
    return String.format("Brackets: { START = %s, END = %s }.", START, END);
  } // }}}
}
