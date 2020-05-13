package jiro.java.util;

import java.util.*;
import java.util.stream.*;

public class RecentFilesUtils {

  /** 保存するログの上限 */
  public static final int MAX = 20;

  /** ログが空だった場合に格納される文字列 */
  public static final String EMPTY = "EMPTY";

  /**
   * 最近開いたファイルの総数がMAXに足りない場合は、NULL文字を格納した新しいリス トを返す。
   *
   * @param list 最近開いたファイル
   * @return NULLを含む最近開いたファイル
   */
  public static List<String> createContainsNullList(List<String> list) { // {{{
    List<String> containsNullList = new ArrayList<>();
    containsNullList.addAll(list);
    int size = containsNullList.size();

    for (int i = size - 1; i < MAX; i++) {
      containsNullList.add(EMPTY);
    }

    return containsNullList;
  } // }}}
}
