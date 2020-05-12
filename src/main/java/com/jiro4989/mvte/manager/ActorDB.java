package com.jiro4989.mvte.manager;

import javafx.beans.property.*;

public class ActorDB {
  private final IntegerProperty id;
  private final StringProperty name;

  public ActorDB(int id, String name) {
    this.id = new SimpleIntegerProperty(id);
    this.name = new SimpleStringProperty(name);
  }

  // ************************************************************
  // getter
  // ************************************************************

  public IntegerProperty idProperty() {
    return this.id;
  }

  public StringProperty nameProperty() {
    return this.name;
  }

  public int getId() {
    return this.id.get();
  }

  public String getName() {
    return this.name.get();
  }

  // ************************************************************
  // setter
  // ************************************************************

  public void setIdProperty(int id) {
    this.id.set(id);
  }

  public void setNameProperty(String name) {
    this.name.set(name);
  }
}
