package app.table;

import static util.Texts.SEP;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.*;

public class TextDB {

  private final StringProperty icon;
  private final IntegerProperty iconIndex;
  private final StringProperty actorName;
  private final StringProperty text;
  private final StringProperty background;
  private final StringProperty position;

  public TextDB(
      String icon,
      int iconIndex,
      String actorName,
      String text,
      String background,
      String position
      )
  {

    this.icon       = new SimpleStringProperty(icon);
    this.iconIndex  = new SimpleIntegerProperty(iconIndex);
    this.actorName  = new SimpleStringProperty(actorName);
    this.text       = new SimpleStringProperty(text);
    this.background = new SimpleStringProperty(background);
    this.position   = new SimpleStringProperty(position);

  }

  public TextDB(
      String icon,
      int iconIndex,
      List<String> textList,
      String background,
      String position
      )
  {
    this(icon, iconIndex, getActorNameFromList(textList), getTextFromList(textList), background, position);
  }

  public TextDB(TextDB textDB) {//{{{

    this . icon       = new SimpleStringProperty(textDB  . iconProperty()       . get());
    this . iconIndex  = new SimpleIntegerProperty(textDB . iconIndexProperty()  . get());
    this . actorName  = new SimpleStringProperty(textDB  . actorNameProperty()  . get());
    this . text       = new SimpleStringProperty(textDB  . textProperty()       . get());
    this . background = new SimpleStringProperty(textDB  . backgroundProperty() . get());
    this . position   = new SimpleStringProperty(textDB  . positionProperty()   . get());

  }//}}}

  // Getter//{{{

  public StringProperty iconProperty() {
    return icon;
  }
  public IntegerProperty iconIndexProperty() {
    return iconIndex;
  }
  public StringProperty actorNameProperty() {
    return actorName;
  }
  public StringProperty textProperty() {
    return text;
  }
  public StringProperty backgroundProperty() {
    return background;
  }
  public StringProperty positionProperty() {
    return position;
  }

  //}}}

  // Setter//{{{

  public void setIcon(String icon) {
    iconProperty().set(icon);
  }
  public void setIconIndex(int iconIndex) {
    iconIndexProperty().set(iconIndex);
  }
  public void setActorName(String actorName) {
    actorNameProperty().set(actorName);
  }
  public void setText(String text) {
    textProperty().set(text);
  }
  public void setBackground(String background) {
    backgroundProperty().set(background);
  }
  public void setPosition(String position) {
    positionProperty().set(position);
  }

  //}}}

  private static String getActorNameFromList(List<String> list) {//{{{
    if (0 < list.size()) {
      String actor = list.get(0);
      actor = actor.startsWith("#") ? actor.replaceAll("^# *", "") : "";
      return actor;
    }
    return "";
  }//}}}

  private static String getTextFromList(List<String> list) {//{{{
    int size = list.size();
    if (0 < size) {
      String actor = list.get(0);
      int fromIndex = actor.startsWith("#") ? 1 : 0;
      List<String> subList = new ArrayList<>(list.subList(fromIndex, size));
      return String.join(SEP, subList);
    }
    return "";
  }//}}}

  @Override
  public String toString() {//{{{
    return String.format("TextDB: {icon: %s, actorName: %s, text: %s, background: %s, position: %s}",
        icon.get(), actorName.get(), text.get(), background.get(), position.get());
  }//}}}

}
