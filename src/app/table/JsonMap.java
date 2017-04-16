package app.table;

import java.util.*;

public class JsonMap {//{{{
  public int id;
  public String name;
  public String note;
  public List<Pages> pages;
}//}}}

class Pages {//{{{
  Map<String, Object> conditions;
  boolean directionFix = false;
  Map<String, Object> images;
  JList list;

  Pages() {//{{{
    conditions = new HashMap<>();
    conditions.put ( "actorId"         , 1     ) ;
    conditions.put ( "actorValid"      , false ) ;
    conditions.put ( "itemId"          , 1     ) ;
    conditions.put ( "itemValid"       , false ) ;
    conditions.put ( "selfSwitchCh"    , "A"   ) ;
    conditions.put ( "selfSwitchValid" , false ) ;
    conditions.put ( "switch1Id"       , 1     ) ;
    conditions.put ( "switch1Valid"    , false ) ;
    conditions.put ( "switch2Id"       , 1     ) ;
    conditions.put ( "switch2Valid"    , false ) ;
    conditions.put ( "variableId"      , 1     ) ;
    conditions.put ( "variableValid"   , false ) ;
    conditions.put ( "variableValue"   , 0     ) ;

    images = new HashMap<>();
    images.put ( "characterIndex" , 0  ) ;
    images.put ( "characterName"  , "" ) ;
    images.put ( "direction"      , 2  ) ;
    images.put ( "pattern"        , 0  ) ;
    images.put ( "tileId"         , 0  ) ;
  }//}}}
}//}}}

class JList {//{{{
  int code = 101;
  int indent = 101;
  Object[] parameters = new Object[4];
}//}}}

