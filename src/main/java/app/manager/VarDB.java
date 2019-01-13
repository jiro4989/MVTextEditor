package app.manager;

import javafx.beans.property.*;

public class VarDB {
  private IntegerProperty id;
  private StringProperty name;

  public VarDB(
      int id,
      String name
      )
  {
    this.id = new SimpleIntegerProperty(id);
    this.name = new SimpleStringProperty(name);
  }

  public VarDB(VarDB varDB) {
    this.id = new SimpleIntegerProperty(varDB.idProperty().get());
    this.name = new SimpleStringProperty(varDB.nameProperty().get());
  }
  // **************************************************
  // Getter
  // **************************************************
  public IntegerProperty idProperty() {
    return id;
  }
  public StringProperty nameProperty() {
    return name;
  }

  // **************************************************
  // Setter
  // **************************************************
  public void setId(int id) {
    idProperty().set(id);
  }
  public void setName(String name) {
    nameProperty().set(name);
  }
}
