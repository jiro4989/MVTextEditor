package com.jiro4989.mvte.manager;

import app.MainController;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class EditManager {

  private final ActorTableManager actorTableManager;
  private final VarTableManager   varTableManager;
  private final IconSetManager    iconSetManager;

  public EditManager(
      MainController mainController

      , TextField                   varSearchTextField
      , TableView<  VarDB>          varTableView
      , TableColumn<VarDB, Integer> varIdColumn
      , TableColumn<VarDB, String>  varNameColumn

      , TextField                     actorSearchTextField
      , TableView<  ActorDB>          actorTableView
      , TableColumn<ActorDB, Integer> actorIdColumn
      , TableColumn<ActorDB, String>  actorNameColumn

      , GridPane  iconGridPane
      , ImageView iconImageView
      , Label     iconFocusLabel
      , Label     iconSelectedLabel

      )
  {//{{{

    actorTableManager = new ActorTableManager(mainController, actorSearchTextField , actorTableView, actorIdColumn, actorNameColumn);
    varTableManager = new VarTableManager(mainController, varSearchTextField, varTableView, varIdColumn, varNameColumn);
    iconSetManager  = new IconSetManager(mainController, iconGridPane, iconImageView, iconFocusLabel, iconSelectedLabel);

  }//}}}

  public void focusVarPane()     { varTableManager   . focus(); }
  public void focusActorPane()   { actorTableManager . focus(); }
  public void focusIconsetPane() { iconSetManager    . focus(); }

  // setter

  public void setVariables(String path) { varTableManager   . setVariables(path); }
  public void setActors(String path)    { actorTableManager . setActors(path);    }
  public void setIconset(String path)   { iconSetManager    . setImage(path);     }

}
