package app.table;

import static util.Texts.*;

import java.io.*;
import java.util.*;

public class Pages {
  public final Map<String, Object> conditions;
  public final boolean directionFix = false;
  public final Map<String, Object> image;
  public final List<EventList> list;
  public final int moveFrequency = 3;
  public final MoveRoute moveRoute = new MoveRoute();
  public final int moveSpeed = 3;
  public final int moveType = 0;
  public final int priorityType = 1;
  public final boolean stepAnime = false;
  public final boolean through = false;
  public final int trigger = 0;
  public final boolean walkAnime = true;

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

    // image {{{
    image = new HashMap<>();
    image.put ( "characterIndex" , 0  ) ;
    image.put ( "characterName"  , "" ) ;
    image.put ( "direction"      , 2  ) ;
    image.put ( "pattern"        , 0  ) ;
    image.put ( "tileId"         , 0  ) ;
    //}}}

    list = new ArrayList<>();
    dbList.stream().forEach(db -> {
      list.add(new EventList(101, 0, db));

      String actorName = db.actorNameProperty().get();
      if (actorName != null && actorName.length() != 0)
        list.add(new EventList(401, 0, actorName));

      String text = db.textProperty().get();
      BufferedReader br = new BufferedReader(new StringReader(text));
      br.lines().forEach(line -> {
        list.add(new EventList(401, 0, line));
      });
    });
    list.add(new EventList(0, 0, ""));

  }//}}}

}

