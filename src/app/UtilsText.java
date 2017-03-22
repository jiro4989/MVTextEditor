package app;

import java.util.*;

public class UtilsText {

  private static final String SEP = System.lineSeparator();

  public static List<List<String>> splitWithParagraph(List<String> list) {//{{{

    List<List<String>> paragraphList = new ArrayList<>();
    List<String> paragraph = new ArrayList<>();
    for (String line : list) {

      if (line.length() <= 0) {

        if (paragraph.size() != 0)
          paragraphList.add(paragraph);
        paragraph = new ArrayList<>();

      }

      paragraph.add(line);

    }

    return paragraphList;

  }//}}}

  public static List<List<String>> replaceActorName(List<List<String>> list) {//{{{

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

  }//}}}

  public static List<List<String>> addActorName(List<List<String>> listList) {//{{{

    List<List<String>> newListList = new ArrayList<>();

    String name = "";
    for (List<String> list : listList) {

      List<String> newList = new ArrayList<>();

      String top = list.get(1);
      System.out.println("top = [ " + top + " ]");
      if (top.startsWith("#")) {
        name = top.replaceAll("^# *", "");
      } else {
        newList.add("# " + name);
      }

      newList.addAll(list);
      newListList.add(newList);

    }

    return newListList;

  }//}}}

}
