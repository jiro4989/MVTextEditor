package app;

/**
 * 対応する括弧クラス。
 * テキストファイルに分離しても良いかも
 */
public enum Brackets {

  TYPE1   ( "「", "」"     )
  ,TYPE2  ( "『", "』"     )
  ,TYPE3  ( "（", "）"     )
  ,TYPE4  ( "【", "】"     )
  ,TYPE5  ( "〈", "〉"     )
  ,TYPE6  ( "《", "》"     )
  ,TYPE7  ( "［", "］"     )
  ,TYPE8  ( " «", "» "     )
  ,TYPE9  ( " {", "} "     )
  ,TYPE10 ( " \"", "\" " )
  ,TYPE11 ( " '", "' "     ) ;

  public final String START;
  public final String END;

  private Brackets(String start, String end) {//{{{
    START = start;
    END   = end;
  }//}}}

}
