package app.table;

import static util.Texts.SEP;

import app.Main;

import java.util.*;

public class JsonMap {//{{{
  public int id;
  public String name;
  public String note = "";
  public List<Pages> pages;
  public int x;
  public int y;

  public JsonMap(int id, int x, int y, List<TextDB> dbList) {//{{{
    this.id = id;
    this.x = x;
    this.y = y;
    this.name = String.format("EV%03d", id);
    this.pages.add(new Pages(dbList));
  }//}}}

}//}}}

class Pages {//{{{
  Map<String, Object> conditions;
  boolean directionFix = false;
  Map<String, Object> images;
  List<EventList> list;

  Pages(List<TextDB> dbList) {//{{{
    list = new ArrayList<>();

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

    dbList.stream().forEach(db -> {
      list.add(new EventList(101, 0, db));

      String text = db.textProperty().get();
      String[] textArray = text.split(SEP);
      for (String t : textArray) {
        //list.add(new EventList(401, 0, t));
      }
    });

  }//}}}

}//}}}

class EventList {//{{{
  int code;
  int indent = 0;
  Object[] parameters;

  EventList(int code, int indent, TextDB db) {//{{{
    this.code   = code;
    this.indent = indent;

    String[] array = db.iconProperty().get().split(":");
    String fileName = "";
    int index = 0;

    if (1 < array.length) {
      fileName = array[0];
      index = Integer.parseInt(array[1]);
    }

    String bg   = db.backgroundProperty().get();
    String pos  = db.positionProperty()  .get();

    parameters = new Object[4];
    parameters[0] = fileName;
    parameters[1] = index;
    parameters[2] = getIndex("background", bg);
    parameters[3] = getIndex("position"  , pos);
  }//}}}

  EventList(int code, int indent, String text) {//{{{
    this.code     = code;
    this.indent   = indent;
    parameters    = new Object[1];
    parameters[0] = text;
  }//}}}

  private static int getIndex(String word, String value) {//{{{
    int index = 0;
    String[] array = Main.resources.getString(word).split(",");
    for (String s : array) {
      if (s.equals(value)) {
        return index;
      }
      index++;
    }
    return 0;
  }//}}}

}//}}}

