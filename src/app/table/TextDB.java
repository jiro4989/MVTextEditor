package app.table;

import static util.Texts.SEP;

import java.util.List;
import javafx.beans.property.*;

public class TextDB {
  private final StringProperty icon;
  private final StringProperty actorName;
  private final StringProperty text;
  private final BooleanProperty background;
  private final BooleanProperty position;

  public TextDB(
      String icon,
      String actorName,
      String text,
      boolean background,
      boolean position
      )
  {

    this.icon       = new SimpleStringProperty(icon);
    this.actorName  = new SimpleStringProperty(actorName);
    this.text       = new SimpleStringProperty(text);
    this.background = new SimpleBooleanProperty(background);
    this.position   = new SimpleBooleanProperty(position);

  }

  public TextDB(
      String icon,
      List<String> textList,
      boolean background,
      boolean position
      )
  {
    this(icon, "a", String.join(SEP, textList), background, position);
  }

  public TextDB(TextDB textDB) {

    this.icon       = new SimpleStringProperty(textDB.iconProperty().get());
    this.actorName  = new SimpleStringProperty(textDB.actorNameProperty().get());
    this.text       = new SimpleStringProperty(textDB.textProperty().get());
    this.background = new SimpleBooleanProperty(textDB.backgroundProperty().get());
    this.position   = new SimpleBooleanProperty(textDB.positionProperty().get());

  }
  // **************************************************
  // Getter
  // **************************************************
  public StringProperty iconProperty() {
    return icon;
  }
  public StringProperty actorNameProperty() {
    return actorName;
  }
  public StringProperty textProperty() {
    return text;
  }
  public BooleanProperty backgroundProperty() {
    return background;
  }
  public BooleanProperty positionProperty() {
    return position;
  }

  // **************************************************
  // Setter
  // **************************************************
  public void setIcon(String icon) {
    iconProperty().set(icon);
  }
  public void setActorName(String actorName) {
    actorNameProperty().set(actorName);
  }
  public void setText(String text) {
    textProperty().set(text);
  }
  public void setBackground(boolean background) {
    backgroundProperty().set(background);
  }
  public void setPosition(boolean position) {
    positionProperty().set(position);
  }
}
