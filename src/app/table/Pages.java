package app.table;

import static util.Texts.*;

import java.util.*;

public class Pages {
  public final Map<String, Object> conditions;
  public final boolean directionFix = false;
  public final Map<String, Object> images;
  public final List<EventList> list;

  public Pages(List<TextDB> dbList) {//{{{
    // conditions//{{{
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
    //}}}

    // images {{{
    images = new HashMap<>();
    images.put ( "characterIndex" , 0  ) ;
    images.put ( "characterName"  , "" ) ;
    images.put ( "direction"      , 2  ) ;
    images.put ( "pattern"        , 0  ) ;
    images.put ( "tileId"         , 0  ) ;
    //}}}

    list = new ArrayList<>();
    dbList.stream().forEach(db -> {
      list.add(new EventList(101, 0, db));

      String text = db.textProperty().get();
      String[] textArray = text.split(SEP);
      for (String t : textArray) {
        list.add(new EventList(401, 0, t));
      }
    });

  }//}}}

}

