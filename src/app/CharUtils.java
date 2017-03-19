package app;

import java.util.regex.*;

public final class CharUtils {

  private static final Pattern PATTERN_ALPHABET         = Pattern.compile("[a-zA-Z0-9_\\-]");
  private static final Pattern PATTERN_HIRAGANA         = Pattern.compile("[あ-ん]");
  private static final Pattern PATTERN_KATAKANA         = Pattern.compile("[ア-ン]");
  private static final Pattern PATTERN_HANKAKU_KATAKANA = Pattern.compile("[ｱ-ﾝ]");
  private static final Pattern PATTERN_WHITE_SPACE      = Pattern.compile("\\s");

  private CharUtils() {}

  public static boolean isAlphabet(char ch) {//{{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_ALPHABET.matcher(str);
    return m.find();

  }//}}}

  public static boolean isSeparator(char ch) {//{{{

    return (
           ch == '!'
        || ch == '?'
        || ch == ','
        || ch == '.'
        );

  }//}}}

  public static boolean isWhiteSpace(char ch) {//{{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_WHITE_SPACE.matcher(str);
    return m.find();

  }//}}}

  public static boolean isHiragana(char ch) {//{{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_HIRAGANA.matcher(str);
    return m.find();

  }//}}}

  public static boolean isKatakana(char ch) {//{{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_KATAKANA.matcher(str);
    return m.find();

  }//}}}

  public static boolean isHankakuKatakana(char ch) {//{{{

    String str = String.valueOf(ch);
    Matcher m = PATTERN_HANKAKU_KATAKANA.matcher(str);
    return m.find();

  }//}}}

}
